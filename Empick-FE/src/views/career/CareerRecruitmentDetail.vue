<template>
  <div>
    <CareerHeader />
    <div class="detail-container">
      <div class="content-wrapper">
        <div class="main-content">
          <div class="job-header">
            <h1 class="job-title">{{ recruitment.title }}</h1>
            <div class="job-meta">
              <span class="meta-item">
                <v-icon size="small">mdi-calendar</v-icon>
                {{ formatDate(recruitment.startedAt) }} ~ {{ formatDate(recruitment.endedAt) }}
              </span>
            </div>
          </div>

          <div class="content-section">
            <div class="section-content" v-html="recruitment.content"></div>
          </div>

          <div class="content-section">
            <h2 class="section-title">
              <v-icon class="section-icon">mdi-briefcase</v-icon>
              주요 업무
            </h2>
            <div class="section-content">
              <ul class="task-list">
                <li v-for="(task, idx) in (request.responsibility ? request.responsibility.split('\n') : [])"
                  :key="idx">{{ task }}</li>
              </ul>
            </div>
          </div>

          <div class="content-section">
            <h2 class="section-title">
              <v-icon class="section-icon">mdi-account-check</v-icon>
              자격 요건
            </h2>
            <div class="section-content">
              <ul class="task-list">
                <li v-for="(qual, idx) in (request.qualification ? request.qualification.split('\n') : [])" :key="idx">
                  {{ qual }}</li>
              </ul>
            </div>
          </div>

          <div class="content-section">
            <h2 class="section-title">
              <v-icon class="section-icon">mdi-star</v-icon>
              우대 사항
            </h2>
            <div class="section-content">
              <ul class="task-list">
                <li v-for="(plus, idx) in (request.preference ? request.preference.split('\n') : [])" :key="idx">{{ plus
                  }}</li>
              </ul>
            </div>
          </div>

          <div class="content-section">
            <h2 class="section-title">
              <v-icon class="section-icon">mdi-format-list-numbered</v-icon>
              전형 절차
            </h2>
            <div class="section-content">
              <div v-if="processList.length" class="process-flow">
                <div v-for="(step, idx) in processList" :key="step.id" class="process-step">
                  <div class="step-number">{{ idx + 1 }}</div>
                  <div class="step-content">
                    <div class="step-title">{{ getStepTypeLabel(step.stepType) }}</div>
                  </div>
                  <div v-if="idx < processList.length - 1" class="step-arrow">
                    <v-icon>mdi-arrow-right</v-icon>
                  </div>
                </div>
              </div>
              <div v-else class="no-process">전형 절차 정보가 없습니다.</div>
            </div>
          </div>
        </div>

        <div class="sidebar">
          <div class="summary-card">
            <div class="card-header">
              <h3 class="card-title">채용 정보</h3>
            </div>
            <div class="info-list">
              <div class="info-item">
                <div class="info-label">직군</div>
                <div class="info-value">{{ request.jobName }}</div>
              </div>
              <div class="info-item">
                <div class="info-label">인원</div>
                <div class="info-value">{{ request.headcount }}명</div>
              </div>
              <div class="info-item">
                <div class="info-label">고용형태</div>
                <div class="info-value">{{ request.employmentType }}</div>
              </div>
              <div class="info-item">
                <div class="info-label">근무지</div>
                <div class="info-value">{{ request.workLocation }}</div>
              </div>
            </div>
            <v-btn block class="apply-button" color="primary" size="large" @click="goToApplyPage">
              <v-icon left>mdi-send</v-icon>
              지원하기
            </v-btn>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useRecruitmentStore } from '@/stores/recruitmentStore'
import { useRecruitmentRequestStore } from '@/stores/recruitmentRequestStore'
import { useRecruitmentProcessStore } from '@/stores/recruitmentProcessStore'
import CareerHeader from '@/components/career/CareerHeader.vue'

const route = useRoute()
const router = useRouter()
const id = Number(route.params.id)

const recruitmentStore = useRecruitmentStore()
const requestStore = useRecruitmentRequestStore()
const processStore = useRecruitmentProcessStore()

const recruitment = computed(() => recruitmentStore.detail?.recruitment || {})
const request = computed(() => requestStore.recruitmentRequestDetail || {})
const processList = computed(() => processStore.processList || [])

const fetchRequestDetail = async () => {
  const reqId = recruitment.value.recruitmentRequestId
  if (reqId) {
    await requestStore.loadRecruitmentRequestDetail(reqId)
  } else if (recruitment.value.id) {
    await requestStore.loadRecruitmentRequestDetail(recruitment.value.id)
  }
}

const fetchProcessList = async () => {
  if (recruitment.value.id) {
    await processStore.loadProcesses(recruitment.value.id)
  }
}

onMounted(async () => {
  await recruitmentStore.loadRecruitmentDetail(id)
  await fetchRequestDetail()
  await fetchProcessList()
})

watch(
  () => recruitment.value.recruitmentRequestId,
  async (newVal, oldVal) => {
    if (newVal && newVal !== oldVal) {
      await requestStore.loadRecruitmentRequestDetail(newVal)
    }
  }
)

