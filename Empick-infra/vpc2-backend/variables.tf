# ================================================================================
# 이 파일은 terraform에서 사용될 변수들을 정의하는 파일입니다.
# 변수를 사용하면 코드를 재사용하기 쉽고, 값을 쉽게 변경할 수 있습니다.
# ================================================================================

# VPC Configuration (가상 네트워크 설정)
# VPC는 AWS에서 제공하는 가상의 네트워크 공간입니다
variable "vpc_cidr" {
  description = "CIDR block for VPC 2 Backend"
  type        = string
  default     = "10.0.0.0/16" # 10.0.0.1 ~ 10.0.255.254 IP 범위를 사용
}

variable "project_name" {
  description = "Project name for resource naming"
  type        = string
  default     = "empick" # 모든 AWS 리소스 이름 앞에 붙을 프로젝트명
}

variable "environment" {
  description = "Environment name"
  type        = string
  default     = "dev" # 개발(dev), 운영(prod) 등 환경 구분용
}

# Region and AZ Configuration (지역 및 가용영역 설정)
# AWS는 전 세계 여러 지역(Region)에 데이터센터를 운영합니다
# 각 지역은 여러 가용영역(AZ)으로 나뉘어 장애 대비를 합니다
variable "aws_region" {
  description = "AWS region"
  type        = string
  default     = "ap-northeast-2" # 서울 리전 (한국에서 가장 가까움)
}

variable "availability_zones" {
  description = "List of availability zones"
  type        = list(string)
  default     = ["ap-northeast-2a", "ap-northeast-2c"] # 서울 리전의 두 가용영역
}

# Subnet Configuration (서브넷 설정)
# 서브넷은 VPC 내부를 더 작은 네트워크로 나눈 것입니다
# Public: 인터넷에 연결 가능 (웹서버, 로드밸런서 등)
# Private: 인터넷에 직접 연결 불가 (데이터베이스, 내부 서버 등)
variable "public_subnet_cidrs" {
  description = "CIDR blocks for public subnets"
  type        = list(string)
  default     = ["10.0.1.0/24", "10.0.2.0/24"] # 각각 254개 IP 주소 사용 가능
}

variable "private_subnet_cidrs" {
  description = "CIDR blocks for private subnets"
  type        = list(string)
  default     = ["10.0.11.0/24", "10.0.12.0/24"] # 데이터베이스용 보안 서브넷
}

# EC2 Configuration (가상 서버 설정)
# EC2는 AWS의 가상 서버 서비스입니다
# 인스턴스 타입에 따라 CPU, 메모리, 네트워크 성능이 달라집니다
variable "bastion_instance_type" {
  description = "Instance type for bastion host"
  type        = string
  default     = "t3.nano" # 매우 작은 사양 (월 $3.80) - 관리용 서버라 작아도 OK
}

variable "springboot_instance_type" {
  description = "Instance type for Spring Boot server"
  type        = string
  default     = "t3.small" # 작은 사양 (월 $19.27) - 개발용으로 충분
}

variable "key_name" {
  description = "Name of the AWS key pair"
  type        = string
  default     = "camp"
}

# RDS Configuration (데이터베이스 설정)
# RDS는 AWS의 관리형 데이터베이스 서비스입니다
# 백업, 복원, 보안 패치 등을 AWS가 자동으로 관리해줍니다
variable "db_instance_class" {
  description = "RDS instance class"
  type        = string
  default     = "db.t3.micro" # 가장 작은 DB 사양 (월 $12.41) - 개발용
}

variable "db_name" {
  description = "Database name (alphanumeric only)"
  type        = string
  default     = "empickdb" # 생성될 데이터베이스 이름 (하이픈 사용금지!)
}

variable "db_username" {
  description = "Database master username"
  type        = string
  default     = "piveguyz" # 데이터베이스 관리자 계정명
}

variable "db_password" {
  description = "Database master password"
  type        = string
  sensitive   = true # TODO: terraform.tfvars에서 실제 강력한 비밀번호로 설정 필요
}

