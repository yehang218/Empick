<template>
  <div class="evaluation-form">
    <!-- 전체 항목 묶는 큰 박스 -->
    <div class="all-criteria-wrapper">
      <div class="evaluation-box">
        <div
          v-for="(item, index) in localCriteria"
          :key="index"
          class="criteria-group"
        >
          <!-- 상단 정보 블록 -->
          <div class="criteria-info">
            <div class="left">
              <h3>{{ index + 1 }}. {{ item.title }}</h3>
              <p class="question">{{ item.question }}</p>
            </div>
            <div class="right">
              <span class="weight">가중치 <strong>{{ item.weight }}%</strong></span>
            </div>
          </div>

          <!-- 입력 블록 -->
          <div class="criteria-input">
            <div class="textarea-wrapper">
              <textarea
                v-model="item.comment"
                placeholder="제시된 평가 기준을 바탕으로 지원자를 평가해 주세요."
              ></textarea>
              <div class="score-overlay">
                <input
                  type="number"
                  v-model.number="item.score"
                  min="0"
                  max="100"
                />
                <span>/100</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

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
  </div>
</template>

<script setup>
import { reactive, ref, defineProps, watchEffect } from 'vue'

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

const localCriteria = reactive([])
const localTotalScore = ref(null)
const localComment = ref('')

watchEffect(() => {
  if (props.evaluationData && props.evaluationData.items) {
    // 평가 항목 데이터를 동적으로 설정
    localCriteria.splice(0, localCriteria.length, ...props.evaluationData.items.map(item => ({
      title: item.title,
      question: item.question || '',
      weight: item.weight || 0,
      score: item.score || null,
      comment: item.comment || '',
    })))
    // 총점과 총평 초기화
    localTotalScore.value = props.evaluationData.totalScore || null
    localComment.value = props.evaluationData.comment || ''
  } else {
    // 데이터가 없을 경우 기본값으로 초기화
    localCriteria.splice(0, localCriteria.length, ...[
      {
        title: '자신감',
        question: '자신감이 있는가?',
        weight: 10,
        score: null,
        comment: '',
      },
      {
        title: '기술력',
        question: '기술에 대한 이해도가 높은가?',
        weight: 40,
        score: null,
        comment: '',
      },
      {
        title: '협업능력',
        question: '팀과 잘 협력할 수 있는가?',
        weight: 30,
        score: null,
        comment: '',
      },
    ])
    localTotalScore.value = null
    localComment.value = ''
  }
})

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

.criteria-info .right .weight {
  font-size: 0.9rem;
  color: #1a8917;
  background: #eef6ee;
  padding: 4px 8px;
  border-radius: 12px;
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
</style>
