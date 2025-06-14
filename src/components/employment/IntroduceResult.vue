<template>
  <v-container fluid>
    <h2 class="text-h6 font-weight-bold mb-6">자기소개서 평가</h2>
    <v-card class="pa-6 introduce-card">
      <!-- 지원서 상태 수정 버튼 -->
      <div class="d-flex justify-end mb-4">
        <v-btn color="primary" @click="updateStatus">
          지원서 상태 수정
        </v-btn>
      </div>

      <div v-for="(item, index) in questions" :key="index" class="mb-6">
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
          />
          <div class="d-flex align-center">
            <v-text-field
              v-model.number="item.score"
              hide-details
              density="compact"
              variant="outlined"
              type="number"
              class="score-input"
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
            class="score-input"
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
    </v-card>
  </v-container>
</template>

<script setup>
import { ref } from 'vue'

const questions = ref([
  {
    title: '자신감',
    weight: 10,
    description: '지원자 답변 기준을 바탕으로 자신감의 정도를 평가해 주세요.',
    input: '',
    score: null
  },
  {
    title: '기술력',
    weight: 40,
    description: '기술에 대한 이해도와 깊이를 평가해 주세요.',
    input: '',
    score: null
  }
])

const finalScore = ref(null)
const overallFeedback = ref('')

const updateStatus = () => {
  alert('지원서 상태 수정 로직 연결 예정!')
}
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
