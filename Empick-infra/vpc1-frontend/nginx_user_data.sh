#!/bin/bash
# ================================================================================
# VPC1 Frontend - Nginx User Data Script
# Vue.js 애플리케이션을 서빙하는 Nginx 서버 설정
# ================================================================================

set -euo pipefail  # 에러 발생 시 스크립트 중단, 정의되지 않은 변수 사용 시 에러

# 로그 설정
LOG_FILE="/var/log/user-data.log"
exec > >(tee -a $LOG_FILE)
exec 2>&1

echo "=================================================="
echo "🚀 Empick Frontend Setup Started"
echo "Environment: ${environment}"
echo "Project: ${project}"
echo "Color: ${color}"
echo "Timestamp: $(date)"
echo "=================================================="

# 시스템 업데이트
echo "📦 Updating system packages..."
dnf update -y

# 필수 패키지 설치 (패키지 충돌 해결)
echo "📦 Installing essential packages..."
dnf install -y --allowerasing \
    nginx \
    git \
    curl \
    wget \
    unzip \
    htop \
    tree \
    jq \
    amazon-ssm-agent \
    amazon-cloudwatch-agent

# Node.js 18 설치 (Amazon Linux용)
echo "📦 Installing Node.js 18..."
curl -fsSL https://rpm.nodesource.com/setup_18.x | bash -
dnf install -y --allowerasing nodejs

# 서비스 시작 및 활성화
echo "🔧 Starting and enabling services..."
systemctl enable nginx
systemctl enable amazon-ssm-agent
systemctl enable amazon-cloudwatch-agent

systemctl start amazon-ssm-agent
systemctl start amazon-cloudwatch-agent

# Nginx 기본 설정 백업
echo "🔧 Backing up default Nginx configuration..."
cp /etc/nginx/nginx.conf /etc/nginx/nginx.conf.backup

# Nginx 설정 생성
echo "🔧 Creating Nginx configuration..."
cat > /etc/nginx/nginx.conf << 'EOF'
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

    # Gzip 압축 설정
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

    # Vue.js SPA 설정
    server {
        listen 80 default_server;
        listen [::]:80 default_server;
        server_name _;
        root /var/www/html;
        index index.html;

        # Health Check 엔드포인트 (ALB용)
        location /health {
            access_log off;
            return 200 "healthy\n";
            add_header Content-Type text/plain;
        }

        # API 프록시 (VPC2 Backend로 전달)
        location /api/ {
            # ✅ 수정: /api/ 요청을 Backend의 /api/로 그대로 전달
            proxy_pass http://backend.empick.internal/api/;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
            proxy_connect_timeout 30s;
            proxy_send_timeout 30s;
            proxy_read_timeout 30s;
            
            # 추가 헤더 설정
            proxy_set_header X-Forwarded-Host $server_name;
            proxy_set_header X-Real-Port $server_port;
        }

        # Vue.js SPA 라우팅 (History Mode)
        location / {
            try_files $uri $uri/ /index.html;
            
            # 정적 파일 캐싱
            location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff2|woff|ttf|eot)$ {
                expires 1y;
                add_header Cache-Control "public, immutable";
            }
        }

        # 보안 헤더
        add_header X-Frame-Options "SAMEORIGIN" always;
        add_header X-XSS-Protection "1; mode=block" always;
        add_header X-Content-Type-Options "nosniff" always;
        add_header Referrer-Policy "no-referrer-when-downgrade" always;
        add_header Content-Security-Policy "default-src 'self' http: https: data: blob: 'unsafe-inline'" always;
    }
}
EOF

# HTML 디렉토리 생성 및 권한 설정
echo "📁 Creating web directory..."
mkdir -p /var/www/html
chown -R nginx:nginx /var/www/html
chmod -R 755 /var/www/html

