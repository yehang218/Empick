<template>
    <div>
        <v-btn color="primary" @click="goEvaluationScoreTest" class="mb-4">평가 점수 테스트</v-btn>
        <v-btn color="primary" @click="goEvaluationCriteriaListTest" class="mb-4">평가 기준 조회 테스트</v-btn>
        <v-btn color="primary" @click="goCalendarTest" class="mb-4">달력 테스트</v-btn>
        <v-btn color="primary" @click="goMailReceiverListTest" class="mb-4">메일 수신자 리스트 테스트</v-btn>
        <v-btn color="primary" @click="goMessageInputBoxTest" class="mb-4">메세지 입력 테스트</v-btn>
        <v-btn color="primary" @click="goOneColumnListTest" class="mb-4">단일 컬럼 리스트 테스트</v-btn>
    </div>

      <SendMailModal ref="mailModal" />
  <v-btn @click="openModal">메일 보내기</v-btn>
</template>

<script setup>

import { useRouter } from 'vue-router'
import { ref } from 'vue'
import SendMailModal from '@/components/mail/SendMailModal.vue'
const mailModal = ref()

const openModal = () => {
  mailModal.value.open()
}

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

const goMailReceiverListTest = () => {
  router.push('/test/mj/mailReceiverList')
}

const goMessageInputBoxTest = () => {
  router.push('/test/mj/messageInputBox')
}

const goOneColumnListTest = () => {
  router.push('/test/mj/oneColumnList')
}

</script>

<style scoped>

</style>