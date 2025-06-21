# Empick Frontend - 개발 가이드 & 아키텍처 문서

## 📋 프로젝트 개요

**Empick Frontend**는 기업용 채용 관리 시스템의 프론트엔드 애플리케이션입니다.

### 🛠️ 기술 스택

- **프레임워크**: Vue 3 (Composition API)
- **UI 라이브러리**: Vuetify 3
- **상태 관리**: Pinia (with persistedstate)
- **라우팅**: Vue Router 4
- **HTTP 클라이언트**: Axios
- **빌드 도구**: Vite
- **스타일링**: SCSS
- **인증**: JWT (Access Token + Refresh Token)

---

## 🏗️ 아키텍처 원칙

### 레이어드 아키텍처 (5-Layer Architecture)

```
┌─────────────────────────────────────────────┐
│ Views Layer (Vue Components)               │ ← UI 렌더링 & 사용자 상호작용
├─────────────────────────────────────────────┤
│ Composables Layer (Vue Composition API)    │ ← 재사용 가능한 로직 캡슐화
├─────────────────────────────────────────────┤
│ Stores Layer (Pinia)                       │ ← 상태 관리 & 컴포넌트-서비스 중재
├─────────────────────────────────────────────┤
│ Services Layer (Business Logic)            │ ← 비즈니스 로직 & 데이터 변환
├─────────────────────────────────────────────┤
│ API Client Layer (Axios Wrapper)           │ ← HTTP 통신 & API 엔드포인트 관리
└─────────────────────────────────────────────┘
                        ↓
                 Backend API
```

### 🔒 핵심 아키텍처 규칙

| 레이어          | 접근 허용 대상             | 금지 사항                   |
| --------------- | -------------------------- | --------------------------- |
| **Views**       | `Composables`, `Stores`    | `Services`, `API` 직접 접근 |
| **Composables** | `Stores`                   | `Services`, `API` 직접 접근 |
| **Stores**      | `Services`                 | `API` 직접 접근             |
| **Services**    | `API Client`, `API Routes` | `axios` 직접 사용           |
| **API Client**  | 모든 접근 허용             | -                           |

### 🚫 ESLint 강제 규칙

```javascript
// ❌ 금지된 패턴들
// Vue 컴포넌트에서
import axios from "axios"; // axios 직접 사용 금지
import { memberService } from "@/services"; // Service 직접 접근 금지

// Store에서
import { API } from "@/apis/routes"; // API 직접 접근 금지 (Service를 통해서만)

// Service에서
import axios from "axios"; // axios 직접 사용 금지 (apiClient 사용)
```

### 📊 DTO/Model 사용 규칙

**DTO(Data Transfer Object)와 Model은 Service 계층에서만 사용해야 합니다.**

| 레이어          | DTO/Model 사용 | 사용 방식                         |
| --------------- | -------------- | --------------------------------- |
| **Views**       | ❌ 금지        | 일반 객체 사용                    |
| **Composables** | ❌ 금지        | 일반 객체 사용                    |
| **Stores**      | ❌ 금지        | Service에서 변환된 일반 객체 사용 |
| **Services**    | ✅ 허용        | DTO 클래스 직접 사용 및 변환      |
| **API Client**  | ❌ 금지        | 원시 데이터만 처리                |

#### ✅ 올바른 DTO 사용 패턴

```javascript
// ✅ Service에서 DTO 사용 (허용)
// services/memberService.js
import { MemberResponseDTO } from "@/dto/member/memberResponseDTO";
import { MemberCreateRequestDTO } from "@/dto/member/memberCreateRequestDTO";

export const memberService = {
  async getMembers() {
    const response = await api.get(API.MEMBER.LIST);
    // DTO 클래스로 변환하여 반환
    return response.data.map((item) => new MemberResponseDTO(item));
  },

  async createMember(memberData) {
    // 요청 데이터를 DTO로 변환
    const requestDTO = new MemberCreateRequestDTO(memberData);
    const response = await api.post(API.MEMBER.CREATE, requestDTO);
    return new MemberResponseDTO(response.data);
  },
};
```

