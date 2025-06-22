# Empick Frontend - 프로젝트 컨텍스트

## 📋 프로젝트 개요

**Empick Frontend**는 기업용 채용 관리 시스템의 프론트엔드 애플리케이션입니다.

### 기술 스택

- **프레임워크**: Vue 3 (Composition API)
- **UI 라이브러리**: Vuetify 3
- **상태 관리**: Pinia (with persistedstate)
- **라우팅**: Vue Router 4
- **HTTP 클라이언트**: Axios
- **빌드 도구**: Vite
- **스타일링**: SCSS
- **인증**: JWT (Access Token + Refresh Token)

---

## 🏗️ 아키텍처 개요

### 레이어드 아키텍처 (Layered Architecture)

```
Views (Vue Components)
    ↓ (Store만 접근 허용)
Stores (Pinia)
    ↓ (Service만 접근 허용)
Services (Business Logic)
    ↓ (apiClient만 접근 허용)
API Client (Axios Wrapper)
    ↓
Backend API
```

### 핵심 설계 원칙

1. **단방향 의존성**: 상위 레이어는 하위 레이어만 참조
2. **레이어 격리**: 각 레이어는 인접한 레이어와만 통신
3. **axios 직접 사용 금지**: 모든 HTTP 요청은 apiClient를 통해서만
4. **ESLint로 아키텍처 강제**: 잘못된 import 패턴을 컴파일 타임에 차단

---

## 📁 디렉토리 구조

```
src/
├── apis/                   # API 관련
│   ├── apiClient.js       # Axios 래퍼, 인터셉터 설정
│   └── routes/            # API 엔드포인트 정의
├── components/            # Vue 컴포넌트
│   ├── common/           # 공통 컴포넌트
│   ├── attendance/       # 근태 관련 컴포넌트
│   ├── employment/       # 채용 관련 컴포넌트
│   └── ...
├── composables/          # Vue Composition 함수
├── config/               # 설정 파일
├── constants/            # 상수 정의
├── dto/                  # 데이터 전송 객체
│   ├── auth/
│   ├── member/
│   ├── attendance/
│   └── ...
├── router/               # 라우팅 설정
├── services/             # 비즈니스 로직 레이어
├── stores/               # Pinia 상태 관리
├── utils/                # 유틸리티 함수
└── views/                # 페이지 컴포넌트
```

---

## 🔒 ESLint 아키텍처 규칙

### 1. Vue 컴포넌트 (.vue 파일)

```javascript
// ❌ 금지된 import
import axios from "axios"; // axios 직접 사용 금지
import { someService } from "@/services"; // Service 직접 접근 금지
import { API } from "@/apis/routes"; // API 직접 접근 금지

// ✅ 허용된 import
import { useAuthStore } from "@/stores/authStore"; // Store만 접근 허용
```

### 2. Store 파일 (*Store.js, *store.js)

```javascript
// ❌ 금지된 import
import axios from "axios"; // axios 직접 사용 금지
import { API } from "@/apis/routes"; // API 직접 접근 금지

// ✅ 허용된 import
import { authService } from "@/services/authService"; // Service만 접근 허용
```

### 3. Service 파일 (services/\*_/_.js)

```javascript
// ❌ 금지된 import
import axios from "axios"; // axios 직접 사용 금지

// ✅ 허용된 import
import api from "@/apis/apiClient"; // apiClient만 접근 허용
import { API } from "@/apis/routes"; // API 라우트 접근 허용
```

### 4. API Client (apiClient.js)

- **모든 제한 해제**: axios 직접 사용 가능
- **유일한 HTTP 클라이언트**: 모든 API 요청의 중앙 집중점

---

## 📊 각 레이어별 역할

### 1. Views Layer (views/, components/)

**역할**: 사용자 인터페이스 및 사용자 상호작용 처리

- Vue 컴포넌트로 구성
- **Store만 접근 허용**
- 사용자 입력 처리 및 화면 렌더링
- 비즈니스 로직 없음

**주요 디렉토리**:

- `views/`: 페이지 단위 컴포넌트
- `components/`: 재사용 가능한 컴포넌트

### 2. Stores Layer (stores/)

**역할**: 상태 관리 및 컴포넌트-서비스 간 중재

- Pinia로 구현된 상태 관리
- **Service만 접근 허용**
- 전역 상태 관리
- 컴포넌트에서 사용할 수 있는 인터페이스 제공

**주요 Store들**:

- `authStore.js`: 인증 상태 관리
- `memberStore.js`: 사원 정보 관리
- `attendanceStore.js`: 근태 관리
- `approvalStore.js`: 결재 관리

### 3. Services Layer (services/)

**역할**: 비즈니스 로직 처리 및 데이터 변환

