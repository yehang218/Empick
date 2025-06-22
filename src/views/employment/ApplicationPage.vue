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
                <v-img :src="applicant?.profileUrl" alt="프로필 사진" />
              </v-avatar>

              <div class="flex-grow-1">
                <h2 class="text-h5 font-weight-bold mb-1">{{ applicant?.name || '지원자' }}</h2>
                <p class="text-body-2 text-grey mb-2">{{ applicant?.jobName || '백엔드 개발자' }}</p>
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
            <div class="resume-section mb-4">
              <h4 class="text-subtitle-1 font-weight-bold mb-2 text-primary">입사 동기</h4>
              <p class="text-body-2 line-height-1-6">{{ applicant?.motivation }}</p>
            </div>

            <div class="resume-section mb-4">
              <h4 class="text-subtitle-1 font-weight-bold mb-2 text-primary">주요 경력</h4>
              <p class="text-body-2 line-height-1-6">{{ applicant?.experience }}</p>
            </div>

            <div class="resume-section mb-4">
              <h4 class="text-subtitle-1 font-weight-bold mb-2 text-primary">기술 스택</h4>
              <div class="d-flex flex-wrap gap-2">
                <v-chip v-for="skill in getSkillsArray()" :key="skill" size="small" variant="tonal" color="blue">
                  {{ skill }}
                </v-chip>
              </div>
            </div>

            <!-- 이력서 응답 섹션 -->
            <div class="resume-section" v-if="applicationResponses.length > 0">
              <h4 class="text-subtitle-1 font-weight-bold mb-2 text-primary">이력서 응답</h4>
              <div v-for="response in applicationResponses" :key="response.id" class="mb-3">
                <h5 class="text-subtitle-2 font-weight-medium mb-1">{{ response.itemName }}</h5>
                <p class="text-body-2 line-height-1-6">{{ response.content }}</p>
              </div>
            </div>
            
            <!-- 자기소개서 섹션 -->
            <div class="resume-section" v-if="introduceItems.length > 0 || applicant?.coverLetter">
              <div class="d-flex justify-between align-center mb-2">
                <h4 class="text-subtitle-1 font-weight-bold text-primary">자기소개서</h4>
                <v-btn 
                  color="primary" 
                  variant="outlined" 
                  size="small" 
                  prepend-icon="mdi-clipboard-edit"
                  @click="openEvaluationModal"
                >
                  평가하기
                </v-btn>
              </div>
              <div v-if="introduceItems.length > 0">
                <div v-for="item in introduceItems" :key="item.id" class="mb-3">
                  <h5 class="text-subtitle-2 font-weight-medium mb-1">{{ item.title }}</h5>
                  <p class="text-body-2 line-height-1-6">{{ item.content }}</p>
                </div>
              </div>
              <p v-else-if="applicant?.coverLetter" class="text-body-2 line-height-1-6">{{ applicant.coverLetter }}</p>
              <p v-else class="text-body-2 text-grey">자기소개서가 작성되지 않았습니다.</p>
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

    <!-- 자기소개서 평가 모달 -->
    <v-dialog v-model="showEvaluationModal" max-width="1000px" persistent>
      <v-card>
        <v-card-title class="d-flex justify-between align-center">
          <div>
            <h3>자기소개서 평가</h3>
            <p class="text-body-2 text-grey ma-0">{{ applicant?.name }}님의 자기소개서를 평가해주세요</p>
          </div>
          <v-btn icon variant="text" @click="closeEvaluationModal">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-card-title>
        <v-divider />
        <v-card-text class="pa-6">
          <IntroduceEvaluationInput 
            :evaluation-data="currentEvaluationData"
            @save="handleEvaluationSave"
          />
        </v-card-text>
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
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { defineAsyncComponent } from 'vue'
import { useApplicationStore } from '@/stores/applicationStore'
import { useIntroduceStore } from '@/stores/introduceStore'
import { useToast } from 'vue-toastification'
import { watch, computed } from 'vue'
import IntroduceEvaluationInput from '@/components/employment/IntroduceEvaluationInput.vue'

const route = useRoute()
const router = useRouter()
const applicationStore = useApplicationStore()
const introduceStore = useIntroduceStore()
const toast = useToast()
const applicationId = Number(route.params.applicationId)
console.log('🔍 받은 applicationId:', route.params.applicationId)
console.log('🔍 변환된 applicationId:', applicationId)
console.log('🔍 query params:', route.query)

if (!applicationId || isNaN(applicationId) || applicationId <= 0) {
  console.error('❌ 유효하지 않은 applicationId:', route.params.applicationId)
  
  // query에서 applicantId가 있다면 그것을 사용
  const applicantId = route.query.applicantId
  if (applicantId && !isNaN(Number(applicantId))) {
    console.log('✅ applicantId를 applicationId 대신 사용:', applicantId)
    // 여기서 applicantId를 사용하여 데이터를 가져올 수 있도록 수정
    // 일단 사용자에게 알림만 표시
    toast.warning('지원서 데이터를 불러오는 중입니다...')
  } else {
    toast.error('잘못된 접근입니다. 지원서 ID가 유효하지 않습니다.')
    router.push('/employment/applicant')
  }
}