#### ❌ 잘못된 DTO 사용 패턴

```javascript
// ❌ Store에서 DTO 직접 사용 (금지)
// stores/memberStore.js
import { MemberResponseDTO } from "@/dto/member/memberResponseDTO"; // 금지

export const useMemberStore = defineStore("member", () => {
  const members = ref([]);

  const fetchMembers = async () => {
    // ❌ Store에서 DTO 직접 사용 금지
    const response = await api.get(API.MEMBER.LIST);
    members.value = response.data.map((item) => new MemberResponseDTO(item));
  };
});

// ❌ Vue 컴포넌트에서 DTO 직접 사용 (금지)
// components/MemberList.vue
<script setup>
import { MemberResponseDTO } from "@/dto/member/memberResponseDTO"; // 금지

const formatMember = (memberData) => {
  // ❌ 컴포넌트에서 DTO 직접 사용 금지
  return new MemberResponseDTO(memberData);
};
</script>
```

#### 🔄 올바른 데이터 흐름

```javascript
// 1. Service 계층에서 DTO 변환
// services/memberService.js
export const memberService = {
  async getMembers() {
    const response = await api.get(API.MEMBER.LIST);
    // DTO → 일반 객체로 변환하여 반환
    return response.data.map((item) => {
      const dto = new MemberResponseDTO(item);
      return dto.toPlainObject(); // 일반 객체로 변환
    });
  },
};

// 2. Store에서 일반 객체 사용
// stores/memberStore.js
export const useMemberStore = defineStore("member", () => {
  const fetchMembers = async () => {
    // Service에서 이미 변환된 일반 객체 사용
    const members = await memberService.getMembers();
    members.value = members;
  };
});

// 3. 컴포넌트에서 일반 객체 사용
// components/MemberList.vue
<script setup>
  const memberStore = useMemberStore(); // 일반 객체로 받아서 사용 const members
  = computed(() => memberStore.members);
</script>;
```

#### 📋 DTO 사용 규칙 요약

1. **DTO 클래스 정의**: `dto/` 디렉토리에만 정의
2. **DTO 사용**: Service 계층에서만 import 및 사용
3. **데이터 변환**: Service에서 DTO → 일반 객체로 변환
4. **상위 계층**: Store, Composables, Views는 일반 객체만 사용
5. **타입 안전성**: DTO 클래스 내부에서 유효성 검사 및 변환 로직 구현

---

## 🎯 단일 책임 원칙 (SRP) 적용

### 각 레이어의 단일 책임

#### Views Layer

- **책임**: UI 렌더링과 사용자 상호작용만
- **변경 이유**: UI 요구사항 변경, UX 개선

#### Composables Layer

- **책임**: 재사용 가능한 로직 캡슐화
- **변경 이유**: 공통 로직 변경, 재사용 요구사항 변경

#### Stores Layer

- **책임**: 상태 관리와 컴포넌트-서비스 중재
- **변경 이유**: 상태 구조 변경, 상태 관리 로직 변경

#### Services Layer

- **책임**: 비즈니스 로직 처리와 데이터 변환
- **변경 이유**: 비즈니스 규칙 변경, 데이터 처리 로직 변경

#### API Client Layer

- **책임**: HTTP 통신 및 API 엔드포인트 관리
- **변경 이유**: API 스펙 변경, 네트워크 정책 변경

---

## 📁 디렉토리 구조 & 파일 명명 규칙

