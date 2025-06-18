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
          <p class="text-body-1 text-grey ma-0">{{ applicant.name }}님의 지원서를 검토하세요</p>
        </div>
        <v-spacer />
        <div class="d-flex align-center gap-3">
          <v-chip :color="getStatusChipColor(applicant.status)" variant="elevated" size="large" class="px-4">
            {{ getStatusText(applicant.status) }}
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
                <v-img :src="applicant.profileUrl" alt="프로필 사진" />
              </v-avatar>

              <div class="flex-grow-1">
                <h2 class="text-h5 font-weight-bold mb-1">{{ applicant.name }}</h2>
                <p class="text-body-2 text-grey mb-2">{{ applicant.jobName || '백엔드 개발자' }}</p>
                <v-chip size="small" color="blue" variant="tonal">
                  {{ getExperiencePreview() }}
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
                  <span class="ml-2 font-weight-medium">{{ formatDate(applicant.birth) }}</span>
                </v-list-item-title>
              </v-list-item>

              <v-list-item class="px-0 py-1">
                <template #prepend>
                  <v-icon class="mr-3" size="small">mdi-phone</v-icon>
                </template>
                <v-list-item-title class="text-body-2">
                  <span class="text-grey">연락처</span>
                  <span class="ml-2 font-weight-medium">{{ applicant.phone }}</span>
                </v-list-item-title>
              </v-list-item>

              <v-list-item class="px-0 py-1">
                <template #prepend>
                  <v-icon class="mr-3" size="small">mdi-email</v-icon>
                </template>
                <v-list-item-title class="text-body-2">
                  <span class="text-grey">이메일</span>
                  <span class="ml-2 font-weight-medium">{{ applicant.email }}</span>
                </v-list-item-title>
              </v-list-item>

              <v-list-item class="px-0 py-1">
                <template #prepend>
                  <v-icon class="mr-3" size="small">mdi-map-marker</v-icon>
                </template>
                <v-list-item-title class="text-body-2">
                  <span class="text-grey">주소</span>
                  <span class="ml-2 font-weight-medium">{{ applicant.address }}</span>
                </v-list-item-title>
              </v-list-item>

              <v-list-item class="px-0 py-1">
                <template #prepend>
                  <v-icon class="mr-3" size="small">mdi-calendar-plus</v-icon>
                </template>
                <v-list-item-title class="text-body-2">
                  <span class="text-grey">지원일자</span>
                  <span class="ml-2 font-weight-medium">{{ formatDate(applicant.createdAt) }}</span>
                </v-list-item-title>
              </v-list-item>

              <v-list-item class="px-0 py-1" v-if="applicant.education">
                <template #prepend>
                  <v-icon class="mr-3" size="small">mdi-school</v-icon>
                </template>
                <v-list-item-title class="text-body-2">
                  <span class="text-grey">학력</span>
                  <span class="ml-2 font-weight-medium">{{ applicant.education }}</span>
                </v-list-item-title>
              </v-list-item>

              <v-list-item class="px-0 py-1" v-if="applicant.portfolioUrl">
                <template #prepend>
                  <v-icon class="mr-3" size="small">mdi-briefcase</v-icon>
                </template>
                <v-list-item-title class="text-body-2">
                  <span class="text-grey">포트폴리오</span>
                  <a :href="applicant.portfolioUrl" target="_blank" class="ml-2 font-weight-medium text-primary">
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
            <div class="resume-section mb-4">
              <h4 class="text-subtitle-1 font-weight-bold mb-2 text-primary">입사 동기</h4>
              <p class="text-body-2 line-height-1-6">{{ applicant.motivation }}</p>
            </div>

            <div class="resume-section mb-4">
              <h4 class="text-subtitle-1 font-weight-bold mb-2 text-primary">주요 경력</h4>
              <p class="text-body-2 line-height-1-6">{{ applicant.experience }}</p>
            </div>

            <div class="resume-section mb-4">
              <h4 class="text-subtitle-1 font-weight-bold mb-2 text-primary">기술 스택</h4>
              <div class="d-flex flex-wrap gap-2">
                <v-chip v-for="skill in getSkillsArray()" :key="skill" size="small" variant="tonal" color="blue">
                  {{ skill }}
                </v-chip>
              </div>
            </div>

            <div class="resume-section" v-if="applicant.coverLetter">
              <h4 class="text-subtitle-1 font-weight-bold mb-2 text-primary">자기소개서</h4>
              <p class="text-body-2 line-height-1-6">{{ applicant.coverLetter }}</p>
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
              <div v-for="evaluation in applicant.evaluationStats" :key="evaluation.type" class="evaluation-card"
                @click="selectEvaluation(evaluation.type)">
                <div class="d-flex justify-between align-center mb-2">
                  <h4 class="text-subtitle-2 font-weight-bold">{{ evaluation.type }}</h4>
                  <v-chip :color="evaluation.result === '합격' ? 'success' : 'error'" size="x-small" variant="elevated">
                    {{ evaluation.result }}
                  </v-chip>
                </div>

                <div class="score-section mb-3">
                  <div class="d-flex justify-between text-body-2 mb-1">
                    <span>개인 점수</span>
                    <span class="font-weight-bold">{{ evaluation.score }}점</span>
                  </div>
                  <v-progress-linear :model-value="evaluation.score" color="primary" height="6" rounded class="mb-2" />

                  <div class="d-flex justify-between text-body-2">
                    <span class="text-grey">평균 점수</span>
                    <span>{{ evaluation.average }}점</span>
                  </div>
                </div>

                <v-btn variant="tonal" size="small" block
                  :color="selectedEvaluation === evaluation.type ? 'primary' : 'grey'" prepend-icon="mdi-eye">
                  평가 상세보기
                </v-btn>
              </div>
            </div>
          </v-card-text>
        </v-card>
      </v-col>


      
      <!-- 우측: 평가 상세 -->
      <v-col cols="12" lg="7">
        <v-card class="modern-card evaluation-detail-card">
          <v-card-title class="pb-2">
            <v-icon class="mr-2 text-primary">mdi-clipboard-text</v-icon>
            {{ selectedEvaluation }} 평가 상세
            <v-spacer />
            <v-btn-toggle v-model="viewMode" mandatory variant="outlined" size="small">
              <v-btn value="detail">상세</v-btn>
              <v-btn value="score">점수</v-btn>
            </v-btn-toggle>
          </v-card-title>
          <v-divider class="mb-4" />
          <v-card-text>
            <div v-if="viewMode === 'detail'">
              <component :is="evaluationComponent" :applicant="applicant" />
            </div>
            <div v-else class="score-analysis">
              <h4 class="text-h6 mb-4">점수 분석</h4>
              <v-row>
                <v-col cols="6">
                  <div class="stat-card">
                    <div class="stat-number text-primary">{{ getCurrentEvaluation()?.score }}</div>
                    <div class="stat-label">개인 점수</div>
                  </div>
                </v-col>
                <v-col cols="6">
                  <div class="stat-card">
                    <div class="stat-number">{{ getCurrentEvaluation()?.average }}</div>
                    <div class="stat-label">평균 점수</div>
                  </div>
                </v-col>
              </v-row>

              <div class="mt-4">
                <h5 class="text-subtitle-1 mb-2">점수 분포</h5>
                <v-progress-linear :model-value="(getCurrentEvaluation()?.score / 100) * 100" color="primary"
                  height="20" rounded>
                  <template #default="{ value }">
                    <strong class="text-white">{{ Math.ceil(value) }}%</strong>
                  </template>
                </v-progress-linear>
              </div>
            </div>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

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
            <v-btn color="error" variant="outlined" prepend-icon="mdi-close" class="px-6">
              불합격 처리
            </v-btn>
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
import { ref, onMounted, defineAsyncComponent } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useApplicationStore } from '@/stores/applicationStore'
import { useToast } from 'vue-toastification'
import apiClient from '@/apis/apiClient'

