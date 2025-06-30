# ================================================================================
# 이 파일은 VPC 2 Backend의 Bastion Host를 정의합니다.
# Bastion Host는 Private 서브넷의 서버들에 SSH 접속하기 위한 게이트웨이 역할을 합니다.
# ================================================================================

# Bastion Host IAM Role - S3 접근 권한 포함
resource "aws_iam_role" "bastion_role" {
  name = "${var.project_name}-bastion-role"

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
    Name = "${var.project_name}-bastion-role"
    Type = "IAM-Role"
    Role = "Bastion"
  })
}

# Bastion Host S3 Access Policy
resource "aws_iam_policy" "bastion_s3_policy" {
  name        = "${var.project_name}-bastion-s3-policy"
  description = "S3 access policy for bastion host - allows read/write to empick buckets"

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "s3:ListBucket",
          "s3:GetBucketLocation",
          "s3:ListBucketMultipartUploads"
        ]
        Resource = [
          "arn:aws:s3:::${var.project_name}-*"
        ]
      },
      {
        Effect = "Allow"
        Action = [
          "s3:GetObject",
          "s3:PutObject",
          "s3:DeleteObject",
          "s3:AbortMultipartUpload",
          "s3:ListMultipartUploadParts"
        ]
        Resource = [
          "arn:aws:s3:::${var.project_name}-*/*"
        ]
      },
      {
        Effect = "Allow"
        Action = [
          "s3:ListAllMyBuckets"
        ]
        Resource = "*"
      }
    ]
  })

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-bastion-s3-policy"
    Type = "IAM-Policy"
    Role = "S3-Access"
  })
}

# Basic EC2 management permissions for bastion
resource "aws_iam_policy" "bastion_ec2_policy" {
  name        = "${var.project_name}-bastion-ec2-policy"
  description = "Basic EC2 management permissions for bastion host"

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "ec2:DescribeInstances",
          "ec2:DescribeInstanceStatus",
          "ec2:DescribeImages",
          "ec2:DescribeSnapshots",
          "ec2:DescribeVolumes"
        ]
        Resource = "*"
      }
    ]
  })

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-bastion-ec2-policy"
    Type = "IAM-Policy"
    Role = "EC2-Management"
  })
}

# Attach S3 policy to bastion role
resource "aws_iam_role_policy_attachment" "bastion_s3_policy_attachment" {
  role       = aws_iam_role.bastion_role.name
  policy_arn = aws_iam_policy.bastion_s3_policy.arn
}

# Attach EC2 policy to bastion role
resource "aws_iam_role_policy_attachment" "bastion_ec2_policy_attachment" {
  role       = aws_iam_role.bastion_role.name
  policy_arn = aws_iam_policy.bastion_ec2_policy.arn
}

# Bastion Host Instance Profile
resource "aws_iam_instance_profile" "bastion_profile" {
  name = "${var.project_name}-bastion-profile"
  role = aws_iam_role.bastion_role.name

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-bastion-profile"
    Type = "IAM-Instance-Profile"
    Role = "Bastion"
  })
}

