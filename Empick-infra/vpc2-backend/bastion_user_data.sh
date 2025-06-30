#!/bin/bash
# ================================================================================
# Bastion Host 초기 설정 스크립트
# ================================================================================

# 로그 설정
exec > >(tee /var/log/user-data.log|logger -t user-data -s 2>/dev/console) 2>&1
echo "=== Bastion Host Setup Started at $(date) ==="

# 시스템 업데이트
echo "1. Updating system packages..."
dnf update -y

# 관리 도구 설치
echo "2. Installing management tools..."
dnf install -y htop vim curl wget net-tools tree unzip git
dnf install -y sysstat iotop nethogs nc telnet nmap

# MariaDB 클라이언트 설치 (DB 관리용)
echo "3. Installing MariaDB client..."
dnf install -y mariadb105

# Redis 클라이언트 설치 (Redis 관리용)
echo "4. Installing Redis client..."
dnf install -y redis6

# AWS CLI 설치 (S3 접근용)
echo "5. Installing AWS CLI..."
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
./aws/install
rm -rf aws awscliv2.zip

# SSH 키 파일 설정 (Spring Boot 서버 접속용)
echo "6. Setting up SSH key for Spring Boot access..."
mkdir -p /home/ec2-user/.ssh
cat > /home/ec2-user/.ssh/camp.pem << 'SSHKEY'
${PRIVATE_KEY_CONTENT}
SSHKEY
chmod 600 /home/ec2-user/.ssh/camp.pem
chown ec2-user:ec2-user /home/ec2-user/.ssh/camp.pem

# SSH 설정 최적화
echo "7. Optimizing SSH configuration..."
sed -i 's/#ClientAliveInterval 0/ClientAliveInterval 300/' /etc/ssh/sshd_config
sed -i 's/#ClientAliveCountMax 3/ClientAliveCountMax 2/' /etc/ssh/sshd_config
systemctl restart sshd

# 유용한 별칭 설정
echo "8. Setting up useful aliases..."
cat >> /home/ec2-user/.bashrc << 'ALIASES'

# ================================================================================
# Bastion Host 관리용 별칭 (Aliases)
# ================================================================================

# 기본 명령어 단축키
alias ll='ls -alF'
alias la='ls -A'
alias l='ls -CF'
alias h='history'
alias c='clear'

# SSH 연결 단축키 (Spring Boot 서버 - Blue/Green)
alias ssh-blue='ssh -i ~/.ssh/camp.pem ec2-user@${SPRINGBOOT_BLUE_IP}'
alias ssh-green='ssh -i ~/.ssh/camp.pem ec2-user@${SPRINGBOOT_GREEN_IP}'
alias ssh-springboot='ssh -i ~/.ssh/camp.pem ec2-user@${SPRINGBOOT_BLUE_IP}'  # 기본값: Blue
alias ssh-sb='ssh -i ~/.ssh/camp.pem ec2-user@${SPRINGBOOT_BLUE_IP}'         # 기본값: Blue

# 데이터베이스 연결 단축키
alias db-connect='mysql -h ${DB_HOST} -P ${DB_PORT} -u ${DB_USERNAME} -p'
alias db-status='mysqladmin -h ${DB_HOST} -P ${DB_PORT} -u ${DB_USERNAME} -p status'
alias db-test='mysql -h ${DB_HOST} -P ${DB_PORT} -u ${DB_USERNAME} -p -e "SELECT 1 as test_connection"'

# Redis 연결 단축키
alias redis-connect='redis-cli -h ${REDIS_HOST} -p ${REDIS_PORT}'
alias redis-ping='redis-cli -h ${REDIS_HOST} -p ${REDIS_PORT} ping'
alias redis-info='redis-cli -h ${REDIS_HOST} -p ${REDIS_PORT} info memory'

# 시스템 모니터링 단축키
alias ports='netstat -tuln'
alias processes='ps aux'
alias disk='df -h'
alias memory='free -h'
alias cpu='top -n 1 -b | head -20'

# Spring Boot 애플리케이션 관리 (원격 실행 - Blue/Green)
# Blue 인스턴스 관리
alias blue-status='ssh -i ~/.ssh/camp.pem ec2-user@${SPRINGBOOT_BLUE_IP} "sudo systemctl status empick-backend"'
alias blue-logs='ssh -i ~/.ssh/camp.pem ec2-user@${SPRINGBOOT_BLUE_IP} "sudo journalctl -u empick-backend -f"'
alias blue-restart='ssh -i ~/.ssh/camp.pem ec2-user@${SPRINGBOOT_BLUE_IP} "sudo systemctl restart empick-backend"'
alias blue-stop='ssh -i ~/.ssh/camp.pem ec2-user@${SPRINGBOOT_BLUE_IP} "sudo systemctl stop empick-backend"'
alias blue-start='ssh -i ~/.ssh/camp.pem ec2-user@${SPRINGBOOT_BLUE_IP} "sudo systemctl start empick-backend"'

# Green 인스턴스 관리
alias green-status='ssh -i ~/.ssh/camp.pem ec2-user@${SPRINGBOOT_GREEN_IP} "sudo systemctl status empick-backend"'
alias green-logs='ssh -i ~/.ssh/camp.pem ec2-user@${SPRINGBOOT_GREEN_IP} "sudo journalctl -u empick-backend -f"'
alias green-restart='ssh -i ~/.ssh/camp.pem ec2-user@${SPRINGBOOT_GREEN_IP} "sudo systemctl restart empick-backend"'
alias green-stop='ssh -i ~/.ssh/camp.pem ec2-user@${SPRINGBOOT_GREEN_IP} "sudo systemctl stop empick-backend"'
alias green-start='ssh -i ~/.ssh/camp.pem ec2-user@${SPRINGBOOT_GREEN_IP} "sudo systemctl start empick-backend"'

