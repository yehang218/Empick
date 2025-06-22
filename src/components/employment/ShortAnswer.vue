<template>
  <input
    class="short-answer-input"
    type="text"
    :value="answer ?? ''"
    @input="onInput"
    placeholder="답안을 입력하세요"
  />
</template>

<script setup>
import { ref, watch } from 'vue'

const props = defineProps({
  answer: [String, Number, null]
})

const emit = defineEmits(['input'])

const onInput = (e) => {
  console.log('🔤 ShortAnswer 입력 이벤트:', {
    value: e.target.value,
    valueType: typeof e.target.value,
    propsAnswer: props.answer
  })
  emit('input', e.target.value)
}

// props.answer가 변경될 때마다 로그 출력
watch(() => props.answer, (newVal) => {
  console.log('📥 ShortAnswer props.answer 변경:', {
    newValue: newVal,
    valueType: typeof newVal
  })
}, { immediate: true })
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