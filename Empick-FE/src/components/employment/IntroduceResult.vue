<template>
  <v-container fluid>
    <h2 class="text-h6 font-weight-bold mb-6">자기소개서 평가</h2>
    <v-card class="pa-6 introduce-card">
      <!-- 지원서 상태 수정 버튼 -->
      <div class="d-flex justify-end mb-4">
        <v-btn color="primary" @click="updateStatus" :loading="loading">
          지원서 상태 수정
        </v-btn>
      </div>

      <div v-for="(item, index) in evaluationCriteria" :key="item.id || index" class="mb-6">
        <!-- 기준 항목 -->
        <v-card class="pa-4 mb-2 criteria-card">
          <div class="d-flex justify-space-between align-center">
            <strong>{{ index + 1 }}. {{ item.title }}</strong>
            <span class="grey--text font-weight-medium">가중치 {{ item.weight }}%</span>
          </div>
        </v-card>

        <!-- 평가 + 점수 박스 -->
        <v-card class="score-card d-flex align-center pa-4">
          <textarea
            v-model="item.input"
            class="evaluation-textarea no-border"
            :placeholder="item.description"
            @input="handleEvaluationChange(item.id, 'input', $event.target.value)"
          />
          <div class="d-flex align-center">
            <v-text-field
              v-model.number="item.score"
              hide-details
              density="compact"
              variant="outlined"
              type="number"
              min="0"
              max="100"
              class="score-input"
              @input="handleEvaluationChange(item.id, 'score', $event.target.value)"
            />
            <span class="ml-1 grey--text text-subtitle-2">/100</span>
          </div>
        </v-card>
      </div>

      <!-- 총 평가 점수 -->
      <div class="d-flex align-center justify-space-between mt-6">
        <span class="font-weight-bold">자기소개서 총 평가</span>
        <div class="d-flex align-center">
          <v-text-field
            v-model.number="finalScore"
            hide-details
            density="compact"
            variant="outlined"
            type="number"
            min="0"
            max="100"
            class="score-input"
            @input="calculateTotalScore"
          />
          <span class="ml-1 grey--text text-subtitle-2">/100</span>
        </div>
      </div>

      <!-- 종합 평가 -->
      <v-textarea
        v-model="overallFeedback"
        placeholder="지원자에 대한 종합평을 남겨주세요."
        variant="outlined"
        rows="4"
        auto-grow
        class="mt-2"
      />

      <!-- 저장 버튼 -->
      <div class="d-flex justify-end mt-4">
        <v-btn color="success" @click="saveEvaluation" :loading="savingLoading">
          평가 저장
        </v-btn>
      </div>
    </v-card>
  </v-container>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import { useToast } from 'vue-toastification'
import { useIntroduceStore } from '@/stores/introduceStore'
import { useApplicationStore } from '@/stores/applicationStore'

// Props
const props = defineProps({
  applicant: {
    type: Object,
    default: () => ({})
  },
  evaluationData: {
    type: Object,
    default: () => ({})
  }
})

// Stores (Model)
const introduceStore = useIntroduceStore()
const applicationStore = useApplicationStore()
const toast = useToast()

// ViewModel: 반응형 데이터
const loading = ref(false)
const savingLoading = ref(false)
const evaluationCriteria = ref([])
const finalScore = ref(null)
const overallFeedback = ref('')

// ViewModel: 계산된 속성
const totalWeightedScore = computed(() => {
  return evaluationCriteria.value.reduce((total, item) => {
    const score = item.score || 0
    const weight = item.weight || 0
    return total + (score * weight / 100)
  }, 0)
})

