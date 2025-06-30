# ================================================================================
# CloudWatch 모니터링 및 알람 설정
# 
# 이 파일은 전체 인프라의 CloudWatch 알람을 중앙 집중식으로 관리합니다.
# 각 서비스별로 섹션을 나누어 체계적으로 구성되어 있습니다.
# ================================================================================

# ================================================================================
# 💡 CloudWatch 알람 아키텍처:
# 
# 🎯 모니터링 계층:
# 1. 웹 계층 (ALB): 사용자 경험 및 트래픽 상태
# 2. 애플리케이션 계층 (EC2): 시스템 리소스 상태  
# 3. 데이터 계층 (RDS): 데이터베이스 성능 상태
# 
# 🚨 알람 우선순위:
# - Critical: 서비스 중단 가능성 (Unhealthy Hosts, High CPU)
# - Warning: 성능 저하 (Response Time, Memory Usage)
# - Info: 리소스 부족 예고 (Disk Usage, DB Connections)
# ================================================================================

# ================================================================================
# 1. ALB (Application Load Balancer) 모니터링
# ================================================================================

# ALB Target Group Unhealthy Hosts 알람 [CRITICAL]
# Target Group에서 비정상 상태인 인스턴스가 있을 때 알림
# 이 알람은 Blue-Green 배포 시 매우 중요한 역할을 합니다
resource "aws_cloudwatch_metric_alarm" "target_group_unhealthy_hosts" {
  # 알람 이름: 프로젝트명-alb-unhealthy-hosts 형태로 생성
  alarm_name = "${var.project_name}-alb-unhealthy-hosts"

  # 비교 연산자: 임계값보다 큰 경우 알람 발생
  comparison_operator = "GreaterThanThreshold"

  # 평가 기간: 2번 연속으로 임계값 초과 시 알람 발생 (false positive 방지)
  evaluation_periods = "2"

  # 메트릭 이름: ALB Target Group의 비정상 호스트 수를 측정
  metric_name = "UnHealthyHostCount"

  # 네임스페이스: AWS Application Load Balancer 메트릭 그룹
  namespace = "AWS/ApplicationELB"

  # 측정 주기: 60초(1분)마다 메트릭 수집 및 평가
  period = "60"

  # 통계 방법: 1분간의 평균값으로 계산
  statistic = "Average"

  # 임계값: 0개 초과 시 알람 (즉, 비정상 호스트가 1개라도 있으면 알람)
  threshold = "0"

  # 알람 설명: 관리자가 알람 내용을 쉽게 이해할 수 있도록 설명
  alarm_description = "This metric monitors unhealthy targets in the target group"

  # 알람 대상 지정: 어떤 ALB와 Target Group을 모니터링할지 명시
  dimensions = {
    # Target Group ARN의 suffix 부분 (arn:aws:elasticloadbalancing:... 뒷부분)
    TargetGroup = aws_lb_target_group.springboot_tg.arn_suffix

    # Load Balancer ARN의 suffix 부분
    LoadBalancer = aws_lb.springboot_alb.arn_suffix
  }

  # TODO: 운영 환경에서는 SNS 토픽을 생성하여 이메일/SMS 알림 설정
  # alarm_actions = [aws_sns_topic.alerts.arn]

  # 태그 설정: 리소스 관리 및 비용 추적을 위한 메타데이터
  tags = merge(var.common_tags, {
    Name     = "${var.project_name}-unhealthy-hosts-alarm" # 알람 이름
    Type     = "CloudWatch-Alarm"                          # 리소스 타입
    Service  = "ALB"                                       # 관련 서비스
    Severity = "Critical"                                  # 심각도 레벨
  })
}

# ALB Response Time 알람 [WARNING]
# 응답 시간이 너무 오래 걸릴 때 알림 - 사용자 경험 품질 모니터링
# 평균 응답 시간이 5초를 초과하면 성능 문제로 판단
resource "aws_cloudwatch_metric_alarm" "alb_response_time" {
  # 알람 이름: 프로젝트명-alb-high-response-time 형태로 생성
  alarm_name = "${var.project_name}-alb-high-response-time"

  # 비교 연산자: 임계값보다 큰 경우 알람 발생
  comparison_operator = "GreaterThanThreshold"

  # 평가 기간: 2번 연속으로 임계값 초과 시 알람 발생
  evaluation_periods = "2"

  # 메트릭 이름: ALB에서 백엔드 서버까지의 응답 시간 측정
  metric_name = "TargetResponseTime"

  # 네임스페이스: AWS Application Load Balancer 메트릭 그룹
  namespace = "AWS/ApplicationELB"

  # 측정 주기: 60초(1분)마다 메트릭 수집
  period = "60"

  # 통계 방법: 1분간의 평균 응답 시간으로 계산
  statistic = "Average"

  # 임계값: 5.0초 초과 시 알람 (일반적으로 3-5초가 허용 가능한 응답 시간)
  threshold = "5.0"

  # 알람 설명: 응답 시간 모니터링 목적 명시
  alarm_description = "This metric monitors ALB target response time"

  # 알람 대상 지정: 특정 Load Balancer의 응답 시간 모니터링
  dimensions = {
    # Load Balancer ARN의 suffix 부분
    LoadBalancer = aws_lb.springboot_alb.arn_suffix
  }

  # 태그 설정: 성능 관련 알람임을 명시
  tags = merge(var.common_tags, {
    Name     = "${var.project_name}-response-time-alarm" # 알람 이름
    Type     = "CloudWatch-Alarm"                        # 리소스 타입
    Service  = "ALB"                                     # 관련 서비스
    Severity = "Warning"                                 # 심각도: 경고 수준
  })
}