# Bastion Host (SSH 접속 게이트웨이)
# Public Subnet에 배치되어 인터넷에서 SSH 접속을 받고, Private 서브넷으로 연결해주는 역할
resource "aws_instance" "bastion" {
  ami                    = var.ami_id # TODO: 최신 Amazon Linux 2023 AMI
  instance_type          = "t3.micro" # 최소 사양 (관리용)
  key_name               = data.aws_key_pair.empick_key.key_name
  vpc_security_group_ids = [aws_security_group.bastion_sg.id]
  subnet_id              = aws_subnet.public_subnets[0].id # Public Subnet AZ-A
  iam_instance_profile   = aws_iam_instance_profile.bastion_profile.name

  # Bastion Host는 고정 IP가 필요할 수 있음
  associate_public_ip_address = true

  # 기본 관리 도구 설치 - 다중 인스턴스 지원
  user_data = base64encode(templatefile("${path.module}/bastion_user_data.sh", {
    # Blue-Green 다중 인스턴스 IP 정보
    SPRINGBOOT_BLUE_IP  = aws_instance.springboot[0].private_ip # Blue 인스턴스 (AZ-A)
    SPRINGBOOT_GREEN_IP = aws_instance.springboot[1].private_ip # Green 인스턴스 (AZ-C)

    # 공유 리소스 정보
    DB_HOST             = aws_db_instance.mariadb.endpoint
    DB_PORT             = var.db_port
    DB_USERNAME         = var.db_username
    REDIS_HOST          = aws_elasticache_replication_group.redis.primary_endpoint_address
    REDIS_PORT          = var.redis_port
    PRIVATE_KEY_CONTENT = var.private_key_content
  }))

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-bastion"
    Type = "Bastion"
    Role = "SSH-Gateway"
  })

  # 의존성: Spring Boot 서버가 먼저 생성되어야 Private IP를 참조할 수 있음
  depends_on = [
    aws_instance.springboot,
    aws_db_instance.mariadb,
    aws_elasticache_replication_group.redis,
    aws_iam_instance_profile.bastion_profile
  ]
}

# Elastic IP for Bastion Host (선택적)
# 고정 IP가 필요한 경우 주석 해제
# resource "aws_eip" "bastion_eip" {
#   instance = aws_instance.bastion.id
#   domain   = "vpc"
#   
#   tags = merge(var.common_tags, {
#     Name = "${var.project_name}-bastion-eip"
#     Type = "ElasticIP"
#   })
# }

