# ================================================================================
# VPC1 Frontend - Nginx Blue/Green EC2 Instances
# Vue.js 애플리케이션을 서빙하는 Nginx 서버들을 구성합니다
# ================================================================================

# Key Pair 생성 (SSH 접근용)
resource "aws_key_pair" "vpc1_key" {
  key_name   = "${var.project_name}-vpc1-key"
  public_key = var.public_key

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-vpc1-key"
    VPC  = "VPC1"
  })
}

# IAM Role for EC2 (SSM 접근용)
resource "aws_iam_role" "vpc1_ec2_role" {
  name = "${var.project_name}-vpc1-ec2-role"

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
    Name = "${var.project_name}-vpc1-ec2-role"
  })
}

# SSM 관리 정책 연결
resource "aws_iam_role_policy_attachment" "vpc1_ssm_policy" {
  role       = aws_iam_role.vpc1_ec2_role.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonSSMManagedInstanceCore"
}

# CloudWatch 에이전트 정책 연결
resource "aws_iam_role_policy_attachment" "vpc1_cloudwatch_policy" {
  role       = aws_iam_role.vpc1_ec2_role.name
  policy_arn = "arn:aws:iam::aws:policy/CloudWatchAgentServerPolicy"
}

# S3 접근 정책 연결 (빌드 파일 다운로드용)
resource "aws_iam_role_policy_attachment" "vpc1_s3_policy" {
  role       = aws_iam_role.vpc1_ec2_role.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonS3ReadOnlyAccess"
}

# EC2 Instance Profile
resource "aws_iam_instance_profile" "vpc1_ec2_profile" {
  name = "${var.project_name}-vpc1-ec2-profile"
  role = aws_iam_role.vpc1_ec2_role.name

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-vpc1-ec2-profile"
  })
}

# Amazon Linux 2023 AMI 조회 (참고용)
data "aws_ami" "amazon_linux" {
  most_recent = true
  owners      = ["amazon"]

  filter {
    name   = "name"
    values = ["al2023-ami-2023*"]
  }

  filter {
    name   = "architecture"
    values = ["x86_64"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }
}

# Blue Instance (Primary)
resource "aws_instance" "nginx_blue" {
  ami                    = var.ami_id
  instance_type          = var.nginx_instance_type
  key_name               = aws_key_pair.vpc1_key.key_name
  vpc_security_group_ids = [aws_security_group.nginx_sg.id]
  subnet_id              = aws_subnet.public_subnets[0].id
  iam_instance_profile   = aws_iam_instance_profile.vpc1_ec2_profile.name

  # User Data Script
  user_data = base64encode(templatefile("${path.module}/nginx_user_data_optimized.sh", {
    environment         = var.environment
    project             = var.project_name
    color               = "blue"
    private_key_content = file("${path.module}/../key/camp.pem")
  }))

  # EBS 최적화
  ebs_optimized = true

  # 루트 볼륨 설정
  root_block_device {
    volume_type           = "gp3"
    volume_size           = 30
    encrypted             = true
    delete_on_termination = true

    tags = merge(var.common_tags, {
      Name = "${var.project_name}-nginx-blue-root"
    })
  }

  # 종료 방지 (실수 방지)
  disable_api_termination = var.environment == "production" ? true : false

  tags = merge(var.common_tags, {
    Name        = "${var.project_name}-nginx-blue"
    Environment = var.environment
    Color       = "blue"
    Role        = "frontend"
    VPC         = "VPC1"
  })

  lifecycle {
    create_before_destroy = true
  }
}

# Green Instance (Standby)
resource "aws_instance" "nginx_green" {
  ami                    = var.ami_id
  instance_type          = var.nginx_instance_type
  key_name               = aws_key_pair.vpc1_key.key_name
  vpc_security_group_ids = [aws_security_group.nginx_sg.id]
  subnet_id              = aws_subnet.public_subnets[1].id
  iam_instance_profile   = aws_iam_instance_profile.vpc1_ec2_profile.name

  # User Data Script
  user_data = base64encode(templatefile("${path.module}/nginx_user_data_optimized.sh", {
    environment         = var.environment
    project             = var.project_name
    color               = "green"
    private_key_content = file("${path.module}/../key/camp.pem")
  }))

  # EBS 최적화
  ebs_optimized = true

  # 루트 볼륨 설정
  root_block_device {
    volume_type           = "gp3"
    volume_size           = 30
    encrypted             = true
    delete_on_termination = true

    tags = merge(var.common_tags, {
      Name = "${var.project_name}-nginx-green-root"
    })
  }

  # 종료 방지 (실수 방지)
  disable_api_termination = var.environment == "production" ? true : false

  tags = merge(var.common_tags, {
    Name        = "${var.project_name}-nginx-green"
    Environment = var.environment
    Color       = "green"
    Role        = "frontend"
    VPC         = "VPC1"
  })

  lifecycle {
    create_before_destroy = true
  }
}

# ================================================================================
# 💡 Nginx Blue/Green 구조:
# 
# 🔵 Blue Instance (Primary):
# - 서브넷: public-subnet-1a (ap-northeast-2a)
# - 역할: 현재 서비스 중인 프로덕션 인스턴스
# - 배포: 안정적인 버전 유지
# 
# 🟢 Green Instance (Standby):
# - 서브넷: public-subnet-1c (ap-northeast-2c)
# - 역할: 새 버전 배포 및 테스트용
# - 배포: 새 버전 검증 후 트래픽 전환
# 
# 🔧 공통 구성:
# - AMI: Amazon Linux 2023 (최신)
# - 인스턴스 타입: t3.micro (비용 최적화)
# - SSM 에이전트: 메모리 교훈 반영 (SSH 실패 시 대체)
# - EBS: gp3 20GB 암호화 (성능 + 보안)
# - IAM: SSM + CloudWatch 권한
# 
# 🚀 배포 전략:
# 1. Green에 새 버전 배포
# 2. Health Check 통과 확인
# 3. ALB 트래픽을 Green으로 전환
# 4. Blue를 새로운 Standby로 전환
# ================================================================================ 