// stepType 한글 변환
const getStepTypeLabel = (type) => {
  switch (type) {
    case 'DOCUMENT': return '서류'
    case 'PRACTICAL': return '실무'
    case 'INTERVIEW': return '면접'
    default: return type
  }
}

const goToApplyPage = () => {
  router.push(`/career/recruitments/${id}/apply`)
}

const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  const d = new Date(dateStr)
  if (isNaN(d)) return '-'
  return `${d.getFullYear()}.${String(d.getMonth() + 1).padStart(2, '0')}.${String(d.getDate()).padStart(2, '0')}`
}
</script>

<style scoped>
.detail-container {
  max-width: none;
  margin: 0;
  padding: 60px 0;
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  min-height: 100vh;
}

.content-wrapper {
  display: grid;
  grid-template-columns: 1fr 320px;
  gap: 32px;
  align-items: start;
  margin: 0 24px;
}

.main-content {
  background: white;
  border-radius: 16px;
  padding: 40px;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
}

.job-header {
  margin-bottom: 40px;
  padding-bottom: 24px;
  border-bottom: 2px solid #f1f5f9;
}

.job-title {
  font-size: 32px;
  font-weight: 800;
  color: #1e293b;
  margin-bottom: 16px;
  line-height: 1.2;
  letter-spacing: -0.025em;
}

.job-meta {
  display: flex;
  align-items: center;
  gap: 16px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #64748b;
  font-size: 14px;
  font-weight: 500;
}

.content-section {
  margin-bottom: 40px;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 24px;
  font-weight: 700;
  color: #1e293b;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 1px solid #e2e8f0;
}

.section-icon {
  color: #3b82f6;
}

.section-content {
  font-size: 16px;
  line-height: 1.7;
  color: #374151;
}

.task-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.task-list li {
  position: relative;
  padding: 12px 0 12px 24px;
  border-bottom: 1px solid #f1f5f9;
}

.task-list li:last-child {
  border-bottom: none;
}

.task-list li::before {
  content: '';
  position: absolute;
  left: 0;
  top: 20px;
  width: 6px;
  height: 6px;
  background: #3b82f6;
  border-radius: 50%;
}

.process-flow {
  display: flex;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
}

.process-step {
  display: flex;
  align-items: center;
  gap: 12px;
}

.step-number {
  width: 32px;
  height: 32px;
  background: linear-gradient(135deg, #3b82f6, #8b5cf6);
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 14px;
}

.step-content {
  background: #f8fafc;
  padding: 8px 16px;
  border-radius: 20px;
  border: 1px solid #e2e8f0;
}

.step-title {
  font-weight: 600;
  color: #374151;
  font-size: 14px;
}

.step-arrow {
  color: #94a3b8;
}

.no-process {
  color: #64748b;
  font-style: italic;
  padding: 20px;
  text-align: center;
  background: #f8fafc;
  border-radius: 8px;
}

.sidebar {
  position: sticky;
  top: 24px;
}

.summary-card {
  background: white;
  border-radius: 16px;
  padding: 32px;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
  border: 1px solid #e2e8f0;
}

.card-header {
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 2px solid #f1f5f9;
}

.card-title {
  font-size: 20px;
  font-weight: 700;
  color: #1e293b;
  margin: 0;
}

.info-list {
  margin-bottom: 32px;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 0;
  border-bottom: 1px solid #f1f5f9;
}

.info-item:last-child {
  border-bottom: none;
}

.info-label {
  font-weight: 600;
  color: #64748b;
  font-size: 14px;
}

.info-value {
  font-weight: 700;
  color: #1e293b;
  font-size: 15px;
}

.apply-button {
  background: linear-gradient(135deg, #3b82f6, #8b5cf6) !important;
  border-radius: 12px !important;
  font-weight: 700 !important;
  font-size: 16px !important;
  height: 56px !important;
  text-transform: none !important;
  box-shadow: 0 4px 14px 0 rgba(59, 130, 246, 0.4) !important;
  transition: all 0.3s ease !important;
}

.apply-button:hover {
  transform: translateY(-2px) !important;
  box-shadow: 0 8px 25px 0 rgba(59, 130, 246, 0.5) !important;
}

/* 반응형 디자인 */
@media (max-width: 1024px) {
  .content-wrapper {
    grid-template-columns: 1fr;
    gap: 24px;
    margin: 0 24px;
  }

  .sidebar {
    position: static;
  }
}

@media (max-width: 768px) {
  .detail-container {
    padding: 40px 0;
  }

  .content-wrapper {
    margin: 0 16px;
  }

  .main-content {
    padding: 24px 20px;
  }

  .job-title {
    font-size: 28px;
  }

  .section-title {
    font-size: 20px;
  }

  .summary-card {
    padding: 24px 20px;
  }
}

@media (max-width: 480px) {
  .detail-container {
    padding: 32px 0;
  }

  .content-wrapper {
    margin: 0 12px;
  }

  .main-content {
    padding: 20px 16px;
  }

  .job-title {
    font-size: 24px;
  }

  .process-flow {
    flex-direction: column;
    align-items: flex-start;
  }

  .process-step {
    width: 100%;
  }

  .step-arrow {
    transform: rotate(90deg);
  }
}
</style>
