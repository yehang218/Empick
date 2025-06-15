<template>
  <v-container fluid>
    <v-card>
      <v-card-title class="d-flex justify-between align-center flex-wrap">
        <span class="text-h6 font-weight-bold">지원자 목록</span>

        <v-spacer />

        <div class="d-flex align-center flex-wrap" style="gap: 8px;">
          <!-- 검색창 (공통 컴포넌트) -->
          <Search v-model="search" />

          <!-- 사원 등록 버튼 -->
          <v-btn color="primary" variant="tonal" size="small" style="min-width: 90px">
            사원 등록
          </v-btn>

          <!-- 문제 할당 버튼 -->
          <v-btn color="secondary" variant="tonal" size="small" style="min-width: 90px">
            문제 할당
          </v-btn>

          <!-- 이메일 전송 버튼 -->
          <v-btn color="success" variant="outlined" size="small" prepend-icon="mdi-email" style="min-width: 110px">
            이메일 전송
          </v-btn>
        </div>
      </v-card-title>

      <v-data-table
        :headers="headers"
        :items="filteredApplicants"
        :items-per-page="8"
        class="elevation-1"
      >
        <!-- 상태 컬럼 -->
        <template #item.status="{ item }">
          <v-chip :color="getStatusColor(item.status)" variant="tonal" size="small">
            {{ item.status }}
          </v-chip>
        </template>

        <!-- 지원서 보기 버튼 -->
        <template #item.actions="{ item }">
          <v-btn icon size="small" @click="viewDetail(item)">
            <v-icon>mdi-arrow-right</v-icon>
          </v-btn>
        </template>
      </v-data-table>
    </v-card>
  </v-container>
</template>

<script setup>
import { ref, computed } from 'vue'
import Search from '@/components/common/Search.vue'

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
  { id: 9, name: '이도윤', email: 'doyoon@example.com', birth: '1999-12-12', phone: '010-9999-0000', status: '서류합격', recruitment: '백엔드 개발자' },
])

const headers = [
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
</script>

<style scoped>
.v-data-table {
  margin-top: 20px;
}
</style>
