<template>
  <div class="exam-layout">
    <div class="exam-sidebar-wrap">
      <ExamSidebar :testInfo="testInfo" :currentIndex="currentIndex" :totalQuestions="questions.length"
        :timeLeft="timeLeft" @moveTo="handleMoveTo" @submit="openSubmitModal" />
    </div>
    <div class="exam-question-area">
      <QuestionView :question="questions[currentIndex]" :answer="answers[currentIndex] ?? ''"
        :questionIndex="currentIndex" @updateAnswer="updateAnswer" />
      <div class="nav-buttons">
        <button @click="async () => await prev()" :disabled="currentIndex === 0">이전</button>
        <button v-if="currentIndex < questions.length - 1" @click="next">다음</button>
        <button v-else class="submit-btn" @click="openSubmitModal">제출하기</button>
      </div>
    </div>
    <Modal v-if="showSubmitModal" message="제출하시겠습니까?" @confirm="handleSubmitConfirm" @cancel="closeSubmitModal" />
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useJobtestExamStore } from '@/stores/jobtestExamStore'
import ExamSidebar from '@/components/employment/ExamSidebar.vue'
import QuestionView from '@/components/employment/QuestionView.vue'
import { QUESTION_TYPES } from '@/constants/employment/questionTypes'
import AnswerRequestDTO from '@/dto/employment/jobtest/answerRequestDTO'
import Modal from '@/components/common/Modal.vue'

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

const showSubmitModal = ref(false)

function updateAnswer(val) {
  console.log('🔄 updateAnswer 호출:', {
    currentIndex: currentIndex.value,
    newValue: val,
    valueType: typeof val,
    currentAnswer: answers.value[currentIndex.value]
  })
  answers.value[currentIndex.value] = val
  console.log('✅ 답안 업데이트 완료:', answers.value[currentIndex.value])
}

async function saveCurrentAnswer(idx = currentIndex.value) {
  const question = questions.value[idx]
  const answer = answers.value[idx]
  
  console.log('🔍 saveCurrentAnswer 디버깅:', {
    questionType: question.type,
    questionId: question.questionId,
    answer: answer,
    answerType: typeof answer
  })
  
  let content = ''
  if (question.type === QUESTION_TYPES.MULTIPLE) {
    content = question.options[answer]?.content || ''
    console.log('📝 객관식 답안:', content)
  } else {
    // 단답형/서술형의 경우 answer 값을 직접 사용
    content = answer || ''
    console.log('📝 단답형/서술형 답안:', content)
  }
  
  // 답안이 비어있으면 저장하지 않음
  if (!content.trim()) {
    console.log('⚠️ 답안이 비어있어 저장하지 않습니다.')
    return
  }
  
  const dto = new AnswerRequestDTO({
    content,
    applicationJobTestId: examData.value.applicationJobTestId,
    questionId: question.questionId
  })
  
  console.log('📤 저장할 답안 DTO:', dto.toJSON())
  
  try {
    await store.saveAnswer(dto)
    console.log('✅ 답안 저장 성공')
  } catch (error) {
    console.error('❌ 답안 저장 실패:', error)
  }
}

async function prev() {
  if (currentIndex.value > 0) {
    await saveCurrentAnswer()
    currentIndex.value--
  }
}

async function next() {
  await saveCurrentAnswer()
  if (currentIndex.value < questions.value.length - 1) currentIndex.value++
}

async function handleMoveTo(idx) {
  await saveCurrentAnswer()
  currentIndex.value = idx
}

function openSubmitModal() {
  showSubmitModal.value = true
}

function closeSubmitModal() {
  showSubmitModal.value = false
}

async function handleSubmitConfirm() {
  await saveCurrentAnswer()
  await store.submitAnswers(examData.value.applicationJobTestId)
  showSubmitModal.value = false
  router.push({ name: 'JobtestEnter', params: { jobtestId: examData.value.jobtestId } })
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