```
src/
├── apis/                      # API 관련
│   ├── apiClient.js          # Axios 래퍼 (camelCase)
│   └── routes/               # API 엔드포인트 정의
├── components/               # Vue 컴포넌트 (PascalCase)
│   ├── common/              # 공통 컴포넌트
│   ├── attendance/          # 도메인별 컴포넌트
│   └── ...
├── composables/             # Composition 함수들 (use + PascalCase)
├── stores/                  # Pinia 상태 관리 (camelCase + Store)
├── services/                # 비즈니스 로직 (camelCase + Service)
├── dto/                     # 데이터 전송 객체 (camelCase + DTO)
├── constants/               # 상수 정의 (camelCase)
├── utils/                   # 유틸리티 함수 (camelCase)
└── views/                   # 페이지 컴포넌트 (PascalCase + Page)
```

### 파일 명명 컨벤션

| 파일 타입           | 패턴                     | 예시                                         |
| ------------------- | ------------------------ | -------------------------------------------- |
| **Vue 컴포넌트**    | `PascalCase.vue`         | `MemberList.vue`, `ProfileEditModal.vue`     |
| **페이지 컴포넌트** | `PascalCase + Page.vue`  | `MemberListPage.vue`, `LoginPage.vue`        |
| **Composables**     | `use + PascalCase.js`    | `useAuth.js`, `useMemberList.js`             |
| **Stores**          | `camelCase + Store.js`   | `memberStore.js`, `authStore.js`             |
| **Services**        | `camelCase + Service.js` | `memberService.js`, `authService.js`         |
| **DTO**             | `camelCase + DTO.js`     | `memberResponseDTO.js`, `loginRequestDTO.js` |
| **상수**            | `camelCase.js`           | `roleCode.js`, `apiEndpoints.js`             |

---

## 🔧 개발 패턴 & 코딩 컨벤션

### 1. Vue 컴포넌트 패턴

```javascript
<template>
  <!-- 템플릿 구조 -->
</template>

<script setup>
// ✅ 올바른 import 순서
// 1. Vue 관련
import { ref, computed, onMounted } from "vue";

// 2. 외부 라이브러리
import dayjs from "dayjs";

// 3. Composables
import { useAuth } from "@/composables/useAuth";
import { useToast } from "@/composables/useToast";

// 4. Stores (Composables가 없을 때만)
import { useMemberStore } from "@/stores/memberStore";

// 5. 상수 및 유틸리티
import { RoleCode } from "@/constants/common/RoleCode";

// ✅ 상태 선언
const loading = ref(false);
const members = ref([]);

// ✅ Composables 사용
const { isAuthenticated, hasHRAccess } = useAuth();
const { showSuccess, showError } = useToast();

// ✅ 계산된 속성
const filteredMembers = computed(() => {
  return members.value.filter(member => member.isActive);
});

// ✅ 메서드 정의
const loadMembers = async () => {
  loading.value = true;
  try {
    // 비즈니스 로직
  } catch (error) {
    showError("데이터 로드 실패");
  } finally {
    loading.value = false;
  }
};

// ✅ 생명주기
onMounted(() => {
  loadMembers();
});
</script>

<style scoped>
/* 스타일 정의 */
</style>
```

### 2. Composables 패턴

```javascript
// composables/useExample.js
import { ref, computed } from "vue";
import { useExampleStore } from "@/stores/exampleStore";

export const useExample = () => {
  const exampleStore = useExampleStore();

  // ✅ 상태 정의
  const loading = ref(false);
  const error = ref(null);

  // ✅ 계산된 속성
  const isReady = computed(() => !loading.value && !error.value);

  // ✅ 메서드 정의
  const loadData = async () => {
    loading.value = true;
    error.value = null;

    try {
      await exampleStore.fetchData();
    } catch (err) {
      error.value = err.message;
    } finally {
      loading.value = false;
    }
  };

  // ✅ 명확한 반환 구조
  return {
    // 상태
    loading,
    error,

    // 계산된 값
    isReady,

    // 메서드
    loadData,
  };
};
```

### 3. Store 패턴

