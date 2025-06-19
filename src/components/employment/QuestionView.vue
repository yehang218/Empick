<template>
    <div>
      <div class="question-title">{{ question.title }}</div>
      <div v-if="question.type === 'multiple'">
        <MultipleChoice
          :options="question.options"
          :answer="answer"
          @select="val => $emit('updateAnswer', val)"
        />
      </div>
      <div v-else-if="question.type === 'short'">
        <ShortAnswer
          :answer="answer"
          @input="val => $emit('updateAnswer', val)"
        />
      </div>
    </div>
  </template>
  <script setup>
  
  defineProps({
    question: Object,
    answer: [Number, String, null]
  })
  </script>