# 기본 명령어 (Blue 인스턴스 대상)
alias sb-status='ssh -i ~/.ssh/camp.pem ec2-user@${SPRINGBOOT_BLUE_IP} "sudo systemctl status empick-backend"'
alias sb-logs='ssh -i ~/.ssh/camp.pem ec2-user@${SPRINGBOOT_BLUE_IP} "sudo journalctl -u empick-backend -f"'
alias sb-restart='ssh -i ~/.ssh/camp.pem ec2-user@${SPRINGBOOT_BLUE_IP} "sudo systemctl restart empick-backend"'
alias sb-stop='ssh -i ~/.ssh/camp.pem ec2-user@${SPRINGBOOT_BLUE_IP} "sudo systemctl stop empick-backend"'
alias sb-start='ssh -i ~/.ssh/camp.pem ec2-user@${SPRINGBOOT_BLUE_IP} "sudo systemctl start empick-backend"'

# S3 관리 단축키
alias s3-list='aws s3 ls'
alias s3-empick='aws s3 ls s3://empick-'
alias s3-frontend='aws s3 ls s3://empick-frontend-artifacts/ --recursive'
alias s3-backend='aws s3 ls s3://empick-backend-artifacts/ --recursive'
alias s3-sync-down='aws s3 sync'
alias s3-sync-up='aws s3 sync'
alias s3-cp='aws s3 cp'

# 네트워크 연결 테스트
alias test-db='nc -zv ${DB_HOST} ${DB_PORT}'
alias test-redis='nc -zv ${REDIS_HOST} ${REDIS_PORT}'
alias test-blue='nc -zv ${SPRINGBOOT_BLUE_IP} 8080'
alias test-green='nc -zv ${SPRINGBOOT_GREEN_IP} 8080'
alias test-springboot='nc -zv ${SPRINGBOOT_BLUE_IP} 8080'  # 기본값: Blue

# 유용한 정보 표시
alias show-info='echo "=== Infrastructure Information ===" && echo "Spring Boot Blue (AZ-A): ${SPRINGBOOT_BLUE_IP}:8080" && echo "Spring Boot Green (AZ-C): ${SPRINGBOOT_GREEN_IP}:8080" && echo "Database: ${DB_HOST}:${DB_PORT}" && echo "Redis: ${REDIS_HOST}:${REDIS_PORT}"'

ALIASES

# 환경 변수 설정 (재로그인 후에도 유지)
echo "9. Setting up environment variables..."
cat >> /home/ec2-user/.bashrc << 'ENVVARS'

# ================================================================================
# 인프라 정보 환경 변수
# ================================================================================
export SPRINGBOOT_BLUE_IP="${SPRINGBOOT_BLUE_IP}"
export SPRINGBOOT_GREEN_IP="${SPRINGBOOT_GREEN_IP}"
export SPRINGBOOT_IP="${SPRINGBOOT_BLUE_IP}"  # 기본값: Blue
export DB_HOST="${DB_HOST}"
export DB_PORT="${DB_PORT}"
export DB_USER="${DB_USERNAME}"
export REDIS_HOST="${REDIS_HOST}"
export REDIS_PORT="${REDIS_PORT}"

ENVVARS

# 소유권 설정
chown ec2-user:ec2-user /home/ec2-user/.bashrc

# 설정 완료 메시지
echo "10. Creating welcome message..."
cat > /home/ec2-user/welcome.txt << 'WELCOME'

🎯 Empick Bastion Host에 오신 것을 환영합니다!

📋 주요 명령어:
  ssh-blue        : Blue 서버 접속 (AZ-A)
  ssh-green       : Green 서버 접속 (AZ-C)
  ssh-springboot  : Spring Boot 서버 접속 (기본: Blue)
  db-connect      : 데이터베이스 접속
  redis-connect   : Redis 접속
  show-info       : 인프라 정보 표시

🔧 애플리케이션 관리 (Blue/Green):
  blue-status     : Blue 서버 상태 확인
  green-status    : Green 서버 상태 확인
  blue-logs       : Blue 서버 실시간 로그
  green-logs      : Green 서버 실시간 로그
  blue-restart    : Blue 서버 재시작
  green-restart   : Green 서버 재시작

🌐 연결 테스트:
  test-db         : DB 연결 테스트
  test-redis      : Redis 연결 테스트
  test-blue       : Blue 서버 연결 테스트
  test-green      : Green 서버 연결 테스트

📊 모니터링:
  htop            : 시스템 리소스 확인
  ports           : 열린 포트 확인
  disk            : 디스크 사용량
  memory          : 메모리 사용량

☁️ S3 관리:
  s3-list         : 모든 S3 버킷 목록
  s3-empick       : Empick 관련 버킷 목록
  s3-frontend     : Frontend 아티팩트 목록
  s3-backend      : Backend 아티팩트 목록
  s3-cp           : S3 파일 복사 (aws s3 cp)
  s3-sync-up      : 로컬 → S3 동기화
  s3-sync-down    : S3 → 로컬 동기화

WELCOME

# 로그인 시 환영 메시지 표시 설정
echo 'cat ~/welcome.txt' >> /home/ec2-user/.bashrc

echo "=== Bastion Host Setup Completed at $(date) ==="
echo "🎉 모든 설정이 완료되었습니다!"
echo "📝 사용 가능한 명령어는 ~/welcome.txt 파일을 확인하세요." 