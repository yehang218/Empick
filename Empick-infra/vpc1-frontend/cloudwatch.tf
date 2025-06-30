# =============================================================================
# VPC1 Frontend CloudWatch 모니터링 구성
# =============================================================================

# CloudWatch Log Groups
resource "aws_cloudwatch_log_group" "nginx_access_logs" {
  name              = "/aws/vpc1-frontend/nginx/access"
  retention_in_days = 7
}

resource "aws_cloudwatch_log_group" "nginx_error_logs" {
  name              = "/aws/vpc1-frontend/nginx/error"
  retention_in_days = 7
}

resource "aws_cloudwatch_log_group" "auto_deploy_logs" {
  name              = "/aws/vpc1-frontend/auto-deploy"
  retention_in_days = 3
}

# CloudWatch Alarms for ALB
resource "aws_cloudwatch_metric_alarm" "alb_unhealthy_targets" {
  alarm_name          = "empick-vpc1-alb-unhealthy-targets"
  comparison_operator = "GreaterThanThreshold"
  evaluation_periods  = "2"
  metric_name         = "UnHealthyHostCount"
  namespace           = "AWS/ApplicationELB"
  period              = "60"
  statistic           = "Average"
  threshold           = "0"
  alarm_description   = "This metric monitors unhealthy ALB targets"
  alarm_actions       = [aws_sns_topic.vpc1_alerts.arn]
  ok_actions          = [aws_sns_topic.vpc1_alerts.arn]

  dimensions = {
    TargetGroup = aws_lb_target_group.nginx_tg.arn_suffix
  }
}

resource "aws_cloudwatch_metric_alarm" "alb_response_time" {
  alarm_name          = "empick-vpc1-alb-high-response-time"
  comparison_operator = "GreaterThanThreshold"
  evaluation_periods  = "2"
  metric_name         = "TargetResponseTime"
  namespace           = "AWS/ApplicationELB"
  period              = "300"
  statistic           = "Average"
  threshold           = "2"
  alarm_description   = "This metric monitors ALB response time"
  alarm_actions       = [aws_sns_topic.vpc1_alerts.arn]

  dimensions = {
    LoadBalancer = aws_lb.nginx_alb.arn_suffix
  }
}

resource "aws_cloudwatch_metric_alarm" "alb_4xx_errors" {
  alarm_name          = "empick-vpc1-alb-4xx-errors"
  comparison_operator = "GreaterThanThreshold"
  evaluation_periods  = "2"
  metric_name         = "HTTPCode_Target_4XX_Count"
  namespace           = "AWS/ApplicationELB"
  period              = "300"
  statistic           = "Sum"
  threshold           = "10"
  alarm_description   = "This metric monitors 4XX errors from ALB"
  alarm_actions       = [aws_sns_topic.vpc1_alerts.arn]

  dimensions = {
    LoadBalancer = aws_lb.nginx_alb.arn_suffix
  }
}

# CloudWatch Alarms for EC2 Instances
resource "aws_cloudwatch_metric_alarm" "ec2_cpu_blue" {
  alarm_name          = "empick-vpc1-ec2-blue-high-cpu"
  comparison_operator = "GreaterThanThreshold"
  evaluation_periods  = "2"
  metric_name         = "CPUUtilization"
  namespace           = "AWS/EC2"
  period              = "300"
  statistic           = "Average"
  threshold           = "80"
  alarm_description   = "This metric monitors Blue instance CPU usage"
  alarm_actions       = [aws_sns_topic.vpc1_alerts.arn]

  dimensions = {
    InstanceId = aws_instance.nginx_blue.id
  }
}

resource "aws_cloudwatch_metric_alarm" "ec2_cpu_green" {
  alarm_name          = "empick-vpc1-ec2-green-high-cpu"
  comparison_operator = "GreaterThanThreshold"
  evaluation_periods  = "2"
  metric_name         = "CPUUtilization"
  namespace           = "AWS/EC2"
  period              = "300"
  statistic           = "Average"
  threshold           = "80"
  alarm_description   = "This metric monitors Green instance CPU usage"
  alarm_actions       = [aws_sns_topic.vpc1_alerts.arn]

  dimensions = {
    InstanceId = aws_instance.nginx_green.id
  }
}

resource "aws_cloudwatch_metric_alarm" "ec2_memory_blue" {
  alarm_name          = "empick-vpc1-ec2-blue-high-memory"
  comparison_operator = "GreaterThanThreshold"
  evaluation_periods  = "2"
  metric_name         = "MemoryUtilization"
  namespace           = "CWAgent"
  period              = "300"
  statistic           = "Average"
  threshold           = "85"
  alarm_description   = "This metric monitors Blue instance memory usage"
  alarm_actions       = [aws_sns_topic.vpc1_alerts.arn]

  dimensions = {
    InstanceId = aws_instance.nginx_blue.id
  }
}

