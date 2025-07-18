<template>
  <v-container fluid style="margin-top: 70px;">
    <!-- 이메일 발송 로딩 화면 -->
    <transition name="fade">
      <div v-if="emailLoadingScreen" class="loading-overlay">
        <div class="plane-animation">
          <div class="plane-body">
            <svg width="80" height="80" viewBox="0 0 80 80">
              <polygon points="10,40 70,10 55,40 70,70" fill="#42a5f5" stroke="#1976d2"
                stroke-width="3" />
              <polygon points="10,40 55,40 70,70 40,55" fill="#90caf9" stroke="#1976d2"
                stroke-width="2" />
            </svg>
          </div>
          <div class="plane-trail"></div>
          <div class="plane-progress">
            <v-progress-linear indeterminate color="blue lighten-2" height="8"
              rounded></v-progress-linear>
          </div>
          <div class="plane-text">메일을 발송 중입니다...</div>
        </div>
      </div>
    </transition>

    <!-- 이메일 발송 성공 모달 -->
    <transition name="fade">
      <div v-if="emailSuccessModal" class="center-success-modal">
        <div class="center-success-content">
          <span class="emoji">🎉</span>
          <div class="center-success-text">메일이 성공적으로 발송되었습니다!</div>
        </div>
      </div>
    </transition>

    <v-card>
      <!-- 상단 타이틀 + 검색/버튼 영역 -->
      <v-card-title class="d-flex justify-between align-center flex-wrap">
        <span class="text-h6 font-weight-bold">지원자 목록</span>

        <v-spacer />

        <div class="d-flex align-center flex-wrap" style="gap: 8px;">
          <!-- 🔍 검색창 (공통 컴포넌트) -->
          <Search v-model="search" placeholder="이름, 이메일, 전화번호, 직무로 검색" @clear="clearSearch" @search="handleSearch" />
          <v-btn icon @click="refreshList" :loading="applicantStore.loading" aria-label="새로고침">
            <v-icon>mdi-refresh</v-icon>
          </v-btn>

          <!-- 👤 사원 등록 버튼 -->
          <v-btn color="primary" variant="tonal" size="small" style="min-width: 90px" @click="handleRegisterClick"
            :disabled="!selectedApplicants.length">
            사원 등록 ({{ selectedApplicants.length }}개 선택)
          </v-btn>



          <!-- 📝 문제 할당 버튼 -->
          <v-btn color="secondary" variant="tonal" size="small" style="min-width: 90px" @click="handleAssignClick"
            :disabled="!selectedApplicants.length">
            {{ getAssignButtonText() }}
          </v-btn>

          <!-- 📧 이메일 전송 버튼 -->
          <v-btn color="success" variant="outlined" size="small" prepend-icon="mdi-email" style="min-width: 110px"
            @click="handleEmailClick" :disabled="!selectedApplicants.length">
            이메일 전송 ({{ selectedApplicants.length }}개 선택)
          </v-btn>
        </div>
      </v-card-title>

      <!-- 필터 영역 -->
      <v-card-text>
        <v-row align="center">
          <v-col cols="12" md="3">
            <v-select
              v-model="statusFilter"
              :items="statusOptions"
              item-title="label"
              item-value="value"
              label="처리 상태"
              clearable
              variant="outlined"
              density="compact"
              @update:model-value="applyFilters"
            >
              <template v-slot:selection="{ item }">
                <v-chip :color="item.raw.color" variant="tonal" size="small">
                  {{ item.raw.label }}
                </v-chip>
              </template>
              <template v-slot:item="{ props, item }">
                <v-list-item v-bind="props">
                  <template v-slot:prepend>
                    <v-chip :color="item.raw.color" variant="tonal" size="small">
                      {{ item.raw.label }}
                    </v-chip>
                  </template>
                </v-list-item>
              </template>
            </v-select>
          </v-col>
          
          <v-col cols="12" md="3">
            <v-select
              v-model="jobtestFilter"
              :items="jobtestStatusOptions"
              item-title="label"
              item-value="value"
              label="실무테스트"
              clearable
              variant="outlined"
              density="compact"
              @update:model-value="applyFilters"
            >
              <template v-slot:selection="{ item }">
                <v-chip :color="item.raw.color" variant="tonal" size="small">
                  {{ item.raw.label }}
                </v-chip>
              </template>
              <template v-slot:item="{ props, item }">
                <v-list-item v-bind="props">
                  <template v-slot:prepend>
                    <v-chip :color="item.raw.color" variant="tonal" size="small">
                      {{ item.raw.label }}
                    </v-chip>
                  </template>
                </v-list-item>
              </template>
            </v-select>
          </v-col>
          
          <v-col cols="12" md="4">
            <v-select
              v-model="recruitmentFilter"
              :items="recruitmentOptions"
              item-title="title"
              item-value="id"
              label="지원 공고"
              clearable
              variant="outlined"
              density="compact"
              @update:model-value="applyFilters"
            >
            </v-select>
          </v-col>
          
          <v-col cols="12" md="2">
            <v-btn 
              color="grey-darken-1" 
              variant="outlined" 
              size="small" 
              @click="clearFilters"
              :disabled="!hasActiveFilters"
              block
            >
              필터 초기화
            </v-btn>
          </v-col>
        </v-row>
        
        <!-- 필터 적용 상태 표시 -->
        <div v-if="hasActiveFilters" class="mt-2">
          <v-chip-group>
            <v-chip v-if="statusFilter !== null && statusFilter !== undefined" closable @click:close="applicantStore.setStatusFilter(null)" color="primary" variant="tonal" size="small">
              상태: {{ getStatusOptionLabel(statusFilter) }}
            </v-chip>
            <v-chip v-if="jobtestFilter !== null && jobtestFilter !== undefined" closable @click:close="applicantStore.setJobtestFilter(null)" color="secondary" variant="tonal" size="small">
              실무테스트: {{ getJobtestOptionLabel(jobtestFilter) }}
            </v-chip>
            <v-chip v-if="recruitmentFilter !== null && recruitmentFilter !== undefined" closable @click:close="applicantStore.setRecruitmentFilter(null)" color="tertiary" variant="tonal" size="small">
              공고: {{ getRecruitmentOptionLabel(recruitmentFilter) }}
            </v-chip>
          </v-chip-group>
        </div>
      </v-card-text>

      <!-- 검색 결과 요약 -->
      <v-card-text v-if="search || hasActiveFilters" class="text-caption text-grey pt-0">
        <span v-if="getUniqueApplicantCount() === 1">
          <span v-if="search">검색어 "{{ search }}"</span>
          <span v-if="search && hasActiveFilters"> 및 </span>
          <span v-if="hasActiveFilters">필터 조건</span>
          에 대한 결과:
          지원자 {{ getUniqueApplicantCount() }}명
          <span v-if="applicantStore.filteredAndSortedApplicants.length > 1">
            (지원서 {{ applicantStore.filteredAndSortedApplicants.length }}건)
          </span>
        </span>
        <span v-else>
          <span v-if="search">검색어 "{{ search }}"</span>
          <span v-if="search && hasActiveFilters"> 및 </span>
          <span v-if="hasActiveFilters">필터 조건</span>
          에 대한 결과:
          지원자 {{ getUniqueApplicantCount() }}명, 지원서 {{ applicantStore.filteredAndSortedApplicants.length }}건
        </span>
      </v-card-text>

      <!-- 📋 지원자 테이블 -->
      <v-data-table :headers="tableHeaders" :items="applicantStore.filteredAndSortedApplicants" :items-per-page="8"
        item-key="uniqueKey" class="elevation-1" @update:options="handleSort" return-object>

        <!-- 전체 선택 체크박스 헤더 -->
        <template #header.select>
          <v-checkbox :model-value="isAllSelected" :indeterminate="isIndeterminate"
            @update:model-value="toggleSelectAll" hide-details density="compact" />
        </template>

        <!-- 커스텀 체크박스 컬럼 -->
        <template #item.select="{ item }">
          <v-checkbox :model-value="isSelected(item)" @update:model-value="toggleSelection(item)" hide-details
            density="compact" />
        </template>

        <!-- 이름 + 지원 횟수 표시 -->
        <template #item.name="{ item }">
          <div>
            <div class="font-weight-medium">{{ item.name || '-' }}</div>
            <div class="text-caption text-grey" v-if="getApplicantCount(item.applicantId) > 1">
              {{ getApplicantApplicationNumber(item) }}번째 지원
            </div>
          </div>
        </template>

        <!-- 이메일 -->
        <template #item.email="{ item }">
          {{ item.email || '-' }}
        </template>

        <!-- 생년월일 -->
        <template #item.birth="{ item }">
          {{ item.birth ? new Date(item.birth).toLocaleDateString() : '-' }}
        </template>

        <!-- 전화번호 -->
        <template #item.phone="{ item }">
          {{ item.phone || '-' }}
        </template>

        <!-- 처리 상태 칩 -->
        <template #item.status="{ item }">
          <v-chip :color="getStatusColor(item.status)" variant="tonal" size="small">
            {{ getStatusText(item.status) }}
          </v-chip>
        </template>

        <!-- 실무테스트 상태 -->
        <template #item.jobtestStatus="{ item }">
          <v-chip :color="item.applicationJobtestTitle ? 'primary' : 'grey'" variant="tonal" size="small">
            {{ item.applicationJobtestTitle ? '할당됨' : '할당안됨' }}
          </v-chip>
        </template>

        <!-- 직무 -->
        <template #item.jobName="{ item }">
          {{ item.jobName || '미지정' }}
        </template>

        <!-- 지원공고 제목 -->
        <template #item.recruitmentTitle="{ item }">
          <div class="text-caption">
            {{ getRecruitmentTitle(item.recruitmentId) || '공고 정보 없음' }}
          </div>
        </template>

        <!-- 지원서 확인 텍스트 버튼 -->
        <template #item.actions="{ item }">
          <v-btn color="primary" variant="text" size="small" @click="viewDetail(item)">
            지원서 확인
          </v-btn>
        </template>

      </v-data-table>

      <!-- 선택된 지원서 정보 표시 -->
      <v-card-text v-if="selectedApplicants.length > 0" class="text-caption">
        <v-chip color="primary" variant="tonal" size="small">
          {{ selectedApplicants.length }}개 지원서 선택됨
        </v-chip>
        <span class="ml-2 text-grey">
          선택된 지원자: {{ getSelectedApplicantNames().join(', ') }}
        </span>
      </v-card-text>

      <!-- 로딩 상태 표시 -->
      <v-overlay :model-value="applicantStore.loading" class="align-center justify-center">
        <v-progress-circular indeterminate size="64"></v-progress-circular>
      </v-overlay>

      <!-- 에러 상태 표시 -->
      <v-snackbar :model-value="!!applicantStore.error" color="error">
        {{ applicantStore.error }}
        <template v-slot:actions>
          <v-btn variant="text" @click="applicantStore.error = null">
            닫기
          </v-btn>
        </template>
      </v-snackbar>
    </v-card>

    <!-- 실무 테스트 선택 모달 -->
    <JobtestSelectModal v-model="jobtestModal" :jobtests="jobtestListStore.jobtests" @select="handleJobtestSelected" />

    <!-- 이메일 타입 선택 모달 -->
    <SelectEmailModal
      v-model="emailTypeModal"
      :selected-count="selectedApplicants.length"
      @select="handleEmailTypeSelected"
      @cancel="handleEmailTypeCancel"
    />

    <!-- 이메일 미리보기 모달 -->
    <EmailPreviewModal
      v-model="emailPreviewModal"
      :email-type="selectedEmailType"
      :selected-count="selectedApplicants.length"
      :applicants="selectedApplicants"
      :loading="sendingEmail"
      @send="handleSendEmail"
      @cancel="handleEmailPreviewCancel"
    />
  </v-container>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch, computed } from 'vue'
