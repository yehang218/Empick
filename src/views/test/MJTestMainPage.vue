<template>
    <div>
        <v-btn color="primary" @click="goEvaluationScoreTest" class="mb-4">평가 점수 테스트</v-btn>
    </div>
</template>

<script setup>

import { useRouter } from 'vue-router'

const router = useRouter()

const goEvaluationScoreTest = () => {
  router.push('/test/mj/evaluationScore')
}

</script>

<style scoped>

</style>