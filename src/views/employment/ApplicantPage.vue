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
      <v-data-table :headers="tableHeaders" :items="filteredApplicants" :items-per-page="8" item-value="id"
        class="elevation-1" show-headers>
        <!-- 체크 박스 -->
        <template #item.select="{ item }">
          <v-btn size="small" icon :color="selectedIds.includes(item.id) ? 'primary' : 'grey-lighten-1'" variant="tonal"
            @click="toggleSelection(item.id)">
            <v-icon>
              {{ selectedIds.includes(item.id) ? 'mdi-checkbox-marked' : 'mdi-checkbox-blank-outline' }}
            </v-icon>
          </v-btn>
        </template>

        <!-- 처리 상태 칩 -->
        <template #item.status="{ item }">
          <v-chip :color="getStatusColor(item.status)" variant="tonal" size="small">
            {{ item.status }}
          </v-chip>
        </template>

        <!-- 지원서 확인 텍스트 버튼 -->
        <template #item.actions="{ item }">
          <v-btn color="primary" variant="text" size="small" @click="viewDetail(item)">
            지원서 확인
          </v-btn>
        </template>
      </v-data-table>
    </v-card>

    <!-- 실무 테스트 선택 모달 -->
    <JobtestSelectModal v-model="jobtestModal" :jobtests="jobtestListStore.jobtests" @select="handleJobtestSelected" />
  </v-container>
</template>

<script setup>
import { ref, computed } from 'vue'
import Search from '@/components/common/Search.vue'
import { useToast } from 'vue-toastification';

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

const search = ref('')

const dummyApplicants = ref([
  { id: 1, name: '김철수', email: 'kim@example.com', birth: '1995-01-01', phone: '010-1234-5678', status: '최종합격', recruitment: '백엔드 개발자' },
  { id: 2, name: '이영희', email: 'lee@example.com', birth: '1996-03-10', phone: '010-9876-5432', status: '불합격', recruitment: '프론트 개발자' },
  { id: 3, name: '박지민', email: 'jimin@example.com', birth: '1994-11-23', phone: '010-5678-1234', status: '서류합격', recruitment: 'AI 연구원' },
  { id: 4, name: '최유리', email: 'yuri@example.com', birth: '1998-02-05', phone: '010-4567-9876', status: '1차합격', recruitment: '마케터' },
  { id: 5, name: '장하늘', email: 'sky@example.com', birth: '1997-05-16', phone: '010-1111-2222', status: '불합격', recruitment: '백엔드 개발자' },
  { id: 6, name: '한예린', email: 'yerin@example.com', birth: '1995-08-08', phone: '010-3333-4444', status: '서류합격', recruitment: '데이터 분석가' },
  { id: 7, name: '김진우', email: 'jinu@example.com', birth: '1996-06-17', phone: '010-5555-6666', status: '2차합격', recruitment: 'AI 연구원' },
  { id: 8, name: '윤서희', email: 'seohee@example.com', birth: '1994-09-21', phone: '010-7777-8888', status: '1차합격', recruitment: '프론트 개발자' },
  { id: 9, name: '이도윤', email: 'doyoon@example.com', birth: '1999-12-12', phone: '010-9999-0000', status: '서류합격', recruitment: '백엔드 개발자' }
])

const tableHeaders = [
  { text: '', value: 'select', sortable: false, width: 48 },   // 체크박스
  { text: '이름', value: 'name', sortable: true },
  { text: '이메일', value: 'email', sortable: true },
  { text: '생년월일', value: 'birth', sortable: true },
  { text: '전화번호', value: 'phone', sortable: true },
  { text: '지원서', value: 'actions', sortable: false },
  { text: '처리 상태', value: 'status', sortable: true },
  { text: '채용 공고 제목', value: 'recruitment', sortable: true }
]

const getStatusColor = (status) => {
  switch (status) {
    case '최종합격': return 'success'
    case '불합격': return 'error'
    case '서류합격': return 'info'
    case '1차합격': return 'teal'
    case '2차합격': return 'blue'
    default: return 'grey'
  }
}

const filteredApplicants = computed(() => {
  if (!search.value) return dummyApplicants.value
  return dummyApplicants.value.filter(applicant =>
    Object.values(applicant).some(val =>
      String(val).toLowerCase().includes(search.value.toLowerCase())
    )
  )
})

const viewDetail = (item) => {
  console.log('지원자 상세:', item)
}

const toggleSelection = (id) => {
  // 🚩 지원서 id로 수정해야 함(현재 지원자 id로 하는 중)
  const idx = selectedIds.value.indexOf(id);
  if (idx > -1) {
    selectedIds.value.splice(idx, 1);
  } else {
    selectedIds.value.push(id);
  }
};

const handleAssignClick = async () => {
  try {
    await jobtestListStore.fetchJobtests();
    jobtestModal.value = true;
  } catch (e) {
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
  } catch (e) {
    toast.error(applicationJobtestStore.errorMessage);
  }
};

</script>

<style scoped>
.v-data-table {
  margin-top: 20px;
}
</style>
