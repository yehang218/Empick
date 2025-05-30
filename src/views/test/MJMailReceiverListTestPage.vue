<template>
  <v-container class="d-flex flex-column align-center pa-0" style="gap: 8px;">
    <v-btn color="primary" @click="goBack" style="margin-bottom: 4px;">
      뒤로가기
    </v-btn>

    <MailReceiverList ref="mailReceiverRef" />

    <v-btn color="success" @click="sendData" style="margin-top: 4px;">
      전송
    </v-btn>
  </v-container>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import MailReceiverList from '@/components/mail/MailReceiverList.vue';

const router = useRouter();
const goBack = () => {
  router.push('/test/mj');
};

// ⬇️ 자식 컴포넌트를 가리킬 ref
const mailReceiverRef = ref(null);

const sendData = () => {
  const users = mailReceiverRef.value.users;
  console.log('전송할 사용자 목록:', users);
};
</script>





<style>

</style>