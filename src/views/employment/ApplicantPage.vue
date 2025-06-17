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
          <v-btn icon @click="refreshList" :loading="applicantStore.isLoading" aria-label="새로고침">
            <v-icon>mdi-refresh</v-icon>
          </v-btn>

          <!-- 👤 사원 등록 버튼 -->
          <v-btn color="primary" variant="tonal" size="small" style="min-width: 90px">
            사원 등록
          </v-btn>

          <!-- 📝 문제 할당 버튼 -->
          <v-btn color="secondary" variant="tonal" size="small" style="min-width: 90px" @click="handleAssignClick"
            :disabled="!selectedIds.length">
            실무테스트 할당 ({{ selectedIds.length }}개 선택)
          </v-btn>

          <!-- 📧 이메일 전송 버튼 -->
          <v-btn color="success" variant="outlined" size="small" prepend-icon="mdi-email" style="min-width: 110px">
            이메일 전송
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
        item-value="uniqueKey" class="elevation-1" v-model:selected="selectedIds" @update:options="handleSort"
        show-select return-object>

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
      <v-card-text v-if="selectedIds.length > 0" class="text-caption">
        <v-chip color="primary" variant="tonal" size="small">
          {{ selectedIds.length }}개 지원서 선택됨
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
  </v-container>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import Search from '@/components/common/Search.vue'
import { useToast } from 'vue-toastification';
import { useApplicantStore } from '@/stores/applicantStore';
import { debounce } from 'lodash'

// 실무테스트 할당
import { useJobtestListStore } from '@/stores/jobtestListStore';
import { useApplicationJobtestStore } from '@/stores/applicationJobtestStore';
import ApplicationJobtestDTO from '@/dto/employment/jobtest/applicationJobtestDTO';
import JobtestSelectModal from '@/components/employment/JobtestSelectModal.vue';

const selectedIds = ref([]);
const jobtestModal = ref(false);
const toast = useToast();
const jobtestListStore = useJobtestListStore();
const applicationJobtestStore = useApplicationJobtestStore();
const applicantStore = useApplicantStore();
const router = useRouter()

const search = ref('')

const tableHeaders = [
  {
    title: '이름',
    key: 'name',
    sortable: true,
    align: 'start'
  },
  {
    title: '이메일',
    key: 'email',
    sortable: true,
    align: 'start'
  },
  {
    title: '생년월일',
    key: 'birth',
    sortable: true,
    align: 'start'
  },
  {
    title: '전화번호',
    key: 'phone',
    sortable: true,
    align: 'start'
  },
  {
    title: '지원서',
    key: 'actions',
    sortable: false,
    align: 'center'
  },
  {
    title: '처리 상태',
    key: 'status',
    sortable: true,
    align: 'center'
  },
  {
    title: '직무',
    key: 'jobName',
    sortable: true,
    align: 'start'
  }
]

// 동일한 지원자의 지원 횟수 계산
const getApplicantCount = (applicantId) => {
  return applicantStore.filteredAndSortedApplicants.filter(
    item => item.applicantId === applicantId
  ).length;
};

// 동일한 지원자의 몇 번째 지원인지 계산
const getApplicantApplicationNumber = (currentItem) => {
  const sameApplicantApplications = applicantStore.filteredAndSortedApplicants
    .filter(item => item.applicantId === currentItem.applicantId)
    .sort((a, b) => new Date(a.createdAt) - new Date(b.createdAt));

  return sameApplicantApplications.findIndex(item =>
    item.uniqueKey === currentItem.uniqueKey
  ) + 1;
};

// 선택된 지원자들의 이름 목록
const getSelectedApplicantNames = () => {
  const selectedNames = selectedIds.value.map(selectedItem => selectedItem.name);
  return [...new Set(selectedNames)]; // 중복 제거
};

// 검색 결과에서 고유한 지원자 수 계산
const getUniqueApplicantCount = () => {
  const uniqueApplicantIds = new Set(
    applicantStore.filteredAndSortedApplicants.map(item => item.applicantId)
  );
  return uniqueApplicantIds.size;
};