resource "aws_cloudwatch_metric_alarm" "ec2_memory_green" {
  alarm_name          = "empick-vpc1-ec2-green-high-memory"
  comparison_operator = "GreaterThanThreshold"
  evaluation_periods  = "2"
  metric_name         = "MemoryUtilization"
  namespace           = "CWAgent"
  period              = "300"
  statistic           = "Average"
  threshold           = "85"
  alarm_description   = "This metric monitors Green instance memory usage"
  alarm_actions       = [aws_sns_topic.vpc1_alerts.arn]

  dimensions = {
    InstanceId = aws_instance.nginx_green.id
  }
}

# SNS Topic for Alerts
resource "aws_sns_topic" "vpc1_alerts" {
  name = "empick-vpc1-frontend-alerts"
}

# CloudWatch Dashboard
resource "aws_cloudwatch_dashboard" "vpc1_frontend" {
  dashboard_name = "Empick-VPC1-Frontend"

  dashboard_body = jsonencode({
    widgets = [
      {
        type   = "metric"
        x      = 0
        y      = 0
        width  = 12
        height = 6

        properties = {
          metrics = [
            ["AWS/ApplicationELB", "RequestCount", "LoadBalancer", aws_lb.nginx_alb.arn_suffix],
            [".", "TargetResponseTime", ".", "."],
            [".", "HTTPCode_Target_2XX_Count", ".", "."],
            [".", "HTTPCode_Target_4XX_Count", ".", "."],
            [".", "HTTPCode_Target_5XX_Count", ".", "."]
          ]
          view    = "timeSeries"
          stacked = false
          region  = "ap-northeast-2"
          title   = "ALB Metrics"
          period  = 300
        }
      },
      {
        type   = "metric"
        x      = 12
        y      = 0
        width  = 12
        height = 6

        properties = {
          metrics = [
            ["AWS/ApplicationELB", "HealthyHostCount", "TargetGroup", aws_lb_target_group.nginx_tg.arn_suffix],
            [".", "UnHealthyHostCount", ".", "."]
          ]
          view    = "timeSeries"
          stacked = false
          region  = "ap-northeast-2"
          title   = "Target Health"
          period  = 300
        }
      },
      {
        type   = "metric"
        x      = 0
        y      = 6
        width  = 12
        height = 6

        properties = {
          metrics = [
            ["AWS/EC2", "CPUUtilization", "InstanceId", aws_instance.nginx_blue.id],
            [".", ".", ".", aws_instance.nginx_green.id]
          ]
          view    = "timeSeries"
          stacked = false
          region  = "ap-northeast-2"
          title   = "EC2 CPU Utilization"
          period  = 300
        }
      },
      {
        type   = "metric"
        x      = 12
        y      = 6
        width  = 12
        height = 6

        properties = {
          metrics = [
            ["CWAgent", "MemoryUtilization", "InstanceId", aws_instance.nginx_blue.id],
            [".", ".", ".", aws_instance.nginx_green.id]
          ]
          view    = "timeSeries"
          stacked = false
          region  = "ap-northeast-2"
          title   = "EC2 Memory Utilization"
          period  = 300
        }
      }
    ]
  })
}

# Custom Metrics for Auto-Deploy Success Rate
resource "aws_cloudwatch_log_metric_filter" "auto_deploy_success" {
  name           = "AutoDeploySuccess"
  log_group_name = aws_cloudwatch_log_group.auto_deploy_logs.name
  pattern        = "[timestamp, level=\"INFO\", message=\"Deployment completed successfully!\"]"

  metric_transformation {
    name      = "AutoDeploySuccessCount"
    namespace = "Empick/VPC1/AutoDeploy"
    value     = "1"
  }
}

resource "aws_cloudwatch_log_metric_filter" "auto_deploy_failure" {
  name           = "AutoDeployFailure"
  log_group_name = aws_cloudwatch_log_group.auto_deploy_logs.name
  pattern        = "[timestamp, level=\"ERROR\", message=\"*failed*\"]"

  metric_transformation {
    name      = "AutoDeployFailureCount"
    namespace = "Empick/VPC1/AutoDeploy"
    value     = "1"
  }
}

# CloudWatch Alarm for Auto-Deploy Failures
resource "aws_cloudwatch_metric_alarm" "auto_deploy_failures" {
  alarm_name          = "empick-vpc1-auto-deploy-failures"
  comparison_operator = "GreaterThanThreshold"
  evaluation_periods  = "1"
  metric_name         = "AutoDeployFailureCount"
  namespace           = "Empick/VPC1/AutoDeploy"
  period              = "300"
  statistic           = "Sum"
  threshold           = "0"
  alarm_description   = "This metric monitors auto-deployment failures"
  alarm_actions       = [aws_sns_topic.vpc1_alerts.arn]
  treat_missing_data  = "notBreaching"
}
