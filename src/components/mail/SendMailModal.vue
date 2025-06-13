<template>
  <v-dialog v-model="isOpen" width="50%" persistent>
    <v-card class="pa-6" style="height: 50vh; max-height: 90vh;">
      <v-card-title class="text-h6 font-weight-bold justify-space-between">
        메일 보내기
      </v-card-title>

      <v-card-text class="d-flex flex-column gap-4" style="height: calc(100% - 96px); overflow-y: auto;">
        <v-text-field
          v-model="email"
          label="받는 사람 이메일"
          type="email"
          outlined
          dense
          required
        />
        <v-text-field
          v-model="title"
          label="제목"
          outlined
          dense
          required
        />
        <v-textarea
          v-model="content"
          label="내용"
          outlined
          rows="6"
          auto-grow
          dense
          required
        />
      </v-card-text>

      <v-card-actions class="justify-end">
        <v-btn variant="text" color="grey" @click="close">취소</v-btn>
        <v-btn color="primary" @click="sendMail">전송</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref } from 'vue'

const isOpen = ref(false)
const email = ref('')
const title = ref('')
const content = ref('')

// 외부에서 모달 제어할 수 있도록 open/close 메서드 노출
const open = () => {
  isOpen.value = true
}
const close = () => {
  isOpen.value = false
}

// 실제 메일 전송 처리 (예: API 호출)
const sendMail = () => {
  if (!email.value || !title.value || !content.value) {
    alert('모든 필드를 입력해주세요.')
    return
  }

  console.log('메일 전송:', {
    email: email.value,
    title: title.value,
    content: content.value
  })

  // 성공 후 초기화 및 닫기
  email.value = ''
  title.value = ''
  content.value = ''
  close()
}

defineExpose({
  open,
  close
})
</script>

<style scoped>
/* 모달 배경 흐리게 처리 */
.v-overlay__scrim {
  background-color: rgba(0, 0, 0, 0.5) !important;
}
</style>
