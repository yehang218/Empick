# ================================================================================
# 이 파일은 VPC 1 Frontend의 ALB(Application Load Balancer)를 정의합니다.
# ALB는 인터넷의 요청을 받아서 여러 Nginx 서버로 분산시켜주는 역할을 합니다.
# ================================================================================

# ================================================================================
# 💡 ALB(Application Load Balancer)란?
# 
# ALB는 AWS의 7계층(애플리케이션 계층) 로드밸런서입니다.
# 
# 🎯 주요 기능:
# 1. 트래픽 분산: 여러 Nginx 서버에 요청을 골고루 분배
# 2. Health Check: 서버 상태를 확인하여 정상 서버로만 요청 전달
# 3. 고가용성: 서버 장애 시 자동으로 정상 서버로 트래픽 우회
# 4. SSL 종료: HTTPS 암호화/복호화 처리
# 5. 경로 기반 라우팅: URL 경로에 따라 다른 처리
# 
# 🏗️ 현재 아키텍처에서의 역할:
# 인터넷 → ALB (Public Subnet) → Nginx 서버 (Public Subnet) → Vue.js
#                                    ↓ API 프록시
#                               VPC2 Backend (Spring Boot)
# 
# 📊 VPC1 vs VPC2 차이점:
# - VPC1: Nginx:80, /health, Public Subnet
# - VPC2: Spring Boot:8080, /actuator/health, Private Subnet
# ================================================================================

# Target Group 생성 (Nginx 서버들을 묶는 그룹)
# Target Group은 ALB가 트래픽을 전달할 대상 서버들의 모음입니다
resource "aws_lb_target_group" "nginx_tg" {
  # 기본 설정
  name     = "${var.project_name}-nginx-tg" # Target Group 이름
  port     = 80                             # HTTP 포트 (Nginx 기본 포트)
  protocol = "HTTP"                         # HTTP 프로토콜 사용
  vpc_id   = aws_vpc.vpc1_frontend.id       # VPC 1에 생성

  # Target Type 설정 (어떤 종류의 대상인지)
  target_type = "instance" # EC2 인스턴스를 대상으로 설정

  # Health Check 설정 (서버 상태 확인)
  # ALB는 주기적으로 각 서버의 상태를 확인하여 정상 서버로만 트래픽을 보냅니다
  health_check {
    enabled = true # Health Check 활성화

    # Health Check 경로 및 응답 설정
    path                = "/health"      # Nginx Health Check 엔드포인트
    protocol            = "HTTP"         # HTTP 프로토콜로 체크
    port                = "traffic-port" # Target 포트와 동일한 포트 사용 (80)
    matcher             = "200"          # HTTP 200 응답이 와야 정상으로 판단
    healthy_threshold   = 2              # 2번 연속 성공하면 정상(Healthy)으로 판단
    unhealthy_threshold = 2              # 2번 연속 실패하면 비정상(Unhealthy)으로 판단
    timeout             = 5              # Health Check 요청 타임아웃 (5초)
    interval            = 30             # Health Check 주기 (30초마다)

    # Health Check 요청 설정
    # ALB가 서버에게 "GET /health HTTP/1.1" 요청을 보냅니다
    # nginx_user_data.sh에서 /health 엔드포인트를 구성했습니다
  }

  # Stickiness 설정 (세션 고정)
  # Frontend는 일반적으로 Stateless이므로 비활성화
  stickiness {
    type            = "lb_cookie" # 로드밸런서 쿠키 방식
    cookie_duration = 86400       # 쿠키 유지 시간 (24시간)
    enabled         = false       # 비활성화 (Vue.js SPA는 Stateless)
  }

  # 등록 해제 지연 설정
  # 서버를 Target Group에서 제거할 때 기존 연결이 완료될 때까지 대기하는 시간
  deregistration_delay = 30 # 30초 대기 (기본값: 300초)

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-nginx-tg"
    Type = "TargetGroup"
    Role = "Frontend-Web"
  })
}

# ================================================================================
# 🎯 ALB Target Group Attachment - Blue/Green 다중 인스턴스 지원
# ================================================================================

