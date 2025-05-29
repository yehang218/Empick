<template>
  <div class="evaluation-form">
    <div class="evaluation-box">
      <div v-for="(item, index) in criteria" :key="index" class="criteria-block">
        <div class="criteria-header">
          <div>
            <h3>{{ index + 1 }}. {{ item.title }}</h3>
            <p>{{ item.question }}</p>
          </div>
          <span class="weight">가중치 {{ item.weight }}%</span>
        </div>

        <div class="criteria-body">
          <div class="textarea-wrapper">
            <textarea
              v-model="item.comment"
              placeholder="해당 항목에 대한 평가 의견을 작성해주세요."
            ></textarea>
            <div class="score-overlay">
              <input
                type="number"
                v-model.number="item.score"
                min="0"
                max="100"
              />
              <span>/ 100</span>
            </div>
          </div>
        </div>
      </div>
    </div>

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
            placeholder=""
          />
          <span>/ 100</span>
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

.evaluation-box {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 1rem;
  background-color: #f8f9fa;
  margin-bottom: 2rem;
}

.criteria-block {
  background-color: #f1f3f5;
  border-radius: 6px;
  padding: 1rem;
  margin-bottom: 1rem;
}

.criteria-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.5rem;
}

.weight {
  font-weight: bold;
  color: #1a8917;
}

.criteria-body {
  display: flex;
  justify-content: space-between;
  gap: 1rem;
  align-items: center;
}

.textarea-wrapper {
  position: relative;
  flex: 1;
}

textarea {
  width: 100%;
  height: 70px;
  padding: 0.5rem;
  padding-right: 90px; /* 점수 입력칸 공간 확보 */
  resize: none;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
}

.score-overlay {
  position: absolute;
  top: 50%; /* 바닥 말고 중앙으로! */
  right: 10px;
  transform: translateY(-50%);
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 0.9rem;
}

.score-overlay input {
  width: 35px;
  height: 24px; /* 숫자와 높이 맞춤 */
  padding: 2px;
  text-align: right;
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
  padding: 2px;
  text-align: right;
  border: 1px solid #ccc;
  border-radius: 4px;
}
</style>
