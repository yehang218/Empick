<template>
  <div class="evaluation-form">
    <div v-for="(item, index) in criteria" :key="index" class="criteria-block">
      <h3>{{ index + 1 }}. {{ item.title }}</h3>
      <p>{{ item.question }}</p>

      <div class="score-input">
        <span class="weight">가중치 {{ item.weight }}%</span>
        <div class="score-row">
          <input
            type="number"
            v-model.number="item.score"
            min="0"
            max="100"
            placeholder="0"
          />
          <span class="out-of">/ 100</span>
        </div>
      </div>

      <textarea
        v-model="item.comment"
        placeholder="해당 항목에 대한 평가 의견을 작성해주세요."
      ></textarea>
    </div>

    <div class="total-score">
      <h3>테스트 / 면접 총 평가</h3>
      <div class="score-input">
        <span class="weight">총점</span>
        <div class="score-row">
          <input
            type="number"
            v-model.number="totalScore"
            min="0"
            max="100"
            placeholder="0"
          />
          <span class="out-of">/ 100</span>
        </div>
      </div>
      <textarea
        v-model="comment"
        placeholder="지원자에 대한 총평을 남겨주세요."
      ></textarea>
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
  max-width: 1500px;
  margin: 0 auto;
  padding: 1.5rem;
  border-radius: 8px;
  background-color: #f9f9f9;
}

.criteria-block {
  border: 1px solid #ddd;
  border-radius: 6px;
  margin-bottom: 1.5rem;
  padding: 1rem;
  background-color: #fff;
}

.score-input {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  margin-bottom: 0.5rem;
}

.score-input .weight {
  font-weight: bold;
  color: #1a8917;
  margin-bottom: 4px;
}

.score-input .score-row {
  display: flex;
  align-items: center;
}

.score-row input {
  width: 60px;
  padding: 4px;
  text-align: right;
}

.out-of {
  margin-left: 6px;
  color: #999;
}

textarea {
  width: 100%;
  margin-top: 0.5rem;
  height: 70px;
  padding: 0.5rem;
  resize: none;
  border: 1px solid #ccc;
  border-radius: 4px;
}
</style>
