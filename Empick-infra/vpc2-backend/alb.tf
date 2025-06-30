# ================================================================================
# 이 파일은 VPC 2 Backend의 ALB(Application Load Balancer)를 정의합니다.
# ALB는 인터넷의 요청을 받아서 여러 Spring Boot 서버로 분산시켜주는 역할을 합니다.
# ================================================================================

# ================================================================================
# 💡 ALB(Application Load Balancer)란?
# 
# ALB는 AWS의 7계층(애플리케이션 계층) 로드밸런서입니다.
# 
# 🎯 주요 기능:
# 1. 트래픽 분산: 여러 서버에 요청을 골고루 분배
# 2. Health Check: 서버 상태를 확인하여 정상 서버로만 요청 전달
# 3. 고가용성: 서버 장애 시 자동으로 정상 서버로 트래픽 우회
# 4. SSL 종료: HTTPS 암호화/복호화 처리
# 5. 경로 기반 라우팅: URL 경로에 따라 다른 서버로 요청 전달
# 
# 🏗️ 현재 아키텍처에서의 역할:
# 인터넷 → ALB (Public Subnet) → Spring Boot 서버 (Private Subnet)
# 
# 📊 구성 요소:
# 1. ALB 본체: 실제 로드밸런서
# 2. Target Group: 대상 서버들을 묶는 그룹
# 3. Listener: 특정 포트의 요청을 받아서 처리하는 규칙
# 4. Health Check: 서버 상태 확인 설정
# ================================================================================

# Target Group 생성 (Spring Boot 서버들을 묶는 그룹)
# Target Group은 ALB가 트래픽을 전달할 대상 서버들의 모음입니다
resource "aws_lb_target_group" "springboot_tg" {
  # 기본 설정
  name     = "${var.project_name}-springboot-tg" # Target Group 이름
  port     = var.springboot_port                 # 8080 포트 (Spring Boot 기본 포트)
  protocol = "HTTP"                              # HTTP 프로토콜 사용
  vpc_id   = aws_vpc.vpc2_backend.id             # VPC 2에 생성

  # Target Type 설정 (어떤 종류의 대상인지)
  target_type = "instance" # EC2 인스턴스를 대상으로 설정

  # Health Check 설정 (서버 상태 확인)
  # ALB는 주기적으로 각 서버의 상태를 확인하여 정상 서버로만 트래픽을 보냅니다
  health_check {
    enabled = true # Health Check 활성화

    # Health Check 경로 및 응답 설정
    path                = "/health"      # 사용자 정의 Health Check 엔드포인트
    protocol            = "HTTP"         # HTTP 프로토콜로 체크
    port                = "traffic-port" # Target 포트와 동일한 포트 사용 (8080)
    matcher             = "200"          # HTTP 200 응답이 와야 정상으로 판단
    healthy_threshold   = 2              # 2번 연속 성공하면 정상(Healthy)으로 판단
    unhealthy_threshold = 2              # 2번 연속 실패하면 비정상(Unhealthy)으로 판단
    timeout             = 5              # Health Check 요청 타임아웃 (5초)
    interval            = 30             # Health Check 주기 (30초마다)

    # Health Check 요청 설정
    # ALB가 서버에게 "GET /health HTTP/1.1" 요청을 보냅니다
  }

  # Stickiness 설정 (세션 고정)
  # 같은 사용자의 요청을 같은 서버로 보내고 싶을 때 사용 (현재는 비활성화)
  stickiness {
    type            = "lb_cookie" # 로드밸런서 쿠키 방식
    cookie_duration = 86400       # 쿠키 유지 시간 (24시간)
    enabled         = false       # 현재는 비활성화 (Stateless 애플리케이션이므로)
  }

  # 등록 해제 지연 설정
  # 서버를 Target Group에서 제거할 때 기존 연결이 완료될 때까지 대기하는 시간
  deregistration_delay = 30 # 30초 대기 (기본값: 300초)

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-springboot-tg"
    Type = "TargetGroup"
    Role = "Backend-API"
  })
}

# ================================================================================
# 🎯 ALB Target Group Attachment - 다중 인스턴스 지원
# ================================================================================

