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
              <v-select v-model="selectedType" :items="employmentTypeOptions" label="고용 형태" clearable dense outlined />
            </v-col>
          </v-row>

          <!-- 채용 카드 목록 -->
          <v-card v-for="(item, index) in paginatedData" :key="index" class="mb-4">
            <v-card-text>
              <div class="font-weight-medium mb-2 text-md">
                {{ item.department }}에서 가족같이 지낼 인재를 찾습니다.
              </div>
              <div class="chip-row">
                <v-chip color="grey lighten-3">{{ item.career }}</v-chip>
                <v-chip color="grey lighten-3">{{ item.employmentType }}</v-chip>
                <v-chip color="grey lighten-3">{{ item.job }}</v-chip>
                <v-chip
                  :color="item.status === '채용 중' ? 'green lighten-4' : 'red lighten-4'"
                  :text-color="item.status === '채용 중' ? 'green darken-2' : 'red darken-2'"
                  small
                >
                  {{ item.status }}
                </v-chip>
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
import { ref, computed } from 'vue'
import CareerHeader from '@/components/career/CareerHeader.vue'

const page = ref(1)
const perPage = 5

const recruitmentList = ref([
  { department: '인사팀', career: '경력 7년 이상', employmentType: '정규직', job: '퍼포먼스 마케터', status: '채용 중' },
  { department: '경영팀', career: '경력무관', employmentType: '계약직', job: '백엔드 개발자', status: '채용 종료' },
  { department: '식당팀', career: '경력 5년 이상', employmentType: '정규직', job: '영업 담당자', status: '채용 중' },
  { department: '물류팀', career: '경력무관', employmentType: '계약직', job: '퍼포먼스 마케터', status: '채용 중' },
  { department: '신발팀', career: '경력무관', employmentType: '정규직', job: '서비스 개발자', status: '채용 중' },
  { department: '디자인팀', career: '경력 3년 이상', employmentType: '계약직', job: 'UX 디자이너', status: '채용 중' },
  { department: '총무팀', career: '경력무관', employmentType: '정규직', job: '총무 담당자', status: '채용 중' },
  { department: 'R&D팀', career: '경력 2년 이상', employmentType: '정규직', job: '제품 연구원', status: '채용 종료' },
  { department: 'QA팀', career: '신입', employmentType: '정규직', job: '테스트 엔지니어', status: '채용 중' },
  { department: '회계팀', career: '경력무관', employmentType: '계약직', job: '회계 보조', status: '채용 중' }
])

const selectedCareer = ref(null)
const selectedJob = ref(null)
const selectedStatus = ref(null)
const selectedType = ref(null)

const careerOptions = ['경력무관', '신입', '경력 2년 이상', '경력 3년 이상', '경력 5년 이상', '경력 7년 이상']
const jobOptions = [
  '퍼포먼스 마케터', '백엔드 개발자', '영업 담당자', '서비스 개발자',
  'UX 디자이너', '총무 담당자', '제품 연구원', '테스트 엔지니어', '회계 보조'
]
const statusOptions = ['채용 중', '채용 종료']
const employmentTypeOptions = ['정규직', '계약직']

const filteredList = computed(() => {
  return recruitmentList.value.filter(item => {
    return (
      (!selectedCareer.value || item.career === selectedCareer.value) &&
      (!selectedJob.value || item.job === selectedJob.value) &&
      (!selectedStatus.value || item.status === selectedStatus.value) &&
      (!selectedType.value || item.employmentType === selectedType.value)
    )
  })
})

const totalPages = computed(() => Math.ceil(filteredList.value.length / perPage))

const paginatedData = computed(() => {
  const start = (page.value - 1) * perPage
  return filteredList.value.slice(start, start + perPage)
})
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

.text-md {
  font-size: 16px;
}

.text-lg {
  font-size: 17px;
  font-weight: 500;
}
</style>
