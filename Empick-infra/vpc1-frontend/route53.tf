# ================================================================================
# Route 53 DNS Configuration
# Domain name resolution and health checks for VPC1 Frontend
# ================================================================================

# Route 53 Hosted Zone (DNS 존 생성)
resource "aws_route53_zone" "main" {
  count = var.create_route53_zone ? 1 : 0

  name    = var.domain_name
  comment = "Hosted zone for Empick Frontend"

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-${var.environment}-dns-zone"
    Type = "route53-hosted-zone"
  })
}

# Data source for existing hosted zone (기존 Zone 사용시)
data "aws_route53_zone" "existing" {
  count = var.create_route53_zone == false && var.route53_zone_id != "" ? 1 : 0

  zone_id = var.route53_zone_id
}

# Local values for zone reference
locals {
  zone_id   = var.create_route53_zone ? aws_route53_zone.main[0].zone_id : (var.route53_zone_id != "" ? data.aws_route53_zone.existing[0].zone_id : "")
  zone_name = var.create_route53_zone ? aws_route53_zone.main[0].name : (var.route53_zone_id != "" ? data.aws_route53_zone.existing[0].name : "")
}

# Main A Record (메인 도메인 → ALB)
resource "aws_route53_record" "main" {
  count = var.create_route53_zone || var.route53_zone_id != "" ? 1 : 0

  zone_id = local.zone_id
  name    = var.domain_name
  type    = "A"

  alias {
    name                   = aws_lb.nginx_alb.dns_name
    zone_id                = aws_lb.nginx_alb.zone_id
    evaluate_target_health = true
  }
}

# WWW A Record (www 서브도메인 → ALB)
resource "aws_route53_record" "www" {
  count = var.create_route53_zone || var.route53_zone_id != "" ? 1 : 0

  zone_id = local.zone_id
  name    = "${var.subdomain}.${var.domain_name}"
  type    = "A"

  alias {
    name                   = aws_lb.nginx_alb.dns_name
    zone_id                = aws_lb.nginx_alb.zone_id
    evaluate_target_health = true
  }
}

# Health Check for ALB (ALB 상태 확인)
resource "aws_route53_health_check" "main" {
  count = var.enable_health_check ? 1 : 0

  fqdn               = aws_lb.nginx_alb.dns_name
  port               = 80
  type               = "HTTP"
  resource_path      = "/health"
  failure_threshold  = var.health_check_failure_threshold
  request_interval   = var.health_check_request_interval
  measure_latency    = true
  invert_healthcheck = false
  enable_sni         = false

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-${var.environment}-health-check"
    Type = "route53-health-check"
  })
}

# Health Check for HTTPS (HTTPS 활성화시)
resource "aws_route53_health_check" "https" {
  count = var.enable_health_check && var.enable_ssl ? 1 : 0

  fqdn               = aws_lb.nginx_alb.dns_name
  port               = 443
  type               = "HTTPS"
  resource_path      = "/health"
  failure_threshold  = var.health_check_failure_threshold
  request_interval   = var.health_check_request_interval
  measure_latency    = true
  invert_healthcheck = false
  enable_sni         = true

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-${var.environment}-https-health-check"
    Type = "route53-health-check"
  })
}

# ================================================================================
# Local Values for External Reference
# ================================================================================

# These values are used by outputs.tf to avoid duplication

# ================================================================================
# 💡 Route 53 구성 설명:
# 
# 🌐 DNS 설정:
# - Hosted Zone: 도메인의 DNS 레코드 관리
# - A Record: 도메인 → ALB 연결 (ALIAS 사용)
# - Health Check: ALB 상태 모니터링
# 
# 🔗 연결:
# - Main: example.com → ALB
# - WWW: www.example.com → ALB
# - Health: /health 엔드포인트 확인
# 
# 💰 비용:
# - Hosted Zone: $0.50/월
# - Health Check: $0.50/월 x 2개 = $1.00/월
# - 총계: $1.50/월
# ================================================================================ 
