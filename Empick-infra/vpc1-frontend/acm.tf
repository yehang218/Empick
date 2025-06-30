# ================================================================================
# AWS Certificate Manager (ACM) Configuration
# SSL certificate for HTTPS connections
# ================================================================================

# SSL Certificate Request (SSL 인증서 요청)
resource "aws_acm_certificate" "main" {
  count = var.enable_ssl ? 1 : 0

  domain_name               = var.domain_name
  subject_alternative_names = ["*.${var.domain_name}"]
  validation_method         = "DNS"

  lifecycle {
    create_before_destroy = true
  }

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-${var.environment}-ssl-cert"
    Type = "acm-certificate"
  })
}

# DNS Validation Records (DNS 검증 레코드)
resource "aws_route53_record" "cert_validation" {
  for_each = var.enable_ssl ? {
    for dvo in aws_acm_certificate.main[0].domain_validation_options : dvo.domain_name => {
      name   = dvo.resource_record_name
      record = dvo.resource_record_value
      type   = dvo.resource_record_type
    }
  } : {}

  allow_overwrite = true
  name            = each.value.name
  records         = [each.value.record]
  ttl             = 60
  type            = each.value.type
  zone_id         = local.zone_id
}

# Certificate Validation (인증서 검증)
resource "aws_acm_certificate_validation" "main" {
  count = var.enable_ssl ? 1 : 0

  certificate_arn         = aws_acm_certificate.main[0].arn
  validation_record_fqdns = [for record in aws_route53_record.cert_validation : record.fqdn]

  timeouts {
    create = var.ssl_validation_timeout
  }

  depends_on = [aws_route53_record.cert_validation]
}

# ================================================================================
# Local Values for SSL Certificate
# ================================================================================

locals {
  ssl_certificate_arn = var.enable_ssl ? aws_acm_certificate_validation.main[0].certificate_arn : ""
}

# ================================================================================
# Local Values for External Reference
# ================================================================================

# These values are used by outputs.tf to avoid duplication

# ================================================================================
# 💡 ACM SSL 인증서 구성 설명:
# 
# 🔐 SSL 설정:
# - Domain: 메인 도메인 + 와일드카드 서브도메인
# - Validation: DNS 방식 (자동 검증)
# - Timeout: 10분 (검증 완료 대기)
# 
# 🔗 연결:
# - Route 53: DNS 검증 레코드 자동 생성
# - ALB: HTTPS 리스너에서 인증서 사용
# 
# 💰 비용:
# - ACM 인증서: 무료 (AWS 서비스 내 사용시)
# - DNS 검증: Route 53 쿼리 비용만 발생
# ================================================================================ 