import { useRouter } from 'vue-router'
import Search from '@/components/common/Search.vue'
import { useApplicationStore } from '@/stores/applicationStore';
import { useMailStore } from '@/stores/mailStore';
import { useMemberStore } from '@/stores/memberStore';
import { useToast } from 'vue-toastification'
import { useApplicantStore } from '@/stores/applicantStore'
import { useRecruitmentStore } from '@/stores/recruitmentStore'
import { useApplicantManager } from '@/composables/useApplicantManager'
import { debounce } from 'lodash'
import { getStatusByCode, getStatusInfoByString, STATUS_OPTIONS } from '@/constants/employment/applicationStatus'

// 실무테스트 할당

import { useJobtestListStore } from '@/stores/jobtestListStore';
import { useApplicationJobtestStore } from '@/stores/applicationJobtestStore';
import ApplicationJobtestDTO from '@/dto/employment/jobtest/createApplicationJobtestDTO';
import JobtestSelectModal from '@/components/employment/JobtestSelectModal.vue';
import SelectEmailModal from '@/components/mail/SelectEmailModal.vue';
import EmailPreviewModal from '@/components/mail/EmailPreviewModal.vue';

// ===== Store 및 기본 설정 =====
const toast = useToast();
const jobtestListStore = useJobtestListStore();
const applicationJobtestStore = useApplicationJobtestStore();
const applicantStore = useApplicantStore();
const applicationStore = useApplicationStore();
const mailStore = useMailStore();
const memberStore = useMemberStore();
const recruitmentStore = useRecruitmentStore();
const router = useRouter()

