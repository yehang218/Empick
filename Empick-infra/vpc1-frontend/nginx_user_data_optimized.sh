#!/bin/bash
set -euo pipefail

LOG_FILE="/var/log/user-data-optimized.log"
exec > >(tee -a $LOG_FILE) 2>&1

echo "🚀 [$(date)] Optimized Empick Frontend Setup Started"
echo "Environment: ${environment} | Project: ${project} | Color: ${color}"

# 중복 실행 방지
USER_DATA_MARKER="/tmp/empick-userdata-completed"
if [[ -f "$USER_DATA_MARKER" ]]; then
    echo "⚠️ User Data already executed. Skipping..."
    exit 0
fi

# 시스템 업데이트 및 패키지 설치
echo "📦 Installing packages..."
dnf update -y
dnf install -y --allowerasing nginx git curl wget tar rsync cronie amazon-ssm-agent

# CloudWatch Agent 설치
echo "📊 Installing CloudWatch Agent..."
wget https://s3.amazonaws.com/amazoncloudwatch-agent/amazon_linux/amd64/latest/amazon-cloudwatch-agent.rpm
rpm -U ./amazon-cloudwatch-agent.rpm

# Node.js 설치
echo "📦 Installing Node.js..."
curl -fsSL https://rpm.nodesource.com/setup_18.x | bash -
dnf install -y nodejs

# 서비스 활성화 및 시작
echo "🔧 Starting services..."
systemctl enable nginx crond amazon-ssm-agent amazon-cloudwatch-agent
systemctl start crond amazon-ssm-agent

# Nginx 설정
echo "🔧 Configuring Nginx..."
cp /etc/nginx/nginx.conf /etc/nginx/nginx.conf.backup

