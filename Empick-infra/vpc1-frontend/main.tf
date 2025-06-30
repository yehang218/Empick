# ================================================================================
# VPC1 Frontend 기본 네트워크 인프라
# Public Subnet 기반 Nginx Blue/Green 배포를 위한 네트워크 구성
# ================================================================================

# Provider 설정
terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

# AWS 리전 설정
provider "aws" {
  region = var.aws_region
}

# VPC 생성 (Frontend용 가상 네트워크)
resource "aws_vpc" "vpc1_frontend" {
  cidr_block           = var.vpc_cidr # 172.16.0.0/16
  enable_dns_hostnames = true
  enable_dns_support   = true

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-vpc1-frontend"
    Type = "Frontend"
  })
}

# Internet Gateway 생성 (인터넷 연결)
resource "aws_internet_gateway" "vpc1_igw" {
  vpc_id = aws_vpc.vpc1_frontend.id

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-vpc1-igw"
  })
}

# Public Subnet 생성 (Blue/Green 배포용)
resource "aws_subnet" "public_subnets" {
  count = length(var.public_subnet_cidrs)

  vpc_id                  = aws_vpc.vpc1_frontend.id
  cidr_block              = var.public_subnet_cidrs[count.index]
  availability_zone       = var.availability_zones[count.index]
  map_public_ip_on_launch = true

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-public-subnet-${count.index + 1}"
    Type = "Public"
    AZ   = var.availability_zones[count.index]
  })
}

# Public Route Table 생성 (인터넷 라우팅)
resource "aws_route_table" "public_rt" {
  vpc_id = aws_vpc.vpc1_frontend.id

  # 모든 인터넷 트래픽을 IGW로 전송
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.vpc1_igw.id
  }

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-public-rt"
  })
}

# Public Subnet과 Route Table 연결
resource "aws_route_table_association" "public_rta" {
  count = length(aws_subnet.public_subnets)

  subnet_id      = aws_subnet.public_subnets[count.index].id
  route_table_id = aws_route_table.public_rt.id
}

# ================================================================================
# 💡 VPC1 Frontend 네트워크 구조:
# 
# VPC1 (172.16.0.0/16)
# ├── AZ-A (ap-northeast-2a)
# │   └── Public Subnet 1 (172.16.1.0/24) → Nginx Blue
# ├── AZ-C (ap-northeast-2c)
# │   └── Public Subnet 2 (172.16.2.0/24) → Nginx Green
# ├── Internet Gateway → 인터넷 연결
# └── Public Route Table → 0.0.0.0/0 → IGW
# 
# 🎯 특징:
# - Private Subnet 없음 (단순화)
# - NAT Gateway 없음 (비용 절약)
# - Multi-AZ 고가용성 확보
# - VPC2 Backend와 동일한 AZ 사용
# ================================================================================ 