// Composable 사용 - 비즈니스 로직 분리
const {
  selectedApplicants,
  handleSearch,
  handleSort,
  getApplicantCount,
  getApplicantApplicationNumber,
  getSelectedApplicantNames,
  getUniqueApplicantCount,
  viewApplicantDetail,
  clearSearch
} = useApplicantManager(applicantStore, router, toast)

// ===== View 상태 관리 =====
const search = ref('')
const jobtestModal = ref(false)
const emailTypeModal = ref(false);
const emailPreviewModal = ref(false);
const selectedEmailType = ref('');
const sendingEmail = ref(false);
const emailLoadingScreen = ref(false);
const emailSuccessModal = ref(false);

// 필터 상태 (Store와 연결)
const statusFilter = computed({
  get: () => applicantStore.statusFilter,
  set: (value) => {
    console.log('🔄 statusFilter 변경됨:', value)
    applicantStore.setStatusFilter(value)
  }
})

const jobtestFilter = computed({
  get: () => applicantStore.jobtestFilter,
  set: (value) => {
    console.log('🔄 jobtestFilter 변경됨:', value)
    applicantStore.setJobtestFilter(value)
  }
})

const recruitmentFilter = computed({
  get: () => applicantStore.recruitmentFilter,
  set: (value) => {
    console.log('🔄 recruitmentFilter 변경됨:', value)
    applicantStore.setRecruitmentFilter(value)
  }
})

