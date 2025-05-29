<template>
  <div id="app">
    <OneColumnList
      :title="evaluationTitle"
      :items="evaluationList"
    />
  </div>
</template>

<script setup>
import OneColumnList from './components/OneColumnList.vue'
import { ref, onMounted } from 'vue'

const evaluationTitle = ref('평가표')
const evaluationList = ref(['목록1', '목록2', '목록3', '목록4', '목록5'])

onMounted(async () => {
  // 예시: API로부터 평가표 리스트 받아오기
  const res = await fetch('/api/evaluationForms')
  const data = await res.json()
  evaluationList.value = data.formNames  // ['양식1', '양식2'...]
})
</script>

<style scoped></style>