cat > /etc/nginx/nginx.conf << 'EOF'
user nginx;
worker_processes auto;
error_log /var/log/nginx/error.log;
pid /run/nginx.pid;
include /usr/share/nginx/modules/*.conf;
events { worker_connections 1024; }
http {
    log_format main '$remote_addr - $remote_user [$time_local] "$request" $status $body_bytes_sent "$http_referer" "$http_user_agent" "$http_x_forwarded_for"';
    access_log /var/log/nginx/access.log main;
    sendfile on; tcp_nopush on; tcp_nodelay on; keepalive_timeout 65; types_hash_max_size 4096; client_max_body_size 20M;
    include /etc/nginx/mime.types; default_type application/octet-stream;
    gzip on; gzip_vary on; gzip_min_length 1024; gzip_proxied any; gzip_comp_level 6;
    gzip_types text/plain text/css text/xml text/javascript application/json application/javascript application/xml+rss application/atom+xml image/svg+xml;
    server {
        listen 80 default_server; listen [::]:80 default_server; server_name _; root /var/www/html; index index.html;
        location /health { access_log off; return 200 "healthy\n"; add_header Content-Type text/plain; }
        location /api/ {
            proxy_pass http://backend.empick.internal; proxy_set_header Host $host; proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for; proxy_set_header X-Forwarded-Proto $scheme;
            proxy_connect_timeout 30s; proxy_send_timeout 30s; proxy_read_timeout 30s;
            proxy_set_header X-Forwarded-Host $server_name; proxy_set_header X-Real-Port $server_port;
        }
        location / { try_files $uri $uri/ /index.html; location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff2|woff|ttf|eot)$ { expires 1y; add_header Cache-Control "public, immutable"; } }
        add_header X-Frame-Options "SAMEORIGIN" always; add_header X-XSS-Protection "1; mode=block" always;
        add_header X-Content-Type-Options "nosniff" always; add_header Referrer-Policy "no-referrer-when-downgrade" always;
        add_header Content-Security-Policy "default-src 'self' http: https: data: blob: 'unsafe-inline'" always;
    }
}
EOF

# 웹 디렉토리 및 임시 페이지
mkdir -p /var/www/html
chown -R nginx:nginx /var/www/html
chmod -R 755 /var/www/html

cat > /var/www/html/index.html << EOF
<!DOCTYPE html><html lang="ko"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Empick Frontend - $(echo ${color} | tr '[:lower:]' '[:upper:]') Server</title>
<style>body{font-family:'Segoe UI',Tahoma,Geneva,Verdana,sans-serif;margin:0;padding:0;background:linear-gradient(135deg,#667eea 0%,#764ba2 100%);color:white;display:flex;justify-content:center;align-items:center;min-height:100vh}.container{text-align:center;padding:2rem;background:rgba(255,255,255,0.1);border-radius:15px;backdrop-filter:blur(10px);box-shadow:0 8px 32px rgba(31,38,135,0.37)}.logo{font-size:3rem;margin-bottom:1rem}.server-info{background:rgba(255,255,255,0.2);padding:1rem;border-radius:10px;margin:1rem 0}.status{color:#4ade80;font-weight:bold}.optimized{color:#fbbf24;font-size:0.9rem;margin-top:1rem}</style>
</head><body><div class="container"><div class="logo">🚀</div><h1>Empick Frontend</h1>
<div class="server-info"><h2>Server: <span style="color:${color};">$(echo ${color} | tr '[:lower:]' '[:upper:]')</span></h2>
<p>Environment: ${environment}</p><p>Status: <span class="status">Optimized Setup Ready</span></p><p>Timestamp: $(date)</p></div>
<p>Vue.js 애플리케이션이 곧 배포됩니다.</p><div class="optimized">🔧 Optimized User Data Applied</div></div></body></html>
EOF

# Nginx 시작
nginx -t && systemctl start nginx

# 자동 배포 시스템 설정
echo "🚀 Setting up auto-deployment..."

# ec2-user 대기
for i in {1..30}; do
    if id ec2-user &>/dev/null && [[ -d /home/ec2-user ]]; then break; fi
    sleep 2
done

# 자동 배포 스크립트
cat > /home/ec2-user/auto-deploy.sh << 'DEPLOY_EOF'
#!/bin/bash
set -euo pipefail
LOG_FILE="/var/log/auto-deploy.log"
exec > >(tee -a $LOG_FILE) 2>&1
echo "[$(date)] Auto-deploy check started"
S3_BUCKET="empick-private-bucket"
TRIGGER_KEY="frontend-builds/deploy-trigger.json"
BUILD_KEY="frontend-builds/latest.tar.gz"
LAST_TRIGGER="/tmp/last-trigger.json"
if aws s3 cp "s3://$S3_BUCKET/$TRIGGER_KEY" /tmp/current-trigger.json 2>/dev/null; then
    if [ ! -f "$LAST_TRIGGER" ] || ! cmp -s /tmp/current-trigger.json "$LAST_TRIGGER"; then
        echo "[$(date)] New deployment trigger detected!"
        if aws s3 cp "s3://$S3_BUCKET/$BUILD_KEY" /tmp/latest.tar.gz; then
            echo "[$(date)] Deploying new build..."
            sudo cp -r /var/www/html /var/www/html.backup.$(date +%H%M%S) 2>/dev/null || true
            sudo rm -rf /var/www/html/*
            sudo tar -xzf /tmp/latest.tar.gz -C /var/www/html/
            sudo chown -R nginx:nginx /var/www/html/
            sudo systemctl reload nginx
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
DEPLOY_EOF

chmod +x /home/ec2-user/auto-deploy.sh
chown ec2-user:ec2-user /home/ec2-user/auto-deploy.sh
touch /var/log/auto-deploy.log
chown ec2-user:ec2-user /var/log/auto-deploy.log

# 지연된 cron 설정
cat > /tmp/setup_cron.sh << 'CRON_EOF'
#!/bin/bash
sleep 30
echo "* * * * * /home/ec2-user/auto-deploy.sh" | crontab -u ec2-user -
echo "Cron setup completed at $(date)" >> /var/log/user-data-optimized.log
CRON_EOF
chmod +x /tmp/setup_cron.sh
nohup /tmp/setup_cron.sh > /dev/null 2>&1 &

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

# 시스템 설정
systemctl stop firewalld 2>/dev/null || true
systemctl disable firewalld 2>/dev/null || true
systemctl enable sshd && systemctl restart sshd

# CloudWatch Agent 설정
echo "📊 Configuring CloudWatch Agent..."
cat > /opt/aws/amazon-cloudwatch-agent/etc/amazon-cloudwatch-agent.json << 'CW_EOF'
{
  "metrics": {
    "namespace": "CWAgent",
    "metrics_collected": {
      "cpu": {
        "measurement": [
          "cpu_usage_idle",
          "cpu_usage_iowait",
          "cpu_usage_user",
          "cpu_usage_system"
        ],
        "metrics_collection_interval": 60,
        "totalcpu": false
      },
      "disk": {
        "measurement": [
          "used_percent"
        ],
        "metrics_collection_interval": 60,
        "resources": [
          "*"
        ]
      },
      "diskio": {
        "measurement": [
          "io_time"
        ],
        "metrics_collection_interval": 60,
        "resources": [
          "*"
        ]
      },
      "mem": {
        "measurement": [
          "mem_used_percent"
        ],
        "metrics_collection_interval": 60
      }
    }
  },
  "logs": {
    "logs_collected": {
      "files": {
        "collect_list": [
          {
            "file_path": "/var/log/nginx/access.log",
            "log_group_name": "/aws/vpc1-frontend/nginx/access",
            "log_stream_name": "{instance_id}",
            "timezone": "UTC"
          },
          {
            "file_path": "/var/log/nginx/error.log",
            "log_group_name": "/aws/vpc1-frontend/nginx/error",
            "log_stream_name": "{instance_id}",
            "timezone": "UTC"
          },
          {
            "file_path": "/var/log/auto-deploy.log",
            "log_group_name": "/aws/vpc1-frontend/auto-deploy",
            "log_stream_name": "{instance_id}",
            "timezone": "UTC"
          }
        ]
      }
    }
  }
}
CW_EOF

# CloudWatch Agent 시작
echo "📊 Starting CloudWatch Agent..."
/opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl \
  -a fetch-config \
  -m ec2 \
  -c file:/opt/aws/amazon-cloudwatch-agent/etc/amazon-cloudwatch-agent.json \
  -s

# 완료 마커
touch "$USER_DATA_MARKER"
echo "timestamp=$(date)" >> "$USER_DATA_MARKER"
echo "status=completed" >> "$USER_DATA_MARKER"
echo "version=optimized" >> "$USER_DATA_MARKER"
echo "cloudwatch=enabled" >> "$USER_DATA_MARKER"

echo "🎉 [$(date)] Optimized Empick Frontend Setup Completed!"
echo "Server: $(echo ${color} | tr '[:lower:]' '[:upper:]') | Auto-deployment: Enabled | CloudWatch: Enabled" 