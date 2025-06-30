# 🎯 Empick 인프라 구축 가이드라인

## 📋 프로젝트 요구사항

### 기본 원칙

- **아키텍처 구조는 최대한 유지**
- **terraform으로 구현 어려운 것은 과감히 제외**
- **요청받은 것만 정확히 구현** (추가 코드/파일 작성 금지)
- **이전 실패 경험 반영하여 단순화**
- **🚨 수동 수정 금지 - 모든 변경은 Terraform으로만 수행**

## 🏗️ 구현할 아키텍처 요소

### ✅ terraform으로 구현

- **Multi-VPC 구조** (VPC 1: 172.16.0.0/16, VPC 2: 10.0.0.0/16)
- **VPC Peering** (VPC 간 연결)
- **Multi-AZ 구성** (AZ-a, AZ-c)
- **서브넷** (Public/Private 각 2개씩)
- **ALB + Target Groups** (양쪽 VPC)
- **EC2 인스턴스** (Nginx, Spring Boot, Bastion)
- **RDS MariaDB** (Private Subnet)
- **Security Groups, Route Tables**
- **NAT Gateway, Internet Gateway**

### ❌ 제외 요소 (구현 어려움)

- **Elastic Beanstalk** → EC2로 대체
- **Blue/Green 배포** → 단순 배포
- **자동 애플리케이션 배포** → 수동/GitHub Actions
- **복잡한 배포 전략**

## 🔧 기술 스택 결정

### 인프라

- **IaC**: Terraform
- **클라우드**: AWS (ap-northeast-2)

### CI/CD

- **배포 도구**: GitHub Actions (AWS CodePipeline 제외)
- **배포 방식**: SSH + 스크립트

### 애플리케이션

- **프론트엔드**: Vue.js + Nginx (VPC 1)
- **백엔드**: Spring Boot (VPC 2)
- **데이터베이스**: RDS MariaDB (VPC 2 Private)

## 🚫 이전 실패 경험 반영

### 피해야 할 것들

- **한글 description** → 모든 설명 영문 작성
- **RDS 이름에 하이픈** → 영숫자만 사용
- **Cross-VPC Security Group 직접 참조** → CIDR 블록 사용
- **복잡한 WAF 설정** → 기본 설정만
- **SSL 인증서 자동 검증** → 수동 검증 고려
- **🚨 수동 인프라 수정** → EC2 콘솔, 보안 그룹 수동 변경 금지

### 인프라 관리 원칙

- **모든 변경은 Terraform 코드로만 수행**
- **AWS 콘솔에서 직접 수정 절대 금지**
- **보안 그룹, 인스턴스, 네트워크 설정 모두 코드로 관리**
- **변경 후 반드시 `terraform apply`로 상태 동기화**
- **수동 변경으로 인한 설정 불일치 방지**

### 검증된 설정값

- **인스턴스 타입**: t3.micro/small (비용 최적화)
- **RDS 스토리지**: 최소 20GB (gp3 제약)
- **SSH 키**: 기존 키 활용

## 📋 작업 순서

### Phase 1: 기본 인프라 (terraform)

1. VPC 2 Backend (Spring Boot + RDS + Bastion)
2. VPC 1 Frontend (Nginx + Vue.js)
3. VPC Peering (네트워크 연결)

### Phase 2: 애플리케이션 배포 (수동/GitHub Actions)

1. EC2 기본 환경 설정
2. 애플리케이션 코드 배포
3. GitHub Actions 워크플로우 구성

## 💰 예상 비용

- **월 예상 비용**: $80-120
- **주요 비용 요소**: EC2, RDS, ALB, NAT Gateway

## 🎯 성공 기준

- **인프라 배포 성공률**: 95% 이상
- **아키텍처 구조 일치**: 원본 다이어그램과 95% 일치
- **비용 효율성**: 월 $150 이하 유지