# 임시 인덱스 페이지 생성 (배포 전까지)
echo "📄 Creating temporary index page..."
cat > /var/www/html/index.html << EOF
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Empick Frontend - $(echo ${color} | tr '[:lower:]' '[:upper:]') Server</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            padding: 0;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
        }
        .container {
            text-align: center;
            padding: 2rem;
            background: rgba(255, 255, 255, 0.1);
            border-radius: 15px;
            backdrop-filter: blur(10px);
            box-shadow: 0 8px 32px rgba(31, 38, 135, 0.37);
        }
        .logo {
            font-size: 3rem;
            margin-bottom: 1rem;
        }
        .server-info {
            background: rgba(255, 255, 255, 0.2);
            padding: 1rem;
            border-radius: 10px;
            margin: 1rem 0;
        }
        .status {
            color: #4ade80;
            font-weight: bold;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="logo">🚀</div>
        <h1>Empick Frontend</h1>
        <div class="server-info">
            <h2>Server: <span style="color: ${color};">$(echo ${color} | tr '[:lower:]' '[:upper:]')</span></h2>
            <p>Environment: ${environment}</p>
            <p>Status: <span class="status">Ready for Deployment</span></p>
            <p>Timestamp: $(date)</p>
        </div>
        <p>Vue.js 애플리케이션이 곧 배포됩니다.</p>
    </div>
</body>
</html>
EOF

# Nginx 설정 테스트 및 시작
echo "🔧 Testing Nginx configuration..."
nginx -t

echo "🚀 Starting Nginx..."
systemctl start nginx

# 방화벽 설정 (Amazon Linux 2023)
echo "🔒 Configuring firewall..."
systemctl stop firewalld 2>/dev/null || true
systemctl disable firewalld 2>/dev/null || true

# SSH 키 설정 (VPC2 Backend와 동일한 방식)
echo "🔑 Setting up SSH keys..."
PRIVATE_KEY_CONTENT="${private_key_content}"

if [ -n "$PRIVATE_KEY_CONTENT" ]; then
    echo "🔑 Setting up SSH keys from private key..."
    
    # ec2-user 홈 디렉토리의 .ssh 디렉토리 생성
    mkdir -p /home/ec2-user/.ssh
    chmod 700 /home/ec2-user/.ssh
    
    # Private key 저장
    echo "$PRIVATE_KEY_CONTENT" > /home/ec2-user/.ssh/camp.pem
    chmod 600 /home/ec2-user/.ssh/camp.pem
    
    # Private key에서 public key 추출
    ssh-keygen -y -f /home/ec2-user/.ssh/camp.pem > /home/ec2-user/.ssh/camp.pub
    chmod 644 /home/ec2-user/.ssh/camp.pub
    
    # Public key를 authorized_keys에 추가
    cat /home/ec2-user/.ssh/camp.pub >> /home/ec2-user/.ssh/authorized_keys
    chmod 600 /home/ec2-user/.ssh/authorized_keys
    
    # 소유권 설정
    chown -R ec2-user:ec2-user /home/ec2-user/.ssh
    
    echo "✅ SSH keys configured successfully"
else
    echo "⚠️ No private key content provided"
fi

# SSH 서비스 확인 및 재시작
echo "🔑 Configuring SSH service..."
systemctl enable sshd
systemctl restart sshd

# 배포 스크립트 생성 (GitHub Actions용)
echo "📜 Creating deployment script..."
cat > /home/ec2-user/deploy.sh << 'DEPLOY_EOF'
#!/bin/bash
# Vue.js 배포 스크립트

set -e

DEPLOY_DIR="/var/www/html"
BACKUP_DIR="/var/www/backup"
BUILD_DIR="/tmp/vue-build"

echo "🚀 Starting Vue.js deployment..."

# 백업 생성
if [ -d "$DEPLOY_DIR" ]; then
    echo "📦 Creating backup..."
    mkdir -p "$BACKUP_DIR"
    cp -r "$DEPLOY_DIR" "$BACKUP_DIR/$(date +%Y%m%d_%H%M%S)"
fi

# 새 빌드 파일 배포
if [ -d "$BUILD_DIR" ]; then
    echo "📁 Deploying new build..."
    rm -rf "$DEPLOY_DIR"/*
    cp -r "$BUILD_DIR"/* "$DEPLOY_DIR"/
    chown -R nginx:nginx "$DEPLOY_DIR"
    chmod -R 755 "$DEPLOY_DIR"
fi

# Nginx 재로드
echo "🔄 Reloading Nginx..."
nginx -t && systemctl reload nginx

echo "✅ Deployment completed successfully!"
DEPLOY_EOF

chmod +x /home/ec2-user/deploy.sh
chown ec2-user:ec2-user /home/ec2-user/deploy.sh

# 자동 배포 스크립트 생성 (S3 기반)
echo "📜 Creating auto-deployment script..."
cat > /home/ec2-user/auto-deploy.sh << 'AUTO_DEPLOY_EOF'
#!/bin/bash

# VPC1 Frontend 자동 배포 스크립트
# S3에서 새로운 빌드를 감지하면 자동으로 배포

set -euo pipefail

# 설정
S3_BUCKET="empick-private-bucket"
S3_TRIGGER_KEY="frontend-builds/deploy-trigger.json"
S3_BUILD_KEY="frontend-builds/latest.tar.gz"
LOCAL_TRIGGER_FILE="/tmp/last-deploy-trigger.json"
LOCAL_BUILD_FILE="/tmp/frontend-build.tar.gz"
DEPLOY_DIR="/tmp/vue-build"
NGINX_ROOT="/var/www/html"
LOG_FILE="/var/log/auto-deploy.log"

# 로그 함수
log() {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] $1" | tee -a "$LOG_FILE"
}

# 배포 함수
deploy_build() {
    log "🚀 Starting deployment..."
    
    # 1. S3에서 빌드 파일 다운로드
    log "📥 Downloading build from S3..."
    aws s3 cp "s3://$S3_BUCKET/$S3_BUILD_KEY" "$LOCAL_BUILD_FILE"
    
    # 2. 기존 배포 디렉토리 정리
    log "🧹 Cleaning up previous deployment..."
    rm -rf "$DEPLOY_DIR"
    mkdir -p "$DEPLOY_DIR"
    
    # 3. 빌드 파일 압축 해제
    log "📦 Extracting build files..."
    tar -xzf "$LOCAL_BUILD_FILE" -C "$DEPLOY_DIR"
    
    # 4. Nginx 배포
    log "🌐 Deploying to Nginx..."
    rsync -av --delete "$DEPLOY_DIR/" "$NGINX_ROOT/"
    chown -R nginx:nginx "$NGINX_ROOT"
    
    # 5. Nginx 설정 테스트 및 재로드
    log "🔧 Testing Nginx configuration..."
    if nginx -t; then
        log "✅ Nginx configuration is valid"
        systemctl reload nginx
        log "🔄 Nginx reloaded successfully"
    else
        log "❌ Nginx configuration test failed"
        return 1
    fi
    
    # 6. 정리
    rm -f "$LOCAL_BUILD_FILE"
    
    log "✅ Deployment completed successfully"
    return 0
}

# 메인 로직
main() {
    log "🔍 Checking for new deployment trigger..."
    
    # S3에서 트리거 파일 다운로드 시도
    if aws s3 cp "s3://$S3_BUCKET/$S3_TRIGGER_KEY" /tmp/current-trigger.json 2>/dev/null; then
        
        # 이전 트리거와 비교
        if [ -f "$LOCAL_TRIGGER_FILE" ]; then
            if cmp -s /tmp/current-trigger.json "$LOCAL_TRIGGER_FILE"; then
                log "📋 No new deployment trigger detected"
                rm -f /tmp/current-trigger.json
                return 0
            fi
        fi
        
        # 새로운 트리거 감지
        log "🆕 New deployment trigger detected!"
        cat /tmp/current-trigger.json | tee -a "$LOG_FILE"
        
        # 배포 실행
        if deploy_build; then
            # 성공 시 트리거 파일 업데이트
            mv /tmp/current-trigger.json "$LOCAL_TRIGGER_FILE"
            log "🎉 Auto-deployment completed successfully"
        else
            log "❌ Auto-deployment failed"
            rm -f /tmp/current-trigger.json
            return 1
        fi
        
    else
        log "📭 No deployment trigger found in S3"
    fi
}

# 스크립트 실행
main "$@"
AUTO_DEPLOY_EOF

chmod +x /home/ec2-user/auto-deploy.sh
chown ec2-user:ec2-user /home/ec2-user/auto-deploy.sh

# Cron 작업 설정
echo "⏰ Setting up auto-deployment cron job..."
touch /var/log/auto-deploy.log
touch /var/log/auto-deploy-cron.log
chown ec2-user:ec2-user /var/log/auto-deploy.log
chown ec2-user:ec2-user /var/log/auto-deploy-cron.log

# ec2-user의 crontab에 자동 배포 작업 추가 (매분 실행)
sudo -u ec2-user bash -c '
    # 기존 auto-deploy 관련 cron 제거
    crontab -l 2>/dev/null | grep -v "auto-deploy.sh" | crontab - 2>/dev/null || true
    
    # 새로운 cron 작업 추가
    (crontab -l 2>/dev/null; echo "* * * * * /home/ec2-user/auto-deploy.sh >> /var/log/auto-deploy-cron.log 2>&1") | crontab -
'

# cron 서비스 시작
systemctl enable crond
systemctl start crond

echo "✅ Auto-deployment cron job configured (runs every minute)"

# 시스템 정보 출력
echo "=================================================="
echo "✅ Empick Frontend Setup Completed!"
echo "Server Color: $(echo ${color} | tr '[:lower:]' '[:upper:]')"
echo "Nginx Status: $(systemctl is-active nginx)"
echo "SSM Agent Status: $(systemctl is-active amazon-ssm-agent)"
echo "Node.js Version: $(node --version)"
echo "Nginx Version: $(nginx -v 2>&1)"
echo "Deployment Script: /home/ec2-user/deploy.sh"
echo "Web Root: /var/www/html"
echo "Timestamp: $(date)"
echo "=================================================="

# 완료 신호
touch /tmp/user-data-completed
echo "🎉 User Data Script Completed Successfully!" 