const getStatusColor = (status) => {
  switch (status) {
    case 'PASSED_FINAL': return 'success'
    case 'FAILED': return 'error'
    case 'PASSED_DOCS': return 'info'
    case 'PASSED_INTERVIEW_1': return 'teal'
    case 'PASSED_INTERVIEW_2': return 'blue'
    case 'PASSED_PRACTICAL': return 'purple'
    case 'WAITING': return 'grey'
    default: return 'grey'
  }
}

const getStatusText = (status) => {
  switch (status) {
    case 'PASSED_FINAL': return '최종합격'
    case 'FAILED': return '불합격'
    case 'PASSED_DOCS': return '서류합격'
    case 'PASSED_INTERVIEW_1': return '1차합격'
    case 'PASSED_INTERVIEW_2': return '2차합격'
    case 'PASSED_PRACTICAL': return '실무합격'
    case 'WAITING': return '대기중'
    default: return '알 수 없음'
  }
}

const handleSearch = debounce((value) => {
  applicantStore.setSearchQuery(value)
}, 300)

const handleSort = (options) => {
  // Vuetify v-data-table의 options 객체에서 정렬 정보 추출
  if (options.sortBy && options.sortBy.length > 0) {
    applicantStore.setSort({
      sortBy: options.sortBy,
      sortDesc: options.sortDesc || [false] // 기본값 설정
    });
  } else {
    // 정렬 해제
    applicantStore.setSort({
      sortBy: [],
      sortDesc: []
    });
  }
}

const viewDetail = (item) => {
  // 지원자 기본 정보를 query parameter로 전달
  router.push({
    path: `/employment/applications/${item.applicationId}`,
    query: {
      name: item.name,
      email: item.email,
      phone: item.phone,
      birth: item.birth,
      address: item.address,
      status: item.status,
      jobName: item.jobName,
      applicantId: item.applicantId,
      profileUrl: item.profileUrl,
      createdAt: item.createdAt,
      recruitmentId: item.recruitmentId,
      updatedAt: item.updatedAt,
      introduceRatingResultId: item.introduceRatingResultId
    }
  })
}

const handleAssignClick = async () => {
  try {
    await jobtestListStore.fetchJobtests();
    jobtestModal.value = true;
  } catch (error) {
    console.error('실무 테스트 목록 로드 실패:', error);
    toast.error('실무 테스트 목록을 불러오는 데 실패했습니다.');
  }
};

const handleJobtestSelected = async (jobtest) => {
  jobtestModal.value = false;

  // selectedIds는 이제 전체 객체를 포함하므로 applicationId를 추출
  const dtoList = selectedIds.value.map(selectedItem => {
    // 추가된 applicationId 필드 사용
    return new ApplicationJobtestDTO(selectedItem.applicationId, jobtest.id);
  });

  try {
    await applicationJobtestStore.assignJobtest(dtoList);
    toast.success(`선택한 ${selectedIds.value.length}개 지원서에 실무테스트를 성공적으로 할당했습니다.`);
    selectedIds.value = []; // 할당 후 선택 초기화
  } catch (error) {
    console.error('실무테스트 할당 실패:', error);
    toast.error(applicationJobtestStore.errorMessage);
  }
};

// 검색어 초기화 함수
const clearSearch = () => {
  search.value = ''
  applicantStore.setSearchQuery('')
}

// 컴포넌트 마운트 시 데이터 로드
onMounted(async () => {
  await refreshList()
})

// 새로고침 함수
const refreshList = async () => {
  try {
    await applicantStore.fetchApplicantFullInfoList()
    search.value = ''
    applicantStore.setSearchQuery('')
    selectedIds.value = [] // 새로고침 시 선택 초기화
  } catch (error) {
    toast.error('지원자 목록을 불러오는데 실패했습니다.')
  }
}

onUnmounted(() => {
  // 상태 초기화
  applicantStore.resetState()
  // debounce 취소
  handleSearch.cancel()
})

</script>

<style scoped>
.v-data-table {
  margin-top: 20px;
}
</style>