# ALB 5XX Error 알람 [WARNING]
# 서버 오류(500, 502, 503, 504 등)가 많이 발생할 때 알림
# 백엔드 서버의 내부 오류나 배포 중 문제를 감지하는 중요한 지표
resource "aws_cloudwatch_metric_alarm" "alb_5xx_errors" {
  # 알람 이름: 프로젝트명-alb-high-5xx-errors 형태로 생성
  alarm_name = "${var.project_name}-alb-high-5xx-errors"

  # 비교 연산자: 임계값보다 큰 경우 알람 발생
  comparison_operator = "GreaterThanThreshold"

  # 평가 기간: 2번 연속으로 임계값 초과 시 알람 발생
  evaluation_periods = "2"

  # 메트릭 이름: HTTP 5XX 상태 코드 개수 측정
  # 500(Internal Server Error), 502(Bad Gateway), 503(Service Unavailable), 504(Gateway Timeout) 등
  metric_name = "HTTPCode_Target_5XX_Count"

  # 네임스페이스: AWS Application Load Balancer 메트릭 그룹
  namespace = "AWS/ApplicationELB"

  # 측정 주기: 300초(5분)마다 메트릭 수집 (에러는 좀 더 긴 주기로 관찰)
  period = "300"

  # 통계 방법: 5분간의 총합으로 계산 (에러 개수이므로 Sum 사용)
  statistic = "Sum"

  # 임계값: 5분간 10개 이상의 5XX 에러 시 알람
  # 일반적으로 전체 요청의 1-2% 이상 에러 시 문제로 판단
  threshold = "10"

  # 알람 설명: 5XX 에러 모니터링 목적 명시
  alarm_description = "This metric monitors 5XX errors from targets"

  # 알람 대상 지정: 특정 Load Balancer의 5XX 에러 모니터링
  dimensions = {
    # Load Balancer ARN의 suffix 부분
    LoadBalancer = aws_lb.springboot_alb.arn_suffix
  }

  # 태그 설정: 에러 관련 알람임을 명시
  tags = merge(var.common_tags, {
    Name     = "${var.project_name}-5xx-errors-alarm" # 알람 이름
    Type     = "CloudWatch-Alarm"                     # 리소스 타입
    Service  = "ALB"                                  # 관련 서비스
    Severity = "Warning"                              # 심각도: 경고 수준
  })
}

# ================================================================================
# 2. EC2 (Spring Boot Application) 모니터링 - Blue/Green 다중 인스턴스
# ================================================================================