// ===== View 데이터 (상수) =====
const tableHeaders = [
  { title: '', key: 'select', sortable: false, align: 'center', width: '50px' },
  { title: '이름', key: 'name', sortable: true, align: 'start' },
  { title: '이메일', key: 'email', sortable: true, align: 'start' },
  { title: '생년월일', key: 'birth', sortable: true, align: 'start' },
  { title: '전화번호', key: 'phone', sortable: true, align: 'start' },
  { title: '지원서', key: 'actions', sortable: false, align: 'center' },
  { title: '처리 상태', key: 'status', sortable: true, align: 'center' },
  { title: '실무테스트', key: 'jobtestStatus', sortable: true, align: 'center', width: '120px' },
  { title: '지원공고', key: 'recruitmentTitle', sortable: true, align: 'start' }
]

// 필터 옵션들
const statusOptions = computed(() => {
  return [
    ...STATUS_OPTIONS.map(status => ({
      label: status.label,
      value: status.code,
      color: status.color
    }))
  ]
})

const jobtestStatusOptions = ref([
  { label: '할당안됨', value: 'UNASSIGNED', color: 'grey' },
  { label: '할당됨', value: 'ASSIGNED', color: 'primary' }
])

const recruitmentOptions = computed(() => {
  return recruitmentStore.list.map(recruitment => ({
    title: recruitment.title,
    id: recruitment.id
  }))
})

// ===== ViewModel: 계산된 속성 =====
// 상태 관련 유틸리티 함수들 (통합된 상태 관리 사용)
const getStatusColor = (status) => {
  // 숫자 코드인 경우 변환
  if (typeof status === 'number') {
    const statusInfo = getStatusByCode(status)
    return statusInfo.color
  }
  
  // 문자열 상태인 경우 새로운 매핑 사용
  if (typeof status === 'string') {
    const statusInfo = getStatusInfoByString(status)
    return statusInfo.color
  }
  
  return 'grey'
}

const getStatusText = (status) => {
  // 숫자 코드인 경우 변환
  if (typeof status === 'number') {
    const statusInfo = getStatusByCode(status)
    return statusInfo.label
  }
  
  // 문자열 상태인 경우 새로운 매핑 사용
  if (typeof status === 'string') {
    const statusInfo = getStatusInfoByString(status)
    return statusInfo.label
  }
  
  return '알 수 없음'
}

const viewDetail = (item) => {
  try {
    viewApplicantDetail(item, { from: '/employment/applicant' })
  } catch (error) {
    console.error('상세 보기 실패:', error)
    toast.error('상세 정보를 불러올 수 없습니다.')
  }
}

const handleAssignClick = async () => {
  if (!selectedApplicants.value || selectedApplicants.value.length === 0) {
    toast.warning('선택된 지원자가 없습니다.')
    return
  }

  try {
    await jobtestListStore.fetchJobtests()
    jobtestModal.value = true
  } catch (error) {
    console.error('실무 테스트 목록 조회 실패:', error)
    toast.error('실무테스트 목록을 불러오는데 실패했습니다.')
  }
}

