<template>
  <div class="short-answer-container">
    <div class="input-label">
      <v-icon class="label-icon">mdi-text-box-outline</v-icon>
      답안 입력
    </div>
    <div class="input-wrapper">
      <input
        class="short-answer-input"
        type="text"
        :value="answer ?? ''"
        @input="onInput"
        placeholder="답안을 입력하세요"
      />
      <div class="input-focus-border"></div>
    </div>
  </div>
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
.short-answer-container {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.input-label {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.95rem;
  font-weight: 500;
  color: #555;
}

.label-icon {
  color: #1976d2;
  font-size: 1rem;
}

.input-wrapper {
  position: relative;
  width: 100%;
  max-width: 600px;
}

.short-answer-input {
  width: 100%;
  padding: 16px 20px;
  font-size: 1rem;
  border: 2px solid #e0e0e0;
  border-radius: 12px;
  background: #fafbfc;
  transition: all 0.2s ease;
  outline: none;
  font-family: inherit;
  line-height: 1.5;
}

.short-answer-input::placeholder {
  color: #999;
  font-style: italic;
}

.short-answer-input:hover {
  border-color: #1976d2;
  background: #f8f9fa;
  box-shadow: 0 2px 8px rgba(25, 118, 210, 0.1);
}

.short-answer-input:focus {
  border-color: #1976d2;
  background: #fff;
  box-shadow: 0 4px 12px rgba(25, 118, 210, 0.15);
  transform: translateY(-1px);
}

.input-focus-border {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  border: 2px solid transparent;
  border-radius: 12px;
  pointer-events: none;
  transition: all 0.2s ease;
}

.short-answer-input:focus + .input-focus-border {
  border-color: #1976d2;
  box-shadow: 0 0 0 4px rgba(25, 118, 210, 0.1);
}

/* 입력값이 있을 때 스타일 */
.short-answer-input:not(:placeholder-shown) {
  border-color: #4caf50;
  background: #f8fff9;
}

.short-answer-input:not(:placeholder-shown):focus {
  border-color: #4caf50;
  box-shadow: 0 4px 12px rgba(76, 175, 80, 0.15);
}

.short-answer-input:not(:placeholder-shown) + .input-focus-border {
  border-color: #4caf50;
}

/* 애니메이션 효과 */
@keyframes inputGlow {
  0% {
    box-shadow: 0 0 0 0 rgba(25, 118, 210, 0.4);
  }
  70% {
    box-shadow: 0 0 0 10px rgba(25, 118, 210, 0);
  }
  100% {
    box-shadow: 0 0 0 0 rgba(25, 118, 210, 0);
  }
}

.short-answer-input:focus {
  animation: inputGlow 0.6s ease-out;
}

/* 반응형 디자인 */
@media (max-width: 600px) {
  .input-label {
    font-size: 0.9rem;
  }
  
  .label-icon {
    font-size: 0.9rem;
  }
  
  .short-answer-input {
    padding: 14px 16px;
    font-size: 0.95rem;
    border-radius: 8px;
  }
  
  .input-focus-border {
    border-radius: 8px;
  }
  
  .input-wrapper {
    max-width: none;
  }
}

/* 다크 모드 대응 */
@media (prefers-color-scheme: dark) {
  .short-answer-input {
    background: #2d3748;
    border-color: #4a5568;
    color: #e2e8f0;
  }
  
  .short-answer-input::placeholder {
    color: #718096;
  }
  
  .short-answer-input:hover {
    background: #4a5568;
    border-color: #63b3ed;
  }
  
  .short-answer-input:focus {
    background: #2d3748;
    border-color: #63b3ed;
  }
}
</style>