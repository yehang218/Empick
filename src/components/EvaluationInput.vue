<template>
  <div class="evaluation-form">
    <!-- 전체 항목 묶는 큰 박스 -->
    <div class="all-criteria-wrapper">
      <div class="evaluation-box">
        <div
          v-for="(item, index) in criteria"
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

          <!-- 입력 블록 (테두리 없음) -->
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

    <!-- 총점 평가만 별도 박스 유지 -->
    <div class="total-score">
      <div class="total-header">
        <h3>테스트 / 면접 총 평가</h3>
      </div>
      <div class="total-body">
        <div class="score-box total-align-right">
          <input
            type="number"
            v-model.number="totalScore"
            min="0"
            max="100"
          />
          <span>/100</span>
        </div>
        <textarea
          v-model="comment"
          placeholder="지원자에 대한 총평을 남겨주세요."
        ></textarea>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'

const criteria = reactive([
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
])

const totalScore = ref(null)
const comment = ref('')
</script>

<style scoped>
.evaluation-form {
  max-width: 800px;
  margin: 0 auto;
  padding: 1.5rem;
  font-size: 0.95rem;
}

/* 전체 묶는 큰 박스 */
.all-criteria-wrapper {
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: #fff; /* 흰색 배경 */
  padding: 1rem;
  margin-bottom: 2rem;
}

/* 개별 항목 묶음 */
.criteria-group {
  margin-bottom: 2rem;
}

/* 상단 설명 칸: 회색 배경 유지 */
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

/* 입력 칸 (하단): 흰색 배경 */
.criteria-input {
  margin-top: 0.5rem;
  background-color: #fff;
}

/* 입력 박스 내부 */
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

/* 총점 영역 */
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

/* 🔻 number input의 위아래 스핀 버튼 제거 */
input[type="number"]::-webkit-inner-spin-button,
input[type="number"]::-webkit-outer-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

input[type="number"] {
  -moz-appearance: textfield; /* Firefox */
}
</style>