variable "db_allocated_storage" {
  description = "Database allocated storage in GB (minimum 20 for gp3)"
  type        = number
  default     = 20 # 데이터베이스 저장 공간 (GB) - gp3는 최소 20GB 필요
}

variable "db_storage_type" {
  description = "Database storage type"
  type        = string
  default     = "gp3" # 최신 SSD 타입 (빠르고 비용효율적)
}

# ALB Configuration (로드밸런서 설정)
# ALB는 여러 서버에 트래픽을 분산시켜주는 서비스입니다
variable "alb_name" {
  description = "Name for Application Load Balancer"
  type        = string
  default     = "vpc2-backend-alb" # 백엔드용 로드밸런서 이름
}

# AMI Configuration (가상머신 이미지 설정)
# AMI는 EC2 인스턴스를 만들 때 사용할 운영체제 이미지입니다
variable "ami_id" {
  description = "AMI ID for EC2 instances"
  type        = string
  default     = "ami-0c2acfcb2ac4d02a0" # TODO: 최신 Amazon Linux 2023 AMI ID로 업데이트 (시간이 지나면 변경됨)
}

# Application Configuration (애플리케이션 설정)
# 애플리케이션이 사용할 포트 번호들을 정의합니다
variable "springboot_port" {
  description = "Spring Boot application port"
  type        = number
  default     = 8080 # Spring Boot 기본 포트
}

variable "db_port" {
  description = "Database port"
  type        = number
  default     = 3306 # MariaDB/MySQL 기본 포트
}

variable "springboot_instance_count" {
  description = "Number of Spring Boot instances"
  type        = number
  default     = 2 # 고가용성을 위해 2대 운영 (각 AZ에 1대씩)
}

# Redis Configuration (Redis 캐시 서버 설정)
# Redis는 인메모리 데이터 구조 저장소로, 캐싱과 세션 관리에 사용됩니다
variable "redis_instance_type" {
  description = "Redis instance type"
  type        = string
  default     = "cache.t3.micro" # 가장 작은 Redis 사양 (월 $12.50) - 개발용
}

variable "redis_port" {
  description = "Redis port"
  type        = number
  default     = 6379 # Redis 기본 포트
}

variable "redis_parameter_group_family" {
  description = "Redis parameter group family"
  type        = string
  default     = "redis7.x" # TODO: 최신 Redis 버전에 맞게 업데이트
}

variable "redis_engine_version" {
  description = "Redis engine version"
  type        = string
  default     = "7.0" # TODO: 최신 stable 버전으로 업데이트
}

variable "redis_num_cache_nodes" {
  description = "Number of cache nodes in the Redis cluster"
  type        = number
  default     = 1 # 단일 노드 (비용 최적화)
}

# VPC Peering Configuration (VPC 연결 설정)
# 다른 VPC와 연결할 때 필요한 설정입니다
variable "vpc1_cidr" {
  description = "VPC 1 CIDR for peering connection"
  type        = string
  default     = "172.16.0.0/16" # 프론트엔드 VPC의 IP 범위
}

# Tags (리소스 태그 설정)
# 태그는 AWS 리소스에 붙이는 라벨입니다
# 비용 관리, 리소스 구분, 자동화 등에 사용됩니다
variable "common_tags" {
  description = "Common tags for all resources"
  type        = map(string)
  default = {
    Project     = "empick"       # 프로젝트 이름
    Environment = "dev"          # 환경 구분 (개발/운영)
    ManagedBy   = "terraform"    # 관리 도구 표시
    VPC         = "vpc2-backend" # VPC 구분
  }
}

# ================================================================================
# EC2 및 애플리케이션 관련 추가 변수들
# ================================================================================

# SSH 키 페어 설정
variable "public_key" {
  description = "SSH public key for EC2 instances"
  type        = string
  # TODO: terraform.tfvars에서 실제 공개 키 설정
}