# EC2 CPU 사용률 알람 [CRITICAL] - Blue/Green 각각 모니터링
# Spring Boot 서버의 CPU 사용률이 높을 때 알림
# Blue-Green 배포 시 각 인스턴스의 CPU 상태를 개별적으로 모니터링
resource "aws_cloudwatch_metric_alarm" "ec2_cpu_high" {
  # count를 사용하여 Blue(index=0), Green(index=1) 각각에 대해 알람 생성
  count = var.springboot_instance_count # 2개 인스턴스

  # 알람 이름: Blue/Green을 구분하여 생성 (예: empick-ec2-blue-high-cpu)
  alarm_name = "${var.project_name}-ec2-${count.index == 0 ? "blue" : "green"}-high-cpu"

  # 비교 연산자: 임계값보다 큰 경우 알람 발생
  comparison_operator = "GreaterThanThreshold"

  # 평가 기간: 2번 연속으로 임계값 초과 시 알람 발생 (일시적 스파이크 무시)
  evaluation_periods = "2"

  # 메트릭 이름: EC2 인스턴스의 CPU 사용률 백분율
  metric_name = "CPUUtilization"

  # 네임스페이스: AWS EC2 서비스의 기본 메트릭 그룹
  namespace = "AWS/EC2"

  # 측정 주기: 300초(5분)마다 메트릭 수집 (CPU는 5분 단위로 충분)
  period = "300"

  # 통계 방법: 5분간의 평균 CPU 사용률로 계산
  statistic = "Average"

  # 임계값: 80% 초과 시 알람 (일반적으로 80% 이상은 높은 부하 상태)
  threshold = "80"

  # 알람 설명: Blue/Green 구분하여 설명
  alarm_description = "This metric monitors EC2 CPU utilization for ${count.index == 0 ? "Blue" : "Green"} instance"

  # 알람 대상 지정: count.index에 따라 Blue(0) 또는 Green(1) 인스턴스 선택
  dimensions = {
    # 각 인스턴스의 고유 ID로 특정 인스턴스 지정
    InstanceId = aws_instance.springboot[count.index].id
  }

  # 태그 설정: Blue/Green 환경 정보 포함
  tags = merge(var.common_tags, {
    Name        = "${var.project_name}-ec2-${count.index == 0 ? "blue" : "green"}-cpu-alarm" # 알람 이름
    Type        = "CloudWatch-Alarm"                                                         # 리소스 타입
    Service     = "EC2"                                                                      # 관련 서비스
    Environment = count.index == 0 ? "blue" : "green"                                        # Blue/Green 환경 구분
    AZ          = count.index == 0 ? var.availability_zones[0] : var.availability_zones[1]   # 가용영역 정보
    Severity    = "Critical"                                                                 # 심각도: 중요 수준
  })
}

# EC2 메모리 사용률 알람 [WARNING] - Blue/Green 각각 모니터링
# Spring Boot 서버의 메모리 사용률이 높을 때 알림
# JVM 힙 메모리 부족이나 메모리 누수를 조기에 감지하기 위한 알람
resource "aws_cloudwatch_metric_alarm" "ec2_memory_high" {
  # count를 사용하여 Blue(index=0), Green(index=1) 각각에 대해 알람 생성
  count = var.springboot_instance_count # 2개 인스턴스

  # 알람 이름: Blue/Green을 구분하여 생성 (예: empick-ec2-blue-high-memory)
  alarm_name = "${var.project_name}-ec2-${count.index == 0 ? "blue" : "green"}-high-memory"

  # 비교 연산자: 임계값보다 큰 경우 알람 발생
  comparison_operator = "GreaterThanThreshold"

  # 평가 기간: 2번 연속으로 임계값 초과 시 알람 발생
  evaluation_periods = "2"

  # 메트릭 이름: 메모리 사용률 백분율 (사용자 정의 메트릭)
  metric_name = "mem_used_percent"

  # 네임스페이스: spring_user_data.sh에서 CloudWatch Agent가 설정한 사용자 정의 네임스페이스
  # AWS 기본 메트릭에는 메모리 정보가 없어서 CloudWatch Agent 필요
  namespace = "Empick/EC2"

  # 측정 주기: 300초(5분)마다 메트릭 수집
  period = "300"

  # 통계 방법: 5분간의 평균 메모리 사용률로 계산
  statistic = "Average"

  # 임계값: 85% 초과 시 알람 (CPU보다 높게 설정, 메모리는 어느 정도 높아도 정상)
  threshold = "85"

  # 알람 설명: Blue/Green 구분하여 설명
  alarm_description = "This metric monitors EC2 memory utilization for ${count.index == 0 ? "Blue" : "Green"} instance"

  # 알람 대상 지정: count.index에 따라 Blue(0) 또는 Green(1) 인스턴스 선택
  dimensions = {
    # 각 인스턴스의 고유 ID로 특정 인스턴스 지정
    InstanceId = aws_instance.springboot[count.index].id
  }

  # 태그 설정: Blue/Green 환경 정보 포함
  tags = merge(var.common_tags, {
    Name        = "${var.project_name}-ec2-${count.index == 0 ? "blue" : "green"}-memory-alarm" # 알람 이름
    Type        = "CloudWatch-Alarm"                                                            # 리소스 타입
    Service     = "EC2"                                                                         # 관련 서비스
    Environment = count.index == 0 ? "blue" : "green"                                           # Blue/Green 환경 구분
    AZ          = count.index == 0 ? var.availability_zones[0] : var.availability_zones[1]      # 가용영역 정보
    Severity    = "Warning"                                                                     # 심각도: 경고 수준
  })
}

