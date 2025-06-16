<template>
  <v-container fluid style="margin-top: 70px;">
    <v-card>
      <!-- 상단 타이틀 + 검색/버튼 영역 -->
      <v-card-title class="d-flex justify-between align-center flex-wrap">
        <span class="text-h6 font-weight-bold">지원자 목록</span>

        <v-spacer />

        <div class="d-flex align-center flex-wrap" style="gap: 8px;">
          <!-- 🔍 검색창 (공통 컴포넌트) -->
          <Search v-model="search" />

          <!-- 👤 사원 등록 버튼 -->
          <v-btn color="primary" variant="tonal" size="small" style="min-width: 90px">
            사원 등록
          </v-btn>

          <!-- 📝 문제 할당 버튼 -->
          <v-btn color="secondary" variant="tonal" size="small" style="min-width: 90px" @click="handleAssignClick"
            :disabled="!selectedIds.length">
            실무테스트 할당
          </v-btn>

          <!-- 📧 이메일 전송 버튼 -->
          <v-btn color="success" variant="outlined" size="small" prepend-icon="mdi-email" style="min-width: 110px">
            이메일 전송
          </v-btn>
        </div>
      </v-card-title>

      <!-- 📋 지원자 테이블 -->
      <v-data-table :headers="tableHeaders" :items="filteredApplicants" :items-per-page="8" item-value="applicantId"
        class="elevation-1" v-model:selected="selectedIds">
        <!-- 이름 -->
        <template #item.name="{ item }">
          {{ item.name || '-' }}
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
import { ref, computed, onMounted } from 'vue'
import Search from '@/components/common/Search.vue'
import { useToast } from 'vue-toastification';
import { useApplicantStore } from '@/stores/applicantStore';

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

const search = ref('')

const tableHeaders = [
  {
    title: '',
    key: 'data-table-select',
    sortable: false,
    width: '48px',
    align: 'center'
  },
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

const filteredApplicants = computed(() => {
  if (!search.value) return applicantStore.applicantList
  return applicantStore.applicantList.filter(applicant =>
    Object.values(applicant).some(val =>
      val && String(val).toLowerCase().includes(search.value.toLowerCase())
    )
  )
})

const viewDetail = (item) => {
  console.log('지원자 상세:', item)
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

  const dtoList = selectedIds.value.map(appId => {
    return new ApplicationJobtestDTO(appId, jobtest.id);
  });

  try {
    await applicationJobtestStore.assignJobtest(dtoList);
    toast.success('선택한 지원서에 실무테스트를 성공적으로 할당했습니다.');
  } catch (error) {
    console.error('실무테스트 할당 실패:', error);
    toast.error(applicationJobtestStore.errorMessage);
  }
};

// 컴포넌트 마운트 시 데이터 로드
onMounted(async () => {
  try {
    await applicantStore.fetchApplicantFullInfoList();
  } catch (error) {
    console.error('지원자 목록 로드 실패:', error);
    toast.error('지원자 목록을 불러오는 데 실패했습니다.');
  }
});

</script>

<style scoped>
.v-data-table {
  margin-top: 20px;
}
</style>
