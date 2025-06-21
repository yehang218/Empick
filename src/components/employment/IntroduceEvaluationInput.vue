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
      <v-btn color="success" @click="handleSave">저장</v-btn>
    </div>
  </div>
</template>

<script setup>
import { ref, defineProps, watchEffect, onMounted, watch, computed } from 'vue'
import { useRouter } from 'vue-router'
import IntroduceStandardSelectModal from './IntroduceStandardSelectModal.vue'
import { useIntroduceStore } from '@/stores/introduceStore'
import { useIntroduceStandardItemStore } from '@/stores/introduceStandardItemStore'

const router = useRouter()
const introduceStore = useIntroduceStore()
const standardItemStore = useIntroduceStandardItemStore()

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

const localTotalScore = ref(null)
const localComment = ref('')

const showStandardModal = ref(false)
const selectedStandard = ref(null)

const localStandardTitle = ref('')
const localStandardItems = ref([])

watchEffect(() => {
  if (props.evaluationData) {
    localTotalScore.value = props.evaluationData.totalScore || null
    localComment.value = props.evaluationData.comment || ''
  } else {
    localTotalScore.value = null
    localComment.value = ''
  }
})

const onStandardSelect = (standard) => {
  selectedStandard.value = standard
  localStandardTitle.value = standard.content
  localStandardItems.value = standard.items
}

const handleSave = async () => {
  try {
    await introduceStore.saveIntroduceRatingResult({
      content: localComment.value,
      ratingScore: localTotalScore.value,
      // 필요시 applicantId, standardId 등 추가
    })
    alert('평가 결과가 저장되었습니다.')
  } catch (e) {
    alert('저장에 실패했습니다.')
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
