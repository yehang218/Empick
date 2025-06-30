# ================================================================================
# VPC1 Frontend 변수 정의
# Frontend 인프라에 필요한 모든 변수들을 정의합니다
# ================================================================================

# VPC Configuration (가상 네트워크 설정)
variable "vpc_cidr" {
  description = "CIDR block for VPC1 Frontend"
  type        = string
  default     = "172.16.0.0/16" # VPC1 Frontend IP 범위
}

variable "project_name" {
  description = "Project name for resource naming"
  type        = string
  default     = "empick"
}

variable "environment" {
  description = "Environment name"
  type        = string
  default     = "dev"
}

# Region and AZ Configuration (지역 및 가용영역 설정)
variable "aws_region" {
  description = "AWS region"
  type        = string
  default     = "ap-northeast-2" # 서울 리전
}

variable "availability_zones" {
  description = "List of availability zones"
  type        = list(string)
  default     = ["ap-northeast-2a", "ap-northeast-2c"] # VPC2와 동일한 AZ
}

# Subnet Configuration (서브넷 설정)
variable "public_subnet_cidrs" {
  description = "CIDR blocks for public subnets"
  type        = list(string)
  default     = ["172.16.1.0/24", "172.16.2.0/24"] # Blue/Green용 Public Subnet
}

# EC2 Configuration (가상 서버 설정)
variable "nginx_instance_type" {
  description = "Instance type for Nginx servers"
  type        = string
  default     = "t3.small" # 메모리 부족 해결 (1GB → 2GB)
}

variable "nginx_instance_count" {
  description = "Number of Nginx instances (Blue/Green)"
  type        = number
  default     = 2 # Blue + Green
}

variable "key_name" {
  description = "Name of the AWS key pair"
  type        = string
  default     = "camp" # VPC2와 동일한 키
}

variable "public_key" {
  description = "Public key content for EC2 key pair"
  type        = string
}

# AMI Configuration (가상머신 이미지 설정)
variable "ami_id" {
  description = "AMI ID for EC2 instances"
  type        = string
  default     = "ami-0c593c3690c32e925" # Amazon Linux 2023 (dnf 호환)
}

# Application Configuration (애플리케이션 설정)
variable "nginx_port" {
  description = "Nginx HTTP port"
  type        = number
  default     = 80
}

variable "nginx_ssl_port" {
  description = "Nginx HTTPS port"
  type        = number
  default     = 443
}

# VPC Peering Configuration (VPC 연결 설정)
variable "vpc2_cidr" {
  description = "VPC2 Backend CIDR for peering"
  type        = string
  default     = "10.0.0.0/16" # VPC2 Backend IP 범위
}

# Tags (리소스 태그 설정)
variable "common_tags" {
  description = "Common tags for all resources"
  type        = map(string)
  default = {
    Project     = "empick"
    Environment = "dev"
    Owner       = "piveguyz"
    ManagedBy   = "terraform"
    VPC         = "vpc1-frontend"
  }
}

# ALB Configuration (로드밸런서 설정)
variable "alb_name" {
  description = "Name for Application Load Balancer"
  type        = string
  default     = "vpc1-frontend-alb" # Frontend용 ALB 이름
}

# SSL Configuration (SSL 인증서 설정)
variable "ssl_certificate_arn" {
  description = "ARN of SSL certificate from ACM"
  type        = string
  default     = "" # terraform.tfvars에서 설정 (HTTPS 사용시)
}

variable "enable_https" {
  description = "Enable HTTPS listener and HTTP redirect"
  type        = bool
  default     = false # 개발 환경에서는 HTTP만 사용
}

# Domain Configuration (도메인 설정)
variable "domain_name" {
  description = "Primary domain name for Frontend"
  type        = string
  default     = ""
}

variable "subdomain" {
  description = "Subdomain for Frontend"
  type        = string
  default     = "www"
}

variable "create_route53_zone" {
  description = "Create new Route 53 hosted zone"
  type        = bool
  default     = true
}

variable "route53_zone_id" {
  description = "Existing Route 53 hosted zone ID (if create_route53_zone is false)"
  type        = string
  default     = ""
}

variable "enable_ssl" {
  description = "Enable SSL certificate creation and HTTPS"
  type        = bool
  default     = true
}

variable "ssl_validation_timeout" {
  description = "SSL certificate validation timeout"
  type        = string
  default     = "10m"
}

# Health Check Configuration (상태 확인 설정)
variable "enable_health_check" {
  description = "Enable Route 53 health check"
  type        = bool
  default     = true
}

variable "health_check_failure_threshold" {
  description = "Number of consecutive health check failures"
  type        = number
  default     = 3
}

variable "health_check_request_interval" {
  description = "Health check request interval in seconds"
  type        = number
  default     = 30
}

# S3 Configuration (S3 버킷 설정)
variable "s3_bucket_name" {
  description = "S3 bucket name for build artifacts"
  type        = string
  default     = "empick-private-bucket" # VPC2 Backend와 동일한 버킷 사용
}

# ================================================================================
# 💡 주요 변수 설명:
# 
# 🌐 네트워크:
# - vpc_cidr: 172.16.0.0/16 (VPC1 Frontend 전용)
# - public_subnet_cidrs: Blue/Green 배포용 2개 서브넷
# - availability_zones: VPC2와 동일한 AZ 사용
# 
# 🖥️ 서버:
# - nginx_instance_type: t3.micro (비용 최적화)
# - nginx_instance_count: 2개 (Blue/Green)
# - ami_id: Amazon Linux 2023 (dnf 호환)
# 
# 🔗 연결:
# - vpc2_cidr: VPC Peering용 Backend CIDR
# - domain_name: Route 53 DNS용 도메인
# ================================================================================ 
