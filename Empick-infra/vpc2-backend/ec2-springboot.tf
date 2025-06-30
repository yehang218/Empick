# ================================================================================
# 이 파일은 VPC 2 Backend의 Spring Boot 애플리케이션 서버를 정의합니다.
# IAM 역할, 키 페어, 그리고 실제 Spring Boot 서버를 포함합니다.
# ================================================================================

# 기존 Key Pair 사용 (이미 AWS에 존재하는 "camp" 키 페어)
# AWS에서 이미 생성된 키 페어를 참조하여 사용
data "aws_key_pair" "empick_key" {
  key_name = var.key_name # "camp" 키 페어 참조
}

# IAM Role for Spring Boot EC2 (AWS 서비스 접근용)
resource "aws_iam_role" "springboot_ec2_role" {
  name = "${var.project_name}-springboot-ec2-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ec2.amazonaws.com"
        }
      }
    ]
  })

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-springboot-ec2-role"
    Type = "IAM"
  })
}

# IAM Policy for S3 Access (Spring Boot에서 S3 사용)
resource "aws_iam_role_policy" "springboot_s3_policy" {
  name = "${var.project_name}-springboot-s3-policy"
  role = aws_iam_role.springboot_ec2_role.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "s3:GetObject",
          "s3:PutObject",
          "s3:DeleteObject",
          "s3:ListBucket"
        ]
        Resource = [
          "arn:aws:s3:::${var.s3_bucket_name}", # TODO: S3 버킷명 설정
          "arn:aws:s3:::${var.s3_bucket_name}/*"
        ]
      }
    ]
  })
}

# IAM Policy for CloudWatch Logs (로깅용)
resource "aws_iam_role_policy" "springboot_logs_policy" {
  name = "${var.project_name}-springboot-logs-policy"
  role = aws_iam_role.springboot_ec2_role.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "logs:CreateLogGroup",
          "logs:CreateLogStream",
          "logs:PutLogEvents",
          "logs:DescribeLogStreams"
        ]
        Resource = "arn:aws:logs:${var.aws_region}:*:*"
      }
    ]
  })
}

# IAM Policy for Systems Manager (SSM) - CI/CD 배포용
resource "aws_iam_role_policy" "springboot_ssm_policy" {
  name = "${var.project_name}-springboot-ssm-policy"
  role = aws_iam_role.springboot_ec2_role.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "ssm:UpdateInstanceInformation",
          "ssm:SendCommand",
          "ssm:ListCommands",
          "ssm:ListCommandInvocations",
          "ssm:DescribeInstanceInformation",
          "ssm:GetCommandInvocation",
          "ssm:DescribeInstanceProperties",
          "ssm:DescribeDocumentParameters",
          "ssm:DescribeInstanceAssociationsStatus",
          "ssm:GetDocument",
          "ssm:ListDocuments"
        ]
        Resource = "*"
      },
      {
        Effect = "Allow"
        Action = [
          "ec2messages:AcknowledgeMessage",
          "ec2messages:DeleteMessage",
          "ec2messages:FailMessage",
          "ec2messages:GetEndpoint",
          "ec2messages:GetMessages",
          "ec2messages:SendReply"
        ]
        Resource = "*"
      }
    ]
  })
}

# IAM Instance Profile (EC2에 IAM Role 연결)
resource "aws_iam_instance_profile" "springboot_profile" {
  name = "${var.project_name}-springboot-profile"
  role = aws_iam_role.springboot_ec2_role.name

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-springboot-profile"
    Type = "IAM"
  })
}

# ================================================================================
# Spring Boot 서버 생성 (Blue-Green 다중 인스턴스)
# 
# 💡 Blue-Green 배포 전략:
# - Blue Environment (AZ-A): 현재 운영 중인 안정적인 환경
# - Green Environment (AZ-C): 새 버전 배포 및 테스트 환경
# 
# 🎯 배포 흐름:
# 1. Green 환경에 새 버전 배포
# 2. Green 환경 Health Check 및 테스트
# 3. 트래픽을 Blue → Green으로 전환
# 4. Blue 환경을 새 Green으로 업데이트
# 
# 📍 인스턴스 배치:
# - Index 0: Blue Environment (AZ-A, Private Subnet 1)
# - Index 1: Green Environment (AZ-C, Private Subnet 2)
# ================================================================================

