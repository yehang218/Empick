<template>
  <div class="exam-layout">
    <div class="exam-sidebar-wrap">
      <ExamSidebar :testInfo="testInfo" :currentIndex="currentIndex" :totalQuestions="questions.length"
        :timeLeft="timeLeft" @moveTo="moveTo" @submit="submitTest" />
    </div>
    <div class="exam-question-area">
      <QuestionView :question="questions[currentIndex]" :answer="answers[currentIndex] ?? ''"
        :questionIndex="currentIndex" @updateAnswer="updateAnswer" />
      <div class="nav-buttons">
        <button @click="prev" :disabled="currentIndex === 0">이전</button>
        <button v-if="currentIndex < questions.length - 1" @click="next">다음</button>
        <button v-else class="submit-btn" @click="submitTest">제출하기</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useJobtestExamStore } from '@/stores/jobtestExamStore'
import ExamSidebar from '@/components/employment/ExamSidebar.vue'
import QuestionView from '@/components/employment/QuestionView.vue'
import { QUESTION_TYPES } from '@/constants/employment/questionTypes'
import { postAnswer } from '@/services/answerService'
import AnswerRequestDTO from '@/dto/employment/jobtest/answerRequestDTO'

const store = useJobtestExamStore()
const route = useRoute()
const router = useRouter()

const examData = computed(() => store.examData)
const questions = computed(() => examData.value?.questions ?? [])
const testInfo = computed(() => ({
  title: examData.value?.title ?? '',
}))

const currentIndex = ref(0)
const answers = ref([])

const getStorageKey = () => `jobtest-answers-${examData.value?.applicationJobTestId}`

onMounted(() => {
  const saved = localStorage.getItem(getStorageKey())
  if (saved) {
    try {
      const parsed = JSON.parse(saved)
      if (Array.isArray(parsed)) {
        answers.value = parsed
      }
    } catch (e) { /* ignore */ }
  }
})

watch(answers, (val) => {
  localStorage.setItem(getStorageKey(), JSON.stringify(val))
}, { deep: true })

const timeLeft = ref(40 * 60) // 40분 (예시)

function updateAnswer(val) {
  answers.value[currentIndex.value] = val
}

async function saveCurrentAnswer(idx = currentIndex.value) {
  const question = questions.value[idx]
  const answer = answers.value[idx]
  let content = ''
  if (question.type === QUESTION_TYPES.MULTIPLE) {
    content = question.options[answer]?.content || ''
  } else {
    content = answer || ''
  }
  const dto = new AnswerRequestDTO({
    content,
    applicationJobTestId: examData.value.applicationJobTestId,
    questionId: question.questionId
  })
  await store.saveAnswer(dto)
}

function prev() {
  if (currentIndex.value > 0) {
    saveCurrentAnswer()
    currentIndex.value--
  }
}

async function next() {
  await saveCurrentAnswer()
  if (currentIndex.value < questions.value.length - 1) currentIndex.value++
}

function moveTo(idx) {
  saveCurrentAnswer()
  currentIndex.value = idx
}

async function submitTest() {
  await saveCurrentAnswer()
  // 이후 제출 완료 처리 (예: 서버에 전체 제출 처리 요청, 결과 페이지 이동 등)
  // router.push({ name: 'JobtestExamResult', params: { applicationJobTestId: examData.value.applicationJobTestId } });
  alert('제출이 완료되었습니다!');
}
</script>

<style scoped>
.exam-layout {
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  min-height: 100vh;
  background: #20432b;
  /* 좌측 배경색, 필요시 조정 */
}

.exam-sidebar-wrap {
  flex: 0 0 340px;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  min-height: 100vh;
  background: transparent;
  padding-top: 60px;
}

.exam-question-area {
  flex: 1 1 0;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: flex-start;
  min-height: 100vh;
  background: #fff;
  border-radius: 24px;
  margin: 40px 0 40px 0;
  padding: 48px 56px 56px 56px;
  box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.10);
  margin-left: 24px;
  max-width: 900px;
  width: 100%;
}

.nav-buttons {
  display: flex;
  gap: 16px;
  margin-top: 32px;
  justify-content: flex-end;
  width: 100%;
}

.nav-buttons button {
  background: #4A7C59;
  color: #fff;
  border: none;
  border-radius: 8px;
  padding: 12px 28px;
  font-size: 1.05rem;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.2s;
}

.nav-buttons button:disabled {
  background: #E5E5E5;
  color: #888;
  cursor: not-allowed;
}
</style>