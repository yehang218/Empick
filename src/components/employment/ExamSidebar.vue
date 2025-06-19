<template>
    <div class="sidebar">
      <div>{{ testInfo.title }}</div>
      <div>남은 시간: {{ formatTime(timeLeft) }}</div>
      <div>
        <button
          v-for="(q, idx) in totalQuestions"
          :key="idx"
          :class="{ active: idx === currentIndex }"
          @click="$emit('moveTo', idx)"
        >{{ idx + 1 }}</button>
      </div>
      <button @click="$emit('submit')">제출하기</button>
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
    const m = String(Math.floor(sec / 60)).padStart(2, '0')
    const s = String(sec % 60).padStart(2, '0')
    return `${m}:${s}`
  }
  </script>