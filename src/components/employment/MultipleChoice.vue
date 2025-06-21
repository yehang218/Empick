<template>
  <div class="multiple-choice-list">
    <div
      v-for="(opt, idx) in options"
      :key="opt.id || idx"
      class="multiple-choice-option"
      :class="{ selected: answer === idx }"
      @click="$emit('select', idx)"
    >
      {{ opt.content }}
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
  padding: 14px 18px;
  border: 1.5px solid #d1d5db;
  border-radius: 8px;
  background: #fff;
  cursor: pointer;
  font-size: 1.1rem;
  transition: background 0.2s, border 0.2s, color 0.2s;
}
.multiple-choice-option:hover {
  background: #f0f4f8;
  border-color: #4a7c59;
}
.multiple-choice-option.selected {
  background: #4a7c59;
  color: #fff;
  border-color: #4a7c59;
}
</style>