const handleJobtestSelected = async (jobtest) => {
  jobtestModal.value = false

  // applicationId를 찾지 못한 경우를 위해 모든 지원서를 미리 가져오기
  let allApplications = null;

  const dtoList = [];
  
  for (const selectedItem of selectedApplicants.value) {
    // id 필드를 우선적으로 사용 (대부분의 경우 id가 applicationId임)
    let applicationId = selectedItem.id || selectedItem.applicationId;

    console.log('🔍 찾은 applicationId:', applicationId, 'for applicant:', selectedItem.name);

    // applicationId가 없으면 모든 지원서에서 applicantId로 매칭되는 것 찾기
    if (!applicationId && selectedItem.applicantId) {
      console.log('🔍 applicationId가 없어서 모든 지원서에서 applicantId로 매칭 시도:', selectedItem.applicantId);

      // 모든 지원서를 한 번만 가져오기
      if (!allApplications) {
        try {
          allApplications = await applicationStore.fetchAllApplications();
          console.log('📋 모든 지원서 가져옴:', allApplications);
        } catch (error) {
          console.error('❌ 모든 지원서 조회 실패:', error);
          allApplications = [];
        }
      }

      // applicantId가 일치하는 지원서 찾기
      const matchingApplication = allApplications.find(app =>
        app.applicantId === selectedItem.applicantId ||
        app.applicant_id === selectedItem.applicantId
      );

      if (matchingApplication) {
        applicationId = matchingApplication.id || matchingApplication.applicationId;
        console.log('🔍 매칭된 지원서 찾음:', matchingApplication);
        console.log('🔍 찾은 applicationId:', applicationId);
      } else {
        console.error('❌ applicantId로 매칭되는 지원서를 찾을 수 없음:', selectedItem.applicantId);
      }
    }

    if (!applicationId) {
      console.error('❌ applicationId를 찾을 수 없음:', selectedItem);
      continue;
    }

    dtoList.push(new ApplicationJobtestDTO(applicationId, jobtest.id));
  }

  if (dtoList.length === 0) {
    toast.warning('할당할 수 있는 지원서가 없습니다.');
    return;
  }

  try {
    await applicationJobtestStore.assignJobtest(dtoList)
    toast.success(`선택한 ${dtoList.length}개 지원서에 실무테스트를 성공적으로 할당했습니다.`)
    selectedApplicants.value = []
    
    // 지원자 목록 새로고침하여 실무테스트 상태 업데이트
    await applicantStore.fetchApplicantFullInfoList()
  } catch (error) {
    console.error('실무테스트 할당 실패:', error)
    
    // 에러 메시지 분석하여 적절한 토스트 표시
    const errorMessage = error.message || error.toString();
    
    if (errorMessage.includes('Authentication failed') || errorMessage.includes('BadCredentials')) {
      toast.error('이메일 서버 인증 실패로 인해 실무테스트 할당에 실패했습니다.');
    } else if (errorMessage.includes('이미 할당')) {
      toast.error('해당 지원서에 이미 실무테스트가 할당되어 있습니다.');
    } else {
      toast.error('실무테스트 할당에 실패했습니다. 잠시 후 다시 시도해주세요.');
    }
  }
}

const handleRegisterClick = () => {
  if (!selectedApplicants.value || selectedApplicants.value.length === 0) {
    toast.warning('선택된 지원자가 없습니다.')
    return
  }

  const selectedApplicantsData = selectedApplicants.value.map(applicant => ({
    applicantId: applicant.applicantId,
    applicationId: applicant.applicationId,
    name: applicant.name,
    email: applicant.email,
    phone: applicant.phone,
    birth: applicant.birth,
    address: applicant.address,
  }))

  router.push({
    path: '/orgstructure/member-register',
    query: {
      applicants: JSON.stringify(selectedApplicantsData)
    }
  })
}

// 선택 관련 유틸리티
const isSelected = (item) => {
  return selectedApplicants.value.some(selected => selected.uniqueKey === item.uniqueKey)
}

const toggleSelection = (item) => {
  const isCurrentlySelected = isSelected(item)
  if (isCurrentlySelected) {
    selectedApplicants.value = selectedApplicants.value.filter(
      selected => selected.uniqueKey !== item.uniqueKey
    )
  } else {
    selectedApplicants.value.push(item)
  }
}

const isAllSelected = computed(() => {
  const totalItems = applicantStore.filteredAndSortedApplicants.length
  return totalItems > 0 && selectedApplicants.value.length === totalItems
})

const isIndeterminate = computed(() => {
  const selectedCount = selectedApplicants.value.length
  const totalItems = applicantStore.filteredAndSortedApplicants.length
  return selectedCount > 0 && selectedCount < totalItems
})

const toggleSelectAll = (selectAll) => {
  if (selectAll) {
    selectedApplicants.value = [...applicantStore.filteredAndSortedApplicants]
  } else {
    selectedApplicants.value = []
  }
}