// ViewModel: 초기 데이터 설정
const initializeEvaluationData = () => {
  // 기존 평가 데이터가 있으면 로드
  if (props.evaluationData && props.evaluationData.items) {
    evaluationCriteria.value = props.evaluationData.items.map(item => ({
      id: item.id,
      title: item.title,
      weight: item.weight || 0,
      description: item.description || `${item.title}에 대해 평가해 주세요.`,
      input: item.input || '',
      score: item.score || null
    }))
    finalScore.value = props.evaluationData.totalScore || null
    overallFeedback.value = props.evaluationData.comment || ''
  } else {
    // 기본 평가 기준 설정
    evaluationCriteria.value = [
      {
        id: 1,
        title: '자신감',
        weight: 10,
        description: '지원자 답변 기준을 바탕으로 자신감의 정도를 평가해 주세요.',
        input: '',
        score: null
      },
      {
        id: 2,
        title: '기술력',
        weight: 40,
        description: '기술에 대한 이해도와 깊이를 평가해 주세요.',
        input: '',
        score: null
      },
      {
        id: 3,
        title: '협업능력',
        weight: 30,
        description: '팀과의 협업 능력을 평가해 주세요.',
        input: '',
        score: null
      },
      {
        id: 4,
        title: '성장가능성',
        weight: 20,
        description: '향후 성장 가능성을 평가해 주세요.',
        input: '',
        score: null
      }
    ]
  }
}

// ViewModel: 이벤트 핸들러
const handleEvaluationChange = (itemId, field, value) => {
  const item = evaluationCriteria.value.find(criteria => criteria.id === itemId)
  if (item) {
    if (field === 'score') {
      item[field] = value ? Number(value) : null
    } else {
      item[field] = value
    }
  }
}

const calculateTotalScore = () => {
  // 가중평균 계산 또는 직접 입력값 사용
  if (finalScore.value === null || finalScore.value === undefined) {
    finalScore.value = Math.round(totalWeightedScore.value)
  }
}

const updateStatus = async () => {
  try {
    loading.value = true
    // TODO: 상태 변경 API 호출
    toast.info('상태 변경 기능은 구현 예정입니다.')
  } catch (error) {
    console.error('상태 변경 실패:', error)
    toast.error('상태 변경에 실패했습니다.')
  } finally {
    loading.value = false
  }
}

const saveEvaluation = async () => {
  try {
    savingLoading.value = true
    
    const evaluationData = {
      applicantId: props.applicant.applicantId,
      applicationId: props.applicant.applicationId,
      totalScore: finalScore.value,
      comment: overallFeedback.value,
      items: evaluationCriteria.value.map(item => ({
        id: item.id,
        title: item.title,
        weight: item.weight,
        input: item.input,
        score: item.score
      }))
    }
    
    await introduceStore.saveIntroduceRatingResult(evaluationData)
    toast.success('평가 결과가 성공적으로 저장되었습니다.')
    
  } catch (error) {
    console.error('평가 저장 실패:', error)
    toast.error('평가 저장에 실패했습니다.')
  } finally {
    savingLoading.value = false
  }
}

// 생명주기: 컴포넌트 마운트 시 초기화
onMounted(() => {
  initializeEvaluationData()
})

// ViewModel: props 변경 감시
watch(() => props.evaluationData, () => {
  initializeEvaluationData()
}, { deep: true })
</script>

<style scoped>
.introduce-card {
  border-radius: 12px;
  border: 1px solid #dcdcdc;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
  background-color: #fff;
}

.criteria-card {
  background-color: #f9f9f9;
  border: 1px solid #ccc;
  border-radius: 8px;
}

.score-card {
  border: 1px solid #ccc;
  background-color: #ffffff;
  border-radius: 8px;
  gap: 12px;
  min-height: 120px;
  display: flex;
  justify-content: space-between;
}

.evaluation-textarea {
  width: calc(100% - 80px);
  resize: none;
  padding: 12px;
  font-size: 14px;
  border: none;
  border-radius: 6px;
  background-color: transparent;
  color: #333;
  height: 100%;
}

.evaluation-textarea::placeholder {
  color: #999;
}

.no-border {
  border: none !important;
  outline: none;
}

.score-input {
  width: 60px;
  min-width: 74px;
  max-width: 60px;
  align-self: center;
}

.font-weight-medium {
  font-weight: 500;
  color: #666;
}
</style>
