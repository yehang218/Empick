<template>
  <v-container fluid style="margin-top: 70px;">
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

          <!-- ➕ 지원자 추가 버튼 (새로 추가) -->
          <v-btn color="primary" variant="tonal" size="small" style="min-width: 90px"
            @click="goToApplicantRegistration">
            지원자 추가
          </v-btn>

          <!-- 📝 문제 할당 버튼 -->
          <v-btn color="secondary" variant="tonal" size="small" style="min-width: 90px" @click="handleAssignClick"
            :disabled="!selectedApplicants.length">
            실무테스트 할당 ({{ selectedApplicants.length }}개 선택)
          </v-btn>

          <!-- 📧 이메일 전송 버튼 -->
          <v-btn color="success" variant="outlined" size="small" prepend-icon="mdi-email" style="min-width: 110px"
            @click="handleEmailClick" :disabled="!selectedApplicants.length">
            이메일 전송 ({{ selectedApplicants.length }}개 선택)
          </v-btn>
        </div>
      </v-card-title>

      <!-- 검색 결과 요약 -->
      <v-card-text v-if="search" class="text-caption text-grey">
        <span v-if="getUniqueApplicantCount() === 1">
          검색어 "{{ search }}"에 대한 검색 결과:
          지원자 {{ getUniqueApplicantCount() }}명
          <span v-if="applicantStore.filteredAndSortedApplicants.length > 1">
            (지원서 {{ applicantStore.filteredAndSortedApplicants.length }}건)
          </span>
        </span>
        <span v-else>
          검색어 "{{ search }}"에 대한 검색 결과:
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

        <!-- 직무 -->
        <template #item.jobName="{ item }">
          {{ item.jobName || '미지정' }}
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
import { useApplicantManager } from '@/composables/useApplicantManager'
import { debounce } from 'lodash'

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

// ===== View 데이터 (상수) =====
const tableHeaders = [
  { title: '', key: 'select', sortable: false, align: 'center', width: '50px' },
  { title: '이름', key: 'name', sortable: true, align: 'start' },
  { title: '이메일', key: 'email', sortable: true, align: 'start' },
  { title: '생년월일', key: 'birth', sortable: true, align: 'start' },
  { title: '전화번호', key: 'phone', sortable: true, align: 'start' },
  { title: '지원서', key: 'actions', sortable: false, align: 'center' },
  { title: '처리 상태', key: 'status', sortable: true, align: 'center' },
  { title: '지원공고', key: 'recruitmentTitle', sortable: true, align: 'start' }
]

// ===== ViewModel: 계산된 속성 =====
// 상태 관련 유틸리티 함수들
const getStatusColor = (status) => {
  const statusMap = {
    'PASSED_FINAL': 'success',
    'FAILED': 'error',
    'PASSED_DOCS': 'info',
    'PASSED_INTERVIEW_1': 'teal',
    'PASSED_INTERVIEW_2': 'blue',
    'PASSED_PRACTICAL': 'purple',
    'WAITING': 'grey'
  }
  return statusMap[status] || 'grey'
}

const getStatusText = (status) => {
  const statusTextMap = {
    'PASSED_FINAL': '최종합격',
    'FAILED': '불합격',
    'PASSED_DOCS': '서류합격',
    'PASSED_INTERVIEW_1': '1차합격',
    'PASSED_INTERVIEW_2': '2차합격',
    'PASSED_PRACTICAL': '실무합격',
    'WAITING': '대기중'
  }
  return statusTextMap[status] || '알 수 없음'
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
    toast.error('실무 테스트 목록을 불러오는 데 실패했습니다.')
  }
}

const handleJobtestSelected = async (jobtest) => {
  jobtestModal.value = false

  const dtoList = selectedApplicants.value.map(selectedItem => {
    return new ApplicationJobtestDTO(selectedItem.applicationId, jobtest.id)
  })

  try {
    await applicationJobtestStore.assignJobtest(dtoList)
    toast.success(`선택한 ${selectedApplicants.value.length}개 지원서에 실무테스트를 성공적으로 할당했습니다.`)
    selectedApplicants.value = []
  } catch (error) {
    console.error('실무테스트 할당 실패:', error)
    toast.error(applicationJobtestStore.errorMessage || '실무테스트 할당에 실패했습니다.')
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
    await applicantStore.fetchApplicantFullInfoList()
  } catch (error) {
    console.error('지원자 목록 조회 실패:', error)
    toast.error('지원자 목록을 불러오는 데 실패했습니다.')
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

// 지원자 등록 페이지로 이동
const goToApplicantRegistration = () => {
  router.push({name: 'ApplicantRegistrationPage'});
};

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

  try {
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
  } catch (error) {
    console.error('❌ 이메일 발송 실패:', error);
    toast.error('이메일 발송에 실패했습니다: ' + error.message);
  } finally {
    sendingEmail.value = false;
  }
};

const handleEmailPreviewCancel = () => {
  console.log('❌ 이메일 미리보기 취소');
  emailPreviewModal.value = false;
};

</script>

<style scoped>
.v-data-table {
  margin-top: 20px;
}
</style>