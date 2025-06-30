# ================================================================================
# 이 파일은 VPC 1 Frontend의 모든 출력값(Outputs)을 정의합니다.
# 출력값은 terraform apply 완료 후 사용자에게 표시되거나 다른 모듈에서 참조할 수 있습니다.
# ================================================================================

# ================================================================================
# 💡 Terraform Outputs란?
# 
# Outputs는 인프라 생성 후 중요한 정보를 외부로 출력하는 기능입니다.
# 
# 🎯 주요 용도:
# 1. 사용자 정보 제공: 접속 주소, IP 등
# 2. 모듈 간 연동: VPC Peering에서 VPC1 정보 참조
# 3. CI/CD 연동: GitHub Actions에서 인스턴스 정보 활용
# 4. 도메인 연결: Route 53에서 ALB 정보 사용
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
  description = "The ID of VPC 1 Frontend"
  value       = aws_vpc.vpc1_frontend.id
}

output "vpc_cidr" {
  description = "The CIDR block of VPC 1 Frontend"
  value       = aws_vpc.vpc1_frontend.cidr_block
}

# 서브넷 정보
output "public_subnet_ids" {
  description = "List of IDs of public subnets"
  value       = aws_subnet.public_subnets[*].id
}

# 게이트웨이 정보
output "internet_gateway_id" {
  description = "The ID of the Internet Gateway"
  value       = aws_internet_gateway.vpc1_igw.id
}

# 라우트 테이블 정보 (VPC Peering용)
output "public_route_table_id" {
  description = "The ID of the public route table (for VPC Peering)"
  value       = aws_route_table.public_rt.id
}

# ================================================================================
# 🖥️ EC2 인스턴스 관련 출력값
# ================================================================================

# Nginx 서버 정보 - Blue/Green 다중 인스턴스
output "nginx_blue_public_ip" {
  description = "The public IP address of the Nginx Blue server (AZ-A)"
  value       = aws_instance.nginx_blue.public_ip
}

output "nginx_green_public_ip" {
  description = "The public IP address of the Nginx Green server (AZ-C)"
  value       = aws_instance.nginx_green.public_ip
}

output "nginx_blue_private_ip" {
  description = "The private IP address of the Nginx Blue server (AZ-A)"
  value       = aws_instance.nginx_blue.private_ip
}

output "nginx_green_private_ip" {
  description = "The private IP address of the Nginx Green server (AZ-C)"
  value       = aws_instance.nginx_green.private_ip
}

output "nginx_blue_instance_id" {
  description = "The instance ID of the Nginx Blue server (AZ-A)"
  value       = aws_instance.nginx_blue.id
}

output "nginx_green_instance_id" {
  description = "The instance ID of the Nginx Green server (AZ-C)"
  value       = aws_instance.nginx_green.id
}

# 호환성을 위한 기본 출력값 (Blue 서버 정보)
output "nginx_public_ip" {
  description = "The public IP address of the Nginx server (default: Blue)"
  value       = aws_instance.nginx_blue.public_ip
}

output "nginx_private_ip" {
  description = "The private IP address of the Nginx server (default: Blue)"
  value       = aws_instance.nginx_blue.private_ip
}

output "nginx_instance_id" {
  description = "The instance ID of the Nginx server (default: Blue)"
  value       = aws_instance.nginx_blue.id
}

# 다중 인스턴스 리스트 정보
output "nginx_public_ips" {
  description = "List of public IP addresses of all Nginx servers"
  value       = [aws_instance.nginx_blue.public_ip, aws_instance.nginx_green.public_ip]
}

output "nginx_private_ips" {
  description = "List of private IP addresses of all Nginx servers"
  value       = [aws_instance.nginx_blue.private_ip, aws_instance.nginx_green.private_ip]
}

output "nginx_instance_ids" {
  description = "List of instance IDs of all Nginx servers"
  value       = [aws_instance.nginx_blue.id, aws_instance.nginx_green.id]
}

# ================================================================================
# 🔐 보안 관련 출력값
# ================================================================================

