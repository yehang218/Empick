# ================================================================================
# 이 파일은 VPC 2 Backend의 모든 출력값(Outputs)을 정의합니다.
# 출력값은 terraform apply 완료 후 사용자에게 표시되거나 다른 모듈에서 참조할 수 있습니다.
# ================================================================================

# ================================================================================
# 💡 Terraform Outputs란?
# 
# Outputs는 인프라 생성 후 중요한 정보를 외부로 출력하는 기능입니다.
# 
# 🎯 주요 용도:
# 1. 사용자 정보 제공: 접속 주소, IP 등
# 2. 모듈 간 연동: 다른 terraform 모듈에서 참조
# 3. CI/CD 연동: GitHub Actions에서 인프라 정보 활용
# 4. 스크립트 자동화: 배포 스크립트에서 동적 값 사용
# 
# 📤 출력 방법:
# - terraform apply 후 자동 표시
# - terraform output 명령어로 개별 조회
# - terraform output -json으로 JSON 형태 출력
# ================================================================================

# ================================================================================
# 🌐 네트워크 관련 출력값
# ================================================================================

# VPC 정보
output "vpc_id" {
  description = "The ID of VPC 2 Backend"
  value       = aws_vpc.vpc2_backend.id
}

output "vpc_cidr" {
  description = "The CIDR block of VPC 2 Backend"
  value       = aws_vpc.vpc2_backend.cidr_block
}

# 서브넷 정보
output "public_subnet_ids" {
  description = "List of IDs of public subnets"
  value       = aws_subnet.public_subnets[*].id
}

output "private_subnet_ids" {
  description = "List of IDs of private subnets"
  value       = aws_subnet.private_subnets[*].id
}

# 게이트웨이 정보
output "internet_gateway_id" {
  description = "The ID of the Internet Gateway"
  value       = aws_internet_gateway.vpc2_igw.id
}

output "nat_gateway_ids" {
  description = "The IDs of the NAT Gateways"
  value       = aws_nat_gateway.vpc2_nat[*].id
}

output "nat_gateway_public_ips" {
  description = "The public IP addresses of the NAT Gateways"
  value       = aws_eip.nat_eip[*].public_ip
}

# 라우트 테이블 정보 (VPC Peering용)
output "public_route_table_id" {
  description = "The ID of the public route table (for VPC Peering)"
  value       = aws_route_table.public_rt.id
}

output "private_route_table_ids" {
  description = "The IDs of the private route tables (for VPC Peering)"
  value       = aws_route_table.private_rt[*].id
}

# ================================================================================
# 🖥️ EC2 인스턴스 관련 출력값
# ================================================================================

# Bastion Host 정보
output "bastion_public_ip" {
  description = "The public IP address of the bastion host"
  value       = aws_instance.bastion.public_ip
}

output "bastion_private_ip" {
  description = "The private IP address of the bastion host"
  value       = aws_instance.bastion.private_ip
}

output "bastion_instance_id" {
  description = "The instance ID of the bastion host"
  value       = aws_instance.bastion.id
}

# Bastion Host IAM 정보
output "bastion_iam_role" {
  description = "The IAM role ARN of the bastion host"
  value       = aws_iam_role.bastion_role.arn
}

output "bastion_iam_policies" {
  description = "The IAM policies attached to the bastion host"
  value = {
    s3_policy  = aws_iam_policy.bastion_s3_policy.arn
    ec2_policy = aws_iam_policy.bastion_ec2_policy.arn
  }
}

# Spring Boot 서버 정보 - Blue/Green 다중 인스턴스
output "springboot_blue_private_ip" {
  description = "The private IP address of the Spring Boot Blue server (AZ-A)"
  value       = aws_instance.springboot[0].private_ip
}

output "springboot_green_private_ip" {
  description = "The private IP address of the Spring Boot Green server (AZ-C)"
  value       = aws_instance.springboot[1].private_ip
}

output "springboot_blue_instance_id" {
  description = "The instance ID of the Spring Boot Blue server (AZ-A)"
  value       = aws_instance.springboot[0].id
}

output "springboot_green_instance_id" {
  description = "The instance ID of the Spring Boot Green server (AZ-C)"
  value       = aws_instance.springboot[1].id
}

# 호환성을 위한 기본 출력값 (Blue 서버 정보)
output "springboot_private_ip" {
  description = "The private IP address of the Spring Boot server (default: Blue)"
  value       = aws_instance.springboot[0].private_ip
}

output "springboot_instance_id" {
  description = "The instance ID of the Spring Boot server (default: Blue)"
  value       = aws_instance.springboot[0].id
}

# 다중 인스턴스 리스트 정보
output "springboot_private_ips" {
  description = "List of private IP addresses of all Spring Boot servers"
  value       = aws_instance.springboot[*].private_ip
}