# Spring Boot 다중 인스턴스를 Target Group에 연결
# Blue-Green 배포를 위해 2개 인스턴스 모두를 ALB Target Group에 등록합니다
resource "aws_lb_target_group_attachment" "springboot_attachment" {
  # count를 사용하여 2개 인스턴스 모두 Target Group에 연결
  count = var.springboot_instance_count # 2개 인스턴스

  # Target Group 설정
  # 💡 Target Group이란?
  # ALB가 트래픽을 전달할 대상 서버들의 그룹입니다.
  # 예: 웹서버 3대가 있다면, 이 3대를 하나의 Target Group으로 묶어서
  # ALB가 요청을 받으면 이 3대 중 하나로 자동으로 전달해줍니다.
  # 
  # 🎯 Target Group의 역할:
  # 1. 서버 목록 관리: 어떤 서버들이 요청을 처리할 수 있는지 관리
  # 2. Health Check: 각 서버가 정상 동작하는지 주기적으로 확인
  # 3. 트래픽 분산: 정상 서버들에게만 요청을 골고루 분배
  # 4. 자동 복구: 장애 서버 복구 시 자동으로 트래픽 전송 재개

  # 🔄 ALB Target Group의 자동 Health Check:
  # - ALB가 각 인스턴스의 /health 엔드포인트를 주기적으로 확인
  # - 응답이 200 OK가 아니면 해당 인스턴스로 트래픽 전송 중단
  # - 정상 복구되면 자동으로 트래픽 전송 재개
  # - 이를 통해 무중단 배포가 가능합니다

  # 🏷️ 태그를 통한 인스턴스 구분:
  # - Blue 인스턴스: Environment="blue", DeployOrder="stable"
  # - Green 인스턴스: Environment="green", DeployOrder="stable"
  # - 배포 시에는 DeployOrder를 "staging"으로 변경하여 구분

  # 이 설정으로 ALB는 2개 인스턴스로 트래픽을 분산하게 됩니다
  target_group_arn = aws_lb_target_group.springboot_tg.arn   # 위에서 생성한 Target Group
  target_id        = aws_instance.springboot[count.index].id # 각 Spring Boot EC2 인스턴스 ID
  port             = var.springboot_port                     # 8080 포트
}

# Application Load Balancer 생성
# 실제 로드밸런서 본체입니다. 인터넷의 요청을 받아서 Target Group으로 전달합니다
resource "aws_lb" "springboot_alb" {
  # 기본 설정
  name               = var.alb_name  # "vpc2-backend-alb"
  internal           = false         # false = 인터넷 연결 ALB, true = 내부 ALB
  load_balancer_type = "application" # ALB 타입 (application/network/gateway)

  # 네트워크 설정
  security_groups = [aws_security_group.alb_sg.id]  # ALB 보안 그룹 연결
  subnets         = aws_subnet.public_subnets[*].id # Public 서브넷에 배치 (인터넷 접근 가능)

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
    Name = "${var.project_name}-springboot-alb"
    Type = "LoadBalancer"
    Role = "Backend-API"
  })

  # 의존성: 보안 그룹이 먼저 생성되어야 함
  depends_on = [aws_security_group.alb_sg]
}

# ================================================================================
# 2단계: HTTP Listener 구현
# ================================================================================

# HTTP Listener (80번 포트 요청 처리)
# Listener는 ALB가 특정 포트에서 오는 요청을 어떻게 처리할지 정의하는 규칙입니다
resource "aws_lb_listener" "springboot_http" {
  # 기본 설정
  load_balancer_arn = aws_lb.springboot_alb.arn # 위에서 생성한 ALB에 연결
  port              = "80"                      # HTTP 포트 (80번)
  protocol          = "HTTP"                    # HTTP 프로토콜

  # 기본 액션 (Default Action)
  # 모든 HTTP 요청을 Spring Boot Target Group으로 전달
  default_action {
    type             = "forward"                             # 요청을 Target Group으로 전달
    target_group_arn = aws_lb_target_group.springboot_tg.arn # Spring Boot Target Group
  }

  # 이 설정으로:
  # http://ALB주소/ → Spring Boot 서버로 전달
  # http://ALB주소/api/users → Spring Boot 서버로 전달
  # http://ALB주소/actuator/health → Spring Boot 서버로 전달

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-http-listener"
    Type = "Listener"
    Port = "80"
  })
}

# HTTPS Listener (443번 포트 요청 처리) - SSL 인증서 필요
# HTTPS는 SSL/TLS 암호화를 사용하는 보안 HTTP 프로토콜입니다
resource "aws_lb_listener" "springboot_https" {
  # 기본 설정
  load_balancer_arn = aws_lb.springboot_alb.arn # 위에서 생성한 ALB에 연결
  port              = "443"                     # HTTPS 포트 (443번)
  protocol          = "HTTPS"                   # HTTPS 프로토콜

  # SSL 설정
  ssl_policy      = "ELBSecurityPolicy-TLS-1-2-2017-01" # TLS 1.2 보안 정책
  certificate_arn = var.ssl_certificate_arn             # TODO: SSL 인증서 ARN 설정 필요

  # 기본 액션
  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.springboot_tg.arn
  }

  # 이 설정으로:
  # https://ALB주소/ → Spring Boot 서버로 전달 (암호화된 연결)
  # SSL 인증서가 있어야 정상 동작

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-https-listener"
    Type = "Listener"
    Port = "443"
  })

  # 조건부 생성: SSL 인증서 ARN이 제공된 경우에만 생성
  count = var.ssl_certificate_arn != "" ? 1 : 0
}

