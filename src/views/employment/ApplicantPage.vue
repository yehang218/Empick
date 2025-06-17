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

          <!-- 👤 사원 등록 버튼 (유지) -->
          <v-btn color="primary" variant="tonal" size="small" style="min-width: 90px">
            사원 등록
          </v-btn>

          <!-- ➕ 지원자 추가 버튼 (새로 추가) -->
          <v-btn color="primary" variant="tonal" size="small" style="min-width: 90px" @click="goToApplicantRegistration">
            지원자 추가
          </v-btn>

          <!-- 📝 문제 할당 버튼 -->
          <v-btn color="secondary" variant="tonal" size="small" style="min-width: 90px">
            문제 할당
          </v-btn>

          <!-- 📧 이메일 전송 버튼 -->
          <v-btn color="success" variant="outlined" size="small" prepend-icon="mdi-email" style="min-width: 110px">
            이메일 전송
          </v-btn>
        </div>
      </v-card-title>

      <!-- 📋 지원자 테이블 -->
      <v-data-table
        :headers="tableHeaders"
        :items="filteredApplicants"
        :items-per-page="8"
        item-value="id"
        show-select
        class="elevation-1"
        show-headers
      >
        <!-- 지원서 확인 텍스트 버튼 -->
        <template #item.actions="{ item }">
          <v-btn color="primary" variant="text" size="small" @click="viewDetail(item)">
            지원서 확인
          </v-btn>
        </template>
      </v-data-table>
    </v-card>
  </v-container>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import Search from '@/components/common/Search.vue'
import applicantService from '@/services/applicantService'

const search = ref('')
const router = useRouter()

const applicants = ref([])

const tableHeaders = [
  { text: '이름', value: 'name', sortable: true },
  { text: '이메일', value: 'email', sortable: true },
  { text: '생년월일', value: 'birth', sortable: true },
  { text: '전화번호', value: 'phone', sortable: true },
  { text: '주소', value: 'address', sortable: true },
  { text: '지원서', value: 'actions', sortable: false },
]

const filteredApplicants = computed(() => {
  if (!search.value) return applicants.value
  return applicants.value.filter(applicant =>
    Object.values(applicant).some(val =>
      String(val).toLowerCase().includes(search.value.toLowerCase())
    )
  )
})

const viewDetail = (item) => {
  console.log('지원자 상세:', item)
  router.push(`/employment/application/${item.id}`)
}

const goToApplicantRegistration = () => {
  router.push('/employment/applicants/register')
}

const loadApplicants = async () => {
  try {
    const response = await applicantService.fetchAllApplicants();
    if (response.isSuccess) {
      applicants.value = response.data.data;
      applicants.value.forEach(applicant => {
        if (applicant.birth) {
          applicant.birth = applicant.birth;
        }
      });
    } else {
      console.error(`지원자 목록 로드 실패: ${response.message}`);
      alert(`지원자 목록 로드 실패: ${response.message}`);
    }
  } catch (error) {
    console.error('지원자 목록 로드 중 오류 발생:', error);
    alert('지원자 목록 로드 중 오류가 발생했습니다. 다시 시도해주세요.');
  }
}

onMounted(() => {
  loadApplicants();
});
</script>

<style scoped>
.v-data-table {
  margin-top: 20px;
}
</style>