- **apiClient만 접근 허용**
- API 응답 데이터 가공
- DTO 변환
- 에러 처리
- 파일 업로드 등 복잡한 로직

**주요 Service들**:

- `authService.js`: 로그인/로그아웃 로직
- `memberService.js`: 사원 관리 로직
- `attendanceService.js`: 근태 관리 로직
- `fileService.js`: 파일 업로드 로직

### 4. API Client Layer (apis/)

**역할**: HTTP 통신 및 API 엔드포인트 관리

- `apiClient.js`: Axios 래퍼, 인터셉터 설정
- `routes/`: API 엔드포인트 정의
- 토큰 관리 (Access/Refresh Token)
- 요청/응답 로깅
- 자동 토큰 갱신

### 5. DTO Layer (dto/)

**역할**: 데이터 구조 정의 및 유효성 검사

- API 요청/응답 데이터 구조화
- 데이터 유효성 검사
- 타입 안전성 보장

**구조**:

```
dto/
├── auth/           # 인증 관련 DTO
├── member/         # 사원 관리 DTO
├── attendance/     # 근태 관리 DTO
├── employment/     # 채용 관리 DTO
└── common/         # 공통 DTO
```

---

## 🔐 인증 시스템

### JWT 토큰 기반 인증

- **Access Token**: API 요청 인증
- **Refresh Token**: Access Token 갱신
- **자동 토큰 갱신**: apiClient에서 401 에러 시 자동 처리

### 인증 플로우

1. 로그인 → JWT 토큰 발급
2. 모든 API 요청에 Access Token 자동 첨부
3. 토큰 만료 시 Refresh Token으로 자동 갱신
4. 갱신 실패 시 자동 로그아웃

---

## 🛠️ 주요 기능 도메인

### 1. 인증 (Auth)

- 로그인/로그아웃
- 토큰 관리
- 권한 기반 라우팅

### 2. 사원 관리 (Member)

- 사원 등록 (개별/일괄)
- 프로필 이미지 업로드
- 사원 정보 관리

### 3. 근태 관리 (Attendance)

- 출퇴근 기록
- 근무시간 계산
- 타임라인 시각화
- 근태 현황 대시보드

### 4. 채용 관리 (Employment)

- 채용 공고 관리
- 지원자 관리
- 면접 관리
- 채용 테스트

### 5. 결재 시스템 (Approval)

- 결재 문서 작성
- 결재선 관리
- 결재 현황 추적

---

## 🎯 개발 가이드라인

### 1. 새로운 기능 추가 시

1. **DTO 정의**: 데이터 구조 먼저 설계
2. **API Route 추가**: 엔드포인트 정의
3. **Service 구현**: 비즈니스 로직 작성
4. **Store 구현**: 상태 관리 로직
5. **Component 구현**: UI 컴포넌트

### 2. 코딩 컨벤션

- **파일명**: camelCase (예: `memberStore.js`)
- **컴포넌트**: PascalCase (예: `MemberList.vue`)
- **함수명**: camelCase (예: `getMemberList`)
- **상수**: UPPER_SNAKE_CASE (예: `API_BASE_URL`)

### 3. 에러 처리

- Service Layer에서 에러 분류 및 처리
- 사용자 친화적인 에러 메시지 제공
- 네트워크 에러, 서버 에러, 비즈니스 에러 구분

### 4. 성능 최적화

- 컴포넌트 레벨 코드 스플리팅
- 이미지 lazy loading
- API 응답 캐싱 (Store 레벨)
- 불필요한 재렌더링 방지

---

## 🔧 환경 설정

### 개발 환경

```bash
npm run dev          # 개발 서버 실행
npm run build        # 프로덕션 빌드
npm run preview      # 빌드 결과 미리보기
```

### 환경 변수

- `VITE_API_BASE_URL`: API 서버 주소
- `VITE_TIMEOUT`: API 요청 타임아웃

---

## 📝 최근 주요 변경사항

### 타임라인 UI 개선 (WeekSummaryCard.vue)

- 모눈종이 배경 제거
- 인터랙티브 툴팁 제거
- 단순한 근무시간 바 표시로 변경
- 출근/퇴근 라벨 직접 표시

### 사원 등록 시스템

- 개별/일괄 등록 지원
- 프로필 이미지 업로드
- API 타임아웃 최적화 (30초/60초)
- 로딩 UI 개선

### 파일 업로드 최적화

- 멀티파트 요청 타임아웃 증가
- 업로드 진행률 표시
- 에러 메시지 분류

---

이 문서는 프로젝트의 전체적인 구조와 개발 가이드라인을 제공합니다. 새로운 개발자가 프로젝트에 참여하거나 기존 기능을 수정할 때 참고하시기 바랍니다.
