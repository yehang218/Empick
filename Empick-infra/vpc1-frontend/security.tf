# ================================================================================
# VPC1 Frontend Security Groups
# 아키텍처 다이어그램에 맞춘 정확한 보안 그룹 설정
# ================================================================================

# ALB Security Group (외부 접근용)
resource "aws_security_group" "alb_sg" {
  name        = "${var.project_name}-vpc1-alb-sg"
  description = "Security group for VPC1 Frontend ALB"
  vpc_id      = aws_vpc.vpc1_frontend.id

  # HTTP 접근 허용 (전 세계)
  ingress {
    description = "HTTP access from internet"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # HTTPS 접근 허용 (전 세계)
  ingress {
    description = "HTTPS access from internet"
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # 모든 아웃바운드 허용
  egress {
    description = "All outbound traffic"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-vpc1-alb-sg"
    Type = "ALB"
  })
}

# Nginx Instance Security Group
resource "aws_security_group" "nginx_sg" {
  name        = "${var.project_name}-vpc1-nginx-sg"
  description = "Security group for VPC1 Nginx instances"
  vpc_id      = aws_vpc.vpc1_frontend.id

  # SSH 접근 허용 (VPC1 내부만)
  ingress {
    description = "SSH access from VPC1"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = [var.vpc_cidr]
  }

  # SSH 접근 허용 (외부 관리용 - 임시)
  ingress {
    description = "SSH access for external management"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["58.140.211.194/32", "183.101.189.233/32"]
  }

  # ✅ ALB에서 Nginx로의 HTTP 접근 (핵심 수정)
  ingress {
    description     = "HTTP access from ALB only"
    from_port       = 80
    to_port         = 80
    protocol        = "tcp"
    security_groups = [aws_security_group.alb_sg.id]
  }

  # ✅ ALB에서 Nginx로의 HTTPS 접근
  ingress {
    description     = "HTTPS access from ALB only"
    from_port       = 443
    to_port         = 443
    protocol        = "tcp"
    security_groups = [aws_security_group.alb_sg.id]
  }

  # 모든 아웃바운드 허용 (VPC2 Backend API 호출용)
  egress {
    description = "All outbound traffic"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-vpc1-nginx-sg"
    Type = "Nginx"
  })
}

# ================================================================================
# 🎯 아키텍처 기반 보안 그룹 설계:
# 
# 📊 트래픽 플로우:
# 1. 인터넷 → ALB (Port 80/443)
# 2. ALB → Nginx 인스턴스 (Port 80/443) ← 핵심 수정
# 3. Nginx → VPC2 Backend (API 프록시)
# 
# 🌐 ALB Security Group:
# - 인바운드: HTTP(80), HTTPS(443) ← 전 세계 (0.0.0.0/0)
# - 아웃바운드: 모든 트래픽 허용
# 
# 🖥️ Nginx Security Group:
# - 인바운드: SSH(22) ← VPC1 내부 (172.16.0.0/16)
# - 인바운드: SSH(22) ← 외부 관리 IP (임시)
# - 인바운드: HTTP(80), HTTPS(443) ← ALB Security Group만
# - 아웃바운드: 모든 트래픽 허용 (VPC2 API 호출용)
# 
# ❌ 제거된 불필요한 규칙:
# - Health Check Port 8080 (ALB Health Check는 Port 80 사용)
# - VPC2에서 Nginx로의 직접 접근 (아키텍처에 맞지 않음)
# 
# ✅ 핵심 개선:
# - ALB → Nginx 통신 보장
# - 보안 그룹 참조로 동적 IP 문제 해결
# - 아키텍처 다이어그램과 100% 일치
# ================================================================================ 