# EC2 디스크 사용률 알람 [INFO] - Blue/Green 각각 모니터링
# Spring Boot 서버의 디스크 사용률이 높을 때 알림
# 로그 파일 누적, 임시 파일 증가 등으로 인한 디스크 부족을 예방하기 위한 알람
resource "aws_cloudwatch_metric_alarm" "ec2_disk_high" {
  # count를 사용하여 Blue(index=0), Green(index=1) 각각에 대해 알람 생성
  count = var.springboot_instance_count # 2개 인스턴스

  # 알람 이름: Blue/Green을 구분하여 생성 (예: empick-ec2-blue-high-disk)
  alarm_name = "${var.project_name}-ec2-${count.index == 0 ? "blue" : "green"}-high-disk"

  # 비교 연산자: 임계값보다 큰 경우 알람 발생
  comparison_operator = "GreaterThanThreshold"

  # 평가 기간: 2번 연속으로 임계값 초과 시 알람 발생
  evaluation_periods = "2"

  # 메트릭 이름: 디스크 사용률 백분율 (사용자 정의 메트릭)
  metric_name = "used_percent"

  # 네임스페이스: CloudWatch Agent에서 설정한 사용자 정의 네임스페이스
  namespace = "Empick/EC2"

  # 측정 주기: 300초(5분)마다 메트릭 수집
  period = "300"

  # 통계 방법: 5분간의 평균 디스크 사용률로 계산
  statistic = "Average"

  # 임계값: 90% 초과 시 알람 (디스크는 높은 임계값 설정, 예방 차원)
  threshold = "90"

  # 알람 설명: Blue/Green 구분하여 설명
  alarm_description = "This metric monitors EC2 disk utilization for ${count.index == 0 ? "Blue" : "Green"} instance"

  # 알람 대상 지정: 특정 인스턴스의 특정 디스크 파티션 모니터링
  dimensions = {
    # 각 인스턴스의 고유 ID로 특정 인스턴스 지정
    InstanceId = aws_instance.springboot[count.index].id

    # 디스크 장치명: 루트 파티션 (Amazon Linux 2023의 기본 설정)
    device = "/"

    # 파일 시스템 타입: XFS (Amazon Linux 2023의 기본 파일 시스템)
    fstype = "xfs"

    # 마운트 경로: 루트 디렉토리
    path = "/"
  }

  # 태그 설정: Blue/Green 환경 정보 포함
  tags = merge(var.common_tags, {
    Name        = "${var.project_name}-ec2-${count.index == 0 ? "blue" : "green"}-disk-alarm" # 알람 이름
    Type        = "CloudWatch-Alarm"                                                          # 리소스 타입
    Service     = "EC2"                                                                       # 관련 서비스
    Environment = count.index == 0 ? "blue" : "green"                                         # Blue/Green 환경 구분
    AZ          = count.index == 0 ? var.availability_zones[0] : var.availability_zones[1]    # 가용영역 정보
    Severity    = "Info"                                                                      # 심각도: 정보 수준
  })
}

# ================================================================================
# 3. RDS (MariaDB Database) 모니터링
# ================================================================================

# RDS 연결 수 알람 [WARNING]
# 데이터베이스 연결 수가 많을 때 알림
# Blue-Green 다중 인스턴스에서 공유하는 RDS의 동시 연결 수 모니터링
resource "aws_cloudwatch_metric_alarm" "rds_connection_high" {
  # 알람 이름: 프로젝트명-rds-high-connections 형태로 생성
  alarm_name = "${var.project_name}-rds-high-connections"

  # 비교 연산자: 임계값보다 큰 경우 알람 발생
  comparison_operator = "GreaterThanThreshold"

  # 평가 기간: 2번 연속으로 임계값 초과 시 알람 발생
  evaluation_periods = "2"

  # 메트릭 이름: RDS 인스턴스의 활성 데이터베이스 연결 수
  metric_name = "DatabaseConnections"

  # 네임스페이스: AWS RDS 서비스의 기본 메트릭 그룹
  namespace = "AWS/RDS"

  # 측정 주기: 300초(5분)마다 메트릭 수집
  period = "300"

  # 통계 방법: 5분간의 평균 연결 수로 계산
  statistic = "Average"

  # 임계값: 15개 초과 시 알람 (t3.micro는 최대 연결 수가 제한적)
  # Blue + Green 인스턴스가 동시에 연결하므로 적절한 임계값 설정 필요
  threshold = "15"

  # 알람 설명: RDS 연결 수 모니터링 목적 명시
  alarm_description = "This metric monitors RDS database connections"

  # 알람 대상 지정: 특정 RDS 인스턴스의 연결 수 모니터링
  dimensions = {
    # RDS 인스턴스 식별자: MariaDB 인스턴스 ID
    DBInstanceIdentifier = aws_db_instance.mariadb.id
  }

  # 태그 설정: RDS 관련 알람임을 명시
  tags = merge(var.common_tags, {
    Name     = "${var.project_name}-rds-connections-alarm" # 알람 이름
    Type     = "CloudWatch-Alarm"                          # 리소스 타입
    Service  = "RDS"                                       # 관련 서비스
    Severity = "Warning"                                   # 심각도: 경고 수준
  })
}