// ===== 생명주기 및 감시자 =====
onMounted(async () => {
  try {
    // 병렬로 데이터 로드
    await Promise.all([
      applicantStore.fetchApplicantFullInfoList(),
      recruitmentStore.loadRecruitmentList()
    ])
    
    // 🎯 지원자 정보 및 application_id 로그 출력
    console.log('🎉 ====== 지원자 목록 로드 완료 ======')
    console.log(`📊 총 지원자 수: ${applicantStore.applicantList.length}명`)
    
    applicantStore.applicantList.forEach((applicant, index) => {
      const applicationId = applicant.id || applicant.applicationId || 'ID 없음'
      const applicantId = applicant.applicantId || 'applicantId 없음'
      
      console.log(`👤 [${index + 1}] 지원자: ${applicant.name || '이름 없음'}`)
      console.log(`   📋 Application ID: ${applicationId}`)
      console.log(`   🆔 Applicant ID: ${applicantId}`)
      console.log(`   📧 이메일: ${applicant.email || '이메일 없음'}`)
      console.log(`   📞 전화번호: ${applicant.phone || '전화번호 없음'}`)
      console.log(`   💼 직무: ${applicant.jobName || '직무 미지정'}`)
      console.log(`   📈 지원서 상태: ${applicant.status || '상태 없음'}`)
      console.log(`   🧪 실무테스트 상태: ${applicant.jobtestStatus || '미할당'}`)
      console.log(`   📑 지원공고 ID: ${applicant.recruitmentId || '공고ID 없음'}`)
      console.log(`   🧪 실무테스트 : ${applicant.applicationJobtestTitle || '미할당'}`)
      console.log('   ─────────────────────────────────')
    })
    
    console.log('🎉 ====== 지원자 정보 로그 출력 완료 ======')
    console.log(`📑 채용공고 수: ${recruitmentStore.list.length}개`)
    
  } catch (error) {
    console.error('❌ 데이터 로드 실패:', error)
    toast.error('데이터를 불러오는 데 실패했습니다.')
  }
})

onUnmounted(() => {
  applicantStore.resetState()
  handleSearch.cancel()
})

// 선택된 지원자 변경 감시 (디버깅 및 로깅용)
watch(selectedApplicants, (newValue) => {
  if (process.env.NODE_ENV === 'development') {
    console.log('🔍 선택된 지원자 변경:', {
      count: newValue.length,
      names: newValue.map(item => item.name)
    })
  }
}, { deep: true })



const handleEmailClick = () => {
  console.log('📧 이메일 전송 클릭, 선택된 항목:', selectedApplicants.value);

  if (!selectedApplicants.value || selectedApplicants.value.length === 0) {
    toast.warning('선택된 지원자가 없습니다.');
    return;
  }

  // 이메일 타입 선택 모달 열기
  selectedEmailType.value = '';
  emailTypeModal.value = true;
};

const handleEmailTypeSelected = (type) => {
  console.log('🎯 이메일 타입 선택:', type);
  selectedEmailType.value = type;
  emailPreviewModal.value = true;
};

const handleEmailTypeCancel = () => {
  console.log('❌ 이메일 타입 취소');
  emailTypeModal.value = false;
};