output "springboot_instance_ids" {
  description = "List of instance IDs of all Spring Boot servers"
  value       = aws_instance.springboot[*].id
}

# ================================================================================
# 🔐 보안 관련 출력값
# ================================================================================

# 보안 그룹 ID들
output "security_group_ids" {
  description = "Map of security group IDs"
  value = {
    bastion    = aws_security_group.bastion_sg.id
    alb        = aws_security_group.alb_sg.id
    springboot = aws_security_group.springboot_sg.id
    rds        = aws_security_group.rds_sg.id
    redis      = aws_security_group.redis_sg.id
  }
}

# SSH 키 정보
output "key_pair_name" {
  description = "The name of the SSH key pair"
  value       = data.aws_key_pair.empick_key.key_name
}

# ================================================================================
# 🏗️ ALB (로드밸런서) 관련 출력값
# ================================================================================

# ALB 기본 정보
output "alb_dns_name" {
  description = "The DNS name of the load balancer"
  value       = aws_lb.springboot_alb.dns_name
}

output "alb_zone_id" {
  description = "The canonical hosted zone ID of the load balancer (for Route 53)"
  value       = aws_lb.springboot_alb.zone_id
}

output "alb_arn" {
  description = "The ARN of the load balancer"
  value       = aws_lb.springboot_alb.arn
}

output "alb_security_group_id" {
  description = "The ID of the ALB security group"
  value       = aws_security_group.alb_sg.id
}

# Target Group 정보
output "target_group_arn" {
  description = "The ARN of the target group"
  value       = aws_lb_target_group.springboot_tg.arn
}

# ALB 접속 URL들
output "alb_urls" {
  description = "URLs to access the application through ALB"
  value = {
    http_url     = "http://${aws_lb.springboot_alb.dns_name}"
    https_url    = var.ssl_certificate_arn != "" ? "https://${aws_lb.springboot_alb.dns_name}" : "Not configured (SSL certificate required)"
    health_check = "http://${aws_lb.springboot_alb.dns_name}/health"
    api_base     = "http://${aws_lb.springboot_alb.dns_name}/api"
  }
}

# ================================================================================
# 🗄️ 데이터베이스 관련 출력값
# ================================================================================

# RDS MariaDB 정보
output "database_endpoint" {
  description = "The RDS MariaDB endpoint"
  value       = aws_db_instance.mariadb.endpoint
}

output "database_port" {
  description = "The RDS MariaDB port"
  value       = aws_db_instance.mariadb.port
}

output "database_name" {
  description = "The name of the database"
  value       = aws_db_instance.mariadb.db_name
}

output "database_username" {
  description = "The username for the database"
  value       = aws_db_instance.mariadb.username
  sensitive   = true # 보안상 민감한 정보로 표시
}

# ================================================================================
# 🔄 Redis 캐시 관련 출력값
# ================================================================================

# Redis 정보
output "redis_primary_endpoint" {
  description = "The primary endpoint of the Redis replication group"
  value       = aws_elasticache_replication_group.redis.primary_endpoint_address
}

output "redis_port" {
  description = "The port of the Redis cluster"
  value       = var.redis_port
}

output "redis_replication_group_id" {
  description = "The ID of the Redis replication group"
  value       = aws_elasticache_replication_group.redis.id
}

# ================================================================================
# 📊 모니터링 관련 출력값 (monitoring.tf에서 생성된 리소스들)
# ================================================================================

# CloudWatch 대시보드
output "cloudwatch_dashboard_url" {
  description = "URL to the CloudWatch dashboard"
  value       = "https://console.aws.amazon.com/cloudwatch/home?region=${var.aws_region}#dashboards:name=${aws_cloudwatch_dashboard.empick_dashboard.dashboard_name}"
}

# ================================================================================
# 🎯 사용자 접속 정보 (가장 중요한 출력값들)
# ================================================================================