# 보안 그룹 ID들
output "security_group_ids" {
  description = "Map of security group IDs"
  value = {
    alb   = aws_security_group.alb_sg.id
    nginx = aws_security_group.nginx_sg.id
  }
}

# SSH 키 정보
output "key_pair_name" {
  description = "The name of the SSH key pair"
  value       = var.key_name
}

# ================================================================================
# 🏗️ ALB (로드밸런서) 관련 출력값
# ================================================================================

# ALB 기본 정보
output "alb_dns_name" {
  description = "The DNS name of the load balancer"
  value       = aws_lb.nginx_alb.dns_name
}

output "alb_zone_id" {
  description = "The canonical hosted zone ID of the load balancer (for Route 53)"
  value       = aws_lb.nginx_alb.zone_id
}

output "alb_arn" {
  description = "The ARN of the load balancer"
  value       = aws_lb.nginx_alb.arn
}

output "alb_security_group_id" {
  description = "The ID of the ALB security group"
  value       = aws_security_group.alb_sg.id
}

# Target Group 정보
output "target_group_arn" {
  description = "The ARN of the target group"
  value       = aws_lb_target_group.nginx_tg.arn
}

# ALB 접속 URL들
output "alb_urls" {
  description = "URLs to access the application through ALB"
  value = {
    http_url     = "http://${aws_lb.nginx_alb.dns_name}"
    https_url    = var.ssl_certificate_arn != "" ? "https://${aws_lb.nginx_alb.dns_name}" : "Not configured (SSL certificate required)"
    health_check = "http://${aws_lb.nginx_alb.dns_name}/health"
    frontend_app = "http://${aws_lb.nginx_alb.dns_name}"
  }
}

# ================================================================================
# 🔗 VPC Peering 관련 출력값
# ================================================================================

# VPC Peering에서 사용할 VPC1 정보
output "vpc_peering_info" {
  description = "VPC information for peering with VPC2"
  value = {
    vpc_id                = aws_vpc.vpc1_frontend.id
    cidr_block            = aws_vpc.vpc1_frontend.cidr_block
    public_route_table_id = aws_route_table.public_rt.id
    region                = var.aws_region
  }
}

# ================================================================================
# 🎯 배포 관련 출력값
# ================================================================================

# CI/CD 배포에서 사용할 인스턴스 정보
output "deployment_targets" {
  description = "Deployment target information for CI/CD"
  value = {
    blue_instance = {
      id         = aws_instance.nginx_blue.id
      public_ip  = aws_instance.nginx_blue.public_ip
      private_ip = aws_instance.nginx_blue.private_ip
      az         = aws_instance.nginx_blue.availability_zone
      role       = "blue"
    }
    green_instance = {
      id         = aws_instance.nginx_green.id
      public_ip  = aws_instance.nginx_green.public_ip
      private_ip = aws_instance.nginx_green.private_ip
      az         = aws_instance.nginx_green.availability_zone
      role       = "green"
    }
    target_group_arn = aws_lb_target_group.nginx_tg.arn
    alb_dns_name     = aws_lb.nginx_alb.dns_name
    s3_bucket_name   = var.s3_bucket_name
  }
}

# ================================================================================
# 📊 접속 정보 요약
# ================================================================================

# 사용자가 쉽게 확인할 수 있는 접속 정보
output "connection_info" {
  description = "Connection information for easy access"
  value = {
    # 🌐 웹 접속
    website_url = "http://${aws_lb.nginx_alb.dns_name}"
    health_url  = "http://${aws_lb.nginx_alb.dns_name}/health"

    # 🖥️ SSH 접속 (Blue/Green)
    ssh_blue_cmd  = "ssh -i ~/.ssh/${var.key_name}.pem ec2-user@${aws_instance.nginx_blue.public_ip}"
    ssh_green_cmd = "ssh -i ~/.ssh/${var.key_name}.pem ec2-user@${aws_instance.nginx_green.public_ip}"

    # 📍 인스턴스 위치
    blue_location  = "${aws_instance.nginx_blue.availability_zone} (${aws_instance.nginx_blue.public_ip})"
    green_location = "${aws_instance.nginx_green.availability_zone} (${aws_instance.nginx_green.public_ip})"

    # 🔗 ALB 정보
    alb_dns     = aws_lb.nginx_alb.dns_name
    alb_zone_id = aws_lb.nginx_alb.zone_id
  }
}

