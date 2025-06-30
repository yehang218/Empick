#!/bin/bash
set -euo pipefail

# =============================================================================
# 고급 User Data 스크립트 - 타이밍 및 재실행 고려
# =============================================================================

LOG_FILE="/var/log/user-data-advanced.log"
exec > >(tee -a $LOG_FILE)
exec 2>&1

echo "🚀 [$(date)] Advanced Empick Frontend Setup Started"
echo "Environment: ${environment}"
echo "Project: ${project}"
echo "Color: ${color}"

# =============================================================================
# 1. 실행 환경 체크 및 초기화
# =============================================================================

# Cloud-init 상태 확인
CLOUD_INIT_STATUS="/var/lib/cloud/instance/boot-finished"
USER_DATA_MARKER="/tmp/empick-userdata-completed"

if [[ -f "$USER_DATA_MARKER" ]]; then
    echo "⚠️ User Data already executed successfully. Skipping..."
    exit 0
fi

# 시스템 준비 대기 (최대 300초)
wait_for_system_ready() {
    local max_wait=300
    local count=0
    
    echo "⏳ Waiting for system to be ready..."
    while [[ $count -lt $max_wait ]]; do
        if systemctl is-system-running --wait 2>/dev/null; then
            echo "✅ System is ready"
            return 0
        fi
        sleep 5
        ((count+=5))
        echo "... waiting ($count/$max_wait seconds)"
    done
    
    echo "⚠️ System readiness timeout, proceeding anyway..."
    return 1
}

wait_for_system_ready

# =============================================================================
# 2. 패키지 설치 (재시도 로직 포함)
# =============================================================================

install_packages_with_retry() {
    local packages="$1"
    local max_retries=3
    local retry=0
    
    while [[ $retry -lt $max_retries ]]; do
        echo "📦 Installing packages (attempt $((retry+1))/$max_retries): $packages"
        
        if dnf update -y && dnf install -y --allowerasing $packages; then
            echo "✅ Packages installed successfully"
            return 0
        fi
        
        ((retry++))
        if [[ $retry -lt $max_retries ]]; then
            echo "❌ Installation failed, retrying in 10 seconds..."
            sleep 10
        fi
    done
    
    echo "❌ Package installation failed after $max_retries attempts"
    return 1
}

# 필수 패키지 설치
ESSENTIAL_PACKAGES="nginx git curl wget tar rsync cronie amazon-ssm-agent amazon-cloudwatch-agent"
install_packages_with_retry "$ESSENTIAL_PACKAGES"

# Node.js 설치 (별도 처리)
install_nodejs() {
    echo "📦 Installing Node.js..."
    if curl -fsSL https://rpm.nodesource.com/setup_18.x | bash - && dnf install -y nodejs; then
        echo "✅ Node.js installed: $(node --version)"
        return 0
    else
        echo "❌ Node.js installation failed"
        return 1
    fi
}

install_nodejs

# =============================================================================
# 3. 서비스 초기화 (순서 및 의존성 고려)
# =============================================================================

initialize_services() {
    echo "🔧 Initializing services..."
    
    # 서비스 활성화 (부팅 시 자동 시작)
    local services=("crond" "amazon-ssm-agent" "amazon-cloudwatch-agent" "nginx")
    
    for service in "$${services[@]}"; do
        echo "Enabling $service..."
        systemctl enable "$service" || echo "⚠️ Failed to enable $service"
    done
    
    # 서비스 시작 (의존성 순서 고려)
    echo "Starting crond..."
    systemctl start crond && echo "✅ crond started" || echo "❌ crond failed"
    
    echo "Starting SSM agent..."
    systemctl start amazon-ssm-agent && echo "✅ SSM agent started" || echo "❌ SSM agent failed"
    
    echo "Starting CloudWatch agent..."
    systemctl start amazon-cloudwatch-agent && echo "✅ CloudWatch agent started" || echo "❌ CloudWatch agent failed"
    
    # Nginx는 설정 후 시작
}

initialize_services

# =============================================================================
# 4. Nginx 설정
# =============================================================================

