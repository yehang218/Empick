# ================================================================================
# 이 파일은 VPC 2 Backend의 RDS MariaDB 데이터베이스를 정의합니다.
# RDS는 AWS의 관리형 데이터베이스 서비스로, 백업/복원/패치를 자동으로 처리합니다.
# ================================================================================

# DB Subnet Group 생성 (데이터베이스가 사용할 서브넷 그룹)
# RDS 인스턴스는 최소 2개의 서로 다른 AZ에 있는 서브넷이 필요합니다
resource "aws_db_subnet_group" "rds_subnet_group" {
  name        = "${var.project_name}-rds-subnet-group"
  description = "Database subnet group for RDS instances" # 영문 description (한글 사용 금지)
  subnet_ids  = aws_subnet.private_subnets[*].id          # 모든 Private 서브넷 포함 (Multi-AZ 지원)

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-rds-subnet-group"
    Type = "Database"
  })
}

# DB Parameter Group 생성 (데이터베이스 설정 그룹)
# MariaDB의 성능 및 동작을 조정하는 설정들을 관리합니다
resource "aws_db_parameter_group" "rds_parameter_group" {
  family      = "mariadb10.11" # TODO: MariaDB 버전에 맞게 업데이트
  name        = "${var.project_name}-rds-parameter-group"
  description = "Custom parameter group for MariaDB"

  # 기본 성능 최적화 파라미터들
  parameter {
    name  = "innodb_buffer_pool_size"     # InnoDB 버퍼 풀 크기 (메모리의 70-80%)
    value = "{DBInstanceClassMemory*3/4}" # 인스턴스 메모리의 75% 사용
  }

  parameter {
    name  = "max_connections" # 최대 동시 연결 수
    value = "100"             # t3.micro에 적합한 연결 수
  }

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-rds-parameter-group"
    Type = "Database"
  })
}

# RDS MariaDB 인스턴스 생성
# 실제 데이터베이스 서버입니다
resource "aws_db_instance" "mariadb" {
  # 기본 인스턴스 설정
  identifier     = "${var.project_name}-mariadb" # RDS 인스턴스 식별자
  engine         = "mariadb"                     # 데이터베이스 엔진
  engine_version = "10.11.8"                     # TODO: 최신 stable 버전으로 업데이트
  instance_class = var.db_instance_class         # db.t3.micro

  # 데이터베이스 설정
  db_name  = var.db_name     # 생성될 데이터베이스 이름
  username = var.db_username # 관리자 계정명
  password = var.db_password # 관리자 비밀번호 (terraform.tfvars에서 설정)

  # 스토리지 설정
  allocated_storage     = var.db_allocated_storage # 할당 스토리지 (20GB)
  max_allocated_storage = 100                      # 자동 확장 최대 크기 (100GB)
  storage_type          = var.db_storage_type      # gp3 (최신 SSD)
  storage_encrypted     = true                     # 스토리지 암호화 활성화

  # 네트워크 및 보안 설정
  db_subnet_group_name   = aws_db_subnet_group.rds_subnet_group.name # 위에서 생성한 서브넷 그룹
  vpc_security_group_ids = [aws_security_group.rds_sg.id]            # RDS 보안 그룹 연결

  # 가용성 설정 (비용 최적화: Single-AZ)
  multi_az          = false                     # Multi-AZ 비활성화 (비용 절약)
  availability_zone = var.availability_zones[0] # AZ-A에 배치 (아키텍처 요구사항)

  # 접근 설정
  publicly_accessible = false       # 인터넷에서 직접 접근 불가 (보안)
  port                = var.db_port # 3306 포트

  # 백업 설정 (비용 최적화)
  backup_retention_period = 3             # 백업 보관 기간 (3일, 최소값)
  backup_window           = "03:00-04:00" # 백업 시간 (새벽 3-4시, 한국 시간 기준)

  # 유지보수 설정
  maintenance_window = "sun:04:00-sun:05:00" # 유지보수 시간 (일요일 새벽 4-5시)

  # 성능 설정
  parameter_group_name = aws_db_parameter_group.rds_parameter_group.name # 위에서 생성한 파라미터 그룹

  # 모니터링 설정 (기본)
  monitoring_interval = 0 # 향상된 모니터링 비활성화 (비용 절약)

  # 삭제 방지 설정
  deletion_protection = false # 개발 환경이므로 삭제 방지 비활성화
  skip_final_snapshot = true  # 삭제 시 최종 스냅샷 건너뛰기 (개발용)

  # 로그 설정 (필요시 활성화)
  enabled_cloudwatch_logs_exports = [] # CloudWatch 로그 비활성화 (비용 절약)

  tags = merge(var.common_tags, {
    Name   = "${var.project_name}-mariadb"
    Type   = "Database"
    Engine = "MariaDB"
  })

  # 의존성 명시: 보안 그룹과 서브넷 그룹이 먼저 생성되어야 함
  depends_on = [
    aws_db_subnet_group.rds_subnet_group,
    aws_security_group.rds_sg
  ]
}



# ================================================================================
# 💡 RDS MariaDB 구성 요소 설명:
# 
# 1. DB Subnet Group: RDS가 사용할 서브넷들 정의 (Multi-AZ 지원용)
# 2. DB Parameter Group: MariaDB 성능 최적화 설정
# 3. RDS Instance: 실제 MariaDB 데이터베이스 서버
# 
# 🎯 아키텍처 반영:
# - Private 서브넷에 배치 (보안)
# - Single-AZ 배치 (AZ-A) - 비용 최적화
# - 다중 Spring Boot 인스턴스에서 공유 사용 (Blue-Green 배포 지원)
# - 공유 사용 장점: 데이터 일관성, 실시간 동기화, Connection Pool 독립 관리
# 
# 💰 비용 최적화 요소:
# - Single-AZ: Multi-AZ 대비 50% 비용 절약
# - 백업 3일: 기본 7일 대비 비용 절약  
# - 모니터링 비활성화: 월 $15 절약
# - t3.micro: 가장 저렴한 인스턴스 클래스
# 
# 🔐 보안 요소:
# - 스토리지 암호화: 데이터 보안
# - Private 서브넷: 인터넷 직접 접근 차단
# - 보안 그룹: Spring Boot와 Bastion에서만 접근 허용
# 
# ⚠️ 주의사항:
# - db_password는 terraform.tfvars에서 반드시 설정 필요
# - 운영 환경에서는 Multi-AZ, 백업 기간 증가 권장
# - MariaDB 버전은 정기적으로 업데이트 필요
# ================================================================================ 
