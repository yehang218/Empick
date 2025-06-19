<template>
  <input
    class="short-answer-input"
    type="text"
    :value="answer"
    @input="$emit('input', $event.target.value)"
    placeholder="답안을 입력하세요"
  />
</template>

<script setup>
defineProps({
  answer: [String, Number, null]
})
</script>

<style scoped>
.short-answer-input {
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
.short-answer-input:focus {
  border-color: #4A7C59;
  outline: none;
  background: #fff;
}
</style>