# RDS CPU 사용률 알람 [CRITICAL]
# 데이터베이스 CPU 사용률이 높을 때 알림
# Blue-Green 다중 인스턴스의 쿼리 부하로 인한 RDS 성능 저하 감지
resource "aws_cloudwatch_metric_alarm" "rds_cpu_high" {
  # 알람 이름: 프로젝트명-rds-high-cpu 형태로 생성
  alarm_name = "${var.project_name}-rds-high-cpu"

  # 비교 연산자: 임계값보다 큰 경우 알람 발생
  comparison_operator = "GreaterThanThreshold"

  # 평가 기간: 2번 연속으로 임계값 초과 시 알람 발생
  evaluation_periods = "2"

  # 메트릭 이름: RDS 인스턴스의 CPU 사용률 백분율
  metric_name = "CPUUtilization"

  # 네임스페이스: AWS RDS 서비스의 기본 메트릭 그룹
  namespace = "AWS/RDS"

  # 측정 주기: 300초(5분)마다 메트릭 수집
  period = "300"

  # 통계 방법: 5분간의 평균 CPU 사용률로 계산
  statistic = "Average"

  # 임계값: 75% 초과 시 알람 (RDS는 EC2보다 낮은 임계값 설정)
  # 데이터베이스는 CPU 집약적 작업이므로 조기 경고 필요
  threshold = "75"

  # 알람 설명: RDS CPU 사용률 모니터링 목적 명시
  alarm_description = "This metric monitors RDS CPU utilization"

  # 알람 대상 지정: 특정 RDS 인스턴스의 CPU 사용률 모니터링
  dimensions = {
    # RDS 인스턴스 식별자: MariaDB 인스턴스 ID
    DBInstanceIdentifier = aws_db_instance.mariadb.id
  }

  # 태그 설정: RDS 성능 관련 알람임을 명시
  tags = merge(var.common_tags, {
    Name     = "${var.project_name}-rds-cpu-alarm" # 알람 이름
    Type     = "CloudWatch-Alarm"                  # 리소스 타입
    Service  = "RDS"                               # 관련 서비스
    Severity = "Critical"                          # 심각도: 중요 수준
  })
}

# ================================================================================
# 4. CloudWatch 대시보드 (선택적)
# ================================================================================