const route = useRoute()
const router = useRouter()
const applicationStore = useApplicationStore()
const toast = useToast()
const applicationId = Number(route.params.applicationId)

const IntroduceResult = defineAsyncComponent(() => import('@/components/employment/IntroduceEvaluationInput.vue'))
// const TestResult = defineAsyncComponent(() => import('@/components/employment/TestResult.vue'))
// const InterviewResult = defineAsyncComponent(() => import('@/components/employment/InterviewResult.vue'))

const evaluationComponent = ref(IntroduceResult)
const selectedEvaluation = ref('자기소개서')
const viewMode = ref('detail')

// query parameter에서 받은 기본 정보로 applicant 객체 구성
const applicant = ref({
  // 기본 ID 필드들
  applicantId: '',
  applicationId: '',

  // 기본 정보
  name: '',
  phone: '',
  email: '',
  profileUrl: '',
  birth: '',
  address: '',

  // 채용 관련 정보
  recruitmentId: '',
  introduceRatingResultId: '',
  jobId: '',
  jobName: '',
  createdAt: '',
  status: '',
  updatedAt: '',
  updatedBy: '',

  // 추가된 필드들
  introduceEvaluationContent: '',
  introduceScore: null,
  introduceStatus: '',
  motivation: '',
  experience: '',
  skills: '',
  education: '',
  portfolioUrl: '',
  coverLetter: '',
  jobtestTotalScore: null,
  jobtestEvaluationScore: null,
  jobtestStatus: '',
  interviewScore: null,
  interviewAddress: '',
  interviewDatetime: '',

  evaluationStats: []
})

