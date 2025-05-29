<template>
    <div>
        <v-btn color="primary" @click="goCalendarTest" class="mb-4">달력 테스트</v-btn>
    </div>
</template>

<script setup>

import { useRouter } from 'vue-router'

const router = useRouter()

const goCalendarTest = () => {
  router.push('/test/mj/calendar')
}

</script>

<style scoped>

</style>