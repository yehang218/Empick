<template>
    <div>
        <v-btn color="primary" @click="goEvaluationCriteriaListTest" class="mb-4">평가 기준 조회 테스트</v-btn>
    </div>
</template>

<script setup>

import { useRouter } from 'vue-router'

const router = useRouter()

const goEvaluationCriteriaListTest = () => {
  router.push('/test/mj/evaluationCriteriaList')
}

</script>

<style scoped>

</style>