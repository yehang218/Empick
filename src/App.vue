<template>
  <MessageInputBox ref="msgRef" />

  <v-btn color="primary" class="mt-4" @click="sendMessage">
    메시지 전송
  </v-btn>
  <!-- 이 버튼은 나중에 위치 조정할 예정 -->
</template>

<script setup>
import { ref } from 'vue'
import MessageInputBox from '@/components/MessageInputBox.vue'

const msgRef = ref()

const sendMessage = () => {
  const message = msgRef.value.inputMessage
  console.log('보낼 메시지:', message)

  // 예: 백엔드 전송
  // await fetch('/api/send-message', {
  //   method: 'POST',
  //   body: JSON.stringify({ content: message }),
  //   headers: { 'Content-Type': 'application/json' }
  // })
}
</script>