# CloudWatch 대시보드 생성 (Blue-Green 다중 인스턴스 모니터링 뷰)
# 전체 인프라의 상태를 한눈에 볼 수 있는 통합 대시보드
# Blue-Green 배포 시 각 인스턴스의 성능을 비교 분석할 수 있도록 구성
resource "aws_cloudwatch_dashboard" "empick_dashboard" {
  # 대시보드 이름: 프로젝트명-infrastructure-dashboard 형태로 생성
  dashboard_name = "${var.project_name}-infrastructure-dashboard"

  # 대시보드 본문: JSON 형태로 위젯 구성 정의
  dashboard_body = jsonencode({
    # 위젯 배열: 대시보드에 표시될 차트들의 배치와 설정
    widgets = [
      # ================================================================
      # 1. ALB 성능 메트릭 위젯 (전체 폭, 상단)
      # ================================================================
      {
        # 위젯 타입: 메트릭 차트
        type = "metric"

        # 위치 및 크기: 좌상단(0,0)에서 시작, 전체 폭(12), 높이 6
        x      = 0
        y      = 0
        width  = 12
        height = 6

        # 위젯 속성 설정
        properties = {
          # 표시할 메트릭 배열
          metrics = [
            # ALB 응답 시간: 사용자 경험 품질 지표
            ["AWS/ApplicationELB", "TargetResponseTime", "LoadBalancer", aws_lb.springboot_alb.arn_suffix],

            # 5XX 에러 수: 서버 내부 오류 지표 (같은 LoadBalancer 차원 사용)
            [".", "HTTPCode_Target_5XX_Count", ".", "."],

            # 비정상 호스트 수: Blue-Green 배포 시 중요한 가용성 지표
            [".", "UnHealthyHostCount", "TargetGroup", aws_lb_target_group.springboot_tg.arn_suffix, "LoadBalancer", aws_lb.springboot_alb.arn_suffix]
          ]

          # 차트 표시 방식: 시계열 라인 차트
          view = "timeSeries"

          # 스택 차트 여부: false (각 메트릭을 별도 라인으로 표시)
          stacked = false

          # AWS 리전: 메트릭 수집 리전 지정
          region = var.aws_region

          # 위젯 제목
          title = "ALB Performance Metrics"

          # 데이터 포인트 간격: 300초(5분)
          period = 300
        }
      },
      # ================================================================
      # 2. Blue 인스턴스 리소스 사용률 위젯 (좌측 절반)
      # ================================================================
      {
        type   = "metric"
        x      = 0 # 좌측 시작
        y      = 6 # ALB 위젯 아래
        width  = 6 # 절반 폭
        height = 6

        properties = {
          # Blue 인스턴스(index=0)의 CPU, 메모리, 디스크 사용률
          metrics = [
            ["AWS/EC2", "CPUUtilization", "InstanceId", aws_instance.springboot[0].id],
            ["Empick/EC2", "mem_used_percent", "InstanceId", aws_instance.springboot[0].id],
            [".", "used_percent", "InstanceId", aws_instance.springboot[0].id, "device", "/", "fstype", "xfs", "path", "/"]
          ]
          view    = "timeSeries"
          stacked = false
          region  = var.aws_region
          title   = "Blue Instance (${var.availability_zones[0]}) - Resource Utilization"
          period  = 300
        }
      },
      # ================================================================
      # 3. Green 인스턴스 리소스 사용률 위젯 (우측 절반)
      # ================================================================
      {
        type   = "metric"
        x      = 6 # 우측 시작 (Blue 위젯 옆)
        y      = 6 # ALB 위젯 아래
        width  = 6 # 절반 폭
        height = 6

        properties = {
          # Green 인스턴스(index=1)의 CPU, 메모리, 디스크 사용률
          metrics = [
            ["AWS/EC2", "CPUUtilization", "InstanceId", aws_instance.springboot[1].id],
            ["Empick/EC2", "mem_used_percent", "InstanceId", aws_instance.springboot[1].id],
            [".", "used_percent", "InstanceId", aws_instance.springboot[1].id, "device", "/", "fstype", "xfs", "path", "/"]
          ]
          view    = "timeSeries"
          stacked = false
          region  = var.aws_region
          title   = "Green Instance (${var.availability_zones[1]}) - Resource Utilization"
          period  = 300
        }
      },
      # Blue vs Green CPU 비교
      {
        type   = "metric"
        x      = 0
        y      = 12
        width  = 6
        height = 6

        properties = {
          metrics = [
            ["AWS/EC2", "CPUUtilization", "InstanceId", aws_instance.springboot[0].id, { "label" : "Blue CPU (${var.availability_zones[0]})" }],
            [".", ".", ".", aws_instance.springboot[1].id, { "label" : "Green CPU (${var.availability_zones[1]})" }]
          ]
          view    = "timeSeries"
          stacked = false
          region  = var.aws_region
          title   = "Blue vs Green - CPU Comparison"
          period  = 300
          yAxis = {
            left = {
              min = 0
              max = 100
            }
          }
        }
      },
      # RDS 성능 메트릭
      {
        type   = "metric"
        x      = 6
        y      = 12
        width  = 6
        height = 6

        properties = {
          metrics = [
            ["AWS/RDS", "CPUUtilization", "DBInstanceIdentifier", aws_db_instance.mariadb.id],
            [".", "DatabaseConnections", ".", "."]
          ]
          view    = "timeSeries"
          stacked = false
          region  = var.aws_region
          title   = "RDS Performance Metrics"
          period  = 300
        }
      },
      # Blue-Green 메모리 사용률 비교
      {
        type   = "metric"
        x      = 0
        y      = 18
        width  = 12
        height = 6

        properties = {
          metrics = [
            ["Empick/EC2", "mem_used_percent", "InstanceId", aws_instance.springboot[0].id, { "label" : "Blue Memory (${var.availability_zones[0]})" }],
            [".", ".", ".", aws_instance.springboot[1].id, { "label" : "Green Memory (${var.availability_zones[1]})" }]
          ]
          view    = "timeSeries"
          stacked = false
          region  = var.aws_region
          title   = "Blue vs Green - Memory Usage Comparison"
          period  = 300
          yAxis = {
            left = {
              min = 0
              max = 100
            }
          }
        }
      }
    ]
  })

  # CloudWatch 대시보드는 tags를 지원하지 않음
  # 다른 AWS 리소스와 달리 대시보드는 태그 기반 관리가 불가능
  # 대신 대시보드 이름으로 프로젝트 구분 및 관리
}