```javascript
// stores/exampleStore.js
import { defineStore } from "pinia";
import { ref, computed } from "vue";
import { exampleService } from "@/services/exampleService";

export const useExampleStore = defineStore("example", () => {
  // ✅ 상태 정의
  const items = ref([]);
  const loading = ref(false);

  // ✅ Getters (computed)
  const itemCount = computed(() => items.value.length);
  const activeItems = computed(() =>
    items.value.filter((item) => item.isActive)
  );

  // ✅ Actions
  const fetchItems = async () => {
    loading.value = true;
    try {
      const result = await exampleService.getItems();
      items.value = result;
    } finally {
      loading.value = false;
    }
  };

  const addItem = async (itemData) => {
    const newItem = await exampleService.createItem(itemData);
    items.value.push(newItem);
  };

  return {
    // 상태
    items,
    loading,

    // Getters
    itemCount,
    activeItems,

    // Actions
    fetchItems,
    addItem,
  };
});
```

### 4. Service 패턴

```javascript
// services/exampleService.js
import api from "@/apis/apiClient";
import { API } from "@/apis/routes";
import { ExampleResponseDTO } from "@/dto/example/exampleResponseDTO";

export const exampleService = {
  // ✅ 조회 메서드
  async getItems() {
    const response = await api.get(API.EXAMPLE.LIST);
    return response.data.map((item) => new ExampleResponseDTO(item));
  },

  // ✅ 생성 메서드
  async createItem(itemData) {
    const response = await api.post(API.EXAMPLE.CREATE, itemData);
    return new ExampleResponseDTO(response.data);
  },

  // ✅ 수정 메서드
  async updateItem(id, itemData) {
    const response = await api.put(API.EXAMPLE.UPDATE(id), itemData);
    return new ExampleResponseDTO(response.data);
  },

  // ✅ 삭제 메서드
  async deleteItem(id) {
    await api.delete(API.EXAMPLE.DELETE(id));
    return true;
  },
};
```

---

## 🔐 인증 시스템 가이드

### JWT 토큰 플로우

```javascript
// ✅ 로그인 처리
const { login } = useAuth();

const handleLogin = async (credentials) => {
  try {
    await login(credentials);
    // 자동으로 토큰 저장 및 헤더 설정
    router.push("/dashboard");
  } catch (error) {
    showError("로그인 실패");
  }
};

// ✅ 권한 체크
const { hasHRAccess, hasRecruitmentOperator } = useAuth();

// 템플릿에서 사용
<template>
  <v-btn v-if="hasHRAccess" @click="handleHRAction">
    HR 기능
  </v-btn>
</template>
```

### 권한별 접근 제어

| 권한           | 코드                               | 설명               |
| -------------- | ---------------------------------- | ------------------ |
| 기본 사용자    | `RoleCode.USER`                    | 기본 인증된 사용자 |
| HR 접근        | `RoleCode.HR_ACCESS`               | 인사 관리 기능     |
| 채용 계획 편집 | `RoleCode.RECRUITMENT_PLAN_EDITOR` | 채용 계획 수정     |
| 결재 처리      | `RoleCode.APPROVAL_PROCESSOR`      | 결재 승인/거부     |
| 채용 운영      | `RoleCode.RECRUITMENT_OPERATOR`    | 채용 전 과정 관리  |

---

## 📊 주요 도메인별 개발 가이드

### 1. 사원 관리 (Member)

```javascript
// ✅ 사원 목록 조회
const { members, loadAllMembers, filteredMembers } = useMemberList();

// ✅ 프로필 이미지 처리
const { profileImageSrc, uploadProfileImage } = useProfileImage();

// ✅ 프로필 폼 관리
const { isEditing, startEditing, saveChanges } = useProfileForm();
```

### 2. 근태 관리 (Attendance)

```javascript
// ✅ 근태 상세 정보
const { memberData, attendanceRecords, attendanceStats } =
  useAttendanceDetail(memberId);

// ✅ 근태 계산
import { attendanceCalculator } from "@/utils/attendance/attendanceCalculator";
const workHours = attendanceCalculator.calculateWorkHours(checkIn, checkOut);
```

