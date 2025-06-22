<template>
  <div class="evaluation-form">
    <!-- 기준표 제목/항목 템플릿 상세 스타일 -->
    <div class="content-card mb-6">
      <div class="field-group">
        <label class="field-label">기준표 제목</label>
        <v-text-field
          :model-value="localStandardTitle || '연결된 기준표가 없습니다.'"
          variant="outlined"
          readonly
          hide-details
          class="field-value-input"
        ></v-text-field>
            </div>
      <template v-if="localStandardItems && localStandardItems.length > 0">
        <div class="field-group">
          <label class="field-label">항목 목록</label>
          <div class="item-list-display">
            <div v-for="(item, index) in localStandardItems" :key="item.id" class="item-display">
              <v-text-field
                :model-value="item.title || item.content"
                variant="outlined"
                readonly
                hide-details
                class="field-value-input"
              ></v-text-field>
            </div>
          </div>
        </div>
      </template>
      <template v-else>
        <div class="field-group">
          <label class="field-label">항목 목록</label>
          <v-textarea
            model-value="연결된 자기소개서 항목이 없습니다."
            variant="outlined"
            readonly
            hide-details
            class="field-value-input no-items-textarea"
            rows="3"
          ></v-textarea>
        </div>
      </template>
    </div>
    <!-- 기준표 불러오기 버튼 -->
    <div class="mb-4" style="text-align:right;">
      <v-btn color="primary" @click="showStandardModal = true">기준표 불러오기</v-btn>
      </div>
    <!-- 선택된 기준표 표시 -->
    <div v-if="selectedStandard" class="mb-2" style="text-align:right; color:#1976d2;">
      선택된 기준표: {{ selectedStandard.content }}
    </div>
    <!-- 전체 항목 묶는 큰 박스 -->
    <!-- 항목별 평가 영역 전체 삭제 -->
    <!-- 총점 평가 -->
    <div class="total-score">
      <div class="total-header">
        <h3>{{ totalTitle }}</h3>
      </div>
      <div class="total-body">
        <div class="score-box total-align-right">
          <input
            type="number"
            v-model.number="localTotalScore"
            min="0"
            max="100"
          />
          <span>/100</span>
        </div>
        <textarea
          v-model="localComment"
          placeholder="지원자에 대한 총평을 남겨주세요."
        ></textarea>
      </div>
    </div>
    <!-- 기준표 선택 모달 -->
    <IntroduceStandardSelectModal v-model="showStandardModal" @select="onStandardSelect" />
    <div class="d-flex justify-end mt-4">
      <v-btn color="success" @click="handleSave" :loading="savingLoading">평가 저장</v-btn>
    </div>
  </div>
</template>

<script setup>
import { ref, defineProps, watchEffect, onMounted, watch, computed } from 'vue'
import { useRouter } from 'vue-router'
import IntroduceStandardSelectModal from './IntroduceStandardSelectModal.vue'
import { useIntroduceStore } from '@/stores/introduceStore'
import { useIntroduceStandardItemStore } from '@/stores/introduceStandardItemStore'
import { useIntroduceStandardStore } from '@/stores/introduceStandardStore'
import { useToast } from 'vue-toastification'

const router = useRouter()
const introduceStore = useIntroduceStore()
const standardItemStore = useIntroduceStandardItemStore()
const introduceStandardStore = useIntroduceStandardStore()
const toast = useToast()

const props = defineProps({
  totalTitle: {
    type: String,
    default: '자기소개서 총 평가'
  },
  evaluationData: {
    type: Object,
    default: () => ({})
  }
})

// ViewModel: 반응형 상태
const localTotalScore = ref(null)
const localComment = ref('')
const showStandardModal = ref(false)
const selectedStandard = ref(null)
const localStandardTitle = ref('')
const localStandardItems = ref([])
const savingLoading = ref(false)

// ViewModel: 데이터 초기화
watchEffect(async () => {
  if (props.evaluationData) {
    console.log('🔄 평가 데이터 초기화:', props.evaluationData)
    
    // 점수와 총평 복원
    const score = props.evaluationData.totalScore || props.evaluationData.ratingScore
    const comment = props.evaluationData.comment || props.evaluationData.content
    
    localTotalScore.value = score || null
    localComment.value = comment || ''
    
    console.log('📊 복원된 평가 데이터:', {
      score: localTotalScore.value,
      comment: localComment.value?.substring(0, 50) + '...',
      introduceStandardId: props.evaluationData.introduceStandardId
    })
    
    // 기존 평가에서 기준표 정보가 있으면 복원
    if (props.evaluationData.introduceStandardId) {
      try {
        console.log('🔍 기존 평가의 기준표 정보 복원 시도:', props.evaluationData.introduceStandardId)
        
        // 기준표 목록이 없으면 먼저 로드
        if (!introduceStandardStore.standards || introduceStandardStore.standards.length === 0) {
          console.log('📋 기준표 목록 로드 중...')
          await introduceStandardStore.fetchStandards()
        }
        
        // 기준표 찾기
        const existingStandard = introduceStandardStore.standards.find(standard => 
          standard.id == props.evaluationData.introduceStandardId
        )
        
        if (existingStandard) {
          console.log('✅ 기존 기준표 복원 성공:', {
            id: existingStandard.id,
            content: existingStandard.content
          })
          selectedStandard.value = existingStandard
          localStandardTitle.value = existingStandard.content
          
          // 기준표 상세 정보 로드
          try {
            await introduceStandardStore.fetchStandardDetail(existingStandard.id)
            if (introduceStandardStore.standardDetail && introduceStandardStore.standardDetail.items) {
              localStandardItems.value = introduceStandardStore.standardDetail.items
              console.log('✅ 기준표 항목 복원 완료:', localStandardItems.value.length, '개')
            }
          } catch (detailError) {
            console.warn('⚠️ 기준표 상세 정보 로드 실패:', detailError)
          }
        } else {
          console.warn('⚠️ 기존 기준표를 찾을 수 없습니다:', props.evaluationData.introduceStandardId)
          console.log('🔍 사용 가능한 기준표들:', introduceStandardStore.standards.map(s => ({
            id: s.id,
            content: s.content
          })))
        }
      } catch (standardError) {
        console.error('❌ 기준표 복원 실패:', standardError)
      }
    } else {
      console.log('ℹ️ 기준표 정보가 없습니다.')
    }
  } else {
    console.log('🔄 평가 데이터 초기화 (빈 상태)')
    localTotalScore.value = null
    localComment.value = ''
    selectedStandard.value = null
    localStandardTitle.value = ''
    localStandardItems.value = []
  }
})

