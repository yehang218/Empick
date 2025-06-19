<template>
    <div class="test-container">
      <ExamSidebar
        :testInfo="testInfo"
        :currentIndex="currentIndex"
        :totalQuestions="questions.length"
        :timeLeft="timeLeft"
        @moveTo="moveTo"
        @submit="submitTest"
      />
      <div class="question-area">
        <QuestionView
          :question="questions[currentIndex]"
          :answer="answers[currentIndex]"
          @updateAnswer="updateAnswer"
        />
        <div class="nav-buttons">
          <button @click="prev" :disabled="currentIndex === 0">이전</button>
          <button @click="next" :disabled="currentIndex === questions.length - 1">다음</button>
        </div>
      </div>
    </div>
  </template>

<script setup>
import { ref } from 'vue'
import ExamSidebar from '@/components/employment/ExamSidebar.vue'
import QuestionView from '@/components/employment/QuestionView.vue'

const testInfo = { title: '[인턴] 2025 하반기 현장실습인턴 - Backend 개발자' }
const questions = [
  { type: 'multiple', title: 'Big-O 표기법에 대한 설명으로 옳은것은?', options: ['...', '...', '...', '...'] },
  { type: 'short', title: '스택(Stack) 자료구조의 연산 순서는?' },
  // ...
]
const answers = ref(Array(questions.length).fill(null))
const currentIndex = ref(0)
const timeLeft = ref(40 * 60) // 40분

function updateAnswer(val) {
  answers.value[currentIndex.value] = val
}
function prev() { if (currentIndex.value > 0) currentIndex.value-- }
function next() { if (currentIndex.value < questions.length - 1) currentIndex.value++ }
function moveTo(idx) { currentIndex.value = idx }
function submitTest() { /* 제출 로직 */ }
</script>