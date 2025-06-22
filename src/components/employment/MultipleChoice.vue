<template>
  <div class="multiple-choice-list">
    <div
      v-for="(opt, idx) in options"
      :key="opt.id || idx"
      class="multiple-choice-option"
      :class="{ selected: answer === idx }"
      @click="$emit('select', idx)"
    >
      <div class="option-content">
        <div class="option-number">{{ String.fromCharCode(65 + idx) }}.</div>
        <div class="option-text">{{ opt.content }}</div>
      </div>
      <div class="option-indicator">
        <v-icon v-if="answer === idx" class="check-icon">mdi-check-circle</v-icon>
        <div v-else class="radio-circle"></div>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  options: Array,
  answer: [Number, String, null]
})
</script>

<style scoped>
.multiple-choice-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin: 16px 0;
}

.multiple-choice-option {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 20px;
  border: 2px solid #e0e0e0;
  border-radius: 12px;
  background: #fff;
  cursor: pointer;
  font-size: 1rem;
  transition: all 0.2s ease;
  position: relative;
  overflow: hidden;
}

.multiple-choice-option::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(25, 118, 210, 0.05), rgba(66, 165, 245, 0.05));
  opacity: 0;
  transition: opacity 0.2s ease;
}

.multiple-choice-option:hover {
  border-color: #1976d2;
  background: #f8f9fa;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(25, 118, 210, 0.15);
}

.multiple-choice-option:hover::before {
  opacity: 1;
}

.multiple-choice-option.selected {
  border-color: #1976d2;
  background: linear-gradient(135deg, #1976d2, #42a5f5);
  color: #fff;
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(25, 118, 210, 0.3);
}

.multiple-choice-option.selected::before {
  opacity: 0;
}

.option-content {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  flex: 1;
  z-index: 1;
  position: relative;
}

.option-number {
  font-weight: 600;
  font-size: 1.1rem;
  color: #1976d2;
  min-width: 20px;
  text-align: center;
  margin-top: 2px;
}

.multiple-choice-option.selected .option-number {
  color: #fff;
}

.option-text {
  line-height: 1.5;
  flex: 1;
}

.option-indicator {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  z-index: 1;
  position: relative;
}

.check-icon {
  color: #fff;
  font-size: 1.2rem;
}

.radio-circle {
  width: 20px;
  height: 20px;
  border: 2px solid #ccc;
  border-radius: 50%;
  background: #fff;
  transition: all 0.2s ease;
}

.multiple-choice-option:hover .radio-circle {
  border-color: #1976d2;
  background: #f0f8ff;
}

/* 애니메이션 효과 */
.multiple-choice-option {
  animation: fadeInUp 0.3s ease forwards;
}

.multiple-choice-option:nth-child(1) { animation-delay: 0.1s; }
.multiple-choice-option:nth-child(2) { animation-delay: 0.2s; }
.multiple-choice-option:nth-child(3) { animation-delay: 0.3s; }
.multiple-choice-option:nth-child(4) { animation-delay: 0.4s; }
.multiple-choice-option:nth-child(5) { animation-delay: 0.5s; }

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 반응형 디자인 */
@media (max-width: 600px) {
  .multiple-choice-list {
    gap: 10px;
    margin: 12px 0;
  }
  
  .multiple-choice-option {
    padding: 14px 16px;
    font-size: 0.95rem;
  }
  
  .option-number {
    font-size: 1rem;
    min-width: 18px;
  }
  
  .option-content {
    gap: 10px;
  }
  
  .check-icon {
    font-size: 1.1rem;
  }
  
  .radio-circle {
    width: 18px;
    height: 18px;
  }
}
</style>