const handleSendEmail = async () => {
  console.log('📧 이메일 발송 시작:', selectedEmailType.value);
  console.log('📧 발송 대상:', selectedApplicants.value);
  console.log('📧 첫 번째 선택된 항목 구조:', selectedApplicants.value[0]);
  console.log('📧 첫 번째 선택된 항목의 모든 키:', Object.keys(selectedApplicants.value[0]));

  sendingEmail.value = true;
  emailLoadingScreen.value = true;

  try {
    // 애니메이션용 딜레이
    await new Promise(res => setTimeout(res, 1200));

    const emailData = [];

    // applicationId를 찾지 못한 경우를 위해 모든 지원서를 미리 가져오기
    let allApplications = null;

    for (const selectedItem of selectedApplicants.value) {
      // id 필드를 우선적으로 사용 (대부분의 경우 id가 applicationId임)
      let applicationId = selectedItem.id || selectedItem.applicationId;

      console.log('🔍 찾은 applicationId:', applicationId, 'for applicant:', selectedItem.name);

      // applicationId가 없으면 모든 지원서에서 applicantId로 매칭되는 것 찾기
      if (!applicationId && selectedItem.applicantId) {
        console.log('🔍 applicationId가 없어서 모든 지원서에서 applicantId로 매칭 시도:', selectedItem.applicantId);

        // 모든 지원서를 한 번만 가져오기
        if (!allApplications) {
          try {
            allApplications = await applicationStore.fetchAllApplications();
            console.log('📋 모든 지원서 가져옴:', allApplications);
          } catch (error) {
            console.error('❌ 모든 지원서 조회 실패:', error);
            allApplications = [];
          }
        }

        // applicantId가 일치하는 지원서 찾기
        const matchingApplication = allApplications.find(app =>
          app.applicantId === selectedItem.applicantId ||
          app.applicant_id === selectedItem.applicantId
        );

        if (matchingApplication) {
          applicationId = matchingApplication.id || matchingApplication.applicationId;
          console.log('🔍 매칭된 지원서 찾음:', matchingApplication);
          console.log('🔍 찾은 applicationId:', applicationId);
        } else {
          console.error('❌ applicantId로 매칭되는 지원서를 찾을 수 없음:', selectedItem.applicantId);
        }
      }

      if (!applicationId) {
        console.error('❌ applicationId를 찾을 수 없음:', selectedItem);
        continue;
      }

      const emailInfo = {
        applicationId: applicationId,
        name: selectedItem.name,
        email: selectedItem.email,
        phone: selectedItem.phone,
        birth: selectedItem.birth,
        address: selectedItem.address,
        profileUrl: selectedItem.profileUrl,
        jobName: selectedItem.jobName,
        createdAt: selectedItem.createdAt,
        status: selectedItem.status,
        recruitmentId: selectedItem.recruitmentId,
        introduceRatingResultId: selectedItem.introduceRatingResultId,
        education: selectedItem.education,
        experience: selectedItem.experience,
        skills: selectedItem.skills,
        motivation: selectedItem.motivation,
        coverLetter: selectedItem.coverLetter,
        portfolioUrl: selectedItem.portfolioUrl,
        introduceScore: selectedItem.introduceScore,
        introduceStatus: selectedItem.introduceStatus,
        jobtestTotalScore: selectedItem.jobtestTotalScore,
        jobtestEvaluationScore: selectedItem.jobtestEvaluationScore,
        jobtestStatus: selectedItem.jobtestStatus,
        interviewScore: selectedItem.interviewScore,
        interviewAddress: selectedItem.interviewAddress,
        interviewDatetime: selectedItem.interviewDatetime
      };

      emailData.push(emailInfo);
    }

    console.log('📧 최종 이메일 데이터:', emailData);

    if (emailData.length === 0) {
      alert('발송할 지원자가 없습니다.');
      return;
    }

    const senderId = memberStore.form.id || 1; // 현재 로그인한 사용자 ID

    console.log('📧 발송할 applicationIds:', emailData.map(item => item.applicationId));
    console.log('📧 senderId:', senderId);

    // 이메일 발송 (병렬 처리)
    if (selectedEmailType.value === 'jobtest') {
      const promises = emailData.map(emailInfo =>
        mailStore.sendJobtestMail(emailInfo.applicationId, senderId)
          .catch(error => {
            console.error(`❌ ${emailInfo.name}에게 실무테스트 메일 발송 실패:`, error);
            return { error: true, name: emailInfo.name, error: error.message };
          })
      );

      const results = await Promise.allSettled(promises);
      const successCount = results.filter(result => result.status === 'fulfilled').length;
      const failCount = results.length - successCount;

      if (failCount > 0) {
        toast.warning(`${successCount}명 발송 성공, ${failCount}명 발송 실패`);
      } else {
        toast.success(`선택한 ${emailData.length}명의 지원자에게 실무테스트 안내 메일을 발송했습니다.`);
      }
    } else if (selectedEmailType.value === 'interview') {
      const promises = emailData.map(emailInfo =>
        mailStore.sendInterviewMail(emailInfo.applicationId, senderId)
          .catch(error => {
            console.error(`❌ ${emailInfo.name}에게 면접 메일 발송 실패:`, error);
            return { error: true, name: emailInfo.name, error: error.message };
          })
      );

      const results = await Promise.allSettled(promises);
      const successCount = results.filter(result => result.status === 'fulfilled').length;
      const failCount = results.length - successCount;

      if (failCount > 0) {
        toast.warning(`${successCount}명 발송 성공, ${failCount}명 발송 실패`);
      } else {
        toast.success(`선택한 ${emailData.length}명의 지원자에게 면접 일정 안내 메일을 발송했습니다.`);
      }
    }

    emailPreviewModal.value = false;
    selectedEmailType.value = '';
    
    // 성공 모달 표시
    emailSuccessModal.value = true;
    setTimeout(() => {
      emailSuccessModal.value = false;
    }, 2200);
    
  } catch (error) {
    console.error('❌ 이메일 발송 실패:', error);
    toast.error('이메일 발송에 실패했습니다: ' + error.message);
  } finally {
    sendingEmail.value = false;
    setTimeout(() => {
      emailLoadingScreen.value = false;
    }, 900);
  }
};

const handleEmailPreviewCancel = () => {
  console.log('❌ 이메일 미리보기 취소');
  emailPreviewModal.value = false;
};


