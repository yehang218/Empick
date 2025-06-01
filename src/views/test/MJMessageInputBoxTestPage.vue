<template>
    <v-btn color="primary" @click="goBack" style="margin-bottom: 4px;">
      뒤로가기
    </v-btn>
  <div>
    <h2>쪽지 작성</h2>
    
    <!-- 메시지 입력 컴포넌트 -->
    <MessageInputBox ref="messageInputRef" />

    <!-- 보내기 버튼 -->
    <v-btn color="primary" class="mt-4" @click="sendMessage">
      보내기
    </v-btn>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import MessageInputBox from '@/components/MessageInputBox.vue' // 경로는 실제 위치에 맞게

import { useRouter } from 'vue-router';
const router = useRouter();
const goBack = () => {
  router.push('/test/mj');
};

const messageInputRef = ref(null)

const sendMessage = () => {
  const message = messageInputRef.value?.inputMessage
  console.log('보낸 메시지:', message)
  // 이후 메시지 서버 전송 API 호출 등 처리 가능
}
</script>