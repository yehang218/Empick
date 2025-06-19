<template>
  <div class="question-view">
    <div class="question-header">
      <span class="question-number">{{ questionIndex + 1 }}.</span>
      <span class="question-title">{{ question.title }}</span>
      <span class="question-score">({{ question.score }}점)</span>
    </div>
    <div v-if="question.type === QUESTION_TYPES.MULTIPLE">
      <MultipleChoice
        :options="question.options"
        :answer="answer"
        @select="val => $emit('updateAnswer', val)"
      />
    </div>
    <div v-else-if="question.type === QUESTION_TYPES.SUBJECTIVE">
      <ShortAnswer
        :answer="answer"
        @input="$emit('updateAnswer', $event.target.value)"
      />
    </div>
  </div>
</template>
<script setup>
import MultipleChoice from '@/components/employment/MultipleChoice.vue'
import ShortAnswer from '@/components/employment/ShortAnswer.vue'
import { QUESTION_TYPES } from '@/constants/employment/questionTypes'

const props = defineProps({
  question: Object,
  answer: [Number, String, null],
  questionIndex: {
    type: Number,
    default: 0
  }
})
</script>
<style scoped>
.question-view {
  width: 100%;
  margin-bottom: 32px;
}
.question-header {
  display: flex;
  align-items: baseline;
  gap: 8px;
  font-size: 1.6rem;
  font-weight: 700;
  margin-bottom: 32px;
}
.question-number {
  color: #222;
}
.question-title {
  color: #222;
}
.question-score {
  font-size: 1.1rem;
  font-weight: 400;
  color: #666;
  margin-left: 8px;
}
.subjective-input {
  width: 100%;
  max-width: 600px;
  padding: 18px 20px;
  font-size: 1.1rem;
  border: 2px solid #e5e5e5;
  border-radius: 8px;
  margin-bottom: 16px;
  background: #fafbfc;
  transition: border 0.2s;
}
.subjective-input:focus {
  border-color: #4A7C59;
  outline: none;
  background: #fff;
}
</style>