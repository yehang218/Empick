# ================================================================================
# 이 파일은 VPC 2 Backend의 보안 그룹(Security Groups)을 정의합니다.
# 보안 그룹은 AWS의 가상 방화벽으로, 인바운드/아웃바운드 트래픽을 제어합니다.
# ================================================================================

# Bastion Host Security Group (관리용 서버 방화벽 설정)
# SSH 접속만 허용하는 최소 권한 보안 그룹입니다
resource "aws_security_group" "bastion_sg" {
  name        = "${var.project_name}-bastion-sg"
  description = "Security group for bastion host" # 영문 description (한글 사용 금지)
  vpc_id      = aws_vpc.vpc2_backend.id

  # 인바운드 규칙: SSH 접속 허용
  ingress {
    description = "SSH access from anywhere" # 전 세계에서 SSH 접속 허용
    from_port   = 22                         # SSH 포트
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"] # TODO: 실제 관리자 IP로 제한 권장
  }

  # 아웃바운드 규칙: 모든 외부 연결 허용
  egress {
    description = "All outbound traffic" # 모든 외부 연결 허용 (업데이트, 패키지 설치 등)
    from_port   = 0
    to_port     = 0
    protocol    = "-1" # 모든 프로토콜
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-bastion-sg"
    Type = "Bastion"
  })
}

# ALB Security Group (로드밸런서 방화벽 설정)
# 웹 트래픽(HTTP/HTTPS)을 허용하는 보안 그룹입니다
resource "aws_security_group" "alb_sg" {
  name        = "${var.project_name}-alb-sg"
  description = "Security group for application load balancer"
  vpc_id      = aws_vpc.vpc2_backend.id

  # 인바운드 규칙: HTTP 트래픽 허용
  ingress {
    description = "HTTP traffic from internet" # 인터넷에서 HTTP 접속 허용
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # 인바운드 규칙: HTTPS 트래픽 허용
  ingress {
    description = "HTTPS traffic from internet" # 인터넷에서 HTTPS 접속 허용
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # 인바운드 규칙: VPC 1에서의 접속 허용 (API 호출용)
  ingress {
    description = "API access from VPC 1" # 프론트엔드 VPC에서 API 호출 허용
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = [var.vpc1_cidr] # VPC 1의 IP 범위 (172.16.0.0/16)
  }

  # 아웃바운드 규칙: 모든 외부 연결 허용
  egress {
    description = "All outbound traffic"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-alb-sg"
    Type = "LoadBalancer"
  })
}

# Spring Boot Security Group (애플리케이션 서버 방화벽 설정)
# Spring Boot 애플리케이션 포트와 관리 접속을 허용하는 보안 그룹입니다
resource "aws_security_group" "springboot_sg" {
  name        = "${var.project_name}-springboot-sg"
  description = "Security group for Spring Boot application servers"
  vpc_id      = aws_vpc.vpc2_backend.id

  # 인바운드 규칙: ALB에서 Spring Boot 포트 접속 허용
  ingress {
    description     = "Spring Boot port from ALB" # ALB에서만 애플리케이션 접속 허용
    from_port       = var.springboot_port         # 8080 포트
    to_port         = var.springboot_port
    protocol        = "tcp"
    security_groups = [aws_security_group.alb_sg.id] # ALB 보안 그룹에서만 허용
  }

  # 인바운드 규칙: Bastion에서 SSH 접속 허용
  ingress {
    description     = "SSH access from bastion host" # 관리용 SSH 접속 허용
    from_port       = 22
    to_port         = 22
    protocol        = "tcp"
    security_groups = [aws_security_group.bastion_sg.id] # Bastion에서만 SSH 허용
  }

  # 아웃바운드 규칙: 모든 외부 연결 허용
  egress {
    description = "All outbound traffic" # 데이터베이스 연결, 외부 API 호출 등
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-springboot-sg"
    Type = "Application"
  })
}