# ================================================================================
# 📋 인프라 요약 정보
# ================================================================================

# 전체 인프라 상태를 한눈에 볼 수 있는 요약 정보
output "infrastructure_summary" {
  description = "Summary of the entire infrastructure"
  value = {
    # 🌐 네트워크
    vpc_id   = aws_vpc.vpc1_frontend.id
    vpc_cidr = aws_vpc.vpc1_frontend.cidr_block
    subnets  = length(aws_subnet.public_subnets)

    # 🖥️ 컴퓨팅
    nginx_instances = var.nginx_instance_count
    instance_type   = var.nginx_instance_type

    # ⚖️ 로드밸런서
    alb_dns  = aws_lb.nginx_alb.dns_name
    alb_type = "application"

    # 🔒 보안
    security_groups = length([
      aws_security_group.alb_sg.id,
      aws_security_group.nginx_sg.id
    ])

    # 🏷️ 환경
    project     = var.project_name
    environment = var.environment
    region      = var.aws_region

    # 💰 비용 예상 (월)
    estimated_cost = "~$37-48 ($19 EC2 + $16.50 ALB + $0.90 Route53)"
  }
}

# ================================================================================
# 📊 CloudWatch 모니터링 관련 출력값
# ================================================================================

# CloudWatch 모니터링 정보
output "cloudwatch_info" {
  description = "CloudWatch monitoring information for VPC1 Frontend"
  value = {
    dashboard_url = "https://ap-northeast-2.console.aws.amazon.com/cloudwatch/home?region=ap-northeast-2#dashboards:name=${aws_cloudwatch_dashboard.vpc1_frontend.dashboard_name}"
    sns_topic_arn = aws_sns_topic.vpc1_alerts.arn
    log_groups = {
      nginx_access = aws_cloudwatch_log_group.nginx_access_logs.name
      nginx_error  = aws_cloudwatch_log_group.nginx_error_logs.name
      auto_deploy  = aws_cloudwatch_log_group.auto_deploy_logs.name
    }
    alarms = {
      alb_unhealthy_targets = aws_cloudwatch_metric_alarm.alb_unhealthy_targets.alarm_name
      alb_response_time     = aws_cloudwatch_metric_alarm.alb_response_time.alarm_name
      alb_4xx_errors        = aws_cloudwatch_metric_alarm.alb_4xx_errors.alarm_name
      ec2_cpu_blue          = aws_cloudwatch_metric_alarm.ec2_cpu_blue.alarm_name
      ec2_cpu_green         = aws_cloudwatch_metric_alarm.ec2_cpu_green.alarm_name
      ec2_memory_blue       = aws_cloudwatch_metric_alarm.ec2_memory_blue.alarm_name
      ec2_memory_green      = aws_cloudwatch_metric_alarm.ec2_memory_green.alarm_name
      auto_deploy_failures  = aws_cloudwatch_metric_alarm.auto_deploy_failures.alarm_name
    }
  }
}

# ================================================================================
# 💡 주요 출력값 설명:
# 
# 🌐 접속 정보:
# - alb_dns_name: 웹사이트 접속 주소
# - nginx_public_ips: SSH 접속용 공인 IP
# - connection_info: 모든 접속 정보 요약
# 
# 🔗 연동 정보:
# - vpc_peering_info: VPC Peering 구성용
# - deployment_targets: CI/CD 배포용
# - security_group_ids: 보안 규칙 참조용
# 
# 📊 모니터링:
# - infrastructure_summary: 전체 현황 파악
# - alb_urls: 헬스체크 및 서비스 상태 확인
# - cloudwatch_info: CloudWatch 대시보드 및 알람 정보
# ================================================================================