# Nginx 다중 인스턴스를 Target Group에 연결
# Blue-Green 배포를 위해 2개 인스턴스 모두를 ALB Target Group에 등록합니다
resource "aws_lb_target_group_attachment" "nginx_attachment" {
  # count를 사용하여 2개 인스턴스 모두 Target Group에 연결
  count = var.nginx_instance_count # 2개 인스턴스

  # Target Group 설정
  # 💡 Frontend Blue-Green 배포 동작 원리:
  # 1. 초기 상태: Blue(AZ-A), Green(AZ-C) 모두 "stable" 상태로 트래픽 분산
  # 2. 배포 시작: Green을 "staging"으로 변경 → ALB에서 일시적으로 제외
  # 3. Green 배포: 새 Vue.js 빌드 파일을 Green 인스턴스에 배포
  # 4. Green 검증: Health Check 통과 확인 (/health 엔드포인트)
  # 5. 트래픽 전환: Green을 "stable"로, Blue를 "staging"으로 변경
  # 6. Blue 업데이트: Blue 인스턴스에도 새 빌드 파일 배포
  # 7. 완료: 두 인스턴스 모두 "stable" 상태로 복원

  target_group_arn = aws_lb_target_group.nginx_tg.arn # 위에서 생성한 Target Group
  target_id        = count.index == 0 ? aws_instance.nginx_blue.id : aws_instance.nginx_green.id
  port             = 80 # HTTP 포트

  # 🔄 ALB Target Group의 자동 Health Check:
  # - ALB가 각 인스턴스의 /health 엔드포인트를 주기적으로 확인
  # - 응답이 200 OK가 아니면 해당 인스턴스로 트래픽 전송 중단
  # - 정상 복구되면 자동으로 트래픽 전송 재개
  # - 이를 통해 무중단 배포가 가능합니다

  # 🏷️ 태그를 통한 인스턴스 구분:
  # - Blue 인스턴스: Environment="blue", DeployOrder="stable"
  # - Green 인스턴스: Environment="green", DeployOrder="stable"
  # - 배포 시에는 DeployOrder를 "staging"으로 변경하여 구분

  # 이 설정으로 ALB는 2개 Nginx 인스턴스로 트래픽을 분산하게 됩니다
}

# Application Load Balancer 생성
# 실제 로드밸런서 본체입니다. 인터넷의 요청을 받아서 Target Group으로 전달합니다
resource "aws_lb" "nginx_alb" {
  # 기본 설정
  name               = var.alb_name  # "vpc1-frontend-alb"
  internal           = false         # false = 인터넷 연결 ALB, true = 내부 ALB
  load_balancer_type = "application" # ALB 타입 (application/network/gateway)

  # 네트워크 설정 - VPC1은 Public Subnet만 사용
  security_groups = [aws_security_group.alb_sg.id]  # ALB 보안 그룹 연결
  subnets         = aws_subnet.public_subnets[*].id # Public 서브넷에 배치 (인터넷 접근)

  # 삭제 방지 설정
  enable_deletion_protection = false # 개발 환경이므로 삭제 방지 비활성화

  # 교차 영역 로드 밸런싱 설정
  # 여러 AZ에 있는 서버들 간의 트래픽 분산을 균등하게 하는 설정
  enable_cross_zone_load_balancing = true # 교차 영역 로드 밸런싱 활성화

  # 유휴 연결 타임아웃 설정
  # 클라이언트가 요청을 보내지 않을 때 연결을 유지하는 시간
  idle_timeout = 60 # 60초 (기본값)

  # 액세스 로그 설정 (선택적)
  # ALB에 오는 모든 요청을 S3에 로그로 저장하는 기능
  # TODO: 운영 환경에서는 S3 버킷 생성 후 활성화 권장
  # access_logs {
  #   bucket  = aws_s3_bucket.alb_logs.bucket  # S3 버킷 필요
  #   prefix  = "alb-logs"                      # 로그 파일 접두사
  #   enabled = true                            # 액세스 로그 활성화
  # }

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-nginx-alb"
    Type = "LoadBalancer"
    Role = "Frontend-Web"
  })

  # 의존성: 보안 그룹이 먼저 생성되어야 함
  depends_on = [aws_security_group.alb_sg]
}

# ================================================================================
# HTTP Listener (80번 포트 요청 처리)
# ================================================================================

# HTTP Listener (80번 포트 요청 처리) - SSL 비활성화시만 사용
# Listener는 ALB가 특정 포트에서 오는 요청을 어떻게 처리할지 정의하는 규칙입니다
resource "aws_lb_listener" "nginx_http" {
  count = var.enable_ssl ? 0 : 1 # SSL 활성화시 비활성화 (리다이렉트 리스너 사용)

  # 기본 설정
  load_balancer_arn = aws_lb.nginx_alb.arn # 위에서 생성한 ALB에 연결
  port              = "80"                 # HTTP 포트 (80번)
  protocol          = "HTTP"               # HTTP 프로토콜

  # 기본 액션 (Default Action)
  # 모든 HTTP 요청을 Nginx Target Group으로 전달
  default_action {
    type             = "forward"                        # 요청을 Target Group으로 전달
    target_group_arn = aws_lb_target_group.nginx_tg.arn # Nginx Target Group
  }

  # 이 설정으로:
  # http://ALB주소/ → Vue.js 앱 (Nginx에서 서빙)
  # http://ALB주소/api/users → VPC2 Backend로 프록시 (Nginx 설정)
  # http://ALB주소/health → Nginx Health Check

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-http-listener"
    Type = "Listener"
    Port = "80"
  })
}