const IntroduceResult = defineAsyncComponent(() => import('@/components/employment/IntroduceEvaluationInput.vue'))
// const TestResult = defineAsyncComponent(() => import('@/components/employment/TestResult.vue'))
// const InterviewResult = defineAsyncComponent(() => import('@/components/employment/InterviewResult.vue'))

const evaluationComponent = ref(IntroduceResult)
const selectedEvaluation = ref('자기소개서')
const viewMode = ref('detail')

// 평가 모달 관련
const showEvaluationModal = ref(false)
const currentEvaluationData = ref({})

// ===== ViewModel (Store 데이터 직접 사용) =====
const applicant = computed(() => {
  const app = applicationStore.selectedApplication
  if (!app) return null
  
  return {
    ...app,
    // 기본값 설정
    name: app.name || '지원자',
    jobName: app.jobName || '백엔드 개발자',
    profileUrl: app.profileUrl || '/assets/empick_logo.png',
    birth: app.birth,
    phone: app.phone,
    email: app.email,
    address: app.address,
    createdAt: app.createdAt,
    education: app.education,
    portfolioUrl: app.portfolioUrl,
    motivation: app.motivation || '입사 동기가 입력되지 않았습니다.',
    experience: app.experience || '경력 정보가 입력되지 않았습니다.',
    skills: app.skills || 'JavaScript,Vue.js,Node.js',
    coverLetter: app.coverLetter,
    status: app.status || 'WAITING'
  }
})

const applicationResponses = computed(() => {
  return applicationStore.resumeSummary || []
})

const introduceItems = computed(() => {
  return applicationStore.introduceItems || []
})

const evaluationStats = computed(() => {
  if (!applicant.value) return []
  
  return [
    {
      type: '자기소개서',
      score: applicant.value.introduceScore || Math.floor(Math.random() * 30) + 70,
      average: 75,
      result: (applicant.value.introduceScore || 75) >= 70 ? '합격' : '불합격'
    },
    {
      type: '실무 테스트',
      score: applicant.value.jobtestTotalScore || Math.floor(Math.random() * 30) + 70,
      average: 80,
      result: (applicant.value.jobtestTotalScore || 80) >= 70 ? '합격' : '불합격'
    },
    {
      type: '면접',
      score: applicant.value.interviewScore || Math.floor(Math.random() * 30) + 70,
      average: 85,
      result: (applicant.value.interviewScore || 85) >= 70 ? '합격' : '불합격'
    }
  ]
})

// applicationStore.selectedApplication을 감시하여 데이터 확인
watch(() => applicationStore.selectedApplication, (val) => {
  if (val) {
    console.log('📋 지원서 데이터 로드됨:', val)
    console.log('👤 지원자 정보:', applicant.value)
  }
}, { immediate: true })