# ================================================================================
# 🌐 Route 53 DNS 관련 출력값
# ================================================================================

# Route 53 기본 정보
output "route53_zone_id" {
  description = "Route 53 hosted zone ID"
  value       = var.domain_name != "" ? local.zone_id : null
}

output "route53_zone_name" {
  description = "Route 53 hosted zone name"
  value       = var.domain_name != "" ? local.zone_name : null
}

output "route53_name_servers" {
  description = "Route 53 name servers for domain configuration"
  value       = var.create_route53_zone && var.domain_name != "" ? aws_route53_zone.main[0].name_servers : []
}

# 도메인 FQDN 정보
output "domain_urls" {
  description = "Domain URLs for the application"
  value = var.domain_name != "" ? {
    main_domain   = var.domain_name
    www_subdomain = "${var.subdomain}.${var.domain_name}"
    main_url      = var.enable_ssl ? "https://${var.domain_name}" : "http://${var.domain_name}"
    www_url       = var.enable_ssl ? "https://${var.subdomain}.${var.domain_name}" : "http://${var.subdomain}.${var.domain_name}"
    health_check  = var.enable_ssl ? "https://${var.domain_name}/health" : "http://${var.domain_name}/health"
  } : {}
}

# Health Check 정보
output "route53_health_check" {
  description = "Route 53 health check information"
  value = var.enable_health_check && var.domain_name != "" ? {
    http_health_check_id  = aws_route53_health_check.main[0].id
    https_health_check_id = var.enable_ssl ? aws_route53_health_check.https[0].id : null
    failure_threshold     = var.health_check_failure_threshold
    request_interval      = var.health_check_request_interval
  } : {}
}

# ================================================================================
# 🔐 SSL 인증서 관련 출력값
# ================================================================================

# SSL 인증서 정보
output "ssl_certificate_info" {
  description = "SSL certificate information"
  value = var.enable_ssl && var.domain_name != "" ? {
    certificate_arn    = local.ssl_certificate_arn
    certificate_status = aws_acm_certificate.main[0].status
    domains            = [var.domain_name, "*.${var.domain_name}"]
    validation_method  = "DNS"
    validation_timeout = var.ssl_validation_timeout
    } : {
    certificate_arn    = "Not configured (SSL disabled)"
    certificate_status = "Not configured (SSL disabled)"
    domains            = []
    validation_method  = "Not configured (SSL disabled)"
    validation_timeout = "Not configured (SSL disabled)"
  }
}

# HTTPS 설정 정보
output "https_configuration" {
  description = "HTTPS configuration status"
  value = {
    ssl_enabled    = var.enable_ssl
    https_listener = var.enable_ssl ? "Active on port 443" : "Disabled"
    http_redirect  = var.enable_ssl ? "HTTP to HTTPS redirect enabled" : "Direct HTTP access"
    ssl_policy     = var.enable_ssl ? "ELBSecurityPolicy-TLS13-1-2-2021-06" : "Not applicable"
  }
}

# ================================================================================
# 🌐 통합 접속 정보 (DNS/SSL 포함)
# ================================================================================

# 모든 접속 방법을 포함한 통합 정보
output "complete_access_info" {
  description = "Complete access information including DNS and SSL"
  value = {
    # ALB 직접 접속
    alb_http_url  = "http://${aws_lb.nginx_alb.dns_name}"
    alb_https_url = var.enable_ssl ? "https://${aws_lb.nginx_alb.dns_name}" : "Not configured"

    # 도메인 접속 (설정된 경우)
    domain_http_url  = var.domain_name != "" ? (var.enable_ssl ? "http://${var.domain_name} (redirects to HTTPS)" : "http://${var.domain_name}") : "Domain not configured"
    domain_https_url = var.enable_ssl && var.domain_name != "" ? "https://${var.domain_name}" : "Not configured"
    www_url          = var.domain_name != "" ? (var.enable_ssl ? "https://${var.subdomain}.${var.domain_name}" : "http://${var.subdomain}.${var.domain_name}") : "Domain not configured"

    # Health Check 접속
    health_check_alb    = "http://${aws_lb.nginx_alb.dns_name}/health"
    health_check_domain = var.domain_name != "" ? (var.enable_ssl ? "https://${var.domain_name}/health" : "http://${var.domain_name}/health") : "Domain not configured"

    # 설정 상태
    ssl_status    = var.enable_ssl ? "Enabled" : "Disabled"
    domain_status = var.domain_name != "" ? "Configured" : "Not configured"
  }
}