// 컴포넌트 마운트 시 query parameter에서 데이터 로드
onMounted(() => {
  const query = route.query

  // 받은 데이터로 실제 평가 통계 구성
  const evaluationStats = []

  // 자기소개서 평가
  if (query.introduceScore) {
    evaluationStats.push({
      type: '자기소개서',
      score: parseInt(query.introduceScore),
      average: null,
      result: query.introduceStatus === 'PASSED' ? '합격' : '불합격'
    })
  }

  // 실무테스트 평가
  if (query.jobtestEvaluationScore) {
    evaluationStats.push({
      type: '실무 테스트',
      score: parseFloat(query.jobtestEvaluationScore),
      average: null,
      result: query.jobtestStatus === 'PASSED' ? '합격' : '불합격'
    })
  }

  // 면접 평가
  if (query.interviewScore) {
    evaluationStats.push({
      type: '면접',
      score: parseFloat(query.interviewScore),
      average: null,
      result: parseFloat(query.interviewScore) >= 70 ? '합격' : '불합격'
    })
  }

  applicant.value = {
    // 기본 ID 필드들
    applicantId: query.applicantId || '',
    applicationId: query.applicationId || '',

    // 기본 정보
    name: query.name || '정보 없음',
    phone: query.phone || '정보 없음',
    email: query.email || '정보 없음',
    profileUrl: query.profileUrl || '',
    birth: query.birth || '정보 없음',
    address: query.address || '정보 없음',

    // 채용 관련 정보
    recruitmentId: query.recruitmentId || '',
    introduceRatingResultId: query.introduceRatingResultId || '',
    jobId: query.jobId || '',
    jobName: query.jobName || '정보 없음',
    createdAt: query.createdAt || '정보 없음',
    status: query.status || 'WAITING',
    updatedAt: query.updatedAt || '',
    updatedBy: query.updatedBy || '',

    // 추가된 필드들
    introduceEvaluationContent: query.introduceEvaluationContent || '',
    introduceScore: query.introduceScore ? parseInt(query.introduceScore) : null,
    introduceStatus: query.introduceStatus || '',
    motivation: query.motivation || '정보 없음',
    experience: query.experience || '정보 없음',
    skills: query.skills || '정보 없음',
    education: query.education || '정보 없음',
    portfolioUrl: query.portfolioUrl || '',
    coverLetter: query.coverLetter || '정보 없음',
    jobtestTotalScore: query.jobtestTotalScore ? parseFloat(query.jobtestTotalScore) : null,
    jobtestEvaluationScore: query.jobtestEvaluationScore ? parseFloat(query.jobtestEvaluationScore) : null,
    jobtestStatus: query.jobtestStatus || '',
    interviewScore: query.interviewScore ? parseFloat(query.interviewScore) : null,
    interviewAddress: query.interviewAddress || '정보 없음',
    interviewDatetime: query.interviewDatetime || '정보 없음',

    evaluationStats: evaluationStats
  }
})

const selectEvaluation = (type) => {
  selectedEvaluation.value = type
  switch (type) {
    case '자기소개서':
      evaluationComponent.value = IntroduceResult
      break
    // case '실무 테스트':
    //   evaluationComponent.value = TestResult
    //   break
    // case '면접':
    //   evaluationComponent.value = InterviewResult
    //   break
    default:
      evaluationComponent.value = IntroduceResult
  }
}


const getCurrentEvaluation = () => {
  return applicant.value.evaluationStats.find(evaluation => evaluation.type === selectedEvaluation.value)
}

const getSkillsArray = () => {
  if (!applicant.value.skills) return ['정보 없음']
  return applicant.value.skills.split(/[,،、]\s*/).filter(skill => skill.trim())
}


const getExperiencePreview = () => {
  if (!applicant.value.experience) return '경력 정보 없음'
  const preview = applicant.value.experience.split(/[,،、]/)[0]
  return preview ? preview.trim() : '경력 정보 없음'
}

const formatDate = (dateString) => {
  if (!dateString) return '정보 없음'
  return new Date(dateString).toLocaleDateString('ko-KR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
}

const getStatusChipColor = (status) => {
  switch (status) {
    case 'PASSED_FINAL': return 'success'
    case 'FAILED': return 'error'
    case 'PASSED_DOCS': return 'info'
    case 'PASSED_INTERVIEW_1': return 'teal'
    case 'PASSED_INTERVIEW_2': return 'blue'
    case 'PASSED_PRACTICAL': return 'purple'
    case 'WAITING': return 'orange'
    default: return 'grey'
  }
}

const getStatusText = (status) => {
  switch (status) {
    case 'PASSED_FINAL': return '최종합격'
    case 'FAILED': return '불합격'
    case 'PASSED_DOCS': return '서류합격'
    case 'PASSED_INTERVIEW_1': return '1차면접 합격'
    case 'PASSED_INTERVIEW_2': return '2차면접 합격'
    case 'PASSED_PRACTICAL': return '실무합격'
    case 'WAITING': return '검토중'
    default: return '알 수 없음'
  }
}

const updateStatus = () => {
  // 상태 변경 모달이나 다이얼로그 열기
  console.log('상태 변경')
}

const goBack = () => {
  // 뒤로가기 또는 목록으로 이동
  const from = route.query.from;
  const page = route.query.page;
  if (from) {
    router.push(page ? { path: from, query: { page } } : { path: from });
  } else {
    router.push('/employment/applicant');
  }
}
</script>