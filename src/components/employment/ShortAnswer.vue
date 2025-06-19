<template>
    <input
      type="text"
      :value="answer"
      @input="$emit('input', $event.target.value)"
    />
  </template>
  defineProps({
  answer: [String, Number, null]
})
</script>
<script setup>
defineProps({
  answer: [String, Number, null]
})
</script>