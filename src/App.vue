<template>
  <v-app>
    <Sidebar />
    <v-main>
      <router-view />
    </v-main>
  </v-app>
</template>

<script setup>
import Sidebar from '@/components/common/MainSidebar.vue'
import { RouterView } from 'vue-router';

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