### 3. 채용 관리 (Employment)

```javascript
// ✅ 지원자 관리
const { selectedApplicants, currentApplicant, toggleRegistrationSelection } =
  useApplicantManager();

// ✅ 등록 진행 상황
const { registrationProgress, setRegistrationProgress, getStatusText } =
  useRegistrationProgress();
```

---

## 🎨 UI/UX 패턴

### 1. 로딩 상태 처리

```javascript
<template>
  <v-container>
    <!-- 로딩 중 -->
    <v-progress-circular
      v-if="loading"
      indeterminate
      color="primary"
    />

    <!-- 데이터 표시 -->
    <div v-else-if="!error">
      <!-- 컨텐츠 -->
    </div>

    <!-- 에러 상태 -->
    <v-alert v-else type="error">
      {{ error }}
    </v-alert>
  </v-container>
</template>
```

### 2. 토스트 메시지 패턴

```javascript
const { showSuccess, showError, showWarning, showInfo } = useToast();

// ✅ 성공 메시지
showSuccess("저장이 완료되었습니다.");

// ✅ 에러 메시지
showError("데이터를 불러오는데 실패했습니다.");

// ✅ 경고 메시지
showWarning("권한이 없습니다.");

// ✅ 정보 메시지
showInfo("처리 중입니다...");
```

### 3. 모달 패턴

```javascript
<template>
  <v-dialog v-model="dialog" max-width="600px">
    <v-card>
      <v-card-title>제목</v-card-title>
      <v-card-text>
        <!-- 컨텐츠 -->
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn @click="dialog = false">취소</v-btn>
        <v-btn @click="handleConfirm" color="primary">확인</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
```

---

## ⚡ 성능 최적화 가이드

### 1. 컴포넌트 최적화

```javascript
// ✅ defineAsyncComponent로 지연 로딩
import { defineAsyncComponent } from "vue";

const HeavyComponent = defineAsyncComponent(() =>
  import("@/components/HeavyComponent.vue")
);

// ✅ v-memo 사용 (리스트 최적화)
<template>
  <div v-for="item in items" :key="item.id" v-memo="[item.id, item.name]">
    {{ item.name }}
  </div>
</template>
```

### 2. 상태 관리 최적화

```javascript
// ✅ Store에서 캐싱 활용
const fetchMembers = async (forceRefresh = false) => {
  if (!forceRefresh && members.value.length > 0) {
    return members.value; // 캐시된 데이터 반환
  }

  // API 호출
  const result = await memberService.getMembers();
  members.value = result;
  return result;
};
```

---

## 🧪 테스트 가이드

### 1. Composables 테스트

```javascript
// tests/composables/useAuth.test.js
import { useAuth } from "@/composables/useAuth";

describe("useAuth", () => {
  it("should return authentication status", () => {
    const { isAuthenticated, userRoles } = useAuth();

    expect(isAuthenticated.value).toBe(false);
    expect(userRoles.value).toEqual([]);
  });
});
```

### 2. 컴포넌트 테스트

```javascript
// tests/components/MemberList.test.js
import { mount } from "@vue/test-utils";
import MemberList from "@/components/MemberList.vue";

describe("MemberList", () => {
  it("should render member list", async () => {
    const wrapper = mount(MemberList);

    // 테스트 로직
    expect(wrapper.find('[data-testid="member-list"]').exists()).toBe(true);
  });
});
```

---

## 🚀 배포 & 환경 설정

### 환경 변수

```bash
# .env.development
VITE_API_BASE_URL=http://localhost:8080/api
VITE_TIMEOUT=30000

# .env.production
VITE_API_BASE_URL=https://api.empick.com
VITE_TIMEOUT=60000
```

### 빌드 명령어