// ViewModel: 이벤트 핸들러
const onStandardSelect = (standard) => {
  selectedStandard.value = standard
  localStandardTitle.value = standard.content
  localStandardItems.value = standard.items
}

const emit = defineEmits(['save'])

const handleSave = async () => {
  try {
    savingLoading.value = true
    
    // 입력 값 검증
    if (!localTotalScore.value || localTotalScore.value < 0 || localTotalScore.value > 100) {
      toast.error('평가 점수를 0~100 사이로 입력해주세요.')
      return
    }
    
    if (!localComment.value || localComment.value.trim() === '') {
      toast.error('총평을 입력해주세요.')
      return
    }
    
    const evaluationData = {
      content: localComment.value.trim(),
      ratingScore: localTotalScore.value,
      totalScore: localTotalScore.value,
      comment: localComment.value.trim(),
      applicantId: props.evaluationData?.applicantId,
      applicationId: props.evaluationData?.applicationId,
      introduceId: props.evaluationData?.introduceId,
      introduceStandardId: selectedStandard.value?.id,
      standardId: selectedStandard.value?.id
    }
    
    console.log('💾 자기소개서 평가 저장 데이터:', evaluationData)
    
    await introduceStore.saveIntroduceRatingResult(evaluationData)
    emit('save', evaluationData)
    toast.success('평가 결과가 성공적으로 저장되었습니다.')
    
  } catch (e) {
    console.error('평가 저장 실패:', e)
    toast.error('저장에 실패했습니다. 다시 시도해주세요.')
  } finally {
    savingLoading.value = false
  }
}
</script>

<style scoped>
.evaluation-form {
  max-width: 800px;
  margin: 0 auto;
  padding: 1.5rem;
  font-size: 0.95rem;
}

.all-criteria-wrapper {
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: #fff;
  padding: 1rem;
  margin-bottom: 2rem;
}

.criteria-group {
  margin-bottom: 2rem;
}

.criteria-info {
  background-color: #f5f5f5;
  border-radius: 8px;
  border: 1px solid #e0e0e0;
  padding: 1rem;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.criteria-info .left h3 {
  margin: 0;
  font-weight: bold;
}

.criteria-info .left .question {
  margin-top: 4px;
  font-size: 0.9rem;
  color: #444;
}

.criteria-input {
  margin-top: 0.5rem;
  background-color: #fff;
}

.textarea-wrapper {
  position: relative;
}

textarea {
  width: 100%;
  height: 70px;
  padding: 0.5rem;
  padding-right: 90px;
  resize: none;
  border: 1px solid #ccc;
  border-radius: 6px;
  font-size: 0.9rem;
  box-sizing: border-box;
}

.score-overlay {
  position: absolute;
  top: 50%;
  right: 10px;
  transform: translateY(-50%);
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 0.9rem;
}

.score-overlay input {
  width: 35px;
  height: 24px;
  padding: 2px;
  text-align: center;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 0.9rem;
}

.total-score {
  background-color: #fff;
  border-radius: 6px;
  padding: 1rem;
  border: 1px solid #ddd;
}

.total-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 0.5rem;
}

.total-body {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.total-body .score-box.total-align-right {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 4px;
}

.total-body .score-box.total-align-right input {
  width: 35px;
  height: 24px;
  padding: 2px;
  text-align: center;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 0.9rem;
}

/* number input 스핀 제거 */
input[type="number"]::-webkit-inner-spin-button,
input[type="number"]::-webkit-outer-spin-button {
  -webkit-appearance: none;
  margin: 0;
}
input[type="number"] {
  -moz-appearance: textfield;
}

.criteria-item-box {
  margin-bottom: 8px;
  display: flex;
  align-items: center;
}

.content-card {
  background-color: #fff;
  border-radius: 12px;
  padding: 32px 40px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.06);
}
.field-group {
  margin-bottom: 24px;
}
.field-label {
  font-size: 1.1rem;
  font-weight: bold;
  color: #555;
  margin-bottom: 8px;
  display: block;
}
.field-value-input.v-text-field .v-input__control,
.field-value-input.v-textarea .v-input__control {
  background-color: #f8f8f8;
  border-radius: 8px;
  box-shadow: none !important;
}
.field-value-input .v-field__outline {
  border-color: #e0e0e0 !important;
}
.field-value-input.v-text-field.v-input--density-compact .v-field--variant-outlined,
.field-value-input.v-textarea.v-input--density-compact .v-field--variant-outlined {
  padding: 8px 12px;
}
.item-list-display .item-display {
  margin-bottom: 16px;
}
.item-list-display .item-display:last-child {
  margin-bottom: 0;
}
.no-items-textarea.v-textarea .v-input__control textarea {
  color: #888;
  font-style: italic;
}
</style>