# 사용자가 바로 사용할 수 있는 접속 정보
output "connection_info" {
  description = "Connection information for accessing the infrastructure"
  value = {
    # 웹 애플리케이션 접속
    web_application = "http://${aws_lb.springboot_alb.dns_name}"
    health_check    = "http://${aws_lb.springboot_alb.dns_name}/health"
    api_endpoint    = "http://${aws_lb.springboot_alb.dns_name}/api"

    # SSH 접속 (Bastion을 통한 관리)
    bastion_ssh      = "ssh -i camp.pem ec2-user@${aws_instance.bastion.public_ip}"
    springboot_blue  = "ssh -i camp.pem ec2-user@${aws_instance.bastion.public_ip} 'ssh-blue'"
    springboot_green = "ssh -i camp.pem ec2-user@${aws_instance.bastion.public_ip} 'ssh-green'"
    springboot_ssh   = "ssh -i camp.pem ec2-user@${aws_instance.bastion.public_ip} 'ssh-sb'" # 기본값: Blue

    # 데이터베이스 접속 (Bastion을 통한 관리)
    database_connect = "mysql -h ${aws_db_instance.mariadb.endpoint} -P ${aws_db_instance.mariadb.port} -u ${aws_db_instance.mariadb.username} -p"

    # S3 관리 (Bastion에서 사용 가능한 명령어들)
    s3_commands = {
      list_buckets  = "s3-list"
      list_empick   = "s3-empick"
      list_frontend = "s3-frontend"
      list_backend  = "s3-backend"
      copy_file     = "s3-cp <source> <destination>"
      sync_to_s3    = "s3-sync-up <local-path> s3://<bucket>/<path>"
      sync_from_s3  = "s3-sync-down s3://<bucket>/<path> <local-path>"
    }

    # 모니터링
    cloudwatch_dashboard = "https://console.aws.amazon.com/cloudwatch/home?region=${var.aws_region}#dashboards:name=${aws_cloudwatch_dashboard.empick_dashboard.dashboard_name}"
  }
}

# ================================================================================
# 📦 S3 정보
# ================================================================================

# S3 Bucket 정보
output "s3_bucket_name" {
  description = "Name of the S3 bucket for application artifacts"
  value       = aws_s3_bucket.empick_bucket.bucket
}

output "s3_bucket_arn" {
  description = "ARN of the S3 bucket"
  value       = aws_s3_bucket.empick_bucket.arn
}

output "s3_bucket_domain_name" {
  description = "Domain name of the S3 bucket"
  value       = aws_s3_bucket.empick_bucket.bucket_domain_name
}

output "s3_bucket_regional_domain_name" {
  description = "Regional domain name of the S3 bucket"
  value       = aws_s3_bucket.empick_bucket.bucket_regional_domain_name
}

# ================================================================================
# 🚀 CI/CD 연동용 출력값
# ================================================================================

# GitHub Actions에서 사용할 정보들 (Blue-Green 다중 인스턴스 지원)
output "cicd_info" {
  description = "Information for CI/CD integration with Blue-Green multiple instances"
  value = {
    # ALB Health Check
    health_check_url = "http://${aws_lb.springboot_alb.dns_name}/health"

    # Target Group 정보
    target_group_arn = aws_lb_target_group.springboot_tg.arn

    # Blue-Green 인스턴스 정보
    blue_instance = {
      instance_id = aws_instance.springboot[0].id
      private_ip  = aws_instance.springboot[0].private_ip
      az          = var.availability_zones[0]
    }
    green_instance = {
      instance_id = aws_instance.springboot[1].id
      private_ip  = aws_instance.springboot[1].private_ip
      az          = var.availability_zones[1]
    }

    # 서비스 관리
    service_name  = "empick-backend"
    app_directory = "/opt/empick"

    # 로그 확인
    log_command = "sudo journalctl -u empick-backend -f"

    # GitHub Actions에서 사용할 배포 명령어
    deployment_commands = {
      # ALB Target 관리
      deregister_blue  = "aws elbv2 deregister-targets --target-group-arn ${aws_lb_target_group.springboot_tg.arn} --targets Id=${aws_instance.springboot[0].id}"
      deregister_green = "aws elbv2 deregister-targets --target-group-arn ${aws_lb_target_group.springboot_tg.arn} --targets Id=${aws_instance.springboot[1].id}"
      register_blue    = "aws elbv2 register-targets --target-group-arn ${aws_lb_target_group.springboot_tg.arn} --targets Id=${aws_instance.springboot[0].id}"
      register_green   = "aws elbv2 register-targets --target-group-arn ${aws_lb_target_group.springboot_tg.arn} --targets Id=${aws_instance.springboot[1].id}"

      # Health Check
      check_blue_health  = "curl -f http://${aws_instance.springboot[0].private_ip}:8080/health"
      check_green_health = "curl -f http://${aws_instance.springboot[1].private_ip}:8080/health"
    }
  }
  sensitive = true # CI/CD 정보는 보안상 민감할 수 있음
}