```bash
# 개발 서버 실행
npm run dev

# 프로덕션 빌드
npm run build

# 빌드 결과 미리보기
npm run preview

# 린트 검사
npm run lint

# 린트 자동 수정
npm run lint:fix
```

---

## 📝 체크리스트

### 🔍 코드 리뷰 체크리스트

#### 아키텍처 준수

- [ ] 레이어 간 의존성 규칙 준수
- [ ] ESLint 규칙 통과
- [ ] SRP 원칙 적용
- [ ] 적절한 레이어에 코드 배치

#### 코딩 컨벤션

- [ ] 파일명 규칙 준수
- [ ] 함수/변수명 camelCase 사용
- [ ] 컴포넌트명 PascalCase 사용
- [ ] 상수명 UPPER_SNAKE_CASE 사용

#### 성능 & 보안

- [ ] 불필요한 리렌더링 방지
- [ ] 적절한 에러 처리
- [ ] 로딩 상태 표시
- [ ] 권한 체크 적용

#### 사용자 경험

- [ ] 토스트 메시지 적절히 사용
- [ ] 로딩 인디케이터 표시
- [ ] 에러 상태 적절히 처리
- [ ] 반응형 디자인 적용

### 🛠️ 새 기능 개발 체크리스트

1. **설계 단계**

   - [ ] DTO 구조 설계
   - [ ] API 엔드포인트 정의
   - [ ] 컴포넌트 구조 계획

2. **개발 단계**

   - [ ] Service 레이어 구현
   - [ ] Store 레이어 구현
   - [ ] Composable 구현 (필요시)
   - [ ] 컴포넌트 구현

3. **테스트 단계**

   - [ ] 단위 테스트 작성
   - [ ] 통합 테스트 확인
   - [ ] 수동 테스트 수행

4. **배포 단계**
   - [ ] 코드 리뷰 완료
   - [ ] 린트 검사 통과
   - [ ] 빌드 오류 없음

---

## 💡 자주 사용되는 코드 스니펫

### 1. 기본 Vue 컴포넌트 템플릿

```javascript
<template>
  <v-container>
    <v-row>
      <v-col>
        <!-- 컨텐츠 -->
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useToast } from "@/composables/useToast";

// Props
const props = defineProps({
  // props 정의
});

// Emits
const emit = defineEmits(['update', 'close']);

// Composables
const { showSuccess, showError } = useToast();

// 상태
const loading = ref(false);

// 메서드
const handleAction = async () => {
  loading.value = true;
  try {
    // 비즈니스 로직
    showSuccess("성공적으로 처리되었습니다.");
    emit('update');
  } catch (error) {
    showError("처리 중 오류가 발생했습니다.");
  } finally {
    loading.value = false;
  }
};

// 생명주기
onMounted(() => {
  // 초기화 로직
});
</script>
```

### 2. API 호출 패턴

```javascript
// Store에서 API 호출
const fetchData = async (params = {}) => {
  loading.value = true;
  error.value = null;

  try {
    const result = await exampleService.getData(params);
    data.value = result;
    return result;
  } catch (err) {
    error.value = err.message;
    throw err;
  } finally {
    loading.value = false;
  }
};
```

### 3. 폼 유효성 검사 패턴

```javascript
const validationRules = {
  required: [(v) => !!v || "필수 입력 항목입니다."],
  email: [
    (v) => !!v || "이메일을 입력해주세요.",
    (v) => /.+@.+\..+/.test(v) || "올바른 이메일 형식이 아닙니다.",
  ],
  phone: [
    (v) => !!v || "연락처를 입력해주세요.",
    (v) => /^010-\d{4}-\d{4}$/.test(v) || "올바른 연락처 형식이 아닙니다.",
  ],
};
```

---

이 문서는 **Empick Frontend 프로젝트의 완전한 개발 가이드**입니다. 모든 팀원과 AI는 이 가이드를 준수하여 일관된 코드 품질과 아키텍처를 유지해야 합니다.
