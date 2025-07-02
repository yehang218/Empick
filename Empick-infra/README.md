# 🏗️ Empick Infrastructure as Code

> **Multi-VPC 아키텍처 기반의 고가용성 웹 애플리케이션 인프라**

## 📋 프로젝트 개요

Empick은 **Multi-VPC 아키텍처**를 기반으로 한 현대적인 웹 애플리케이션 인프라입니다. Frontend와 Backend를 완전히 분리하여 보안성과 확장성을 극대화했습니다.

### 🎯 주요 특징

- ✨ **Multi-VPC 아키텍처** (Frontend/Backend 완전 분리)
- 🔄 **Blue-Green 배포 전략** (무중단 배포)
- 🛡️ **고급 보안 구성** (Private Subnet, Security Groups)
- 📊 **모니터링 & 로깅** (CloudWatch 통합)
- 🚀 **CI/CD 파이프라인** (GitHub Actions)
- 💰 **비용 최적화** (월 $80-120 예상)

## 🏛️ 아키텍처 구조

<img width="1404" alt="architecture" src="https://github.com/user-attachments/assets/a49e3aea-e8bf-4fbf-8b21-ab44c7a31912" />


## 🛠️ 기술 스택

### 인프라

- **IaC**: Terraform v1.0+
- **클라우드**: AWS (ap-northeast-2)
- **컨테이너**: Docker (선택적)

### 애플리케이션

- **Frontend**: Vue.js + Nginx
- **Backend**: Spring Boot + Java 17
- **Database**: RDS MariaDB 10.6+
- **Cache**: Redis 6.x

### DevOps

- **CI/CD**: GitHub Actions
- **모니터링**: CloudWatch
- **로깅**: CloudWatch Logs
- **배포**: SSH + Shell Scripts

## 📁 프로젝트 구조

```
infra/
├── vpc1-frontend/          # Frontend VPC 인프라
│   ├── main.tf            # 네트워크 구성
│   ├── alb.tf             # 로드 밸런서
│   ├── ec2-nginx.tf       # Nginx 인스턴스
│   ├── route53.tf         # DNS 설정
│   ├── security.tf        # 보안 그룹
│   ├── variables.tf       # 변수 정의
│   └── auto-deploy.sh     # 자동 배포 스크립트
│
├── vpc2-backend/          # Backend VPC 인프라
│   ├── main.tf            # 네트워크 구성
│   ├── alb.tf             # 로드 밸런서
│   ├── ec2-springboot.tf  # Spring Boot 인스턴스
│   ├── rds.tf             # 데이터베이스
│   ├── redis.tf           # 캐시
│   ├── monitoring.tf      # 모니터링
│   ├── security.tf        # 보안 그룹
│   ├── variables.tf       # 변수 정의
│   └── spring_user_data.sh # 애플리케이션 설정
│
├── vpc-peering/           # VPC 간 연결
│   ├── main.tf            # Peering 설정
│   ├── outputs.tf         # 출력 값
│   └── variables.tf       # 변수 정의
│
└── log/                   # 문서 및 로그
    └── guideline.md       # 인프라 가이드라인
```

## 🚀 빠른 시작

### 사전 요구사항

- AWS CLI v2.x
- Terraform v1.0+
- SSH 키 페어
- AWS 계정 및 적절한 권한

### 환경 변수 설정

```bash
# AWS 자격 증명
export AWS_ACCESS_KEY_ID="your-access-key"
export AWS_SECRET_ACCESS_KEY="your-secret-key"
export AWS_DEFAULT_REGION="ap-northeast-2"

# 프로젝트 변수
export PROJECT_NAME="empick"
export ENVIRONMENT="production"
```

### 배포 순서

1. **Backend VPC 배포**

   ```bash
   cd vpc2-backend
   terraform init
   terraform plan
   terraform apply
   ```

2. **Frontend VPC 배포**

   ```bash
   cd ../vpc1-frontend
   terraform init
   terraform plan
   terraform apply
   ```