# Blue-Green 배포 대상 정보
output "deployment_targets" {
  description = "Blue-Green deployment target information for multiple instances"
  value = {
    blue = {
      instance_id  = aws_instance.springboot[0].id
      private_ip   = aws_instance.springboot[0].private_ip
      az           = var.availability_zones[0] # AZ-A
      environment  = "blue"
      role         = "stable"
      subnet_id    = aws_subnet.private_subnets[0].id
      deploy_order = 2 # Blue는 나중에 배포 (안정성 확보)
      ssh_command  = "ssh -i ~/.ssh/camp.pem ec2-user@${aws_instance.springboot[0].private_ip}"
      health_check = "curl -f http://${aws_instance.springboot[0].private_ip}:8080/health"
    }
    green = {
      instance_id  = aws_instance.springboot[1].id
      private_ip   = aws_instance.springboot[1].private_ip
      az           = var.availability_zones[1] # AZ-C
      environment  = "green"
      role         = "stable"
      subnet_id    = aws_subnet.private_subnets[1].id
      deploy_order = 1 # Green은 먼저 배포 (위험 최소화)
      ssh_command  = "ssh -i ~/.ssh/camp.pem ec2-user@${aws_instance.springboot[1].private_ip}"
      health_check = "curl -f http://${aws_instance.springboot[1].private_ip}:8080/health"
    }

    # Blue-Green 배포 전략 정보
    deployment_strategy = {
      type                   = "blue-green"
      order                  = ["green", "blue"] # Green 먼저, Blue 나중
      health_check_wait_time = 300               # 5분 대기
      deregistration_delay   = 30                # ALB에서 제외 후 30초 대기
      rollback_enabled       = true

      # 배포 단계
      phases = {
        "1_staging" = "Green 인스턴스를 staging으로 전환 (ALB에서 제외)"
        "2_deploy"  = "Green 인스턴스에 새 버전 배포"
        "3_verify"  = "Green 인스턴스 Health Check 및 검증"
        "4_switch"  = "Green을 stable로, Blue를 staging으로 전환"
        "5_update"  = "Blue 인스턴스에 새 버전 배포"
        "6_restore" = "Blue 인스턴스를 stable로 복원"
      }
    }

    # 배포 스크립트 예시
    deployment_commands = {
      # 1단계: Green 서버 준비
      prepare_green = [
        "# Green 서버를 ALB에서 제외",
        "aws elbv2 deregister-targets --target-group-arn ${aws_lb_target_group.springboot_tg.arn} --targets Id=${aws_instance.springboot[1].id}",
        "sleep 30"
      ]

      # 2단계: Green 서버 배포
      deploy_green = [
        "# Green 서버에 새 버전 배포",
        "ssh -i ~/.ssh/camp.pem ec2-user@${aws_instance.springboot[1].private_ip} 'sudo systemctl stop empick-backend'",
        "# 여기에 실제 배포 명령어 추가",
        "ssh -i ~/.ssh/camp.pem ec2-user@${aws_instance.springboot[1].private_ip} 'sudo systemctl start empick-backend'"
      ]

      # 3단계: Green 서버 검증
      verify_green = [
        "# Green 서버 Health Check",
        "curl -f http://${aws_instance.springboot[1].private_ip}:8080/health",
        "sleep 60"
      ]

      # 4단계: 트래픽 전환
      switch_traffic = [
        "# Green 서버를 ALB에 등록",
        "aws elbv2 register-targets --target-group-arn ${aws_lb_target_group.springboot_tg.arn} --targets Id=${aws_instance.springboot[1].id}",
        "# Blue 서버를 ALB에서 제외",
        "aws elbv2 deregister-targets --target-group-arn ${aws_lb_target_group.springboot_tg.arn} --targets Id=${aws_instance.springboot[0].id}",
        "sleep 30"
      ]
    }
  }
  sensitive = true
}

# ================================================================================
# 📋 전체 인프라 요약 정보
# ================================================================================

output "infrastructure_summary" {
  description = "Summary of the entire infrastructure"
  value = {
    project_name = var.project_name
    aws_region   = var.aws_region

    # 네트워크
    vpc_cidr           = aws_vpc.vpc2_backend.cidr_block
    availability_zones = var.availability_zones

    # 컴퓨팅
    bastion_instance_type    = "t3.micro"
    springboot_instance_type = var.springboot_instance_type

    # 데이터베이스
    database_engine  = "mariadb"
    database_version = "10.11.8"
    database_class   = var.db_instance_class

    # 캐시
    redis_engine  = "redis"
    redis_version = var.redis_engine_version

    # 로드밸런서
    alb_type = "application"

    # 생성 시간
    created_at = timestamp()
  }
}

# ================================================================================
# 📤 Output 활용 예시:
# 
# 1. 전체 출력값 확인:
#    terraform output
# 
# 2. 특정 값만 확인:
#    terraform output alb_dns_name
#    terraform output bastion_public_ip
# 
# 3. JSON 형태로 출력:
#    terraform output -json
# 
# 4. 스크립트에서 활용:
#    ALB_URL=$(terraform output -raw alb_dns_name)
#    curl http://$ALB_URL/actuator/health
# 
# 5. 다른 terraform 모듈에서 참조:
#    data "terraform_remote_state" "backend" {
#      backend = "s3"
#      config = {
#        bucket = "terraform-state"
#        key    = "vpc2-backend/terraform.tfstate"
#      }
#    }
#    
#    # 사용 예시
#    api_endpoint = data.terraform_remote_state.backend.outputs.alb_dns_name
# ================================================================================ 
