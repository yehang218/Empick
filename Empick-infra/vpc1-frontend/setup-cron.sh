#!/bin/bash

# VPC1 Frontend Cron 설정 스크립트
# 자동 배포 스크립트를 주기적으로 실행하도록 설정

set -euo pipefail

# 설정
AUTO_DEPLOY_SCRIPT="/home/ec2-user/auto-deploy.sh"
CRON_JOB="* * * * * /home/ec2-user/auto-deploy.sh >> /var/log/auto-deploy-cron.log 2>&1"

echo "🔧 Setting up auto-deployment cron job..."

# 1. 자동 배포 스크립트 실행 권한 설정
chmod +x "$AUTO_DEPLOY_SCRIPT"

# 2. 로그 파일 생성 및 권한 설정
sudo touch /var/log/auto-deploy.log
sudo touch /var/log/auto-deploy-cron.log
sudo chown ec2-user:ec2-user /var/log/auto-deploy.log
sudo chown ec2-user:ec2-user /var/log/auto-deploy-cron.log

# 3. 기존 cron 작업 제거 (중복 방지)
crontab -l 2>/dev/null | grep -v "auto-deploy.sh" | crontab - 2>/dev/null || true

# 4. 새로운 cron 작업 추가 (매분 실행)
(crontab -l 2>/dev/null; echo "$CRON_JOB") | crontab -

# 5. cron 서비스 시작/재시작
sudo systemctl enable crond
sudo systemctl start crond

echo "✅ Auto-deployment cron job configured successfully"
echo "📋 Cron job: $CRON_JOB"
echo "📝 Logs: /var/log/auto-deploy.log, /var/log/auto-deploy-cron.log"

# 6. 첫 번째 배포 시도 (기존 빌드가 있다면)
echo "🚀 Running initial deployment check..."
"$AUTO_DEPLOY_SCRIPT" || echo "⚠️ No initial deployment trigger found (this is normal)"

echo "🎉 Auto-deployment setup completed!" 