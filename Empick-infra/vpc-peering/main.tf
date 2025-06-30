# =============================================================================
# VPC Peering Configuration
# VPC1 (Frontend) ↔ VPC2 (Backend) Private 통신 연결
# =============================================================================

# VPC Peering Connection 생성
resource "aws_vpc_peering_connection" "vpc1_to_vpc2" {
  # Requester VPC (VPC1 - Frontend)
  vpc_id = data.terraform_remote_state.vpc1.outputs.vpc_id

  # Accepter VPC (VPC2 - Backend) 
  peer_vpc_id = data.terraform_remote_state.vpc2.outputs.vpc_id

  # 같은 리전, 같은 계정이므로 자동 수락
  auto_accept = true

  tags = {
    Name        = "${var.project_name}-vpc1-to-vpc2-peering"
    Environment = var.environment
    Project     = var.project_name
    Purpose     = "Frontend-Backend Private Communication"
  }
}

# =============================================================================
# Remote State 데이터 소스 정의
# =============================================================================

# VPC1 (Frontend) 상태 참조
data "terraform_remote_state" "vpc1" {
  backend = "local"

  config = {
    path = "../vpc1-frontend/terraform.tfstate"
  }
}

# VPC2 (Backend) 상태 참조  
data "terraform_remote_state" "vpc2" {
  backend = "local"

  config = {
    path = "../vpc2-backend/terraform.tfstate"
  }
}

# =============================================================================
# Route Table 업데이트 - VPC1 → VPC2
# =============================================================================

# VPC1 Public 서브넷 Route Table에 VPC2 경로 추가
resource "aws_route" "vpc1_public_to_vpc2" {
  route_table_id            = data.terraform_remote_state.vpc1.outputs.public_route_table_id
  destination_cidr_block    = "10.0.0.0/16" # VPC2 CIDR
  vpc_peering_connection_id = aws_vpc_peering_connection.vpc1_to_vpc2.id
}

# VPC1에는 Private Route Table이 없으므로 주석 처리
# resource "aws_route" "vpc1_private_to_vpc2" {
#   route_table_id            = data.terraform_remote_state.vpc1.outputs.private_route_table_id
#   destination_cidr_block    = "10.0.0.0/16" # VPC2 CIDR
#   vpc_peering_connection_id = aws_vpc_peering_connection.vpc1_to_vpc2.id
# }

# =============================================================================
# Route Table 업데이트 - VPC2 → VPC1
# =============================================================================

# VPC2 Public Route Table에 VPC1 경로 추가
resource "aws_route" "vpc2_public_to_vpc1" {
  route_table_id            = data.terraform_remote_state.vpc2.outputs.public_route_table_id
  destination_cidr_block    = "172.16.0.0/16" # VPC1 CIDR
  vpc_peering_connection_id = aws_vpc_peering_connection.vpc1_to_vpc2.id
}

# VPC2 Private Route Tables에 VPC1 경로 추가 (다중 AZ)
resource "aws_route" "vpc2_private_to_vpc1" {
  count                     = length(data.terraform_remote_state.vpc2.outputs.private_route_table_ids)
  route_table_id            = data.terraform_remote_state.vpc2.outputs.private_route_table_ids[count.index]
  destination_cidr_block    = "172.16.0.0/16" # VPC1 CIDR
  vpc_peering_connection_id = aws_vpc_peering_connection.vpc1_to_vpc2.id
}

# =============================================================================
# Security Group 규칙 업데이트
# =============================================================================

# VPC1 Nginx Security Group - VPC2로의 Outbound 허용
resource "aws_security_group_rule" "vpc1_nginx_to_vpc2_backend" {
  type              = "egress"
  from_port         = 8080
  to_port           = 8080
  protocol          = "tcp"
  cidr_blocks       = ["10.0.0.0/16"] # VPC2 CIDR
  security_group_id = data.terraform_remote_state.vpc1.outputs.security_group_ids.nginx
  description       = "Allow Nginx to Backend API (VPC Peering)"
}

# VPC2 SpringBoot Security Group - VPC1에서의 Inbound 허용
resource "aws_security_group_rule" "vpc2_springboot_from_vpc1_nginx" {
  type              = "ingress"
  from_port         = 8080
  to_port           = 8080
  protocol          = "tcp"
  cidr_blocks       = ["172.16.0.0/16"] # VPC1 CIDR
  security_group_id = data.terraform_remote_state.vpc2.outputs.security_group_ids.springboot
  description       = "Allow SpringBoot API from Nginx (VPC Peering)"
}

# VPC2 ALB Security Group - VPC1에서의 Inbound 허용 (중복 규칙으로 주석 처리)
# resource "aws_security_group_rule" "vpc2_alb_from_vpc1_frontend" {
#   type              = "ingress"
#   from_port         = 80
#   to_port           = 80
#   protocol          = "tcp"
#   cidr_blocks       = ["172.16.0.0/16"] # VPC1 CIDR
#   security_group_id = data.terraform_remote_state.vpc2.outputs.security_group_ids.alb
#   description       = "Allow ALB access from Frontend (VPC Peering)"
# }

# =============================================================================
# Route 53 Private DNS 구성
# =============================================================================

# Private Hosted Zone 생성
resource "aws_route53_zone" "empick_internal" {
  name = "empick.internal"

  # VPC1과 VPC2 모두에 연결
  vpc {
    vpc_id = data.terraform_remote_state.vpc1.outputs.vpc_id
  }

  vpc {
    vpc_id = data.terraform_remote_state.vpc2.outputs.vpc_id
  }

  tags = {
    Name        = "empick-internal-zone"
    Environment = "production"
    Project     = "empick"
  }
}

# Backend ALB를 위한 A 레코드 생성
resource "aws_route53_record" "backend_internal" {
  zone_id = aws_route53_zone.empick_internal.zone_id
  name    = "backend.empick.internal"
  type    = "A"

  alias {
    name                   = data.terraform_remote_state.vpc2.outputs.alb_dns_name
    zone_id                = data.terraform_remote_state.vpc2.outputs.alb_zone_id
    evaluate_target_health = true
  }
}


