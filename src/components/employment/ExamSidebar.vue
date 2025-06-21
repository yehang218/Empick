<template>
  <div class="sidebar-box">
    <div class="sidebar-title">
      <div class="main-title">{{ testInfo.title }}</div>
      <div class="divider"></div>
    </div>
    <div class="sidebar-timer-row">
      <span class="timer-label">남은 시간</span>
      <span class="timer-value">{{ formatTime(timeLeft) }}</span>
    </div>
    <div class="sidebar-question-numbers">
      <button
        v-for="idx in totalQuestions"
        :key="idx"
        class="question-number"
        :class="{ selected: idx - 1 === currentIndex }"
        @click="$emit('moveTo', idx - 1)"
      >{{ idx }}</button>
    </div>
    <div class="sidebar-actions">
      <button class="submit-btn" @click="$emit('submit')">제출하기</button>
    </div>
  </div>
</template>
<script setup>
const props = defineProps({
  testInfo: Object,
  currentIndex: Number,
  totalQuestions: Number,
  timeLeft: Number
})

function formatTime(sec) {
  if (!Number.isFinite(sec) || sec < 0) return '00:00'
  const m = String(Math.floor(sec / 60)).padStart(2, '0')
  const s = String(sec % 60).padStart(2, '0')
  return `${m} : ${s}`
}
</script>
<style scoped>
.sidebar-box {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 8px 32px 0 rgba(0,0,0,0.18);
  padding: 36px 32px 32px 32px;
  width: 350px;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.sidebar-title {
  width: 100%;
  margin-bottom: 18px;
}
.main-title {
  font-size: 1.25rem;
  font-weight: 700;
  margin-bottom: 12px;
  line-height: 1.4;
}
.divider {
  width: 100%;
  height: 2px;
  background: #E5E5E5;
  margin-bottom: 8px;
}
.sidebar-timer-row {
  width: 100%;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin-bottom: 18px;
}
.timer-label {
  font-size: 1.05rem;
  color: #222;
  margin-right: 8px;
}
.timer-value {
  font-size: 2rem;
  font-weight: 700;
  letter-spacing: 2px;
}
.sidebar-question-numbers {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 12px;
  margin-bottom: 32px;
}
.question-number {
  width: 38px;
  height: 38px;
  border-radius: 8px;
  border: none;
  background: #E5E5E5;
  color: #222;
  font-size: 1.15rem;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.2s, color 0.2s;
}
.question-number.selected {
  background: #7EDC92;
  color: #fff;
}
.sidebar-actions {
  width: 100%;
  display: flex;
  justify-content: center;
  gap: 16px;
  margin-top: 24px;
}
.back-btn {
  background: #E5E5E5;
  color: #888;
  border: none;
  border-radius: 8px;
  padding: 12px 28px;
  font-size: 1.05rem;
  font-weight: 600;
  cursor: not-allowed;
}
.submit-btn {
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
.submit-btn:hover {
  background: #3a6247;
}
.test-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 80vh;
}
</style>