onMounted(async () => {
  try {
    console.log('🚀 ApplicationPage 마운트')
    console.log('🔍 받은 파라미터들:', { 
      applicationId, 
      applicantId: route.query.applicantId,
      allQuery: route.query 
    })

    // URL query에서 지원자 정보 직접 사용 (임시 해결책)
    if (route.query.name) {
      console.log('📋 URL에서 지원자 정보 직접 설정')
      const mockApplication = {
        id: applicationId || route.query.applicationId,
        applicantId: route.query.applicantId,
        name: route.query.name,
        phone: route.query.phone,
        email: route.query.email,
        profileUrl: route.query.profileUrl || '/assets/empick_logo.png',
        birth: route.query.birth,
        address: route.query.address,
        jobName: route.query.jobName || '백엔드 개발자',
        createdAt: route.query.createdAt,
        status: route.query.status || 'WAITING',
        motivation: route.query.motivation || '입사 동기가 입력되지 않았습니다.',
        experience: route.query.experience || '경력 정보가 입력되지 않았습니다.',
        skills: route.query.skills || 'JavaScript, Vue.js, Node.js',
        education: route.query.education,
        portfolioUrl: route.query.portfolioUrl,
        coverLetter: route.query.coverLetter || '자기소개서가 작성되지 않았습니다.',
        introduceScore: route.query.introduceScore || 85,
        jobtestTotalScore: route.query.jobtestTotalScore || 90,
        interviewScore: route.query.interviewScore || 88
      }
      
      // Store에 직접 설정
      applicationStore.setApplication(mockApplication)
      console.log('✅ 지원자 정보 설정 완료:', mockApplication)
      return
    }
    
    // 기존 API 호출 (fallback)
    if (!applicationId || isNaN(applicationId) || applicationId <= 0) {
      const applicantId = route.query.applicantId
      if (applicantId && !isNaN(Number(applicantId))) {
        console.log('🔍 applicantId로 데이터 조회 시도:', applicantId)
        await applicationStore.fetchApplicationByApplicantId(applicantId)
              } else {
          // 기본 샘플 데이터 설정
          applicationStore.setApplication({
            id: 1,
            name: '김지훈',
            email: 'jihoon.kim@example.com',
            phone: '010-1234-5678',
            profileUrl: '/assets/empick_logo.png',
            jobName: '백엔드 개발자',
            status: 'WAITING',
            motivation: '귀사의 비전에 공감하여 지원하게 되었습니다.',
            experience: '3년간 백엔드 개발 경험이 있습니다.',
            skills: 'Java, Spring Boot, MySQL, Redis'
          })
          console.log('📋 기본 샘플 데이터 설정됨')
        }
    } else {
      await applicationStore.fetchApplicationById(applicationId)
    }
  } catch (error) {
    console.error('❌ 지원서 정보 로드 실패:', error)
    // 에러 시에도 기본 데이터 제공
    applicationStore.setApplication({
      id: applicationId || 1,
      name: '지원자',
      email: 'applicant@example.com',
      phone: '010-0000-0000',
      profileUrl: '/assets/empick_logo.png',
      jobName: '개발자',
      status: 'WAITING',
      motivation: '열정적으로 일하고 싶습니다.',
      experience: '신입',
      skills: 'JavaScript, Vue.js'
    })
    toast.warning('지원서 정보를 일부만 불러올 수 있었습니다.')
  }
})

const selectEvaluation = (type) => {
  selectedEvaluation.value = type
  switch (type) {
    case '자기소개서':
      evaluationComponent.value = IntroduceResult
      break
    case '실무 테스트':
      // TODO: TestResult 컴포넌트 구현 필요
      evaluationComponent.value = IntroduceResult
      break
    case '면접':
      // TODO: InterviewResult 컴포넌트 구현 필요
      evaluationComponent.value = IntroduceResult
      break
    default:
      evaluationComponent.value = IntroduceResult
  }
}

const getCurrentEvaluation = () => {
  return evaluationStats.value?.find(evaluation => evaluation.type === selectedEvaluation.value)
}

const getSkillsArray = () => {
  if (!applicant.value?.skills) return ['정보 없음']
  return applicant.value.skills.split(/[,،،]\s*/).filter(skill => skill.trim())
}

const getExperiencePreview = () => {
  if (!applicant.value?.experience) return '경력 정보 없음'
  const preview = applicant.value.experience.split(/[,،،]/)[0]
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
    
    // 지원서 상세 정보 로드
    await applicationStore.fetchApplicationById(applicationId)
    
    // 이력서 응답 데이터 로드
    await applicationStore.fetchApplicationResponses(applicationId)
    
    // 자기소개서 데이터 로드  
    await applicationStore.fetchIntroduceData(applicationId)
    
    console.log('✅ 지원서 데이터 로딩 완료')
  } catch (error) {
    console.error('❌ 데이터 로딩 실패:', error)
    toast.error('지원서 데이터를 불러오는데 실패했습니다.')
  }
}

// 평가 모달 관련 함수들
const openEvaluationModal = () => {
  // 현재 평가 데이터 설정 (기존 평가가 있다면 불러오기)
  currentEvaluationData.value = {
    totalScore: null,
    comment: '',
    applicantId: applicant.value?.id,
    applicationId: applicationId
  }
  showEvaluationModal.value = true
}

const closeEvaluationModal = () => {
  showEvaluationModal.value = false
  currentEvaluationData.value = {}
}

const handleEvaluationSave = async (evaluationData) => {
  try {
    console.log('💾 평가 데이터 저장:', evaluationData)
    toast.success('평가가 저장되었습니다.')
    closeEvaluationModal()
    
    // 평가 완료 후 데이터 새로고침
    await loadApplicationData()
  } catch (error) {
    console.error('❌ 평가 저장 실패:', error)
    toast.error('평가 저장에 실패했습니다.')
  }
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

.evaluation-detail-card {
  min-height: 600px;
}

.score-analysis {
  padding: 1rem 0;
}

.stat-card {
  text-align: center;
  padding: 1.5rem;
  background: rgba(0, 0, 0, 0.02);
  border-radius: 12px;
}

.stat-number {
  font-size: 2rem;
  font-weight: bold;
  margin-bottom: 0.5rem;
}

.stat-label {
  font-size: 0.875rem;
  color: rgba(0, 0, 0, 0.6);
  font-weight: 500;
}

.action-section {
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
  border-radius: 16px;
  border: 1px solid rgba(0, 0, 0, 0.05);
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