# ================================================================================
# 💡 Blue-Green 다중 인스턴스 모니터링 구성 완료!
# 
# ✅ 구현된 내용:
# 1. ALB 모니터링: 3개 알람 (서비스 가용성, 성능, 오류율)
# 2. EC2 모니터링: 6개 알람 (Blue/Green 각각 CPU, 메모리, 디스크)
# 3. RDS 모니터링: 2개 알람 (연결수, CPU)
# 4. CloudWatch 대시보드: Blue-Green 비교 모니터링 뷰
# 
# 🎯 알람 심각도 분류:
# - Critical: 즉시 대응 필요 (서비스 중단 가능성)
# - Warning: 모니터링 강화 필요 (성능 저하)
# - Info: 예방적 조치 필요 (리소스 부족 예고)
# 
# 📊 Blue-Green 모니터링 범위:
# - 사용자 경험: ALB 응답 시간, 오류율, 가용성
# - Blue 인스턴스: CPU, 메모리, 디스크 사용률 (AZ-A)
# - Green 인스턴스: CPU, 메모리, 디스크 사용률 (AZ-C)
# - 성능 비교: Blue vs Green 리소스 사용률 비교
# - 데이터베이스: RDS 연결 상태, 성능 지표
# 
# 🎛️ CloudWatch 대시보드 구성:
# - ALB 성능 메트릭 (전체 폭)
# - Blue 인스턴스 리소스 (좌측) | Green 인스턴스 리소스 (우측)
# - Blue vs Green CPU 비교 (좌측) | RDS 성능 (우측)
# - Blue vs Green 메모리 사용률 비교 (전체 폭)
# 
# 🚀 배포 시 모니터링 활용:
# 1. 평상시: Blue/Green 모두 정상 상태 확인
# 2. 배포 중: 한쪽 인스턴스 비활성화 시 다른 쪽 모니터링 강화
# 3. 성능 비교: 배포 후 Blue vs Green 성능 차이 분석
# 4. 롤백 판단: 성능 저하 감지 시 즉시 롤백 결정
# 
# 🔧 확장 가능성:
# - SNS 알림 연동: 알람 발생 시 즉시 알림
# - 추가 메트릭: 네트워크, 커스텀 애플리케이션 메트릭
# - 자동 복구: Lambda 함수 연동으로 자동 대응
# - 배포 자동화: CloudWatch 메트릭 기반 자동 배포/롤백
# 
# ================================================================================
# 📊 Blue-Green 모니터링 시나리오 가이드
# ================================================================================
# 
# 🟢 평상시 운영 모니터링:
# ┌─────────────────────────────────────────────────────────────────────────────┐
# │ ✅ Blue Instance (AZ-A)     │ ✅ Green Instance (AZ-C)                     │
# │    CPU: 25%                 │    CPU: 30%                                  │
# │    Memory: 60%              │    Memory: 55%                               │
# │    Disk: 40%                │    Disk: 45%                                 │
# │    Status: Active           │    Status: Active                            │
# │    Traffic: 50%             │    Traffic: 50%                              │
# └─────────────────────────────────────────────────────────────────────────────┘
# 
# 🔄 배포 중 단계별 모니터링:
# 
# **1단계: Green 준비 (ALB에서 제외)**
# ┌─────────────────────────────────────────────────────────────────────────────┐
# │ ✅ Blue: 정상 운영           │ 🔧 Green: 배포 준비                          │
# │    CPU: 35% ↑               │    CPU: 15% ↓                                │
# │    Memory: 70% ↑            │    Memory: 45% ↓                             │
# │    Traffic: 100% ↑          │    Traffic: 0% ↓                             │
# │    Connections: 증가         │    Connections: 0                            │
# └─────────────────────────────────────────────────────────────────────────────┘
# 
# **2단계: Green 배포 및 검증**
# ┌─────────────────────────────────────────────────────────────────────────────┐
# │ ✅ Blue: 정상 운영           │ 🔍 Green: Health Check 중                   │
# │    CPU: 35%                 │    CPU: 25%                                  │
# │    Memory: 70%              │    Memory: 55%                               │
# │    Traffic: 100%            │    Traffic: 0% (Health Check만)             │
# │    Status: Stable           │    Status: Testing                           │
# └─────────────────────────────────────────────────────────────────────────────┘
# 
# **3단계: 트래픽 전환**
# ┌─────────────────────────────────────────────────────────────────────────────┐
# │ 🔄 Blue: 비활성화 중         │ ✅ Green: 활성화                             │
# │    CPU: 25% ↓               │    CPU: 35% ↑                               │
# │    Memory: 60% ↓            │    Memory: 70% ↑                            │
# │    Traffic: 0% ↓            │    Traffic: 100% ↑                          │
# │    Status: Deregistering    │    Status: Active                           │
# └─────────────────────────────────────────────────────────────────────────────┘
# 
# **4단계: Blue 업데이트**
# ┌─────────────────────────────────────────────────────────────────────────────┐
# │ 🔧 Blue: 배포 중             │ ✅ Green: 정상 운영                          │
# │    CPU: 15%                 │    CPU: 35%                                  │
# │    Memory: 45%              │    Memory: 70%                               │
# │    Traffic: 0%              │    Traffic: 100%                            │
# │    Status: Updating         │    Status: Stable                           │
# └─────────────────────────────────────────────────────────────────────────────┘
# 
# **5단계: 배포 완료**
# ┌─────────────────────────────────────────────────────────────────────────────┐
# │ ✅ Blue: 새 버전 활성화      │ ✅ Green: 새 버전 활성화                     │
# │    CPU: 25%                 │    CPU: 30%                                  │
# │    Memory: 58%              │    Memory: 62%                               │
# │    Traffic: 50%             │    Traffic: 50%                              │
# │    Status: Active           │    Status: Active                           │
# └─────────────────────────────────────────────────────────────────────────────┘
# 
# 🚨 알람 발생 시나리오:
# 
# **Critical 알람 (즉시 대응 필요)**
# - empick-ec2-blue-high-cpu: Blue 서버 CPU 80% 초과
#   → 대응: Green으로 트래픽 전환 또는 스케일링
# - empick-ec2-green-high-cpu: Green 서버 CPU 80% 초과
#   → 대응: Blue로 트래픽 전환 또는 스케일링
# 
# **Warning 알람 (모니터링 강화)**
# - empick-ec2-blue-high-memory: Blue 서버 메모리 85% 초과
#   → 대응: 메모리 사용량 분석 및 최적화 계획
# - empick-ec2-green-high-memory: Green 서버 메모리 85% 초과
#   → 대응: 메모리 사용량 분석 및 최적화 계획
# 
# **Info 알람 (예방 조치)**
# - empick-ec2-blue-high-disk: Blue 서버 디스크 90% 초과
#   → 대응: 로그 정리, 임시 파일 삭제, 디스크 확장 계획
# - empick-ec2-green-high-disk: Green 서버 디스크 90% 초과
#   → 대응: 로그 정리, 임시 파일 삭제, 디스크 확장 계획
# 
# 📈 성능 비교 분석 예시:
# 
# **배포 전후 성능 비교**
# ┌─────────────────────────────────────────────────────────────────────────────┐
# │ 메트릭          │ 배포 전      │ 배포 후      │ 변화율    │ 상태           │
# │ ─────────────── │ ─────────── │ ─────────── │ ───────── │ ─────────────  │
# │ Blue CPU        │ 25%         │ 28%         │ +3%       │ ✅ 정상        │
# │ Green CPU       │ 30%         │ 27%         │ -3%       │ ✅ 정상        │
# │ Blue Memory     │ 60%         │ 58%         │ -2%       │ ✅ 개선        │
# │ Green Memory    │ 55%         │ 62%         │ +7%       │ ⚠️ 주의        │
# │ ALB Response    │ 150ms       │ 140ms       │ -10ms     │ ✅ 개선        │
# │ 5XX Errors      │ 2/hour      │ 1/hour      │ -50%      │ ✅ 개선        │
# └─────────────────────────────────────────────────────────────────────────────┘
# 
# 🎯 모니터링 기반 의사결정:
# 
# **정상 배포 (계속 진행)**
# - 모든 메트릭이 임계값 이내
# - 성능 저하 없음 또는 개선
# - Health Check 통과
# 
# **주의 필요 (모니터링 강화)**
# - 일부 메트릭 상승하지만 임계값 이내
# - 성능 약간 저하하지만 허용 범위
# - 추가 모니터링 후 판단
# 
# **롤백 필요 (즉시 이전 버전 복원)**
# - Critical 알람 발생
# - 성능 현저히 저하
# - Health Check 실패
# - 5XX 에러 급증
# 
# 📋 운영팀 대응 가이드:
# 
# **1. 일상 모니터링**
# - CloudWatch 대시보드 정기 확인 (30분마다)
# - Blue vs Green 성능 비교 분석
# - 알람 발생 시 즉시 대응
# 
# **2. 배포 시 모니터링**
# - 배포 전: 베이스라인 메트릭 확인
# - 배포 중: 실시간 모니터링 (5분 간격)
# - 배포 후: 30분간 집중 모니터링
# 
# **3. 장애 대응**
# - Critical 알람: 5분 이내 대응
# - Warning 알람: 15분 이내 확인
# - Info 알람: 1시간 이내 조치 계획
# 
# **4. 성능 최적화**
# - 주간 성능 리포트 작성
# - Blue vs Green 성능 차이 분석
# - 리소스 사용률 기반 인스턴스 타입 조정
# ================================================================================ 
