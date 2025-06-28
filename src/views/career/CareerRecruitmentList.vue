<template>
  <div>
    <CareerHeader />

    <div class="recruitment-container">
      <div class="page-header">
        <h1 class="page-title">채용 공고</h1>
        <p class="page-subtitle">EMPICK과 함께 성장할 인재를 찾습니다</p>
      </div>

      <div class="content-wrapper">
        <div class="stats-bar">
          <span class="stats-text">{{ filteredList.length }}개의 채용 공고가 있습니다.</span>
        </div>

        <!-- 필터 영역 -->
        <div class="filter-section">
          <v-row dense>
            <v-col cols="12" sm="6" md="4">
              <v-select v-model="selectedJob" :items="jobOptions" label="직무별" clearable dense outlined
                class="filter-select" prepend-inner-icon="mdi-briefcase" />
            </v-col>
            <v-col cols="12" sm="6" md="4">
              <v-select v-model="selectedStatus" :items="statusOptions" label="채용 상태" clearable dense outlined
                class="filter-select" prepend-inner-icon="mdi-calendar-check" />
            </v-col>
            <v-col cols="12" sm="6" md="4">
              <v-select v-model="selectedType" :items="employmentTypeOptions" label="고용형태" clearable dense outlined
                class="filter-select" prepend-inner-icon="mdi-account-multiple" />
            </v-col>
          </v-row>
        </div>

        <!-- 채용 카드 목록 -->
        <div class="recruitment-list">
          <div v-for="(item, index) in paginatedData" :key="item.id" class="recruitment-card"
            @click="goToDetail(item.id)">
            <div class="card-header">
              <h3 class="job-title">{{ item.title }}</h3>
              <div class="status-badge" :class="getStatusClass(item.status)">
                {{ getStatusLabel(item.status) }}
              </div>
            </div>

            <div class="card-tags">
              <span class="tag">{{ getRecruitTypeLabel(item.recruitType) }}</span>
              <span v-if="item.jobName" class="tag">{{ item.jobName }}</span>
            </div>

            <div class="card-dates">
              <div class="date-item">
                <v-icon size="small" class="date-icon">mdi-calendar-plus</v-icon>
                <span class="date-label">게시일</span>
                <span class="date-value">{{ formatDate(item.startedAt) }}</span>
              </div>
              <div class="date-item">
                <v-icon size="small" class="date-icon">mdi-calendar-remove</v-icon>
                <span class="date-label">마감일</span>
                <span class="date-value">{{ formatDate(item.endedAt) }}</span>
              </div>
            </div>

            <div class="card-arrow">
              <v-icon>mdi-chevron-right</v-icon>
            </div>
          </div>
        </div>

        <!-- 페이지네이션 -->
        <div class="pagination-wrapper" v-if="totalPages > 1">
          <v-pagination v-model="page" :length="totalPages" :total-visible="7" class="custom-pagination" />
        </div>
      </div>
    </div>
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

const getStatusClass = (status) => {
  switch (status) {
    case 'OPEN':
    case '채용 중':
    case 'PUBLISHED':
      return 'active'
    case 'CLOSED':
    case '채용 종료':
      return 'closed'
    default:
      return ''
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
.recruitment-container {
  max-width: none;
  margin: 0;
  padding: 60px 0;
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  min-height: 100vh;
}

.page-header {
  text-align: center;
  margin-bottom: 48px;
  padding: 0 24px;
}

.page-title {
  font-size: 42px;
  font-weight: 800;
  color: #1e293b;
  margin-bottom: 16px;
  letter-spacing: -0.025em;
}

.page-subtitle {
  font-size: 18px;
  color: #64748b;
  line-height: 1.6;
}

.content-wrapper {
  background: white;
  border-radius: 16px;
  padding: 32px;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
  margin: 0 24px;
}

.stats-bar {
  background: linear-gradient(135deg, #3b82f6, #8b5cf6);
  color: white;
  padding: 16px 20px;
  border-radius: 12px;
  margin-bottom: 32px;
}

.stats-text {
  font-size: 16px;
  font-weight: 600;
}

.filter-section {
  margin-bottom: 32px;
  padding: 24px;
  background: #f8fafc;
  border-radius: 12px;
  border: 1px solid #e2e8f0;
}

.filter-select {
  background: white;
}

.recruitment-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.recruitment-card {
  background: white;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  padding: 24px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
}

.recruitment-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: linear-gradient(90deg, #3b82f6, #8b5cf6);
  transform: scaleX(0);
  transition: transform 0.3s ease;
}

.recruitment-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
  border-color: #3b82f6;
}

.recruitment-card:hover::before {
  transform: scaleX(1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16px;
}

.job-title {
  font-size: 20px;
  font-weight: 700;
  color: #1e293b;
  margin: 0;
  line-height: 1.3;
  flex: 1;
  margin-right: 16px;
}

.status-badge {
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  white-space: nowrap;
}

.status-badge.active {
  background: #dcfce7;
  color: #166534;
}

.status-badge.closed {
  background: #fee2e2;
  color: #991b1b;
}

.card-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 16px;
}

.tag {
  background: #f1f5f9;
  color: #475569;
  padding: 4px 12px;
  border-radius: 16px;
  font-size: 13px;
  font-weight: 500;
}

.card-dates {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.date-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #64748b;
}

.date-icon {
  color: #94a3b8;
}

.date-label {
  font-weight: 500;
  min-width: 50px;
}

.date-value {
  color: #374151;
  font-weight: 600;
}

.card-arrow {
  position: absolute;
  top: 50%;
  right: 24px;
  transform: translateY(-50%);
  color: #94a3b8;
  transition: all 0.3s ease;
}

.recruitment-card:hover .card-arrow {
  color: #3b82f6;
  transform: translateY(-50%) translateX(4px);
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 40px;
  padding-top: 32px;
  border-top: 1px solid #e2e8f0;
}

.custom-pagination {
  --v-pagination-item-border-radius: 8px;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .recruitment-container {
    padding: 40px 0;
  }

  .page-header {
    padding: 0 16px;
  }

  .content-wrapper {
    margin: 0 16px;
    padding: 24px 20px;
  }

  .page-title {
    font-size: 32px;
  }

  .page-subtitle {
    font-size: 16px;
  }

  .filter-section {
    padding: 20px;
  }

  .recruitment-card {
    padding: 20px;
  }

  .job-title {
    font-size: 18px;
  }

  .card-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .status-badge {
    align-self: flex-start;
  }
}

@media (max-width: 480px) {
  .recruitment-container {
    padding: 32px 0;
  }

  .page-header {
    padding: 0 12px;
  }

  .content-wrapper {
    margin: 0 12px;
    padding: 20px 16px;
  }

  .page-title {
    font-size: 28px;
  }

  .recruitment-card {
    padding: 16px;
  }

  .card-arrow {
    right: 16px;
  }
}
</style>