# ================================================================================
# 💡 Bastion Host 구성 요소 설명:
# 
# 1. EC2 Instance: Public Subnet에 배치된 SSH 게이트웨이
# 2. Public IP: 인터넷에서 SSH 접속 가능
# 3. Management Tools: 
#    - 기본 도구: htop, vim, curl, wget, tree, git, nmap
#    - 네트워크 진단: nc, telnet, nethogs, sysstat, iotop
#    - DB 클라이언트: MariaDB 105 클라이언트
#    - 캐시 클라이언트: Redis 6 클라이언트
# 4. SSH Optimization: 연결 유지 시간 최적화 (5분 간격)
# 
# 🎯 주요 기능 및 역할:
# 
# **1. 인프라 관리 워크스테이션**
# - Private 서브넷의 모든 서버에 SSH 접속 중계
# - 안전한 관리 접근 경로 제공 (보안 게이트웨이)
# - 모든 관리 작업의 중앙 집중화
# 
# **2. 데이터베이스 관리 및 운영**
# - 직접 DB 접속: mysql -h <RDS_ENDPOINT> -u admin -p
# - 데이터 조회, 스키마 변경, 백업/복원 작업
# - 성능 모니터링 및 쿼리 최적화
# - 장애 시 빠른 진단 및 복구
# 
# **3. Redis 캐시 관리**
# - Redis 상태 모니터링: redis-cli info memory
# - 세션 관리: keys "session:*" | wc -l
# - 캐시 플러시 및 데이터 정리
# - 메모리 사용량 최적화
# 
# **4. 시스템 모니터링 및 진단**
# - 네트워크 연결 테스트 (nc, telnet, nmap)
# - 성능 분석 (htop, iotop, nethogs)
# - 로그 분석 및 문제 해결
# - 리소스 사용량 모니터링
# 
# 🔐 보안 아키텍처:
# 
# **접근 제어 구조:**
# 인터넷 → Bastion Host (Public) → Private Resources
#                                 ├── Spring Boot Server
#                                 ├── RDS MariaDB  
#                                 └── Redis ElastiCache
# 
# **보안 정책:**
# - SSH 포트만 개방 (22번), 다른 모든 포트 차단
# - 키 페어 기반 인증 (비밀번호 로그인 비활성화)
# - 세션 타임아웃 설정 (5분 무응답 시 연결 종료)
# - 접속 로그 기록 및 감사 추적
# - IP 기반 접근 제한 권장 (특정 관리자 IP만 허용)
# 
# 🛠️ 설치된 관리 도구들:
# 
# **데이터베이스 관리:**
# - MariaDB 클라이언트: mysql, mysqldump, mysqladmin
# - 별칭: db-connect, db-status
# 
# **캐시 관리:**
# - Redis 클라이언트: redis-cli
# - 별칭: redis-connect, redis-ping
# 
# **네트워크 진단:**
# - 연결 테스트: nc (netcat), telnet
# - 포트 스캔: nmap
# - 트래픽 모니터링: nethogs
# 
# **시스템 모니터링:**
# - 프로세스: htop, ps
# - I/O 모니터링: iotop
# - 시스템 통계: sysstat
# - 파일 시스템: tree, df, du
# 
# 📋 실제 사용 시나리오:
# 
# **1. 일상적인 관리 작업**
# ssh bastion
# db-connect                    # DB 접속하여 사용자 관리
# redis-connect                 # Redis 상태 확인
# ssh springboot-server         # 애플리케이션 서버 관리
# 
# **2. 장애 대응 상황**
# # DB 연결 문제 진단
# nc -zv <DB_HOST> 3306        # DB 포트 연결 테스트
# mysql -h <DB_HOST> -u admin -p -e "SELECT 1"  # DB 쿼리 테스트
# 
# # Redis 메모리 부족 해결
# redis-cli info memory        # 메모리 사용량 확인
# redis-cli flushdb            # 필요시 캐시 정리
# 
# **3. 데이터 분석 및 백업**
# mysqldump -h <DB_HOST> -u admin -p empickdb > backup.sql
# mysql -h <DB_HOST> -u admin -p -e "SELECT COUNT(*) FROM users"
# 
# **4. 성능 모니터링**
# htop                         # 시스템 리소스 확인
# iotop                        # 디스크 I/O 모니터링
# nethogs                      # 네트워크 사용량 확인
# 
# 🚨 장애 대응 프로세스:
# 
# 1. **문제 발생 감지**
#    - 애플리케이션 에러 발생
#    - 사용자 접속 불가 신고
# 
# 2. **Bastion을 통한 진단**
#    - SSH로 Bastion 접속
#    - 네트워크 연결 상태 확인
#    - DB/Redis 접속 테스트
# 
# 3. **문제 해결**
#    - 직접 DB 쿼리로 데이터 확인
#    - Redis 캐시 정리
#    - 애플리케이션 서버 재시작
# 
# 4. **복구 확인**
#    - 서비스 정상 동작 검증
#    - 로그 확인 및 근본 원인 분석
# 
# 📊 모니터링 별칭 활용:
# 
# **시스템 상태 확인:**
# ports      # 열린 포트 확인 (netstat -tuln)
# processes  # 실행 중인 프로세스 (ps aux)
# disk       # 디스크 사용량 (df -h)
# memory     # 메모리 사용량 (free -h)
# 
# **데이터베이스 관리:**
# db-connect # 즉시 DB 접속
# db-status  # DB 서버 상태 확인
# 
# **Redis 캐시 관리:**
# redis-connect  # 즉시 Redis 접속
# redis-ping     # Redis 연결 테스트
# 
# ⚠️ 운영 시 주의사항:
# 
# **보안 강화:**
# - Elastic IP 할당으로 고정 IP 사용
# - 보안 그룹에서 관리자 IP만 SSH 허용
# - 정기적인 보안 패치 및 업데이트
# - SSH 키 정기 교체 (3-6개월)
# 
# **접근 관리:**
# - 관리자별 개별 SSH 키 발급
# - 접속 로그 정기 검토
# - 불필요한 권한 제거
# - 퇴사자 계정 즉시 비활성화
# 
# **백업 및 복구:**
# - 정기적인 DB 백업 스크립트 실행
# - 백업 파일 검증 및 복구 테스트
# - 장애 대응 매뉴얼 숙지
# 
# **성능 최적화:**
# - 정기적인 시스템 리소스 모니터링
# - 불필요한 프로세스 정리
# - 로그 파일 크기 관리
# ================================================================================ 