configure_nginx() {
    echo "🔧 Configuring Nginx..."
    
    # 백업
    cp /etc/nginx/nginx.conf /etc/nginx/nginx.conf.backup
    
    # 설정 파일 생성
    cat > /etc/nginx/nginx.conf << 'NGINX_EOF'
user nginx;
worker_processes auto;
error_log /var/log/nginx/error.log;
pid /run/nginx.pid;

include /usr/share/nginx/modules/*.conf;

events {
    worker_connections 1024;
}

http {
    log_format main '$remote_addr - $remote_user [$time_local] "$request" '
                    '$status $body_bytes_sent "$http_referer" '
                    '"$http_user_agent" "$http_x_forwarded_for"';

    access_log /var/log/nginx/access.log main;
    sendfile on;
    tcp_nopush on;
    tcp_nodelay on;
    keepalive_timeout 65;
    types_hash_max_size 4096;
    client_max_body_size 20M;

    include /etc/nginx/mime.types;
    default_type application/octet-stream;

    gzip on;
    gzip_vary on;
    gzip_min_length 1024;
    gzip_proxied any;
    gzip_comp_level 6;
    gzip_types
        text/plain
        text/css
        text/xml
        text/javascript
        application/json
        application/javascript
        application/xml+rss
        application/atom+xml
        image/svg+xml;

    server {
        listen 80 default_server;
        listen [::]:80 default_server;
        server_name _;
        root /var/www/html;
        index index.html;

        location /health {
            access_log off;
            return 200 "healthy\n";
            add_header Content-Type text/plain;
        }

        location /api/ {
            proxy_pass http://backend.empick.internal/api/;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
            proxy_connect_timeout 30s;
            proxy_send_timeout 30s;
            proxy_read_timeout 30s;
            proxy_set_header X-Forwarded-Host $server_name;
            proxy_set_header X-Real-Port $server_port;
        }

        location / {
            try_files $uri $uri/ /index.html;
            
            location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff2|woff|ttf|eot)$ {
                expires 1y;
                add_header Cache-Control "public, immutable";
            }
        }

        add_header X-Frame-Options "SAMEORIGIN" always;
        add_header X-XSS-Protection "1; mode=block" always;
        add_header X-Content-Type-Options "nosniff" always;
        add_header Referrer-Policy "no-referrer-when-downgrade" always;
        add_header Content-Security-Policy "default-src 'self' http: https: data: blob: 'unsafe-inline'" always;
    }
}
NGINX_EOF
    
    # 웹 디렉토리 생성
    mkdir -p /var/www/html
    chown -R nginx:nginx /var/www/html
    chmod -R 755 /var/www/html
    
    # 임시 인덱스 페이지
    cat > /var/www/html/index.html << HTML_EOF
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Empick Frontend - $(echo ${color} | tr '[:lower:]' '[:upper:]') Server</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0; padding: 0;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white; display: flex; justify-content: center; align-items: center;
            min-height: 100vh;
        }
        .container {
            text-align: center; padding: 2rem;
            background: rgba(255, 255, 255, 0.1); border-radius: 15px;
            backdrop-filter: blur(10px); box-shadow: 0 8px 32px rgba(31, 38, 135, 0.37);
        }
        .logo { font-size: 3rem; margin-bottom: 1rem; }
        .server-info { background: rgba(255, 255, 255, 0.2); padding: 1rem; border-radius: 10px; margin: 1rem 0; }
        .status { color: #4ade80; font-weight: bold; }
        .advanced { color: #fbbf24; font-size: 0.9rem; margin-top: 1rem; }
    </style>
</head>
<body>
    <div class="container">
        <div class="logo">🚀</div>
        <h1>Empick Frontend</h1>
        <div class="server-info">
            <h2>Server: <span style="color: ${color};">$(echo ${color} | tr '[:lower:]' '[:upper:]')</span></h2>
            <p>Environment: ${environment}</p>
            <p>Status: <span class="status">Advanced Setup Ready</span></p>
            <p>Timestamp: $(date)</p>
        </div>
        <p>Vue.js 애플리케이션이 곧 배포됩니다.</p>
        <div class="advanced">🔧 Advanced User Data Script Applied</div>
    </div>
</body>
</html>
HTML_EOF
    
    # 설정 테스트 후 시작
    if nginx -t; then
        systemctl start nginx && echo "✅ Nginx started successfully"
    else
        echo "❌ Nginx configuration test failed"
        return 1
    fi
}

configure_nginx

# =============================================================================
# 5. 자동 배포 시스템 설정 (타이밍 고려)
# =============================================================================

setup_auto_deployment() {
    echo "🚀 Setting up auto-deployment system..."
    
    # ec2-user 사용자 존재 확인 및 대기
    wait_for_user() {
        local max_wait=60
        local count=0
        
        while [[ $count -lt $max_wait ]]; do
            if id ec2-user &>/dev/null && [[ -d /home/ec2-user ]]; then
                echo "✅ ec2-user is ready"
                return 0
            fi
            sleep 2
            ((count+=2))
        done
        
        echo "❌ ec2-user not ready after $max_wait seconds"
        return 1
    }
    
    if ! wait_for_user; then
        echo "❌ Cannot setup auto-deployment: ec2-user not available"
        return 1
    fi
    
    # 자동 배포 스크립트 생성
    cat > /home/ec2-user/auto-deploy.sh << 'AUTO_DEPLOY_EOF'
#!/bin/bash
set -euo pipefail

LOG_FILE="/var/log/auto-deploy.log"
exec > >(tee -a $LOG_FILE) 2>&1

echo "[$(date)] Auto-deploy check started"

S3_BUCKET="empick-private-bucket"
TRIGGER_KEY="frontend-builds/deploy-trigger.json"
BUILD_KEY="frontend-builds/latest.tar.gz"
LAST_TRIGGER="/tmp/last-trigger.json"

# S3에서 트리거 파일 확인
if aws s3 cp "s3://$S3_BUCKET/$TRIGGER_KEY" /tmp/current-trigger.json 2>/dev/null; then
    if [ ! -f "$LAST_TRIGGER" ] || ! cmp -s /tmp/current-trigger.json "$LAST_TRIGGER"; then
        echo "[$(date)] New deployment trigger detected!"
        
        if aws s3 cp "s3://$S3_BUCKET/$BUILD_KEY" /tmp/latest.tar.gz; then
            echo "[$(date)] Deploying new build..."
            
            # 백업 생성
            sudo cp -r /var/www/html /var/www/html.backup.$(date +%H%M%S) 2>/dev/null || true
            
            # 새 파일 배포
            sudo rm -rf /var/www/html/*
            sudo tar -xzf /tmp/latest.tar.gz -C /var/www/html/
            sudo chown -R nginx:nginx /var/www/html/
            
            # Nginx 재시작
            sudo systemctl reload nginx
            
            # 트리거 파일 저장
            mv /tmp/current-trigger.json "$LAST_TRIGGER"
            rm -f /tmp/latest.tar.gz
            
            echo "[$(date)] Deployment completed successfully!"
        else
            echo "[$(date)] Failed to download build file"
        fi
    else
        echo "[$(date)] No new deployment detected"
        rm -f /tmp/current-trigger.json
    fi
else
    echo "[$(date)] No trigger file found"
fi
AUTO_DEPLOY_EOF
    
    # 권한 설정
    chmod +x /home/ec2-user/auto-deploy.sh
    chown ec2-user:ec2-user /home/ec2-user/auto-deploy.sh
    
    # 로그 파일 준비
    touch /var/log/auto-deploy.log
    chown ec2-user:ec2-user /var/log/auto-deploy.log
    
    # Cron 설정 (지연 실행으로 안정성 확보)
    setup_cron_delayed() {
        # 30초 후 cron 설정 (시스템 완전 초기화 대기)
        cat > /tmp/setup_cron.sh << 'CRON_SETUP_EOF'
#!/bin/bash
sleep 30
echo "Setting up cron job for ec2-user..."
echo "* * * * * /home/ec2-user/auto-deploy.sh" | crontab -u ec2-user -
echo "Cron job setup completed at $(date)" >> /var/log/user-data-advanced.log
CRON_SETUP_EOF
        
        chmod +x /tmp/setup_cron.sh
        nohup /tmp/setup_cron.sh > /dev/null 2>&1 &
    }
    
    setup_cron_delayed
    
    echo "✅ Auto-deployment system configured (with delayed cron setup)"
}

setup_auto_deployment

# =============================================================================
# 6. 시스템 설정 완료
# =============================================================================

# 방화벽 비활성화
systemctl stop firewalld 2>/dev/null || true
systemctl disable firewalld 2>/dev/null || true

# SSH 설정
systemctl enable sshd
systemctl restart sshd

# =============================================================================
# 7. 최종 검증 및 완료 처리
# =============================================================================

final_verification() {
    echo "🔍 Final verification..."
    
    # 서비스 상태 확인
    local services=("nginx" "crond" "amazon-ssm-agent")
    local all_good=true
    
    for service in "$${services[@]}"; do
        if systemctl is-active --quiet "$service"; then
            echo "✅ $service is running"
        else
            echo "❌ $service is not running"
            all_good=false
        fi
    done
    
    # 파일 존재 확인
    if [[ -f /home/ec2-user/auto-deploy.sh ]]; then
        echo "✅ Auto-deploy script exists"
    else
        echo "❌ Auto-deploy script missing"
        all_good=false
    fi
    
    # 웹 서비스 확인
    if curl -s http://localhost/health > /dev/null; then
        echo "✅ Web service responding"
    else
        echo "❌ Web service not responding"
        all_good=false
    fi
    
    if $all_good; then
        echo "🎉 All systems operational!"
        return 0
    else
        echo "⚠️ Some issues detected, but continuing..."
        return 1
    fi
}

final_verification

# 완료 마커 생성
touch "$USER_DATA_MARKER"
echo "timestamp=$(date)" >> "$USER_DATA_MARKER"
echo "status=completed" >> "$USER_DATA_MARKER"
echo "version=advanced" >> "$USER_DATA_MARKER"

echo "=================================="
echo "🎉 [$(date)] Advanced Empick Frontend Setup Completed!"
echo "=================================="
echo "Server Color: $(echo ${color} | tr '[:lower:]' '[:upper:]')"
echo "Auto-deployment: Enabled (with delayed cron setup)"
echo "Log file: $LOG_FILE"
echo "Completion marker: $USER_DATA_MARKER"
echo "Node.js Version: $(node --version 2>/dev/null || echo 'Not available')"
echo "Nginx Version: $(nginx -v 2>&1 | head -1 || echo 'Not available')"
echo "==================================" 