# ================================================================================
# 🔒 HTTPS Listener (443번 포트) - SSL 인증서 사용
# ================================================================================

# HTTPS Listener (443번 포트 요청 처리)
resource "aws_lb_listener" "nginx_https" {
  count = var.enable_ssl ? 1 : 0

  load_balancer_arn = aws_lb.nginx_alb.arn
  port              = "443"
  protocol          = "HTTPS"
  ssl_policy        = "ELBSecurityPolicy-TLS13-1-2-2021-06" # 최신 TLS 1.3 정책
  certificate_arn   = local.ssl_certificate_arn

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.nginx_tg.arn
  }

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-https-listener"
    Type = "Listener"
    Port = "443"
  })

  depends_on = [aws_acm_certificate_validation.main]
}

# HTTP to HTTPS 리다이렉트 (SSL 활성화시)
resource "aws_lb_listener" "http_redirect" {
  count = var.enable_ssl ? 1 : 0

  load_balancer_arn = aws_lb.nginx_alb.arn
  port              = "80"
  protocol          = "HTTP"

  default_action {
    type = "redirect"

    redirect {
      port        = "443"
      protocol    = "HTTPS"
      status_code = "HTTP_301"
    }
  }

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-http-redirect"
    Type = "Listener"
    Port = "80-redirect"
  })
}

# ================================================================================
# 🎯 ALB Listener Rules - 경로 기반 라우팅 (선택적)
# ================================================================================

# Frontend 특화 라우팅 규칙들 (필요시 활성화)
# Vue.js SPA 특성상 대부분 단순한 포워딩이면 충분하지만,
# 특정 경로에 대한 세밀한 제어가 필요한 경우 사용

# API 프록시 라우팅 (Nginx에서 처리하지만 ALB 레벨에서도 가능)
/*
resource "aws_lb_listener_rule" "api_routing" {
  listener_arn = aws_lb_listener.nginx_http.arn
  priority     = 100

  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.nginx_tg.arn
  }

  condition {
    path_pattern {
      values = ["/api/*"]
    }
  }

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-api-routing"
    Type = "ListenerRule"
  })
}
*/

# Health Check 라우팅 (HTTP 리스너용)
resource "aws_lb_listener_rule" "health_check_http" {
  count = var.enable_ssl ? 0 : 1

  listener_arn = aws_lb_listener.nginx_http[0].arn
  priority     = 50 # 높은 우선순위

  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.nginx_tg.arn
  }

  condition {
    path_pattern {
      values = ["/health"]
    }
  }

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-health-check-http-routing"
    Type = "ListenerRule"
  })
}

# Health Check 라우팅 (HTTPS 리스너용)
resource "aws_lb_listener_rule" "health_check_https" {
  count = var.enable_ssl ? 1 : 0

  listener_arn = aws_lb_listener.nginx_https[0].arn
  priority     = 50 # 높은 우선순위

  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.nginx_tg.arn
  }

  condition {
    path_pattern {
      values = ["/health"]
    }
  }

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-health-check-https-routing"
    Type = "ListenerRule"
  })
}

# ================================================================================
# 💡 주요 구성 요소 설명:
# 
# 1. Target Group: Nginx 서버들을 묶는 그룹 (Health Check: /health)
# 2. ALB: 인터넷 트래픽을 받아서 Target Group으로 분산
# 3. HTTP Listener: 80번 포트 요청을 Target Group으로 전달
# 4. Target Group Attachment: Blue/Green 인스턴스 연결
# 5. Listener Rules: 경로별 세밀한 라우팅 제어
# 
# 🎯 Blue-Green 배포 지원:
# - 2개 인스턴스 모두 Target Group에 등록
# - Health Check 기반 자동 트래픽 제어
# - 배포 시 Target Group에서 임시 제거 후 재등록
# 
# 🔒 보안 고려사항:
# - Public Subnet 배치 (인터넷 접근 허용)
# - Security Group으로 포트 제한 (80, 443만 허용)
# - HTTPS는 SSL 인증서 발급 후 활성화
# 
# ⚠️ 검증된 주의사항:
# - 모든 description은 영문으로 작성 (한글 에러 방지)
# - Health Check 경로는 nginx_user_data.sh와 일치해야 함
# - Target Group 이름은 32자 제한 준수
# ================================================================================ 
