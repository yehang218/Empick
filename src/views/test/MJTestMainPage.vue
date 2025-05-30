<template>
    <div>
        <v-btn color="primary" @click="goEvaluationScoreTest" class="mb-4">평가 점수 테스트</v-btn>
        <v-btn color="primary" @click="goEvaluationCriteriaListTest" class="mb-4">평가 기준 조회 테스트</v-btn>
        <v-btn color="primary" @click="goCalendarTest" class="mb-4">달력 테스트</v-btn>
    </div>
</template>

<script setup>

import { useRouter } from 'vue-router'

const router = useRouter()

const goEvaluationScoreTest = () => {
  router.push('/test/mj/evaluationScore')
}

const goEvaluationCriteriaListTest = () => {
  router.push('/test/mj/evaluationCriteriaList')
}

const goCalendarTest = () => {
  router.push('/test/mj/calendar')
}

</script>

<style scoped>

</style>