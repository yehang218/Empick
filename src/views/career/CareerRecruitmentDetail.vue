<template>
  <div>
    <CareerHeader />
    <v-container class="custom-container">
      <v-row>
        <v-col cols="12" md="8">
          <h2 class="title">{{ recruitment.title }}</h2>
          <p class="mb-4 intro-text">{{ recruitment.content }}</p>

          <h3 class="subtitle">주요 업무</h3>
          <ul>
            <li v-for="(task, idx) in (request.responsibility ? request.responsibility.split('\n') : [])" :key="idx">{{ task }}</li>
          </ul>

          <h3 class="subtitle">자격 요건</h3>
          <ul>
            <li v-for="(qual, idx) in (request.qualification ? request.qualification.split('\n') : [])" :key="idx">{{ qual }}</li>
          </ul>

          <h3 class="subtitle">우대 사항</h3>
          <ul>
            <li v-for="(plus, idx) in (request.preference ? request.preference.split('\n') : [])" :key="idx">{{ plus }}</li>
          </ul>

          <h3 class="subtitle">전형 절차</h3>
          <div v-if="processList.length">
            <ol style="display: flex; gap: 12px; padding-left: 0; list-style: none;">
              <li v-for="(step, idx) in processList" :key="step.id">
                <span>{{ getStepTypeLabel(step.stepType) }}</span>
                <span v-if="idx < processList.length - 1">→</span>
              </li>
            </ol>
          </div>
          <div v-else>전형 절차 정보가 없습니다.</div>
        </v-col>

        <v-col cols="12" md="4">
          <v-card class="pa-4 summary-card">
            <div class="info-box">
              <div><strong>직군</strong><br />{{ request.jobName }}</div>
              <v-divider class="my-2" />
              <div><strong>인원</strong><br />{{ request.headcount }}</div>
              <v-divider class="my-2" />
              <div><strong>고용형태</strong><br />{{ request.employmentType }}</div>
              <v-divider class="my-2" />
              <div><strong>근무지</strong><br />{{ request.workLocation }}</div>
            </div>
            <v-btn block class="mt-4 apply-btn" color="success" @click="goToApplyPage">
              지원하기
            </v-btn>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
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
</script>

<style scoped>
.custom-container {
  max-width: 1240px;
}
.title {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 24px;
}
.subtitle {
  font-size: 20px;
  font-weight: 600;
  margin-top: 28px;
  margin-bottom: 14px;
}
.intro-text {
  font-size: 16px;
  color: #444;
  margin-bottom: 20px;
}
.info-box > div {
  margin-bottom: 10px;
  font-size: 15px;
  line-height: 1.6;
}
.summary-card {
  background-color: #f3f9f3;
  border-radius: 10px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.08);
}
.apply-btn {
  font-weight: bold;
  border-radius: 999px;
  font-size: 15px;
  height: 44px;
}
ul {
  padding-left: 18px;
  line-height: 1.8;
  font-size: 16px;
  color: #333;
}
</style>
