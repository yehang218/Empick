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
NGINX_ROOT="/usr/share/nginx/html"
LOG_FILE="/var/log/auto-deploy.log"

# 로그 함수
log() {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] $1" | sudo tee -a "$LOG_FILE"
}

# 배포 함수
deploy_build() {
    log "🚀 Starting deployment..."
    
    # 1. S3에서 빌드 파일 다운로드
    log "📥 Downloading build from S3..."
    aws s3 cp "s3://${S3_BUCKET}/${S3_BUILD_KEY}" "$LOCAL_BUILD_FILE"
    
    # 2. 기존 배포 디렉토리 정리
    log "🧹 Cleaning up previous deployment..."
    sudo rm -rf "$DEPLOY_DIR"
    sudo mkdir -p "$DEPLOY_DIR"
    
    # 3. 빌드 파일 압축 해제
    log "📦 Extracting build files..."
    sudo tar -xzf "$LOCAL_BUILD_FILE" -C "$DEPLOY_DIR"
    sudo chown -R nginx:nginx "$DEPLOY_DIR"
    
    # 4. Nginx 배포
    log "🌐 Deploying to Nginx..."
    sudo rsync -av --delete "$DEPLOY_DIR/" "$NGINX_ROOT/"
    sudo chown -R nginx:nginx "$NGINX_ROOT"
    
    # 5. Nginx 설정 테스트 및 재로드
    log "🔧 Testing Nginx configuration..."
    if sudo nginx -t; then
        log "✅ Nginx configuration is valid"
        sudo systemctl reload nginx
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
    if aws s3 cp "s3://${S3_BUCKET}/${S3_TRIGGER_KEY}" /tmp/current-trigger.json 2>/dev/null; then
        
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
        cat /tmp/current-trigger.json | sudo tee -a "$LOG_FILE"
        
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