# HTTP → HTTPS 리다이렉트 Listener (선택적)
# HTTP 요청을 자동으로 HTTPS로 리다이렉트하는 설정
resource "aws_lb_listener" "http_redirect" {
  load_balancer_arn = aws_lb.springboot_alb.arn
  port              = "80"
  protocol          = "HTTP"

  # 리다이렉트 액션
  default_action {
    type = "redirect"

    redirect {
      port        = "443"      # HTTPS 포트로 리다이렉트
      protocol    = "HTTPS"    # HTTPS 프로토콜로 변경
      status_code = "HTTP_301" # 영구 리다이렉트 (301)
    }
  }

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-http-redirect"
    Type = "Listener"
    Role = "Redirect"
  })

  # 조건부 생성: SSL 인증서가 있고 리다이렉트를 원하는 경우에만 생성
  count = var.ssl_certificate_arn != "" && var.enable_http_redirect ? 1 : 0
}

# ================================================================================
# 💡 Listener 동작 원리:
# 
# 🌐 HTTP Listener (80번 포트):
# 1. 사용자가 http://ALB주소/api/users 요청
# 2. ALB가 80번 포트에서 요청 수신
# 3. HTTP Listener가 요청을 처리
# 4. default_action에 따라 Spring Boot Target Group으로 전달
# 5. Target Group이 정상 상태인 Spring Boot 서버로 요청 전달
# 6. Spring Boot가 응답을 ALB로 반환
# 7. ALB가 사용자에게 응답 전달
# 
# 🔒 HTTPS Listener (443번 포트):
# 1. 사용자가 https://ALB주소/api/users 요청 (암호화됨)
# 2. ALB가 443번 포트에서 요청 수신
# 3. ALB가 SSL/TLS 복호화 수행 (SSL 종료)
# 4. 복호화된 요청을 Spring Boot로 전달 (내부는 HTTP)
# 5. Spring Boot 응답을 ALB가 암호화하여 사용자에게 전달
# 
# 🔄 HTTP → HTTPS 리다이렉트:
# 1. 사용자가 http://ALB주소/api/users 요청
# 2. ALB가 301 리다이렉트 응답 반환
# 3. 브라우저가 자동으로 https://ALB주소/api/users 재요청
# ================================================================================

# ================================================================================
# 3단계: Listener Rules 구현 (경로 기반 라우팅)
# ================================================================================

# API 경로별 라우팅 규칙 (HTTP)
# 특정 경로 패턴에 따라 다른 처리를 하고 싶을 때 사용합니다
resource "aws_lb_listener_rule" "api_routing_http" {
  listener_arn = aws_lb_listener.springboot_http.arn
  priority     = 100 # 우선순위 (낮을수록 먼저 적용)

  # 조건: /api/* 경로로 오는 요청
  condition {
    path_pattern {
      values = ["/api/*"] # API 요청 패턴
    }
  }

  # 액션: Spring Boot Target Group으로 전달
  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.springboot_tg.arn
  }

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-api-rule-http"
    Type = "ListenerRule"
    Path = "api-path"
  })
}

# Health Check 경로 라우팅 규칙 (HTTP)
# Health Check 요청을 특별히 처리하고 싶을 때 사용
resource "aws_lb_listener_rule" "health_check_http" {
  listener_arn = aws_lb_listener.springboot_http.arn
  priority     = 50 # API 규칙보다 높은 우선순위

  # 조건: /health 경로
  condition {
    path_pattern {
      values = ["/health"] # Spring Boot Actuator Health Check
    }
  }

  # 액션: Spring Boot Target Group으로 전달
  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.springboot_tg.arn
  }

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-health-rule-http"
    Type = "ListenerRule"
    Path = "health-check"
  })
}

# 정적 파일 처리 규칙 (향후 S3 연동 시 사용)
# 현재는 Spring Boot로 전달하지만, 나중에 S3로 변경 가능
resource "aws_lb_listener_rule" "static_files_http" {
  listener_arn = aws_lb_listener.springboot_http.arn
  priority     = 200 # 낮은 우선순위

  # 조건: 정적 파일 확장자
  condition {
    path_pattern {
      values = [
        "*.css",
        "*.js",
        "*.png",
        "/static/*",
        "/assets/*"
      ]
    }
  }

  # 현재는 Spring Boot로 전달 (나중에 S3로 변경 가능)
  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.springboot_tg.arn
  }

  # TODO: S3 연동 시 아래와 같이 변경
  # action {
  #   type = "redirect"
  #   redirect {
  #     host        = "your-static-bucket.s3.amazonaws.com"
  #     path        = "/#{path}"
  #     protocol    = "HTTPS"
  #     status_code = "HTTP_301"
  #   }
  # }

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-static-rule-http"
    Type = "ListenerRule"
    Path = "static-files"
  })
}

