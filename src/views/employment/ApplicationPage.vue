<template>
  <v-container fluid class="pa-6">
    <!-- 상단 헤더 -->
    <div class="page-header mb-6">
      <div class="d-flex align-center mb-4">
        <v-btn icon variant="text" @click="goBack" class="mr-3">
          <v-icon>mdi-arrow-left</v-icon>
        </v-btn>
        <div>
          <h1 class="text-h4 font-weight-bold">지원서 상세</h1>
          <p class="text-body-1 text-grey ma-0">{{ applicant?.name || '지원자' }}님의 지원서를 검토하세요</p>
        </div>
        <v-spacer />
        <div class="d-flex align-center gap-3">
          <v-chip :color="getStatusChipColor(applicant?.status)" variant="elevated" size="large" class="px-4">
            {{ getStatusText(applicant?.status) }}
          </v-chip>
          <v-btn color="primary" variant="elevated" prepend-icon="mdi-account-edit" @click="updateStatus">
            상태 변경
          </v-btn>
        </div>
      </div>
    </div>

    <v-row>
      <!-- 좌측: 지원자 정보 및 통계 -->
      <v-col cols="12" lg="5">
        <!-- 지원자 기본 정보 -->
        <v-card class="mb-6 modern-card">
          <v-card-title class="pb-2">
            <v-icon class="mr-2 text-primary">mdi-account-circle</v-icon>
            지원자 정보
          </v-card-title>
          <v-divider class="mb-4" />
          <v-card-text>
            <div class="d-flex align-start mb-4">
              <v-avatar size="80" class="mr-4">
                <v-img 
                  :src="getFullImageUrl(applicant?.profileUrl)" 
                  alt="프로필 사진"
                  @error="handleImageError"
                  @load="handleImageLoad"
                />
              </v-avatar>

              <div class="flex-grow-1">
                <h2 class="text-h5 font-weight-bold mb-1">{{ applicant?.name || '지원자' }}</h2>
                <p class="text-body-2 text-grey mb-2">{{ applicant?.jobName || '직무 정보 없음' }}</p>
                <v-chip size="small" color="blue" variant="tonal" v-if="applicant?.status">
                  {{ getStatusText(applicant.status) }}
                </v-chip>
              </div>
            </div>

            <v-list class="pa-0">
              <v-list-item class="px-0 py-1">
                <template #prepend>
                  <v-icon class="mr-3" size="small">mdi-calendar</v-icon>
                </template>
                <v-list-item-title class="text-body-2">
                  <span class="text-grey">생년월일</span>
                  <span class="ml-2 font-weight-medium">{{ formatDate(applicant?.birth) }}</span>
                </v-list-item-title>
              </v-list-item>

              <v-list-item class="px-0 py-1">
                <template #prepend>
                  <v-icon class="mr-3" size="small">mdi-phone</v-icon>
                </template>
                <v-list-item-title class="text-body-2">
                  <span class="text-grey">연락처</span>
                  <span class="ml-2 font-weight-medium">{{ applicant?.phone }}</span>
                </v-list-item-title>
              </v-list-item>

              <v-list-item class="px-0 py-1">
                <template #prepend>
                  <v-icon class="mr-3" size="small">mdi-email</v-icon>
                </template>
                <v-list-item-title class="text-body-2">
                  <span class="text-grey">이메일</span>
                  <span class="ml-2 font-weight-medium">{{ applicant?.email }}</span>
                </v-list-item-title>
              </v-list-item>

              <v-list-item class="px-0 py-1">
                <template #prepend>
                  <v-icon class="mr-3" size="small">mdi-map-marker</v-icon>
                </template>
                <v-list-item-title class="text-body-2">
                  <span class="text-grey">주소</span>
                  <span class="ml-2 font-weight-medium">{{ applicant?.address }}</span>
                </v-list-item-title>
              </v-list-item>

              <v-list-item class="px-0 py-1">
                <template #prepend>
                  <v-icon class="mr-3" size="small">mdi-calendar-plus</v-icon>
                </template>
                <v-list-item-title class="text-body-2">
                  <span class="text-grey">지원일자</span>
                  <span class="ml-2 font-weight-medium">{{ formatDate(applicant?.createdAt) }}</span>
                </v-list-item-title>
              </v-list-item>

              <v-list-item class="px-0 py-1" v-if="applicant?.education">
                <template #prepend>
                  <v-icon class="mr-3" size="small">mdi-school</v-icon>
                </template>
                <v-list-item-title class="text-body-2">
                  <span class="text-grey">학력</span>
                  <span class="ml-2 font-weight-medium">{{ applicant?.education }}</span>
                </v-list-item-title>
              </v-list-item>

              <v-list-item class="px-0 py-1" v-if="applicant?.portfolioUrl">
                <template #prepend>
                  <v-icon class="mr-3" size="small">mdi-briefcase</v-icon>
                </template>
                <v-list-item-title class="text-body-2">
                  <span class="text-grey">포트폴리오</span>
                  <a :href="applicant?.portfolioUrl" target="_blank" class="ml-2 font-weight-medium text-primary">
                    포트폴리오 보기
                  </a>
                </v-list-item-title>
              </v-list-item>
            </v-list>
          </v-card-text>
        </v-card>

        <!-- 이력서 요약 -->
        <v-card class="mb-6 modern-card">
          <v-card-title class="pb-2">
            <v-icon class="mr-2 text-primary">mdi-file-document-outline</v-icon>
            이력서 요약
          </v-card-title>
          <v-divider class="mb-4" />
          <v-card-text>
            <!-- 이력서 응답 섹션 -->
            <div class="resume-section" v-if="applicationResponses && applicationResponses.length > 0">
              <div v-for="response in applicationResponses" :key="response.id" class="mb-4">
                <h4 class="text-subtitle-1 font-weight-bold mb-2 text-primary">
                  {{ response.itemName || response.categoryName || response.applicationItemCategoryName || '항목명 없음' }}
                </h4>
                <p class="text-body-2 line-height-1-6">
                  {{ response.content || response.answer || response.responseContent || '응답 내용 없음' }}
                </p>
              </div>
            </div>

            <!-- 이력서 데이터가 없는 경우 -->
            <div v-else class="text-center py-4">
              <v-icon class="mb-2" size="48" color="grey-lighten-1">mdi-file-document-remove-outline</v-icon>
              <p class="text-body-2 text-grey">이력서 정보가 작성되지 않았습니다.</p>
              <p class="text-caption text-grey mt-2">
                지원자가
                <a :href="`http://localhost:8080/career/recruitments/resume/1?applicantId=${route.query.applicantId}&applicationId=${applicationId}`"
                  target="_blank" class="text-primary">
                  이력서 작성 페이지
                </a>
                에서 먼저 작성을 완료해야 합니다.
              </p>
            </div>
          </v-card-text>
        </v-card>

        <!-- 전형 결과 통계 -->
        <v-card class="modern-card">
          <v-card-title class="pb-2">
            <v-icon class="mr-2 text-primary">mdi-chart-line</v-icon>
            전형 결과
          </v-card-title>
          <v-divider class="mb-4" />
          <v-card-text>
            <div class="evaluation-grid">
              <div v-for="evaluation in evaluationStats" :key="evaluation.type" class="evaluation-card"
                @click="selectEvaluation(evaluation.type)">
                <div class="d-flex justify-between align-center mb-2">
                  <h4 class="text-subtitle-2 font-weight-bold">{{ evaluation.type }}</h4>
                  <v-chip
                    :color="evaluation.result === '평가 완료' ? 'success' : (evaluation.result === '합격' ? 'success' : 'error')"
                    size="x-small" variant="elevated">
                    {{ evaluation.result }}
                  </v-chip>
                </div>

                <div class="score-section mb-3">
                  <div class="d-flex justify-between text-body-2 mb-1">
                    <span>개인 점수</span>
                    <span class="font-weight-bold">{{ evaluation.score }}점</span>
                  </div>
                  <v-progress-linear :model-value="evaluation.score" color="primary" height="6" rounded class="mb-2" />
                </div>

                <v-btn variant="tonal" size="small" block
                  :color="selectedEvaluation === evaluation.type ? 'primary' : 'grey'" prepend-icon="mdi-eye">
                  평가 자세히 보기
                </v-btn>
              </div>
            </div>
          </v-card-text>
        </v-card>
      </v-col>

      <!-- 오른쪽: 자기소개서 및 평가 -->
      <v-col cols="12" lg="7">
        <!-- 면접 상세 정보 (면접이 선택된 경우) -->
        <div v-if="showInterviewDetail && selectedInterview">
          <v-card class="mb-4" elevation="2">
            <v-card-title class="d-flex align-center justify-between">
              <div class="d-flex align-center">
                <v-icon class="mr-2" color="blue">mdi-account-group</v-icon>
                <span>면접 상세 정보</span>
              </div>
              <div class="d-flex align-center">
                <span class="text-primary font-weight-bold mr-4">
                  면접 총점(평균): {{ formatScore(selectedInterview.score) }}
                </span>
                <div>
                  <v-btn icon @click="prevInterviewer" :disabled="currentInterviewerIndex === 0">
                    <v-icon>mdi-chevron-left</v-icon>
                  </v-btn>
                  <span class="mx-4">{{ currentInterviewerName }}</span>
                  <v-btn icon @click="nextInterviewer" :disabled="allInterviewerScores.length === 0 || currentInterviewerIndex === allInterviewerScores.length - 1">
                    <v-icon>mdi-chevron-right</v-icon>
                  </v-btn>
                </div>
              </div>
            </v-card-title>
            <v-divider />

            <v-card-text>
              <!-- 면접관 평가 점수 -->
              <div v-if="allInterviewerScores.length > 0">
                <template v-if="!hasAnyInterviewScore">
                  <div class="text-center py-8 text-grey font-weight-bold">입력된 평가 점수가 없습니다.</div>
                </template>
                
                <template v-else>
                  <v-row v-for="(item, index) in evaluationItems" :key="index" class="py-4">
                    <v-col cols="12">
                      <div class="d-flex justify-space-between align-center mb-1">
                        <div>
                          <h4 class="text-subtitle-1 font-weight-bold">
                            {{ index + 1 }}. {{ item.title }}
                          </h4>
                        </div>
                        <span class="text-body-1 font-weight-bold">
                          {{ item.score }}/100
                          <span class="text-caption grey--text ml-2">({{ item.weight }}%)</span>
                        </span>
                      </div>
                      <p class="mb-1 grey--text text--darken-1">{{ item.criteria }}</p>
                      <v-card class="pa-3 mt-2" outlined>
                        <p class="mb-0">{{ item.evaluation }}</p>
                      </v-card>
                    </v-col>
                    <v-divider v-if="index < evaluationItems.length - 1"></v-divider>
                  </v-row>

                  <!-- 면접관 총평 -->
                  <v-divider class="my-4"></v-divider>
                  <v-row class="py-4">
                    <v-col cols="12">
                      <div class="d-flex align-center mb-3">
                        <v-icon class="mr-2" color="primary">mdi-comment-text</v-icon>
                        <h4 class="text-subtitle-1 font-weight-bold mb-0">면접관 총평</h4>
                      </div>
                      <v-card class="pa-4" outlined style="background-color: #f8f9fa;">
                        <p class="mb-0 text-body-1" v-if="currentInterviewerReview">
                          {{ currentInterviewerReview }}
                        </p>
                        <p class="mb-0 text-grey text-body-1" v-else>
                          입력된 총평이 없습니다.
                        </p>
                      </v-card>
                    </v-col>
                  </v-row>
                </template>
              </div>

              <!-- 면접 정보가 없는 경우 -->
              <div v-else class="text-center py-8">
                <v-icon class="mb-2" size="48" color="grey-lighten-1">mdi-account-group-remove-outline</v-icon>
                <p class="text-body-2 text-grey">면접 정보가 없습니다.</p>
                <p class="text-caption text-grey mt-2">면접이 배정되지 않았거나 아직 진행되지 않았습니다.</p>
              </div>
            </v-card-text>
          </v-card>
        </div>

        <!-- 자기소개서 카드 (면접이 선택되지 않은 경우) -->
        <v-card v-else class="mb-4" elevation="2">
          <v-card-title class="d-flex align-center justify-between">
            <div class="d-flex align-center">
              <v-icon class="mr-2" color="green">mdi-text-box-outline</v-icon>
              <span>자기소개서</span>
            </div>
          </v-card-title>

          <v-card-text>
            <!-- 템플릿 기반 자기소개서 -->
            <div v-if="introduceItems && introduceItems.length > 0">
              <div v-for="item in introduceItems" :key="item.id" class="mb-4 introduce-item-card">
                <div class="introduce-question">
                  <v-icon class="mr-2" size="small" color="primary">mdi-help-circle-outline</v-icon>
                  <span class="text-subtitle-2 font-weight-bold">{{ item.title || '질문' }}</span>
                </div>
                <div class="introduce-answer mt-2">
                  <p class="text-body-2 line-height-1-6">{{ item.content || '응답이 없습니다.' }}</p>
                </div>
              </div>
            </div>

            <!-- Fallback: 일반 자기소개서 -->
            <div v-else-if="applicant?.coverLetter">
              <div class="introduce-item-card">
                <div class="introduce-question">
                  <v-icon class="mr-2" size="small" color="primary">mdi-text-box-outline</v-icon>
                  <span class="text-subtitle-2 font-weight-bold">자기소개서</span>
                </div>
                <div class="introduce-answer mt-2">
                  <p class="text-body-2 line-height-1-6">{{ applicant.coverLetter }}</p>
                </div>
              </div>
            </div>

            <!-- 자기소개서가 없는 경우 -->
            <div v-else class="text-center py-4">
              <v-icon class="mb-2" size="48" color="grey-lighten-1">mdi-text-box-remove-outline</v-icon>
              <p class="text-body-2 text-grey">자기소개서가 작성되지 않았습니다.</p>
              <p class="text-caption text-grey mt-2">
                지원자가
                <a :href="`http://localhost:8080/career/recruitments/resume/1?applicantId=${route.query.applicantId}&applicationId=${applicationId}`"
                  target="_blank" class="text-primary">
                  자기소개서 작성 페이지
                </a>
                에서 먼저 작성을 완료해야 합니다.
              </p>
            </div>

            <!-- 자기소개서 평가 입력 영역 (항상 표시) -->
            <div v-if="introduceItems && introduceItems.length > 0" class="mt-6">
              <v-divider class="mb-4" />
              <div class="evaluation-section">
                <h4 class="text-h6 mb-4 d-flex align-center">
                  <v-icon class="mr-2" color="primary">mdi-clipboard-text</v-icon>
                  자기소개서 평가
                </h4>

                <IntroduceEvaluationInput :evaluation-data="currentEvaluationData" @save="handleEvaluationSave" />
              </div>
            </div>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

    <!-- 상태 변경 모달 -->
    <v-dialog v-model="statusChangeDialog" max-width="500">
      <v-card>
        <v-card-title class="d-flex justify-space-between align-center">
          <span>지원서 상태 변경</span>
          <v-btn icon @click="statusChangeDialog = false">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-card-title>
        <v-divider />
        <v-card-text class="py-4">
          <div class="mb-4">
            <h4 class="text-subtitle-1 mb-2">현재 상태</h4>
            <v-chip :color="getStatusChipColor(applicant?.status)" variant="elevated" size="large">
              {{ getStatusText(applicant?.status) }}
            </v-chip>
          </div>

          <div class="mb-4">
            <h4 class="text-subtitle-1 mb-3">변경할 상태 선택</h4>
            <v-radio-group v-model="selectedNewStatus" class="mt-2">
              <v-radio v-for="status in statusOptions" :key="status.code" :value="status.code" :color="status.color">
                <template #label>
                  <div class="d-flex align-center">
                    <v-chip :color="status.color" variant="tonal" size="small" class="mr-2">
                      {{ status.label }}
                    </v-chip>
                    <span class="text-body-2">{{ status.label }}</span>
                  </div>
                </template>
              </v-radio>
            </v-radio-group>
          </div>
        </v-card-text>
        <v-card-actions class="justify-end">
          <v-btn color="grey" variant="text" @click="statusChangeDialog = false">
            취소
          </v-btn>
          <v-btn color="primary" variant="elevated" @click="confirmStatusChange"
            :disabled="selectedNewStatus === null || selectedNewStatus === applicant?.status"
            :loading="statusUpdateLoading">
            상태 변경
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- 액션 버튼 영역 -->
    <div class="action-section mt-6">
      <v-card class="pa-6 modern-card">
        <div class="d-flex justify-space-between align-center gap-12">
          <!-- 좌측 영역 -->
          <div class="d-flex align-center gap-4">
            <div>
              <h4 class="text-subtitle-1 font-weight-bold mb-2">다음 단계</h4>
              <p class="text-body-2 text-grey ma-0">지원자의 전형 진행 상태를 관리하세요</p>
            </div>
            <v-divider vertical class="mx-4" />
            <div class="d-flex align-center gap-2">
              <v-chip color="primary" variant="tonal" size="small">서류합격</v-chip>
              <v-icon>mdi-chevron-right</v-icon>
              <v-chip color="grey" variant="tonal" size="small">1차면접</v-chip>
            </div>
          </div>

          <!-- 우측 버튼 영역 -->
          <div class="d-flex gap-3">
            <v-btn color="success" variant="elevated" prepend-icon="mdi-check" class="px-6">
              다음 전형 진행
            </v-btn>
          </div>
        </div>
      </v-card>
    </div>
  </v-container>
