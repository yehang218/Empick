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
                <v-img :src="applicant.pictureUrl" alt="프로필 사진" />
              </v-avatar>
              <div class="flex-grow-1">
                <h2 class="text-h5 font-weight-bold mb-1">{{ applicant.name }}</h2>
                <p class="text-body-2 text-grey mb-2">{{ applicant.jobTitle || '백엔드 개발자' }}</p>
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

            <div class="resume-section">
              <h4 class="text-subtitle-1 font-weight-bold mb-2 text-primary">기술 스택</h4>
              <div class="d-flex flex-wrap gap-2">
                <v-chip v-for="skill in getSkillsArray()" :key="skill" size="small" variant="tonal" color="blue">
                  {{ skill }}
                </v-chip>
              </div>
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
              <component :is="evaluationComponent" />
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
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import IntroduceResult from '@/components/employment/IntroduceEvaluationInput.vue'
// import InterviewResult from '@/components/employment/InterviewResult.vue'
// import TestResult from '@/components/employment/TestResult.vue'

const route = useRoute()
const router = useRouter()

const evaluationComponent = ref(IntroduceResult)
const selectedEvaluation = ref('자기소개서')
const viewMode = ref('detail')

// query parameter에서 받은 기본 정보로 applicant 객체 구성
const applicant = ref({
  name: '',
  birth: '',
  phone: '',
  email: '',
  address: '',
  pictureUrl: 'https://randomuser.me/api/portraits/women/1.jpg',
  status: '',
  jobTitle: '',
  motivation: '안정적이고 성장 가능성이 높은 기업에서 제 경험을 활용하여 더 나은 서비스를 만들어가고 싶습니다. 특히 귀사의 기술 스택과 개발 문화에 큰 매력을 느꼈습니다.',
  experience: '백엔드 개발 3년 경력, Spring Boot를 활용한 REST API 개발 및 마이크로서비스 아키텍처 구축 경험',
  skills: 'Java, Spring Boot, JPA, MySQL, Vue.js, Docker, AWS',
  evaluationStats: [
    { type: '자기소개서', score: 85, average: 76, result: '합격' },
    { type: '실무 테스트', score: 90, average: 65, result: '합격' },
    { type: '면접', score: 71, average: 78, result: '불합격' }
  ]
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
      average: 76, // 평균은 별도 계산 필요
      result: query.introduceStatus === '합격' ? '합격' : '불합격'
    })
  }

  // 실무테스트 평가
  if (query.jobtestEvaluationScore) {
    evaluationStats.push({
      type: '실무 테스트',
      score: parseFloat(query.jobtestEvaluationScore),
      average: 65, // 평균은 별도 계산 필요
      result: parseFloat(query.jobtestEvaluationScore) >= 70 ? '합격' : '불합격'
    })
  }

  // 면접 평가
  if (query.interviewScore) {
    evaluationStats.push({
      type: '면접',
      score: parseFloat(query.interviewScore),
      average: 78, // 평균은 별도 계산 필요
      result: parseFloat(query.interviewScore) >= 70 ? '합격' : '불합격'
    })
  }

  // 기본 평가 통계가 없으면 mock 데이터 사용
  if (evaluationStats.length === 0) {
    evaluationStats.push(
      { type: '자기소개서', score: 85, average: 76, result: '합격' },
      { type: '실무 테스트', score: 90, average: 65, result: '합격' },
      { type: '면접', score: 71, average: 78, result: '불합격' }
    )
  }

  applicant.value = {
    ...applicant.value,
    name: query.name || '정보 없음',
    birth: query.birth || '',
    phone: query.phone || '',
    email: query.email || '',
    address: query.address || '',
    status: query.status || 'WAITING',
    jobTitle: query.jobName || '백엔드 개발자',
    pictureUrl: query.profileUrl || 'https://randomuser.me/api/portraits/women/1.jpg',
    recruitmentId: query.recruitmentId || '',
    createdAt: query.createdAt || '',
    updatedAt: query.updatedAt || '',
    introduceRatingResultId: query.introduceRatingResultId || null,
    // 실제 데이터로 교체
    motivation: query.motivation || '안정적이고 성장 가능성이 높은 기업에서 제 경험을 활용하여 더 나은 서비스를 만들어가고 싶습니다.',
    experience: query.experience || '백엔드 개발 3년 경력, Spring Boot를 활용한 REST API 개발 및 마이크로서비스 아키텍처 구축 경험',
    skills: query.skills || 'Java, Spring Boot, JPA, MySQL, Vue.js, Docker, AWS',
    evaluationStats: evaluationStats
  }
})

const selectEvaluation = (type) => {
  selectedEvaluation.value = type

  switch (type) {
    case '자기소개서':
      evaluationComponent.value = IntroduceResult
      break
    case '실무 테스트':
      // evaluationComponent.value = TestResult
      break
    case '면접':
      // evaluationComponent.value = InterviewResult
      break
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
  router.push('/employment/applicant')
}
</script>