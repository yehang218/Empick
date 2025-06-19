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
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

import { useJobtestExamStore } from '@/stores/jobtestExamStore'

import ExamSidebar from '@/components/employment/ExamSidebar.vue'
import QuestionView from '@/components/employment/QuestionView.vue'

const store = useJobtestExamStore()
const route = useRoute()
const router = useRouter()

const examData = computed(() => store.examData)
const questions = computed(() => examData.value?.questions ?? [])
const testInfo = computed(() => ({
    title: examData.value?.title ?? '',
}))

const currentIndex = ref(0)
const answers = ref(Array(questions.value.length).fill(null))


function updateAnswer(val) {
    answers.value[currentIndex.value] = val
}
function prev() { if (currentIndex.value > 0) currentIndex.value-- }
function next() { if (currentIndex.value < questions.value.length - 1) currentIndex.value++ }
function moveTo(idx) { currentIndex.value = idx }
function submitTest() { /* 제출 로직 */ }

</script>