# HTTPS Listener Rules (SSL 인증서가 있는 경우에만 생성)
# HTTP와 동일한 규칙을 HTTPS에도 적용
resource "aws_lb_listener_rule" "api_routing_https" {
  count        = var.ssl_certificate_arn != "" ? 1 : 0
  listener_arn = aws_lb_listener.springboot_https[0].arn
  priority     = 100

  condition {
    path_pattern {
      values = ["/api/*"]
    }
  }

  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.springboot_tg.arn
  }

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-api-rule-https"
    Type = "ListenerRule"
    Path = "api-path"
  })
}

resource "aws_lb_listener_rule" "health_check_https" {
  count        = var.ssl_certificate_arn != "" ? 1 : 0
  listener_arn = aws_lb_listener.springboot_https[0].arn
  priority     = 50

  condition {
    path_pattern {
      values = ["/health"]
    }
  }

  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.springboot_tg.arn
  }

  tags = merge(var.common_tags, {
    Name = "${var.project_name}-health-rule-https"
    Type = "ListenerRule"
    Path = "health-check"
  })
}

# ================================================================================
# 💡 Listener Rules 동작 원리:
# 
# 🎯 우선순위 기반 처리:
# 1. Priority 50: /health (가장 높은 우선순위)
# 2. Priority 100: /api/* (API 요청)
# 3. Priority 200: 정적 파일 (*.css, *.js 등)
# 4. Default Action: 위 조건에 맞지 않는 모든 요청
# 
# 🌐 요청 처리 예시:
# - GET /health → Priority 50 규칙 적용 → Spring Boot
# - GET /api/users → Priority 100 규칙 적용 → Spring Boot
# - GET /static/logo.png → Priority 200 규칙 적용 → Spring Boot (현재)
# - GET / → Default Action 적용 → Spring Boot
# 
# 🔧 확장 가능성:
# - 나중에 Frontend 서버가 추가되면 별도 Target Group 생성
# - 정적 파일은 S3로 리다이렉트 가능
# - 마이크로서비스 아키텍처로 확장 시 경로별로 다른 서비스 연결
# ================================================================================

# ================================================================================
# 💡 CloudWatch 모니터링은 monitoring.tf 파일로 분리되었습니다.
# ALB 관련 알람들도 monitoring.tf에서 중앙 집중식으로 관리됩니다.
# ================================================================================

# ================================================================================
# 💡 ALB Output 값들은 outputs.tf 파일에서 중앙 집중식으로 관리됩니다.
# 모든 인프라 출력값을 한 곳에서 확인할 수 있도록 구성했습니다.
# ================================================================================

# ================================================================================
# 🎉 ALB 구현 완료!
# 
# ✅ 완료된 구성 요소:
# 1. Target Group: Spring Boot 서버 그룹 관리
# 2. Target Group Attachment: EC2 인스턴스 연결
# 3. ALB 본체: 로드밸런서 생성
# 4. HTTP Listener: 80번 포트 처리
# 5. HTTPS Listener: 443번 포트 처리 (SSL 인증서 필요)
# 6. HTTP → HTTPS 리다이렉트: 보안 강화
# 7. Listener Rules: 경로 기반 라우팅
# 8. CloudWatch 알람: 모니터링 및 알림
# 9. Output 값: 다른 모듈과의 연동
# 
# 🎯 주요 기능:
# - 고가용성: 서버 장애 시 자동 우회
# - Health Check: 서버 상태 자동 감지
# - SSL 종료: HTTPS 암호화/복호화 처리
# - 경로 라우팅: URL 패턴별 처리
# - 모니터링: CloudWatch 통합 감시
# - 확장성: 향후 서버 추가/제거 용이
# 
# 🚀 사용 준비 완료:
# - HTTP: http://ALB-DNS-NAME으로 즉시 접속 가능
# - HTTPS: SSL 인증서 설정 후 https:// 접속 가능
# - API: http://ALB-DNS-NAME/api/* 경로로 API 호출
# - Health Check: http://ALB-DNS-NAME/health
# 
# 📋 운영 시 TODO:
# 1. SSL 인증서 발급 및 설정 (ACM 사용)
# 2. SNS 토픽 생성하여 알람 알림 설정
# 3. Route 53으로 도메인 연결
# 4. WAF 연동으로 보안 강화
# 5. 액세스 로그 S3 저장 활성화
# ================================================================================ 
