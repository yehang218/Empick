# ================================================================================
# 이 파일은 VPC 2 Backend의 ElastiCache Redis 클러스터를 정의합니다.
# Redis는 Spring Boot 애플리케이션의 세션 관리 및 캐싱을 위한 인메모리 데이터베이스입니다.
# ================================================================================

# ElastiCache Subnet Group 생성 (Redis가 사용할 서브넷 그룹)
# RDS와 동일한 서브넷 그룹을 사용하여 같은 위치에 배치합니다
resource "aws_elasticache_subnet_group" "redis_subnet_group" {
  name       = "${var.project_name}-redis-subnet-group"
  subnet_ids = aws_subnet.private_subnets[*].id # RDS와 동일한 Private 서브넷 사용

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-redis-subnet-group"
    Type = "Cache"
  })
}

# ElastiCache Parameter Group 생성 (Redis 설정 그룹)
# Redis의 성능 및 동작을 조정하는 설정들을 관리합니다
resource "aws_elasticache_parameter_group" "redis_parameter_group" {
  family      = var.redis_parameter_group_family # TODO: Redis 버전에 맞게 업데이트 (현재: redis7.x)
  name        = "${var.project_name}-redis-parameter-group"
  description = "Custom parameter group for Redis"

  # Redis 기본 최적화 파라미터들
  parameter {
    name  = "maxmemory-policy"
    value = "allkeys-lru" # 메모리 부족 시 LRU 알고리즘으로 키 삭제
  }

  parameter {
    name  = "timeout"
    value = "300" # 유휴 연결 타임아웃 (5분)
  }

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-redis-parameter-group"
    Type = "Cache"
  })
}

# ElastiCache Redis 클러스터 생성
# 실제 Redis 캐시 서버입니다
resource "aws_elasticache_replication_group" "redis" {
  # 기본 클러스터 설정
  replication_group_id = "${var.project_name}-redis"
  description          = "Redis cluster for Spring Boot caching"
  port                 = var.redis_port # 6379 포트
  parameter_group_name = aws_elasticache_parameter_group.redis_parameter_group.name

  # 인스턴스 설정
  node_type          = var.redis_instance_type   # cache.t3.micro
  num_cache_clusters = var.redis_num_cache_nodes # 1개 노드 (비용 최적화)

  # Redis 엔진 설정
  engine_version = var.redis_engine_version # TODO: 최신 stable 버전으로 업데이트 (현재: 7.0)

  # 네트워크 및 보안 설정
  subnet_group_name  = aws_elasticache_subnet_group.redis_subnet_group.name
  security_group_ids = [aws_security_group.redis_sg.id]

  # 가용성 설정 (RDS와 동일한 AZ에 배치)
  multi_az_enabled            = false                       # Multi-AZ 비활성화 (비용 절약)
  preferred_cache_cluster_azs = [var.availability_zones[0]] # RDS와 동일한 AZ-A에 배치

  # 백업 설정 (기본값 사용, 비용 최적화)
  snapshot_retention_limit = 0             # TODO: 운영 환경에서는 1-5일 권장 (현재: 비용 절약용 0일)
  snapshot_window          = "03:00-05:00" # 백업 시간 (새벽 3-5시, 한국 시간 기준)

  # 유지보수 설정
  maintenance_window = "sun:05:00-sun:07:00" # 유지보수 시간 (일요일 새벽 5-7시)

  # 보안 설정
  at_rest_encryption_enabled = true  # 저장 데이터 암호화
  transit_encryption_enabled = false # TODO: 운영 환경에서는 true 권장 (t3.micro는 미지원)

  # 로그 설정 (선택적)
  log_delivery_configuration {
    destination      = "cloudwatch-logs" # TODO: CloudWatch 로그 그룹 미리 생성 필요
    destination_type = "cloudwatch-logs"
    log_format       = "text"
    log_type         = "slow-log" # TODO: 필요에 따라 "redis-log" 추가 가능
  }

  tags = merge(var.common_tags, {
    Name   = "${var.project_name}-redis"
    Type   = "Cache"
    Engine = "Redis"
  })

  # 의존성 명시: 보안 그룹과 서브넷 그룹이 먼저 생성되어야 함
  depends_on = [
    aws_elasticache_subnet_group.redis_subnet_group,
    aws_security_group.redis_sg
  ]
}

# ================================================================================
# 💡 ElastiCache Redis 구성 요소 설명:
# 
# 1. ElastiCache Subnet Group: Redis가 사용할 서브넷들 정의
# 2. ElastiCache Parameter Group: Redis 성능 최적화 설정
# 3. Redis Replication Group: 실제 Redis 캐시 서버
# 
# 🎯 아키텍처 반영:
# - RDS와 동일한 Private 서브넷에 배치 (보안)
# - RDS와 동일한 AZ-A에 배치 (네트워크 지연 최소화)
# - 다중 Spring Boot 인스턴스에서 공유 사용 (Blue-Green 배포의 핵심 장점)
# - 공유 사용 장점: 세션 연속성, 캐시 효율성, 배포 중 사용자 세션 유지
# 
# 💰 비용 최적화 요소:
# - Single-AZ: Multi-AZ 대비 50% 비용 절약
# - 스냅샷 비활성화: 스토리지 비용 절약
# - cache.t3.micro: 가장 저렴한 인스턴스
# - 모니터링 최소화: 추가 비용 방지
# 
# 🔐 보안 요소:
# - Private 서브넷: 인터넷 직접 접근 차단
# - 저장 데이터 암호화: 데이터 보안
# - 보안 그룹: Spring Boot와 Bastion에서만 접근 허용
# 
# 🚀 성능 최적화:
# - Redis 7.x: 최신 기능과 성능 향상
# - LRU 정책: 메모리 효율적 관리
# - RDS와 동일 AZ: 네트워크 지연 최소화
# 
# ⚠️ 주의사항:
# - 운영 환경에서는 Multi-AZ, 백업 강화 권장
# - transit_encryption은 t3.micro에서 미지원
# - Spring Boot 애플리케이션 환경 변수 설정 필요: REDIS_HOST, REDIS_PORT
# - 다중 인스턴스 공유 시 메모리 사용량 모니터링 필요
# - Blue-Green 배포 시 Redis 공유로 세션 연속성 자동 확보
# 
# 📋 TODO 체크리스트:
# 1. Redis 버전 업데이트 (현재: 7.0 → 최신 stable)
# 2. Parameter Group Family 버전 매칭 (redis7.x)
# 3. 운영 환경 백업 정책 설정 (snapshot_retention_limit)
# 4. 보안 강화 (transit_encryption_enabled for production)
# 5. CloudWatch 로그 그룹 사전 생성
# 6. terraform.tfvars에서 실제 값 설정
# 7. Spring Boot application-production.yml 환경 변수 매핑
# ================================================================================ 
