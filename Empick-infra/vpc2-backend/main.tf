# ================================================================================
# 이 파일은 VPC 2 Backend의 기본 네트워크 인프라를 정의합니다.
# VPC, 서브넷, 게이트웨이, 라우팅 테이블 등을 생성합니다.
# ================================================================================

# Provider 설정 (AWS 서비스 사용을 위한 설정)
terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0" # TODO: 최신 stable 버전으로 업데이트 (5.x.x)
    }
  }
}

# AWS 리전 설정
provider "aws" {
  region = var.aws_region
}

# VPC 생성 (가상 네트워크 공간)
# VPC는 AWS 클라우드 내에서 논리적으로 격리된 네트워크 섹션입니다
resource "aws_vpc" "vpc2_backend" {
  cidr_block           = var.vpc_cidr # IP 주소 범위 (10.0.0.0/16)
  enable_dns_hostnames = true         # DNS 호스트명 활성화
  enable_dns_support   = true         # DNS 해석 활성화

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-vpc2-backend" # VPC 이름 태그
  })
}

# Internet Gateway 생성 (인터넷 연결 관문)
# VPC가 인터넷과 통신하기 위해 필수적인 구성요소입니다
resource "aws_internet_gateway" "vpc2_igw" {
  vpc_id = aws_vpc.vpc2_backend.id

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-vpc2-igw"
  })
}

# Public Subnet 생성 (인터넷 연결 가능한 서브넷)
# 웹서버, 로드밸런서, Bastion Host 등이 위치할 공간입니다
resource "aws_subnet" "public_subnets" {
  count = length(var.public_subnet_cidrs)

  vpc_id                  = aws_vpc.vpc2_backend.id
  cidr_block              = var.public_subnet_cidrs[count.index] # 각 서브넷의 IP 범위
  availability_zone       = var.availability_zones[count.index]  # 가용영역 분산 배치
  map_public_ip_on_launch = true                                 # 인스턴스에 자동으로 공인 IP 할당

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-public-subnet-${count.index + 1}"
    Type = "Public" # 서브넷 타입 구분
    AZ   = var.availability_zones[count.index]
  })
}

# Private Subnet 생성 (인터넷 직접 연결 불가능한 보안 서브넷)
# 데이터베이스, 내부 애플리케이션 등이 위치할 보안 공간입니다
resource "aws_subnet" "private_subnets" {
  count = length(var.private_subnet_cidrs)

  vpc_id            = aws_vpc.vpc2_backend.id
  cidr_block        = var.private_subnet_cidrs[count.index] # 각 서브넷의 IP 범위
  availability_zone = var.availability_zones[count.index]   # 가용영역 분산 배치

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-private-subnet-${count.index + 1}"
    Type = "Private" # 서브넷 타입 구분
    AZ   = var.availability_zones[count.index]
  })
}

# Elastic IP for NAT Gateway (NAT 게이트웨이용 고정 IP)
# 각 AZ별로 NAT Gateway를 위한 고정 공인 IP 주소를 생성합니다
resource "aws_eip" "nat_eip" {
  count = length(var.availability_zones) # 각 AZ별로 EIP 생성

  domain = "vpc" # VPC 내에서 사용

  depends_on = [aws_internet_gateway.vpc2_igw] # IGW 생성 후에 생성

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-nat-eip-${count.index + 1}"
    AZ   = var.availability_zones[count.index]
  })
}

# NAT Gateway 생성 (Public Subnet에 생성되어 Private Subnet의 인터넷 연결 중계)
# 각 AZ의 Public Subnet에 NAT Gateway를 생성하여 고가용성을 확보합니다
# ⚠️ 중요: NAT Gateway는 반드시 Public Subnet에 생성되어야 합니다!
resource "aws_nat_gateway" "vpc2_nat" {
  count = length(var.availability_zones) # 각 AZ별로 NAT Gateway 생성

  allocation_id = aws_eip.nat_eip[count.index].id           # 해당 AZ의 EIP 사용
  subnet_id     = aws_subnet.public_subnets[count.index].id # 해당 AZ의 Public 서브넷에 배치

  depends_on = [aws_internet_gateway.vpc2_igw]

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-nat-gateway-${count.index + 1}"
    AZ   = var.availability_zones[count.index]
  })
}

