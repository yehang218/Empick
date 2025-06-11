<template>
  <v-app v-if="isReady">
    <Sidebar v-if="showSidebar" />
    <v-main>
      <router-view />
    </v-main>
  </v-app>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import Sidebar from '@/components/common/MainSidebar.vue'
import { RouterView } from 'vue-router';
import { useAuthStore } from '@/stores/authStore';

const route = useRoute();
const router = useRouter();
const authStore = useAuthStore();
const isReady = ref(false);

// 사이드바 표시 여부 결정
const showSidebar = computed(() => {
  // 로그인 페이지나 인증이 필요한 페이지가 아닌 경우 사이드바 숨김
  return !route.meta.hideSidebar;
});

onMounted(async () => {
  // 앱 시작 시 저장된 인증 상태 복원
  authStore.restoreAuth();
  await router.isReady(); // 라우터가 준비될 때까지 대기
  isReady.value = true;
});
</script>

<style>
.v-main {
  background-color: #f5f5f5;
}
</style>