# Spring Boot 서버 생성 (다중 인스턴스)
# Private Subnet에 배치되어 ALB로부터 트래픽을 받아 처리하는 애플리케이션 서버들
resource "aws_instance" "springboot" {
  # 💡 count 설명: var.springboot_instance_count = 2로 설정되어 있어 2개의 인스턴스가 생성됩니다
  # - Blue-Green 배포를 위해 2개 인스턴스 필요 (Blue 환경 + Green 환경)
  # - 고가용성(Multi-AZ) 확보: 한 AZ 장애 시에도 다른 AZ에서 서비스 지속
  # - 무중단 배포: 한 인스턴스 업데이트 중에도 다른 인스턴스가 트래픽 처리
  count = var.springboot_instance_count # variables.tf에서 default = 2로 설정

  ami                    = var.ami_id                   # TODO: 최신 Amazon Linux 2023 AMI
  instance_type          = var.springboot_instance_type # t3.small
  key_name               = data.aws_key_pair.empick_key.key_name
  vpc_security_group_ids = [aws_security_group.springboot_sg.id]

  # 💡 Private Subnet 배치 이유:
  # - 보안: 인터넷에서 직접 접근 불가, ALB를 통해서만 접근 가능
  # - 네트워크 격리: 내부 트래픽만 허용, 외부 공격 차단
  # - 아키텍처 요구사항: Private Subnet에 애플리케이션 서버 배치가 표준 3-tier 구조
  # 
  # 각 인스턴스를 다른 AZ의 Private Subnet에 배치
  # count.index = 0 → AZ-A Private Subnet (Blue Environment)
  # count.index = 1 → AZ-C Private Subnet (Green Environment)
  subnet_id = aws_subnet.private_subnets[count.index].id

  iam_instance_profile = aws_iam_instance_profile.springboot_profile.name

  # Private 서브넷이므로 Public IP 없음
  associate_public_ip_address = false

  # Spring Boot 자동 설치 및 실행 스크립트
  user_data = base64encode(templatefile("${path.module}/spring_user_data.sh", {
    # 💡 다중 인스턴스 공유 리소스 설명:
    # 
    # 🗄️ RDS MariaDB (공유 - 안전함):
    # - 단일 데이터베이스를 2개 인스턴스가 공유 사용
    # - 각 인스턴스가 독립적인 Connection Pool 관리
    # - MariaDB가 동시 접근 및 트랜잭션 격리 처리
    # - 장점: 데이터 일관성, 실시간 동기화
    # 
    # 🔄 Redis ElastiCache (공유 - 장점):
    # - 단일 Redis 클러스터를 2개 인스턴스가 공유
    # - 세션 연속성: 배포 중에도 사용자 세션 유지
    # - 캐시 효율성: 한 인스턴스의 캐시를 다른 인스턴스도 활용
    # - Blue-Green 배포 시 매우 유리함
    # 
    # 📦 S3 버킷 (공유 - 안전함):
    # - 글로벌 리소스로 모든 인스턴스에서 접근 가능
    # - 파일 공유: 업로드된 파일을 모든 인스턴스에서 조회 가능
    # - IAM Role 기반 동일한 권한으로 접근

    # 데이터베이스 설정 (공유 리소스)
    DB_HOST     = aws_db_instance.mariadb.endpoint
    DB_PORT     = var.db_port
    DB_NAME     = var.db_name
    DB_USERNAME = var.db_username
    DB_PASSWORD = var.db_password

    # Redis 설정 (공유 리소스 - 세션 연속성 확보)
    REDIS_HOST     = aws_elasticache_replication_group.redis.primary_endpoint_address
    REDIS_PORT     = var.redis_port
    REDIS_PASSWORD = "" # Redis 클러스터에 비밀번호가 없으면 빈 문자열

    # JWT 설정
    JWT_SECRET = var.jwt_secret # TODO: terraform.tfvars에서 안전한 시크릿 설정

    # 이메일 설정
    MAIL_HOST     = var.mail_host # TODO: SMTP 서버 설정
    MAIL_PORT     = var.mail_port
    MAIL_USERNAME = var.mail_username
    MAIL_PASSWORD = var.mail_password

    # AWS S3 설정 (공유 리소스 - 파일 공유)
    AWS_S3_BUCKET  = var.s3_bucket_name
    AWS_S3_REGION  = var.aws_region
    AWS_ACCESS_KEY = var.aws_access_key # TODO: IAM Role 사용 권장
    AWS_SECRET_KEY = var.aws_secret_key

    # 서버 설정
    SERVER_PORT = var.springboot_port

    # JAR 파일 S3 경로
    S3_JAR_PATH = var.s3_jar_path # S3 경로: s3://empick-private-bucket/releases/empick-backend-latest.jar

    # SSH 키 설정 (CI/CD 접근용)
    PRIVATE_KEY_CONTENT = var.private_key_content
  }))

  # Blue-Green 구분을 위한 태그 설정
  tags = merge(var.common_tags, {
    Name        = "${var.project_name}-springboot-${count.index + 1}"
    Type        = "Application"
    Role        = "Backend-API"
    Environment = count.index == 0 ? "blue" : "green" # Blue(AZ-A), Green(AZ-C)
    AZ          = var.availability_zones[count.index]
    DeployOrder = count.index == 0 ? "stable" : "staging" # Blue는 안정, Green은 스테이징
  })

  # 의존성: RDS와 Redis가 먼저 생성되어야 함
  depends_on = [
    aws_db_instance.mariadb,
    aws_elasticache_replication_group.redis,
    aws_nat_gateway.vpc2_nat
  ]
}