3. **VPC Peering 연결**
   ```bash
   cd ../vpc-peering
   terraform init
   terraform plan
   terraform apply
   ```

## 🔐 보안 구성

### 네트워크 보안

- **VPC 분리**: Frontend와 Backend 완전 격리
- **Private Subnet**: 데이터베이스와 애플리케이션 서버 보호
- **Security Groups**: 최소 권한 원칙 적용
- **NAT Gateway**: Private Subnet의 안전한 인터넷 접근

### 접근 제어

- **Bastion Host**: SSH 접근 제어
- **IAM Roles**: EC2 인스턴스 권한 관리
- **Secrets Manager**: 민감한 정보 관리

### SSL/TLS

- **ACM**: SSL 인증서 자동 관리
- **HTTPS**: 모든 외부 통신 암호화

## 📊 모니터링 & 로깅

### CloudWatch 통합

- **메트릭 수집**: CPU, 메모리, 디스크, 네트워크
- **로그 집계**: 애플리케이션 및 시스템 로그
- **알람 설정**: 임계값 기반 알림
- **대시보드**: 실시간 모니터링

### 로그 구성

- **Nginx Access Logs**: 웹 트래픽 분석
- **Spring Boot Logs**: 애플리케이션 로그
- **RDS Logs**: 데이터베이스 성능 모니터링

## 🔄 CI/CD 파이프라인

### GitHub Actions 워크플로우

```yaml
# .github/workflows/deploy.yml
name: Deploy to AWS
on:
  push:
    branches: [main]

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Deploy Backend
        run: |
          cd infra/vpc2-backend
          terraform apply -auto-approve
      - name: Deploy Frontend
        run: |
          cd infra/vpc1-frontend
          terraform apply -auto-approve
```

### 배포 전략

- **Blue-Green 배포**: 무중단 배포
- **자동 롤백**: 실패 시 이전 버전으로 복구
- **헬스 체크**: 배포 후 서비스 상태 확인

## 💰 비용 최적화

### 월 예상 비용

- **EC2 인스턴스**: $30-50
- **RDS MariaDB**: $20-30
- **ALB + NAT Gateway**: $20-30
- **기타 서비스**: $10-20
- **총 예상**: $80-130/월

### 비용 절약 전략

- **t3.micro/small**: 개발/테스트 환경
- **Reserved Instances**: 프로덕션 환경
- **Auto Scaling**: 트래픽 기반 자동 조정
- **S3 Lifecycle**: 로그 파일 자동 정리

## 🔧 환경별 설정

### 개발 환경

```hcl
# variables.tf
variable "environment" {
  default = "development"
}

variable "instance_type" {
  default = "t3.micro"
}

variable "rds_instance_class" {
  default = "db.t3.micro"
}
```

### 프로덕션 환경

```hcl
# terraform.tfvars
environment = "production"
instance_type = "t3.small"
rds_instance_class = "db.t3.small"
```

## 🚨 문제 해결

### 일반적인 문제들

1. **VPC Peering 연결 실패**

   - Route Table 설정 확인
   - Security Group 규칙 점검

2. **RDS 연결 실패**

   - Private Subnet 설정 확인
   - Security Group 규칙 점검

3. **ALB Health Check 실패**
   - Target Group 설정 확인
   - 애플리케이션 포트 확인

## 📚 참고 자료

- [Terraform AWS Provider](https://registry.terraform.io/providers/hashicorp/aws/latest/docs)
- [AWS VPC Peering](https://docs.aws.amazon.com/vpc/latest/peering/)
- [Vue.js Deployment](https://vuejs.org/guide/best-practices/production-deployment.html)

## 📄 라이선스

이 프로젝트는 Apache 라이선스 하에 배포됩니다. 자세한 내용은 [LICENSE](LICENSE) 파일을 참조하세요.

---

**⭐ 이 프로젝트가 도움이 되었다면 스타를 눌러주세요!**