variable "private_key_content" {
  description = "Private key content for SSH access to Spring Boot server from Bastion (camp.pem 파일 내용)"
  type        = string
  sensitive   = true
  # TODO: terraform.tfvars에서 camp.pem 파일의 전체 내용 설정
  # 보안상 민감한 정보이므로 sensitive = true로 설정
}

# SSL/TLS 인증서 설정 (HTTPS 지원용)
variable "ssl_certificate_arn" {
  description = "SSL certificate ARN for HTTPS listener (AWS Certificate Manager에서 발급받은 인증서 ARN)"
  type        = string
  default     = ""
  # TODO: 운영 환경에서는 ACM에서 SSL 인증서 발급 후 ARN 설정
  # 개발 환경에서는 빈 문자열로 두면 HTTP만 사용
  # 예시: "arn:aws:acm:ap-northeast-2:123456789012:certificate/12345678-1234-1234-1234-123456789012"
}

variable "enable_http_redirect" {
  description = "Enable HTTP to HTTPS redirect (HTTP 요청을 HTTPS로 자동 리다이렉트할지 여부)"
  type        = bool
  default     = false
  # SSL 인증서가 있는 경우에만 true로 설정 권장
  # true: http://도메인 → https://도메인 자동 리다이렉트
  # false: HTTP와 HTTPS 모두 독립적으로 동작
}

# Spring Boot 애플리케이션 설정
variable "jwt_secret" {
  description = "JWT secret key for Spring Boot application"
  type        = string
  sensitive   = true
  # TODO: terraform.tfvars에서 안전한 JWT 시크릿 설정
}

variable "s3_jar_path" {
  description = "S3 path to download Spring Boot JAR file"
  type        = string
  default     = "s3://empick-private-bucket/releases/empick-backend-latest.jar"
  # S3 경로 형식: s3://bucket-name/path/to/file.jar
}

# 이메일 서버 설정
variable "mail_host" {
  description = "SMTP server host for email functionality"
  type        = string
  default     = "smtp.gmail.com"
  # TODO: 실제 SMTP 서버로 설정
}

variable "mail_port" {
  description = "SMTP server port"
  type        = number
  default     = 587
}

variable "mail_username" {
  description = "SMTP server username"
  type        = string
  # TODO: terraform.tfvars에서 이메일 계정 설정
}

variable "mail_password" {
  description = "SMTP server password"
  type        = string
  sensitive   = true
  # TODO: terraform.tfvars에서 이메일 비밀번호 설정
}

# AWS S3 설정
variable "s3_bucket_name" {
  description = "S3 bucket name for file storage"
  type        = string
  # TODO: terraform.tfvars에서 S3 버킷명 설정
}

variable "aws_access_key" {
  description = "AWS access key for S3 access"
  type        = string
  sensitive   = true
  # TODO: IAM Role 사용 권장, 임시용으로만 사용
}

variable "aws_secret_key" {
  description = "AWS secret key for S3 access"
  type        = string
  sensitive   = true
  # TODO: IAM Role 사용 권장, 임시용으로만 사용
}

# ================================================================================
# 💡 이 파일 사용법:
# 1. terraform.tfvars 파일에서 실제 값들을 설정합니다
# 2. 특히 db_password는 반드시 설정해야 합니다
# 3. 값을 바꾸고 싶으면 terraform.tfvars에서 덮어쓰기 하면 됩니다
# 
# 📋 TODO 체크리스트:
# 1. key_name: 실제 EC2 키 페어 이름으로 설정
# 2. db_password: 안전한 데이터베이스 비밀번호 설정  
# 3. ami_id: 최신 Amazon Linux 2023 AMI ID 확인
# 4. Redis 설정값들 운영 환경에 맞게 조정
# 5. terraform.tfvars 파일에서 실제 값들 설정
# 6. public_key: SSH 공개 키 설정
# 7. jwt_secret: 안전한 JWT 시크릿 생성
# 8. 이메일 서버 설정 (SMTP)
# 9. S3 버킷 생성 및 설정
# 10. spring_user_data.sh 스크립트 파일 생성
# ================================================================================
