# =============================================================================
# VPC Peering Variables
# =============================================================================

variable "project_name" {
  description = "프로젝트 이름"
  type        = string
  default     = "empick"
}

variable "environment" {
  description = "환경 (production, staging, development)"
  type        = string
  default     = "production"
}

variable "vpc1_cidr" {
  description = "VPC1 (Frontend) CIDR 블록"
  type        = string
  default     = "172.16.0.0/16"
}

variable "vpc2_cidr" {
  description = "VPC2 (Backend) CIDR 블록"
  type        = string
  default     = "10.0.0.0/16"
}

variable "dns_zone_name" {
  description = "Private DNS Zone 이름"
  type        = string
  default     = "empick.internal"
}

variable "backend_subdomain" {
  description = "Backend 서브도메인"
  type        = string
  default     = "backend"
}
