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
            <v-col cols="12" sm="6" md="4">
              <v-select v-model="selectedJob" :items="jobOptions" label="직무별" clearable dense outlined />
            </v-col>
            <v-col cols="12" sm="6" md="4">
              <v-select v-model="selectedStatus" :items="statusOptions" label="채용 상태" clearable dense outlined />
            </v-col>
            <v-col cols="12" sm="6" md="4">
              <v-select v-model="selectedType" :items="employmentTypeOptions" label="유형" clearable dense outlined />
            </v-col>
          </v-row>

          <!-- 채용 카드 목록 -->
          <v-card v-for="(item, index) in paginatedData" :key="item.id" class="mb-4" @click="goToDetail(item.id)" hover style="cursor: pointer">
            <v-card-text>
              <div class="font-weight-medium mb-2 text-md">
                {{ item.title }}
              </div>
              <div class="chip-row">
                <v-chip color="grey lighten-3">{{ getRecruitTypeLabel(item.recruitType) }}</v-chip>
                <v-chip color="grey lighten-3">{{ getStatusLabel(item.status) }}</v-chip>
                <v-chip color="grey lighten-3">{{ item.title }}</v-chip>
                <v-chip v-if="item.jobName" color="grey lighten-3">{{ item.jobName }}</v-chip>
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
import { getRecruitTypeLabel, recruitTypeOptions } from '@/constants/employment/recruitTypes'

const store = useRecruitmentStore()
const page = ref(1)
const perPage = 5
const router = useRouter()

onMounted(() => {
  store.loadRecruitmentList()
})

const selectedJob = ref(null)
const selectedStatus = ref(null)
const selectedType = ref(null)

const jobOptions = computed(() => {
  const jobs = store.list.map(item => item.jobName || item.title)
  return [...new Set(jobs)]
})
const statusOptions = ['게시', '종료']
const employmentTypeOptions = recruitTypeOptions.map(opt => opt.label)

const filteredList = computed(() => {
  return store.list.filter(item => {
    return (
      // WAITING 상태의 채용 공고는 제외
      item.status !== 'WAITING' &&
      (!selectedJob.value || (item.jobName || item.title) === selectedJob.value) &&
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

const getStatusLabel = (status) => {
  switch (status) {
    case 'OPEN':
    case '채용 중':
    case 'PUBLISHED':
      return '게시'
    case 'CLOSED':
    case '채용 종료':
      return '종료'
    default:
      return status
  }
}

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