# ================================================================================
# 💡 Spring Boot 서버 구성 요소 설명:
# 
# 1. Key Pair: SSH 접속을 위한 키 페어
# 2. IAM Role & Policies: AWS 서비스 접근 권한
#    - S3 Policy: 파일 업로드/다운로드
#    - CloudWatch Logs Policy: 애플리케이션 로그
# 3. Instance Profile: EC2에 IAM Role 연결
# 4. EC2 Instance: 실제 Spring Boot 애플리케이션 서버
# 
# 🎯 아키텍처 특징:
# - Private Subnet에 배치 (보안)
# - 다중 인스턴스 Blue-Green 배포 지원
# - 공유 리소스 활용: RDS, Redis, S3 (세션 연속성 및 데이터 일관성)
# - 모든 환경 변수 자동 설정
# - Java 17 + Spring Boot 3.5.0 자동 설치
# - systemd 서비스로 자동 시작/재시작
# 
# 🔧 자동 설치 항목:
# - Java 17 (Amazon Corretto)
# - Spring Boot 애플리케이션
# - 환경 변수 설정 (15개 변수)
# - systemd 서비스 등록
# - 네트워크 연결 테스트
# 
# 🔐 보안 요소:
# - IAM Role 기반 AWS 서비스 접근
# - Private Subnet 배치 (인터넷 직접 접근 불가)
# - 보안 그룹으로 포트 제한 (8080, 22)
# - 환경 변수 파일 권한 제한 (600)
# 
# 📊 모니터링:
# - CloudWatch Logs 자동 전송
# - systemd 서비스 상태 관리
# - 애플리케이션 Health Check 엔드포인트
# 
# 📋 TODO 체크리스트:
# 1. terraform.tfvars에서 모든 환경 변수 설정
# 2. 최신 Amazon Linux 2023 AMI ID 확인
# 3. SSH 키 페어 생성 및 공개 키 설정
# 4. JAR 파일 배포 방식 결정 (S3/GitHub)
# 5. SMTP 서버 설정 (이메일 기능용)
# 6. S3 버킷 생성 및 권한 설정
# 
# ⚠️ 주의사항:
# - JAR 파일 URL이 없으면 플레이스홀더로 생성됨
# - 실제 배포 시 JAR 파일 업로드 후 서비스 재시작 필요
# - 운영 환경에서는 Auto Scaling Group 사용 권장
# - RDS 연결 수 제한: t3.micro 최대 100개 연결 (인스턴스당 50개 권장)
# - 공유 리소스로 인한 장점: 세션 연속성, 데이터 일관성, 파일 공유
# ================================================================================ 
