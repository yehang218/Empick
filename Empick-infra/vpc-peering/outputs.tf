# =============================================================================
# VPC Peering Outputs
# =============================================================================

output "vpc_peering_connection_id" {
  description = "VPC Peering Connection ID"
  value       = aws_vpc_peering_connection.vpc1_to_vpc2.id
}

output "vpc_peering_status" {
  description = "VPC Peering Connection Status"
  value       = aws_vpc_peering_connection.vpc1_to_vpc2.accept_status
}

output "private_dns_zone_id" {
  description = "Private DNS Zone ID"
  value       = aws_route53_zone.empick_internal.zone_id
}

output "private_dns_zone_name" {
  description = "Private DNS Zone Name"
  value       = aws_route53_zone.empick_internal.name
}

output "backend_internal_fqdn" {
  description = "Backend Internal FQDN"
  value       = aws_route53_record.backend_internal.fqdn
}

output "backend_internal_url" {
  description = "Backend Internal URL"
  value       = "http://${aws_route53_record.backend_internal.fqdn}"
}

output "peering_summary" {
  description = "VPC Peering Configuration Summary"
  value = {
    peering_id  = aws_vpc_peering_connection.vpc1_to_vpc2.id
    vpc1_cidr   = var.vpc1_cidr
    vpc2_cidr   = var.vpc2_cidr
    backend_url = "http://${aws_route53_record.backend_internal.fqdn}"
    dns_zone    = var.dns_zone_name
    status      = aws_vpc_peering_connection.vpc1_to_vpc2.accept_status
  }
}

# 네트워크 연결 테스트용 정보
output "network_test_info" {
  description = "네트워크 연결 테스트를 위한 정보"
  value = {
    vpc1_public_subnets  = data.terraform_remote_state.vpc1.outputs.public_subnet_ids
    vpc2_private_subnets = data.terraform_remote_state.vpc2.outputs.private_subnet_ids
    backend_alb_dns      = data.terraform_remote_state.vpc2.outputs.alb_dns_name
    frontend_alb_dns     = data.terraform_remote_state.vpc1.outputs.alb_dns_name
  }
}