# RDS Security Group (데이터베이스 방화벽 설정)
# 데이터베이스 포트 접속을 제한하는 최고 보안 수준의 보안 그룹입니다
resource "aws_security_group" "rds_sg" {
  name        = "${var.project_name}-rds-sg"
  description = "Security group for RDS MariaDB database"
  vpc_id      = aws_vpc.vpc2_backend.id

  # 인바운드 규칙: Spring Boot에서 데이터베이스 접속 허용
  ingress {
    description     = "Database access from Spring Boot servers" # 애플리케이션에서만 DB 접속 허용
    from_port       = var.db_port                                # 3306 포트 (MariaDB)
    to_port         = var.db_port
    protocol        = "tcp"
    security_groups = [aws_security_group.springboot_sg.id] # Spring Boot SG에서만 허용
  }

  # 인바운드 규칙: Bastion에서 데이터베이스 관리 접속 허용
  ingress {
    description     = "Database management access from bastion" # 관리용 데이터베이스 접속 허용
    from_port       = var.db_port
    to_port         = var.db_port
    protocol        = "tcp"
    security_groups = [aws_security_group.bastion_sg.id] # Bastion에서만 DB 관리 허용
  }

  # 아웃바운드 규칙: 일반적으로 데이터베이스는 아웃바운드 연결이 필요 없음
  # 하지만 백업, 모니터링 등을 위해 기본 설정 유지
  egress {
    description = "All outbound traffic"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-rds-sg"
    Type = "Database"
  })
}

# Redis Security Group (캐시 서버 방화벽 설정)
# Redis 캐시 서버 접속을 제한하는 보안 그룹입니다
resource "aws_security_group" "redis_sg" {
  name        = "${var.project_name}-redis-sg"
  description = "Security group for Redis cache server"
  vpc_id      = aws_vpc.vpc2_backend.id

  # 인바운드 규칙: Spring Boot에서 Redis 접속 허용
  ingress {
    description     = "Redis access from Spring Boot servers" # 애플리케이션에서만 Redis 접속 허용
    from_port       = var.redis_port                          # 6379 포트 (Redis)
    to_port         = var.redis_port
    protocol        = "tcp"
    security_groups = [aws_security_group.springboot_sg.id] # Spring Boot SG에서만 허용
  }

  # 인바운드 규칙: Bastion에서 Redis 관리 접속 허용
  ingress {
    description     = "Redis management access from bastion" # 관리용 Redis 접속 허용
    from_port       = var.redis_port
    to_port         = var.redis_port
    protocol        = "tcp"
    security_groups = [aws_security_group.bastion_sg.id] # Bastion에서만 Redis 관리 허용
  }

  # 아웃바운드 규칙: Redis는 일반적으로 아웃바운드 연결이 필요 없음
  egress {
    description = "All outbound traffic"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-redis-sg"
    Type = "Cache"
  })
}

# ================================================================================
# 💡 보안 그룹 아키텍처 설명:
# 
# 1. Bastion SG: 관리자만 SSH 접속 (22번 포트)
# 2. ALB SG: 인터넷에서 웹 트래픽 (80, 443번 포트) + VPC1에서 API 호출
# 3. Spring Boot SG: ALB에서만 애플리케이션 접속 (8080번 포트) + Bastion에서 SSH
# 4. RDS SG: Spring Boot와 Bastion에서만 데이터베이스 접속 (3306번 포트)
# 5. Redis SG: Spring Boot와 Bastion에서만 캐시 서버 접속 (6379번 포트)
# 
# 🔐 보안 원칙: 최소 권한 원칙 적용
# - 각 서버는 필요한 포트만 열어둠
# - 데이터베이스와 캐시는 애플리케이션에서만 접근 가능
# - 모든 관리 작업은 Bastion Host를 통해서만 수행
# 
# ⚠️ 주의사항: 
# - Bastion SSH 접속은 실제 관리자 IP로 제한하는 것이 좋습니다
# - 운영 환경에서는 더 엄격한 보안 정책 적용 권장
# ================================================================================ 