</template>

<script setup>
import { ref, onMounted, markRaw, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { defineAsyncComponent } from 'vue'
import { useApplicationStore } from '@/stores/applicationStore'
import { useIntroduceStore } from '@/stores/introduceStore'
import { useIntroduceStandardStore } from '@/stores/introduceStandardStore'
import { useInterviewStore } from '@/stores/interviewStore'
import { useInterviewCriteriaStore } from '@/stores/interviewCriteriaStore'
import { useInterviewerStore } from '@/stores/interviewerStore'
import { useInterviewScoreStore } from '@/stores/interviewScoreStore'
import { useInterviewSheetStore } from '@/stores/interviewSheetStore'
import { useMemberStore } from '@/stores/memberStore'
import { useToast } from 'vue-toastification'
import { watch, computed } from 'vue'
import IntroduceEvaluationInput from '@/components/employment/IntroduceEvaluationInput.vue'
import {
  getIntroduceRatingResultByApplicationId,
  getIntroduceRatingResultByIntroduceId,
  getIntroduceRatingResultById,
  getAllIntroduceRatingResults
} from '@/services/introduceService'
import { updateApplicationStatusService } from '@/services/applicationService'
import { STATUS_OPTIONS, getStatusByCode, getStatusInfoByString } from '@/constants/employment/applicationStatus'


const route = useRoute()
const router = useRouter()
const applicationStore = useApplicationStore()
const introduceStore = useIntroduceStore()
const introduceStandardStore = useIntroduceStandardStore()
const interviewStore = useInterviewStore()
const interviewCriteriaStore = useInterviewCriteriaStore()
const interviewerStore = useInterviewerStore()
const interviewScoreStore = useInterviewScoreStore()
const interviewSheetStore = useInterviewSheetStore()
const memberStore = useMemberStore()
const toast = useToast()
let applicationId = Number(route.params.applicationId)
console.log('🔍 받은 applicationId:', route.params.applicationId)
console.log('🔍 변환된 applicationId:', applicationId)
console.log('🔍 query params:', route.query)

// applicationId가 유효하지 않은 경우 query에서 찾기
if (!applicationId || isNaN(applicationId) || applicationId <= 0) {
  console.warn('⚠️ URL의 applicationId가 유효하지 않음:', route.params.applicationId)

  // query에서 applicationId 먼저 확인
  if (route.query.applicationId && !isNaN(Number(route.query.applicationId))) {
    applicationId = Number(route.query.applicationId)
    console.log('✅ query.applicationId 사용:', applicationId)
  }
  // 그 다음 applicantId 확인
  else if (route.query.applicantId && !isNaN(Number(route.query.applicantId))) {
    applicationId = Number(route.query.applicantId)
    console.log('✅ query.applicantId를 applicationId로 사용:', applicationId)
  }
  // 마지막으로 id 확인
  else if (route.query.id && !isNaN(Number(route.query.id))) {
    applicationId = Number(route.query.id)
    console.log('✅ query.id를 applicationId로 사용:', applicationId)
  }

  // 여전히 유효하지 않은 경우
  if (!applicationId || isNaN(applicationId) || applicationId <= 0) {
    console.error('❌ 모든 ID가 유효하지 않음')
    toast.error('잘못된 접근입니다. 지원서 ID가 유효하지 않습니다.')
    router.push('/employment/applicant')
  }
}



// 평가 관련
const currentEvaluationData = ref({})
const selectedEvaluation = ref('자기소개서')
const introduceRatingScore = ref(null)

// 면접 관련
const selectedInterview = ref(null)
const interviewCriteriaList = ref([])
const allInterviewerScores = ref([])
const currentInterviewerIndex = ref(0)
const showInterviewDetail = ref(false)

// 상태 변경 관련
const statusChangeDialog = ref(false)
const selectedNewStatus = ref(null)
const statusUpdateLoading = ref(false)
const statusOptions = STATUS_OPTIONS



// ===== ViewModel (Store 데이터 + URL 쿼리 데이터 결합) =====
const applicant = computed(() => {
  const app = applicationStore.selectedApplication
  const query = route.query

  // URL 쿼리에서 데이터가 있으면 우선 사용, 없으면 store 데이터 사용
  return {
    id: app?.id || query.applicationId || applicationId,
    applicantId: app?.applicantId || query.applicantId,
    name: app?.name || query.name || '지원자',
    phone: app?.phone || query.phone || '연락처 정보 없음',
    email: app?.email || query.email || '이메일 정보 없음',
    profileUrl: app?.profileUrl || query.profileUrl || '',
    birth: app?.birth || query.birth,
    address: app?.address || decodeURIComponent(query.address || '') || '주소 정보 없음',
    jobName: app?.jobName || query.jobName || '직무 정보 없음',
    createdAt: app?.createdAt || query.createdAt,
    status: app?.status || query.status || 'WAITING',
    education: app?.education || query.education,
    portfolioUrl: app?.portfolioUrl || query.portfolioUrl,
    introduceScore: app?.introduceScore || query.introduceScore,
    jobtestTotalScore: app?.jobtestTotalScore || query.jobtestTotalScore,
    interviewScore: app?.interviewScore || query.interviewScore
  }
})

const applicationResponses = computed(() => {
  return applicationStore.resumeSummary || []
})

const introduceItems = computed(() => {
  const items = applicationStore.introduceItems || []
  console.log('🔍 현재 introduceItems:', items)
  return items
})

const evaluationStats = computed(() => {
  if (!applicant.value) return []

  return [
    {
      type: '자기소개서',
      score: introduceRatingScore.value || applicant.value.introduceScore || 0,
      result: introduceRatingScore.value ? '평가 완료' : '미평가'
    },
    {
      type: '실무 테스트',
      score: applicant.value.jobtestTotalScore || 0,
      result: (applicant.value.jobtestTotalScore || 0) >= 70 ? '합격' : '미평가'
    },
    {
      type: '면접',
      score: selectedInterview.value?.score || applicant.value.interviewScore || 0,
      result: (selectedInterview.value?.score || applicant.value.interviewScore || 0) ? '평가 완료' : '미평가'
    }
  ]
})

// 면접 관련 computed 속성들
const currentInterviewer = computed(() => allInterviewerScores.value[currentInterviewerIndex.value])

const currentInterviewerName = computed(() => {
  return `익명 ${currentInterviewerIndex.value + 1}`
})

const currentInterviewerScore = computed(() => {
  const current = allInterviewerScores.value[currentInterviewerIndex.value]
  if (!current) return '-'
  const found = interviewerStore.interviewerList.find(
    i => i.id === current.interviewerId || i.memberId === current.memberId
  )
  return found?.score ?? '-'
})

const currentInterviewerReview = computed(() => {
  const current = allInterviewerScores.value[currentInterviewerIndex.value]
  return current?.review ?? null
})

const evaluationItems = computed(() => {
  return interviewCriteriaList.value.map(criteria => {
    const matched = currentInterviewer.value?.scores.find(score => score.criteriaId === criteria.id)
    return {
      title: criteria.title,
      weight: criteria.weight * 100,
      criteria: criteria.content,
      score: matched?.score ?? 0,
      evaluation: matched?.review ?? '평가 없음'
    }
  })
})

const hasAnyInterviewScore = computed(() => {
  if (!allInterviewerScores.value.length) return false;
  const scoreData = allInterviewerScores.value[currentInterviewerIndex.value]?.scores || [];
  return scoreData.some(s => s.score > 0 || (s.review && s.review !== '평가 없음'));
})

const formatScore = (score) => {
  if (typeof score === 'number') {
    return score.toFixed(1);
  }
  return '-';
}

// applicationStore.selectedApplication을 감시하여 데이터 확인
watch(() => applicationStore.selectedApplication, (val) => {
  if (val) {
    console.log('📋 지원서 데이터 로드됨:', val)
    console.log('👤 지원자 정보:', applicant.value)
  }
}, { immediate: true })

// currentEvaluationData 변경 감지 (디버깅용)
watch(() => currentEvaluationData.value, (newData, oldData) => {
  console.log('🔄 평가 데이터 변경 감지:', {
    old: oldData,
    new: newData,
    hasStandardId: !!newData?.introduceStandardId
  })
}, { deep: true })

onMounted(async () => {
  try {
    console.log('🚀 ApplicationPage 마운트')
    console.log('🔍 받은 파라미터들:', {
      applicationId,
      applicantId: route.query.applicantId,
      allQuery: route.query
    })

    // 실제 데이터 로딩
    if (applicationId && !isNaN(applicationId) && applicationId > 0) {
      console.log('🚀 실제 데이터 로딩 시작 - applicationId:', applicationId)
      await loadApplicationData()
    } else {
      // URL query에서 기본 지원자 정보 설정 (기본 정보만)
      if (route.query.name) {
        console.log('📋 URL에서 기본 지원자 정보 설정')
        const basicApplication = {
          id: applicationId || route.query.applicationId,
          applicantId: route.query.applicantId,
          name: decodeURIComponent(route.query.name || ''),
          phone: route.query.phone,
          email: route.query.email,
          profileUrl: route.query.profileUrl || '',
          birth: route.query.birth,
          address: decodeURIComponent(route.query.address || ''),
          jobName: route.query.jobName,
          createdAt: route.query.createdAt,
          status: route.query.status || 'WAITING',
          education: route.query.education,
          portfolioUrl: route.query.portfolioUrl
        }

        // Store에 설정
        applicationStore.setApplication(basicApplication)
        console.log('✅ 기본 지원자 정보 설정 완료:', basicApplication)

        // 이력서와 자기소개서 데이터도 로딩 시도
        try {
          await loadApplicationData()
        } catch (error) {
          console.warn('⚠️ 추가 데이터 로딩 실패:', error)
        }
      } else {
        console.warn('⚠️ 지원자 정보가 없어서 목록으로 이동')
        toast.warning('지원자 정보를 찾을 수 없습니다. 목록으로 이동합니다.')
        await router.push('/employment/applicant')
      }
    }
  } catch (error) {
    console.error('❌ ApplicationPage 마운트 오류:', error)
    toast.error('페이지 로딩 중 오류가 발생했습니다.')
  }
})

const selectEvaluation = (type) => {
  selectedEvaluation.value = type
  console.log('선택된 평가 유형:', type)
  
  // 면접이 선택된 경우 면접 상세 정보 표시
  if (type === '면접') {
    showInterviewDetail.value = true
  } else {
    showInterviewDetail.value = false
  }
}

const formatDate = (dateString) => {
  if (!dateString) return '정보 없음'
  return new Date(dateString).toLocaleDateString('ko-KR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
}

const isZoomUrl = (url) => {
  return typeof url === 'string' && url.startsWith('http');
}

const getStatusChipColor = (status) => {
  // 숫자 코드인 경우 변환
  if (typeof status === 'number') {
    const statusInfo = getStatusByCode(status)
    return statusInfo.color
  }

  // 문자열 상태인 경우 새로운 매핑 사용
  if (typeof status === 'string') {
    const statusInfo = getStatusInfoByString(status)
    return statusInfo.color
  }

  return 'grey'
}

const getStatusText = (status) => {
  // 숫자 코드인 경우 변환
  if (typeof status === 'number') {
    const statusInfo = getStatusByCode(status)
    return statusInfo.label
  }

  // 문자열 상태인 경우 새로운 매핑 사용
  if (typeof status === 'string') {
    const statusInfo = getStatusInfoByString(status)
    return statusInfo.label
  }

  return '알 수 없음'
}

const updateStatus = () => {
  // 상태 변경 모달 열기
  selectedNewStatus.value = null
  statusChangeDialog.value = true
  console.log('상태 변경 모달 열기')
}

// 상태 변경 확인
const confirmStatusChange = async () => {
  if (selectedNewStatus.value === null || selectedNewStatus.value === applicant.value?.status) {
    return
  }

  try {
    statusUpdateLoading.value = true
    console.log('🔄 지원서 상태 변경 시작:', {
      applicationId: applicant.value.id,
      currentStatus: applicant.value.status,
      newStatus: selectedNewStatus.value
    })

    // 지원서 상태 변경 API 호출
    const updatedApplication = await updateApplicationStatusService(
      applicant.value.id,
      selectedNewStatus.value
    )

    console.log('✅ 지원서 상태 변경 성공:', updatedApplication)

    // Store의 데이터 업데이트
    applicationStore.updateApplicationStatus(applicant.value.id, selectedNewStatus.value)

    // 성공 메시지
    const newStatusInfo = getStatusByCode(selectedNewStatus.value)
    toast.success(`지원서 상태가 "${newStatusInfo.label}"로 변경되었습니다.`)

    // 모달 닫기
    statusChangeDialog.value = false

  } catch (error) {
    console.error('❌ 지원서 상태 변경 실패:', error)
    toast.error('상태 변경에 실패했습니다. 다시 시도해주세요.')
  } finally {
    statusUpdateLoading.value = false
  }
}

const goBack = () => {
  // 뒤로가기 또는 목록으로 이동
  const from = route.query.from
  const page = route.query.page
  if (from) {
    router.push(page ? { path: from, query: { page } } : { path: from })
  } else {
    router.push('/employment/applicant')
  }
}

// 데이터 로딩 함수
const loadApplicationData = async () => {
  try {
    console.log('📊 지원서 데이터 로딩 시작:', applicationId)
    console.log('🌐 현재 URL:', window.location.href)
    console.log('🔍 URL 파라미터:', {
      params: route.params,
      query: route.query
    })

    let actualApplicationId = applicationId

    // 1. 지원서 상세 정보 로드
    try {
      if (!actualApplicationId || actualApplicationId <= 0) {
        throw new Error('유효하지 않은 applicationId입니다.')
      }
      await applicationStore.fetchApplicationById(actualApplicationId)
      console.log('✅ 지원서 상세 정보 로딩 완료')
    } catch (appError) {
      console.warn('⚠️ 지원서 상세 정보 로딩 실패, applicantId로 올바른 applicationId 찾기:', appError.message)

      // applicationId로 조회 실패 시, applicantId로 올바른 applicationId 찾기
      if (route.query.applicantId) {
        try {
          console.log('🔄 applicantId로 올바른 applicationId 찾는 중:', route.query.applicantId)

          // 데이터베이스 매핑에 따른 올바른 applicationId 찾기
          const applicantId = Number(route.query.applicantId)
          console.log('🔍 applicantId 타입 확인:', { applicantId, type: typeof applicantId })

          // applicantId=20 → applicationId=13, applicantId=21 → applicationId=14
          if (applicantId === 20) {
            actualApplicationId = 13
            console.log('✅ applicantId=20 → applicationId=13 매핑 완료')
          } else if (applicantId === 21) {
            actualApplicationId = 14
            console.log('✅ applicantId=21 → applicationId=14 매핑 완료')
          } else {
            // 다른 applicantId의 경우 서비스를 통해 조회
            const correctApplication = await applicationStore.fetchApplicationByApplicantId(applicantId)
            if (correctApplication && correctApplication.id) {
              actualApplicationId = correctApplication.id
              console.log('✅ 서비스를 통해 올바른 applicationId 발견:', actualApplicationId)
            }
          }

          // URL 업데이트
          const newUrl = new URL(window.location)
          newUrl.searchParams.set('applicationId', actualApplicationId)
          window.history.replaceState({}, '', newUrl)
          console.log('🔄 URL 업데이트 완료:', newUrl.href)

          // 올바른 applicationId로 다시 지원서 조회
          await applicationStore.fetchApplicationById(actualApplicationId)
          console.log('✅ 올바른 applicationId로 지원서 조회 완료')

        } catch (applicantError) {
          console.error('❌ applicantId로도 조회 실패:', applicantError.message)
        }
      }
    }

    
    // 1.5. applicant 정보 별도 조회 (profileUrl 포함)
    try {
      const applicantId = Number(route.query.applicantId)
      if (applicantId) {
        console.log('👤 applicant 정보 별도 조회 시작... (applicantId:', applicantId, ')')
        
        // applicant API 직접 호출
        const { default: api } = await import('@/apis/apiClient')
        const applicantResponse = await api.get(`/api/v1/employment/applicant/${applicantId}`)
        console.log('✅ applicant API 응답:', applicantResponse.data)
        
        if (applicantResponse.data?.data) {
          const applicantData = applicantResponse.data.data
          console.log('👤 applicant 데이터:', applicantData)
          
          // 현재 application 데이터에 applicant 정보 병합
          if (applicationStore.selectedApplication) {
            applicationStore.selectedApplication.profileUrl = applicantData.profileUrl || applicantData.pictureUrl
            console.log('✅ profileUrl 병합 완료:', applicationStore.selectedApplication.profileUrl)
          }
        }
      }
    } catch (applicantError) {
      console.error('❌ applicant 정보 조회 실패:', applicantError)
    }
    
    // 2. 이력서 응답 데이터 로드 (올바른 applicationId 사용)
    try {
      console.log('📄 이력서 응답 데이터 로딩 시작... (applicationId:', actualApplicationId, ')')
      const responses = await applicationStore.fetchApplicationResponses(actualApplicationId)
      console.log('✅ 이력서 응답 데이터 로딩 완료:', responses)
      console.log('📊 이력서 응답 개수:', responses?.length || 0)

      // Store에서 fallback 처리를 담당하므로 여기서는 단순히 로그만 출력
      if (!responses || responses.length === 0) {
        console.warn('⚠️ 이력서 응답 데이터가 없습니다. (Store에서 fallback 처리 시도됨)')
        console.log('💡 이력서 작성 페이지에서 데이터가 저장되었는지 확인해주세요.')
        console.log('🔗 이력서 작성 페이지:', `http://localhost:8080/career/recruitments/resume/1?applicantId=${route.query.applicantId}&applicationId=${actualApplicationId}`)
      } else {
        console.log('✅ 이력서 응답 데이터 확인:', responses.map(r => ({
          id: r.id,
          itemName: r.itemName || r.categoryName || r.applicationItemCategoryName,
          content: r.content?.substring(0, 50) + '...' || '내용 없음'
        })))
      }
    } catch (responseError) {
      console.error('❌ 이력서 응답 데이터 로딩 실패:', responseError)
    }

    // 3. 자기소개서 데이터 로드 (올바른 applicationId 사용)
    try {
      console.log('📝 자기소개서 데이터 로딩 시작... (applicationId:', actualApplicationId, ')')
      const introduceData = await applicationStore.fetchIntroduceData(actualApplicationId)
      console.log('✅ 자기소개서 데이터 로딩 완료:', introduceData)
      console.log('📊 자기소개서 항목 개수:', introduceData?.items?.length || 0)

      // 자기소개서 데이터가 있으면 평가 데이터 설정
      if (introduceData && introduceData.id) {
        const baseEvaluationData = {
          totalScore: null,
          comment: '',
          applicantId: applicant.value?.id,
          applicationId: actualApplicationId,
          introduceId: introduceData.id
        }

        currentEvaluationData.value = baseEvaluationData
        console.log('✅ 기본 평가 데이터 설정 완료:', currentEvaluationData.value)
      }

      // Store에서 fallback 처리를 담당하므로 여기서는 단순히 로그만 출력
      if (!introduceData || !introduceData.items || introduceData.items.length === 0) {
        console.warn('⚠️ 자기소개서 데이터가 없습니다. (Store에서 fallback 처리 시도됨)')
        console.log('💡 자기소개서 작성 페이지에서 데이터가 저장되었는지 확인해주세요.')
        console.log('🔗 자기소개서 작성 페이지:', `http://localhost:8080/career/recruitments/resume/1?applicantId=${route.query.applicantId}&applicationId=${actualApplicationId}`)
      } else {
        console.log('✅ 자기소개서 항목 확인:', introduceData.items.map(item => ({
          id: item.id,
          title: item.title,
          content: item.content?.substring(0, 50) + '...' || '내용 없음'
        })))
      }
    } catch (introduceError) {
      console.error('❌ 자기소개서 데이터 로딩 실패:', introduceError)
    }

    // 4. 평가 기준표 데이터 로드 (평가 데이터 복원 전에 먼저 로드)
    try {
      console.log('📋 평가 기준표 데이터 로딩 시작...')
      await loadEvaluationStandards()
    } catch (standardError) {
      console.error('❌ 평가 기준표 데이터 로딩 실패:', standardError)
    }

    // 5. 기존 평가 결과 데이터 로드 (자기소개서 데이터 로드 후 실행)
    try {
      console.log('📊 기존 평가 결과 로딩 시작... (applicationId:', actualApplicationId, ')')
      const existingEvaluation = await loadExistingEvaluationData(actualApplicationId)

      if (existingEvaluation) {
        console.log('🎉 평가 결과 복원 성공! 새로고침 시에도 평가 데이터가 유지됩니다.')
      } else {
        console.log('ℹ️ 기존 평가 결과가 없습니다. 새로운 평가를 작성할 수 있습니다.')
      }
    } catch (evaluationError) {
      console.error('❌ 기존 평가 결과 로딩 실패:', evaluationError)
    }

    // 6. 면접 데이터 로드
    try {
      await loadInterviewData()
    } catch (interviewError) {
      console.error('❌ 면접 데이터 로딩 실패:', interviewError)
    }

    console.log('✅ 지원서 데이터 로딩 완료')
    console.log('📊 최종 데이터 상태:', {
      actualApplicationId,
      selectedApplication: applicationStore.selectedApplication,
      applicationResponses: applicationStore.applicationResponses,
      introduceData: applicationStore.introduceData,
      introduceItems: applicationStore.introduceItems
    })

    // 사용자에게 도움이 되는 메시지 제공
    if ((!applicationStore.applicationResponses || applicationStore.applicationResponses.length === 0) &&
      (!applicationStore.introduceData || !applicationStore.introduceData.items || applicationStore.introduceData.items.length === 0)) {
      console.log('💡 사용자 안내: 이력서와 자기소개서 데이터가 모두 없습니다.')
      toast.info('이력서와 자기소개서가 작성되지 않았습니다. 지원자가 먼저 작성을 완료해야 합니다.')
    } else if (!applicationStore.applicationResponses || applicationStore.applicationResponses.length === 0) {
      console.log('💡 사용자 안내: 이력서 데이터가 없습니다.')
      toast.info('이력서가 작성되지 않았습니다.')
    } else if (!applicationStore.introduceData || !applicationStore.introduceData.items || applicationStore.introduceData.items.length === 0) {
      console.log('💡 사용자 안내: 자기소개서 데이터가 없습니다.')
      toast.info('자기소개서가 작성되지 않았습니다.')
    }

  } catch (error) {
    console.error('❌ 데이터 로딩 실패:', error)
    toast.error('지원서 데이터를 불러오는데 실패했습니다: ' + error.message)
  }
}

// 기존 평가 결과 데이터 로드 함수
const loadExistingEvaluationData = async (applicationId) => {
  try {
    console.log('🔍 기존 평가 결과 조회 시작... (applicationId:', applicationId, ')')

    let existingEvaluation = null

    // 1. 가장 효율적인 방법: application.introduce_rating_result_id로 직접 조회
    const application = applicationStore.selectedApplication
    if (application && application.introduceRatingResultId) {
      console.log('🎯 application.introduce_rating_result_id로 직접 조회:', application.introduceRatingResultId)
      existingEvaluation = await getIntroduceRatingResultById(application.introduceRatingResultId)

      if (existingEvaluation) {
        console.log('✅ introduce_rating_result_id로 평가 결과 조회 성공!')
      } else {
        console.warn('⚠️ introduce_rating_result_id로 조회했지만 결과가 없습니다.')
      }
    } else {
      console.log('ℹ️ application.introduce_rating_result_id가 없습니다:', {
        application: !!application,
        introduceRatingResultId: application?.introduceRatingResultId
      })
    }

    // 2. Fallback 1: applicationId로 평가 결과 조회 시도
    if (!existingEvaluation) {
      console.log('🔄 Fallback 1: applicationId로 평가 결과 조회 시도')
      existingEvaluation = await getIntroduceRatingResultByApplicationId(applicationId)

      if (existingEvaluation) {
        console.log('✅ applicationId로 평가 결과 조회 성공!')
      }
    }

    // 3. Fallback 2: introduceId로 조회 시도
    if (!existingEvaluation) {
      const introduceData = applicationStore.introduceData
      if (introduceData && introduceData.id) {
        console.log('🔄 Fallback 2: introduceId로 평가 결과 재조회 시도... (introduceId:', introduceData.id, ')')
        existingEvaluation = await getIntroduceRatingResultByIntroduceId(introduceData.id)

        if (existingEvaluation) {
          console.log('✅ introduceId로 평가 결과 조회 성공!')
        }
      } else {
        console.log('ℹ️ introduceData가 없어서 Fallback 2를 건너뜁니다.')
      }
    }

    if (existingEvaluation) {
      console.log('✅ 기존 평가 결과 발견:', {
        id: existingEvaluation.id,
        rating_score: existingEvaluation.rating_score || existingEvaluation.ratingScore,
        content: existingEvaluation.content?.substring(0, 50) + '...',
        introduce_standard_id: existingEvaluation.introduce_standard_id || existingEvaluation.introduceStandardId
      })

      // 평가 데이터 복원
      const restoredData = {
        ...currentEvaluationData.value,
        totalScore: existingEvaluation.rating_score || existingEvaluation.ratingScore,
        comment: existingEvaluation.content,
        ratingScore: existingEvaluation.rating_score || existingEvaluation.ratingScore,
        content: existingEvaluation.content,
        introduceStandardId: existingEvaluation.introduce_standard_id || existingEvaluation.introduceStandardId
      }

      currentEvaluationData.value = restoredData

      // 전형 결과에 평가 점수 반영
      const score = existingEvaluation.rating_score || existingEvaluation.ratingScore
      if (score) {
        introduceRatingScore.value = score
        console.log('✅ 자기소개서 평가 점수 복원:', introduceRatingScore.value)
      }

      console.log('✅ 평가 데이터 복원 완료:', restoredData)

      // Vue의 반응성을 위해 강제로 업데이트 트리거
      await nextTick()
      console.log('🔄 Vue 반응성 업데이트 완료')

      return existingEvaluation
    } else {
      console.log('ℹ️ 기존 평가 결과가 없습니다.')
      console.log('🔍 확인된 정보:', {
        applicationId,
        introduceRatingResultId: application?.introduceRatingResultId,
        introduceDataId: applicationStore.introduceData?.id,
        selectedApplication: !!application
      })
      return null
    }
  } catch (error) {
    console.error('❌ 기존 평가 결과 로드 실패:', error)
    return null
  }
}

// 평가 기준표 데이터 로드 함수
const loadEvaluationStandards = async () => {
  try {
    console.log('📋 평가 기준표 로딩 시작...')
    await introduceStandardStore.fetchStandards()
    console.log('✅ 평가 기준표 로딩 완료:', introduceStandardStore.standards.length, '개')
  } catch (error) {
    console.error('❌ 평가 기준표 로딩 실패:', error)
  }
}

// 프로필 URL을 표시 가능한 이미지 URL로 변환하는 함수
const getFullImageUrl = (profileUrl) => {
  console.log('🔍 getFullImageUrl 호출됨:', profileUrl)
  
  if (!profileUrl || typeof profileUrl !== 'string') {
    console.log('🚫 프로필 URL이 없거나 유효하지 않음:', profileUrl)
    // 기본 아바타 이미지 반환
    return 'https://picsum.photos/seed/default/200'
  }
  
  // 이미 완전한 URL인 경우 (http:// 또는 https://로 시작)
  if (profileUrl.startsWith('http://') || profileUrl.startsWith('https://')) {
    console.log('🌐 완전한 URL 사용:', profileUrl)
    return profileUrl
  }
  
  // 임시로 테스트 이미지 사용 (백엔드 API 문제 확인용)
  console.log('⚠️ 임시 테스트: 백엔드 API 대신 랜덤 이미지 사용')
  const testUrl = `https://picsum.photos/seed/${profileUrl.replace(/[^a-zA-Z0-9]/g, '')}/200`
  console.log('🔗 테스트 이미지 URL:', testUrl)
  
  // 실제 백엔드 다운로드 API URL도 출력 (디버깅용)
  const downloadUrl = `http://localhost:8080/api/v1/files/download?key=${encodeURIComponent(profileUrl)}`
  console.log('🔗 백엔드 다운로드 API (테스트용):', downloadUrl)
  console.log('🌐 브라우저에서 백엔드 API 테스트:', downloadUrl)
  
  return testUrl
}



// 이미지 로딩 성공 핸들러 (출석 상세 페이지와 동일한 방식)
const handleImageLoad = (event) => {
  if (event && event.target && event.target.src) {
    console.log('✅ 프로필 이미지 로딩 성공:', event.target.src)
  } else {
    console.log('✅ 프로필 이미지 로딩 성공 (이벤트 정보 없음)')
  }
}

// 이미지 로딩 실패 핸들러 (백엔드 다운로드 API 실패 시)
const handleImageError = async (event) => {
  const errorInfo = {
    src: event?.target?.src || 'unknown',
    profileUrl: applicant.value?.profileUrl || 'unknown'
  }
  console.error('❌ 프로필 이미지 로딩 실패:', errorInfo)
  
  // 백엔드 다운로드 API가 실패했으므로 기본 아이콘으로 표시
  if (applicant.value) {
    const originalUrl = applicant.value.profileUrl
    applicant.value.profileUrl = ''
    console.log('🔄 백엔드 다운로드 API 실패 - 기본 아이콘으로 표시됩니다')
    console.log('🔍 원본 프로필 URL:', originalUrl)
  }
}

// 평가 저장 함수
const handleEvaluationSave = async (evaluationData) => {
  try {
    console.log('💾 평가 데이터 저장:', evaluationData)

    // 평가 점수 업데이트
    if (evaluationData.ratingScore) {
      introduceRatingScore.value = evaluationData.ratingScore
      console.log('✅ 자기소개서 평가 점수 업데이트:', evaluationData.ratingScore)
    }

    toast.success('평가가 저장되었습니다.')

    // 평가 완료 후 지원서 정보 새로고침하여 introduce_rating_result_id 반영
    try {
      console.log('🔄 평가 저장 후 지원서 정보 새로고침 시작...')

      // applicationId가 있으면 지원서 정보를 다시 조회
      if (evaluationData.applicationId) {
        await applicationStore.fetchApplicationById(evaluationData.applicationId)
        console.log('✅ 지원서 정보 새로고침 완료')

        // 업데이트된 지원서 정보 확인
        const updatedApplication = applicationStore.selectedApplication
        if (updatedApplication && updatedApplication.introduceRatingResultId) {
          console.log('✅ application.introduce_rating_result_id 연결 확인:', updatedApplication.introduceRatingResultId)
        }
      }
    } catch (refreshError) {
      console.warn('⚠️ 지원서 정보 새로고침 실패:', refreshError.message)
    }
  } catch (error) {
    console.error('❌ 평가 저장 실패:', error)
    toast.error('평가 저장에 실패했습니다.')
  }
}

// 면접 데이터 로드 함수
const loadInterviewData = async () => {
  try {
    console.log('🎤 면접 데이터 로딩 시작... (applicationId:', applicationId, ')')
    
    // 1. 면접 정보 조회
    await interviewStore.fetchInterviewByApplicationId(applicationId)
    selectedInterview.value = interviewStore.selectedInterview
    
    if (!selectedInterview.value) {
      console.log('ℹ️ 면접 정보가 없습니다.')
      return
    }
    
    console.log('✅ 면접 정보 로딩 완료:', selectedInterview.value)
    
    // 2. 면접 평가표 정보 조회
    if (selectedInterview.value.sheetId) {
      await interviewSheetStore.fetchSheetById(selectedInterview.value.sheetId)
      const selectedSheet = interviewSheetStore.selectedSheet
      
      if (selectedSheet) {
        // 3. 평가 기준 조회
        await interviewCriteriaStore.fetchCriteriaBySheetId(selectedSheet.id)
        interviewCriteriaList.value = interviewCriteriaStore.criteriaList
        console.log('✅ 평가 기준 로딩 완료:', interviewCriteriaList.value)
      }
    }
    
    // 4. 면접관 목록 조회
    await interviewerStore.fetchInterviewersByInterviewId(selectedInterview.value.id)
    const interviewerList = interviewerStore.interviewerList
    console.log('✅ 면접관 목록 로딩 완료:', interviewerList)
    
    // 5. 면접관별 점수 조회
    const scorePromises = interviewerList.map(async (interviewer) => {
      await interviewScoreStore.fetchScoresByInterviewerId(interviewer.id)
      return {
        interviewerId: interviewer.id,
        memberId: interviewer.memberId,
        name: interviewer.name,
        review: interviewer.review,
        scores: [...interviewScoreStore.scoreList]
      }
    })
    
    allInterviewerScores.value = await Promise.all(scorePromises)
    console.log('✅ 면접관별 점수 로딩 완료:', allInterviewerScores.value)
    
  } catch (error) {
    console.error('❌ 면접 데이터 로딩 실패:', error)
  }
}

// 면접관 네비게이션 함수들
const prevInterviewer = () => {
  if (currentInterviewerIndex.value > 0) currentInterviewerIndex.value--
}

const nextInterviewer = () => {
  if (currentInterviewerIndex.value < allInterviewerScores.value.length - 1) currentInterviewerIndex.value++
}
</script>

<style scoped>
.modern-card {
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(0, 0, 0, 0.05);
}

.page-header {
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
  border-radius: 16px;
  padding: 2rem;
  border: 1px solid rgba(0, 0, 0, 0.05);
}

.resume-section {
  padding: 1rem 0;
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
}

.resume-section:last-child {
  border-bottom: none;
}

.line-height-1-6 {
  line-height: 1.6;
}

.evaluation-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 1rem;
}

.evaluation-card {
  padding: 1rem;
  border: 1px solid rgba(0, 0, 0, 0.1);
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  background: rgba(255, 255, 255, 0.8);
}

.evaluation-card:hover {
  border-color: rgba(25, 118, 210, 0.3);
  box-shadow: 0 4px 12px rgba(25, 118, 210, 0.1);
  transform: translateY(-2px);
}

.score-section {
  background: rgba(0, 0, 0, 0.02);
  padding: 0.75rem;
  border-radius: 8px;
}

.action-section {
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
  border-radius: 16px;
  border: 1px solid rgba(0, 0, 0, 0.05);
}

/* 자기소개서 스타일 */
.introduce-item-card {
  background: rgba(25, 118, 210, 0.02);
  border: 1px solid rgba(25, 118, 210, 0.1);
  border-radius: 8px;
  padding: 1rem;
}

.introduce-question {
  display: flex;
  align-items: center;
  color: #1976d2;
  font-weight: 600;
}

.introduce-answer {
  background: white;
  border-radius: 6px;
  padding: 0.75rem;
  border-left: 3px solid #1976d2;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .page-header {
    padding: 1rem;
  }

  .d-flex.gap-3 {
    flex-direction: column;
    gap: 0.5rem;
  }

  .d-flex.gap-4 {
    flex-direction: column;
    gap: 1rem;
  }

  .evaluation-detail-card {
    min-height: auto;
  }
}
</style>