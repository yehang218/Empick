<template>
  <div>
    <CareerHeader />

    <v-container>
      <v-row>
        <v-col cols="12">
          <div class="text-body-1 mb-4 text-lg">
            {{ filteredList.length }}개의 채용 공고가 있습니다.
          </div>

          <!-- 필터 영역 -->
          <v-row class="mb-6" dense>
            <v-col cols="12" sm="6" md="3">
              <v-select v-model="selectedCareer" :items="careerOptions" label="경력별" clearable dense outlined />
            </v-col>
            <v-col cols="12" sm="6" md="3">
              <v-select v-model="selectedJob" :items="jobOptions" label="직무별" clearable dense outlined />
            </v-col>
            <v-col cols="12" sm="6" md="3">
              <v-select v-model="selectedStatus" :items="statusOptions" label="채용 상태" clearable dense outlined />
            </v-col>
            <v-col cols="12" sm="6" md="3">
              <v-select v-model="selectedType" :items="employmentTypeOptions" label="유형" clearable dense outlined />
            </v-col>
          </v-row>

          <!-- 채용 카드 목록 -->
          <v-card v-for="(item, index) in paginatedData" :key="item.id" class="mb-4" @click="goToDetail(item.id)" hover style="cursor: pointer">
            <v-card-text>
              <div class="font-weight-medium mb-2 text-md">
                {{ item.departmentName }}에서 가족같이 지낼 인재를 찾습니다.
              </div>
              <div class="chip-row">
                <v-chip color="grey lighten-3">{{ getRecruitTypeLabel(item.recruitType) }}</v-chip>
                <v-chip color="grey lighten-3">{{ getStatusLabel(item.status) }}</v-chip>
                <v-chip color="grey lighten-3">{{ item.title }}</v-chip>
              </div>
              <div class="date-row mt-2">
                <span class="date-label">게시일</span>
                <span>{{ formatDate(item.startedAt) }}</span>
                <span class="date-label ml-4">마감일</span>
                <span>{{ formatDate(item.endedAt) }}</span>
              </div>
            </v-card-text>
          </v-card>

          <!-- 페이지네이션 -->
          <div class="text-center mt-4">
            <v-pagination v-model="page" :length="totalPages" circle />
          </div>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRecruitmentStore } from '@/stores/recruitmentStore'
import CareerHeader from '@/components/career/CareerHeader.vue'
import { useRouter } from 'vue-router'

const store = useRecruitmentStore()
const page = ref(1)
const perPage = 5
const router = useRouter()

onMounted(() => {
  store.loadRecruitmentList()
})

const selectedCareer = ref(null)
const selectedJob = ref(null)
const selectedStatus = ref(null)
const selectedType = ref(null)

const careerOptions = ['경력무관', '신입', '경력 2년 이상', '경력 3년 이상', '경력 5년 이상', '경력 7년 이상']
const jobOptions = computed(() => {
  // store.list에서 직무명(title)만 추출하여 중복 없이 옵션 제공
  const jobs = store.list.map(item => item.title)
  return [...new Set(jobs)]
})
// 상태 옵션: '게시', '종료'
const statusOptions = ['게시', '종료']
const employmentTypeOptions = computed(() => {
  // store.list에서 recruitType만 추출하여 중복 없이 옵션 제공
  const types = store.list.map(item => getRecruitTypeLabel(item.recruitType))
  return [...new Set(types)]
})

const filteredList = computed(() => {
  return store.list.filter(item => {
    return (
      (!selectedCareer.value || item.career === selectedCareer.value) &&
      (!selectedJob.value || item.title === selectedJob.value) &&
      (!selectedStatus.value || getStatusLabel(item.status) === selectedStatus.value) &&
      (!selectedType.value || getRecruitTypeLabel(item.recruitType) === selectedType.value)
    )
  })
})

const totalPages = computed(() => Math.ceil(filteredList.value.length / perPage))
const paginatedData = computed(() => {
  const start = (page.value - 1) * perPage
  return filteredList.value.slice(start, start + perPage)
})

const goToDetail = (id) => {
  router.push({ name: 'RecruitmentDetail', params: { id } })
}

// 상태 한글 변환: OPEN=게시, CLOSED=종료
const getStatusLabel = (status) => {
  switch (status) {
    case 'OPEN':
    case '채용 중':
      return '게시'
    case 'CLOSED':
    case '채용 종료':
      return '종료'
    default:
      return status
  }
}
// 고용 형태 한글 변환
const getRecruitTypeLabel = (type) => {
  switch (type) {
    case 'FULL_TIME':
      return '정규직'
    case 'PART_TIME':
      return '계약직'
    case 'INTERN':
      return '인턴'
    case '정규직':
    case '계약직':
    case '인턴':
      return type
    default:
      return type
  }
}
// 날짜 포맷 YYYY.MM.DD
const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  const d = new Date(dateStr)
  if (isNaN(d)) return '-'
  return `${d.getFullYear()}.${String(d.getMonth() + 1).padStart(2, '0')}.${String(d.getDate()).padStart(2, '0')}`
}
</script>

<style scoped>
.v-chip {
  font-size: 13px;
}

.chip-row {
  display: flex;
  flex-wrap: nowrap;
  gap: 8px;
  margin-bottom: 8px;
}

.date-row {
  font-size: 14px;
  color: #666;
  display: flex;
  align-items: center;
  gap: 8px;
}
.date-label {
  font-weight: 500;
  color: #333;
}
.text-md {
  font-size: 16px;
}

.text-lg {
  font-size: 17px;
  font-weight: 500;
}
</style>