# ================================================================================
# 📋 DNS/SSL 설정 가이드
# ================================================================================

# DNS/SSL 설정 완료를 위한 가이드 정보
output "setup_guide" {
  description = "Setup guide for DNS and SSL configuration"
  value = var.domain_name != "" ? {
    step_1 = "✅ Route 53 hosted zone created"
    step_2 = var.create_route53_zone ? "📝 Update your domain registrar's name servers to: ${join(", ", aws_route53_zone.main[0].name_servers)}" : "✅ Using existing hosted zone"
    step_3 = var.enable_ssl ? "✅ SSL certificate requested and validated automatically" : "⏳ SSL not enabled (set enable_ssl = true to activate HTTPS)"
    step_4 = "🌐 Access your website at: ${var.enable_ssl ? "https" : "http"}://${var.domain_name}"

    dns_propagation = "⏰ DNS propagation may take 24-48 hours globally"
    ssl_validation  = var.enable_ssl ? "⏰ SSL validation typically completes in 5-10 minutes" : "N/A"

    troubleshooting = {
      dns_check    = "dig ${var.domain_name} or nslookup ${var.domain_name}"
      ssl_check    = var.enable_ssl ? "curl -I https://${var.domain_name}" : "N/A"
      health_check = "curl ${var.enable_ssl ? "https" : "http"}://${var.domain_name}/health"
    }
    } : {
    step_1          = "🔧 To enable DNS/SSL: Set domain_name in terraform.tfvars and enable_ssl = true"
    step_2          = "📋 Example: domain_name = \"example.com\", enable_ssl = true, create_route53_zone = true"
    step_3          = "⚠️ DNS/SSL not configured"
    step_4          = "💡 Run terraform apply after setting domain_name"
    dns_propagation = "N/A (domain not configured)"
    ssl_validation  = "N/A (domain not configured)"
    troubleshooting = {
      dns_check    = "N/A (domain not configured)"
      ssl_check    = "N/A (domain not configured)"
      health_check = "N/A (domain not configured)"
    }
  }
}

# ================================================================================
# 💰 비용 정보 업데이트 (DNS/SSL 포함)
# ================================================================================

# DNS/SSL 포함 전체 비용 정보
output "complete_cost_estimation" {
  description = "Complete cost estimation including DNS and SSL"
  value = {
    # 기본 인프라 비용
    ec2_instances = "$19.00/month (t3.small x2)"
    alb           = "$16.50/month"

    # DNS/SSL 비용
    route53_hosted_zone  = var.domain_name != "" ? "$0.50/month" : "$0 (not configured)"
    route53_health_check = var.enable_health_check && var.domain_name != "" ? "${var.enable_ssl ? "$1.00" : "$0.50"}/month" : "$0 (not configured)"
    ssl_certificate      = "$0 (AWS ACM free for AWS services)"

    # 총 비용
    monthly_total = var.domain_name != "" ? (var.enable_health_check ? (var.enable_ssl ? "$37.00/month" : "$36.50/month") : "$36.00/month") : "$35.50/month"

    # 비용 최적화 팁
    cost_optimization = [
      "💡 EC2 Reserved Instances로 최대 75% 절약 가능",
      "💡 CloudWatch 로그 보존 기간 조정",
      "💡 개발 환경에서는 health check 비활성화 고려"
    ]
  }
}
