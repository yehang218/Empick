<template>
    <div>
      <div
        v-for="(opt, idx) in options"
        :key="idx"
        :class="{ selected: answer === idx }"
        @click="$emit('select', idx)"
      >{{ opt }}</div>
    </div>
  </template>
  <script setup>
  defineProps({
    options: Array,
    answer: [Number, String, null]
  })
  </script>