const getAssignButtonText = () => {
  if (selectedApplicants.value.length === 0) {
    return '실무테스트 할당'
  } else if (selectedApplicants.value.length === 1) {
    return '실무테스트 할당 (1개 선택)'
  } else {
    return '실무테스트 할당 (' + selectedApplicants.value.length + '개 선택)'
  }
}

// 필터 관련 함수들
const hasActiveFilters = computed(() => {
  const result = statusFilter.value !== null && statusFilter.value !== undefined || 
                jobtestFilter.value !== null && jobtestFilter.value !== undefined || 
                recruitmentFilter.value !== null && recruitmentFilter.value !== undefined
  console.log('🔍 hasActiveFilters 체크:', {
    statusFilter: statusFilter.value,
    jobtestFilter: jobtestFilter.value,
    recruitmentFilter: recruitmentFilter.value,
    hasActive: result
  })
  return result
})

const applyFilters = () => {
  // computed를 통해 자동으로 Store에 연결되므로 별도 호출 불필요
}

const clearFilters = () => {
  applicantStore.clearFilters()
}

const getStatusOptionLabel = (value) => {
  const option = statusOptions.value.find(opt => opt.value === value)
  return option ? option.label : ''
}

const getJobtestOptionLabel = (value) => {
  const option = jobtestStatusOptions.value.find(opt => opt.value === value)
  return option ? option.label : ''
}

const getRecruitmentOptionLabel = (value) => {
  const option = recruitmentOptions.value.find(opt => opt.id === value)
  return option ? option.title : ''
}

const getRecruitmentTitle = (recruitmentId) => {
  const recruitment = recruitmentStore.list.find(r => r.id === recruitmentId)
  return recruitment ? recruitment.title : null
}

const refreshList = async () => {
  try {
    await applicantStore.fetchApplicantFullInfoList()
    toast.success('목록을 새로고침했습니다.')
  } catch (error) {
    console.error('새로고침 실패:', error)
    toast.error('목록 새로고침에 실패했습니다.')
  }
}

</script>

<style scoped>
.v-data-table {
  margin-top: 20px;
}

.loading-overlay {
  position: fixed;
  z-index: 9999;
  left: 0;
  top: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.92);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  animation: fadein 0.5s;
}

@keyframes fadein {
  0% {
    opacity: 0;
  }
  100% {
    opacity: 1;
  }
}

.plane-animation {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.plane-body {
  animation: plane-fly 1.2s infinite alternate cubic-bezier(.4, 2, .6, 1);
}

@keyframes plane-fly {
  0% {
    transform: translateY(0) rotate(-8deg);
  }
  100% {
    transform: translateY(-24px) rotate(8deg);
  }
}

.plane-trail {
  width: 80px;
  height: 12px;
  margin-top: -10px;
  background: linear-gradient(90deg, #42a5f5 0%, #fff 100%);
  border-radius: 8px;
  filter: blur(2px);
  opacity: 0.5;
  animation: trail-move 1.2s infinite alternate;
}

@keyframes trail-move {
  0% {
    width: 80px;
    opacity: 0.5;
  }
  100% {
    width: 120px;
    opacity: 0.8;
  }
}

.plane-progress {
  width: 180px;
  margin: 32px 0 8px 0;
}

.plane-text {
  font-size: 1.2rem;
  color: #1976d2;
  font-weight: 600;
  margin-top: 8px;
  letter-spacing: 0.01em;
}

.center-success-modal {
  position: fixed;
  z-index: 20000;
  left: 0; top: 0; right: 0; bottom: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255,255,255,0.55);
  animation: fadein 0.3s;
}

.center-success-content {
  background: rgba(255,255,255,0.98);
  border-radius: 32px;
  box-shadow: 0 8px 32px 0 rgba(80,120,200,0.18);
  padding: 3rem 4rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  animation: alert-pop 0.7s;
}

@keyframes alert-pop {
  0% {
    opacity: 0;
    transform: scale(0.8);
  }
  100% {
    opacity: 1;
    transform: scale(1);
  }
}

.center-success-text {
  font-size: 1.6rem;
  font-weight: 700;
  color: #1976d2;
  margin-top: 1.2rem;
  text-align: center;
}


.emoji {
  font-size: 1.5rem;
  margin-right: 0.5rem;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

@media (max-width: 600px) {
  .center-success-content {
    padding: 1.2rem 1rem;
    border-radius: 18px;
  }
  .center-success-text {
    font-size: 1.1rem;
  }
  .plane-progress {
    width: 120px;
  }
  .plane-text {
    font-size: 1rem;
  }
}
</style>