# Public Route Table 생성 (Public 서브넷용 라우팅 테이블)
# 인터넷 트래픽을 Internet Gateway로 보내는 규칙을 정의합니다
resource "aws_route_table" "public_rt" {
  vpc_id = aws_vpc.vpc2_backend.id

  # 모든 인터넷 트래픽(0.0.0.0/0)을 IGW로 전송
  route {
    cidr_block = "0.0.0.0/0"                      # 모든 IP 대상
    gateway_id = aws_internet_gateway.vpc2_igw.id # IGW로 전송
  }

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-public-rt"
  })
}

# Private Route Table 생성 (각 AZ별 Private 서브넷용 라우팅 테이블)
# 각 Private Subnet이 해당 AZ Public Subnet의 NAT Gateway를 사용하도록 라우팅 설정
resource "aws_route_table" "private_rt" {
  count = length(var.availability_zones) # 각 AZ별로 Private RT 생성

  vpc_id = aws_vpc.vpc2_backend.id

  # 모든 인터넷 트래픽을 해당 AZ의 NAT Gateway로 전송
  route {
    cidr_block     = "0.0.0.0/0"                              # 모든 IP 대상
    nat_gateway_id = aws_nat_gateway.vpc2_nat[count.index].id # 해당 AZ의 NAT Gateway로 전송
  }

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-private-rt-${count.index + 1}"
    AZ   = var.availability_zones[count.index]
  })
}

# Public Subnet과 Public Route Table 연결
# Public 서브넷들이 인터넷에 연결될 수 있도록 라우팅 테이블을 연결합니다
resource "aws_route_table_association" "public_rta" {
  count = length(aws_subnet.public_subnets)

  subnet_id      = aws_subnet.public_subnets[count.index].id # 각 Public 서브넷
  route_table_id = aws_route_table.public_rt.id              # Public 라우팅 테이블
}

# Private Subnet과 Private Route Table 연결
# 각 Private 서브넷이 해당 AZ Public Subnet의 NAT Gateway를 사용하도록 Route Table 연결
resource "aws_route_table_association" "private_rta" {
  count = length(aws_subnet.private_subnets)

  subnet_id      = aws_subnet.private_subnets[count.index].id # 각 Private 서브넷
  route_table_id = aws_route_table.private_rt[count.index].id # 해당 AZ의 Private 라우팅 테이블
}

# ================================================================================
# 💡 고가용성 NAT Gateway 구성 설명:
# 
# 🎯 변경 사항:
# 1. Elastic IP: 1개 → 2개 (각 AZ별)
# 2. NAT Gateway: 1개 → 2개 (각 AZ별 Public Subnet에 생성)
# 3. Private Route Table: 1개 → 2개 (각 AZ별로 분리)
# 4. 라우팅 격리: 각 AZ의 Private Subnet이 해당 AZ Public Subnet의 NAT Gateway 사용
# 
# 🌐 올바른 네트워크 아키텍처:
# 
# AZ-A (ap-northeast-2a):
# ├── Public Subnet (10.0.1.0/24)  ← NAT Gateway #1 생성 위치
# │   └── [NAT Gateway #1] → Internet Gateway → 인터넷
# └── Private Subnet (10.0.11.0/24) 
#     └── [Route Table] → AZ-A Public Subnet의 NAT Gateway #1 사용
# 
# AZ-C (ap-northeast-2c):
# ├── Public Subnet (10.0.2.0/24)  ← NAT Gateway #2 생성 위치  
# │   └── [NAT Gateway #2] → Internet Gateway → 인터넷
# └── Private Subnet (10.0.12.0/24)
#     └── [Route Table] → AZ-C Public Subnet의 NAT Gateway #2 사용
# 
# 🔄 실제 트래픽 흐름:
# Private Subnet의 인스턴스 → Private Route Table → Public Subnet의 NAT Gateway → 인터넷
# 
# ⚠️ 중요: NAT Gateway는 오직 Public Subnet에만 생성됩니다!
# Private Subnet은 Route Table 설정을 통해 Public Subnet의 NAT Gateway를 사용합니다.
# 
# ✅ 장점:
# - AZ 장애 시에도 다른 AZ에서 인터넷 접근 가능
# - 각 AZ 내부 트래픽으로 Cross-AZ 데이터 전송 비용 절약
# - 네트워크 성능 향상 (지연시간 감소)
# 
# 💸 비용 증가:
# - NAT Gateway: 시간당 $0.045 × 2개 = $0.09/시간
# - 월 예상 비용: ~$65 (기존 $32에서 100% 증가)
# - 하지만 고가용성과 성능 향상으로 프로덕션 환경에 적합
# ================================================================================ 
