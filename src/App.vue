<template>
  <v-app>
    <Sidebar v-if="showSidebar" />
    <v-main>
      <router-view />
    </v-main>
  </v-app>
</template>

<script setup>
import { computed, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import Sidebar from '@/components/common/MainSidebar.vue'
import { RouterView } from 'vue-router';
import { useAuthStore } from '@/stores/authStore';

const route = useRoute();
const authStore = useAuthStore();

// 사이드바 표시 여부 결정
const showSidebar = computed(() => {
  // 로그인 페이지나 인증이 필요한 페이지가 아닌 경우 사이드바 숨김
  return !route.meta.hideSidebar;
});

onMounted(() => {
  // 앱 시작 시 저장된 인증 상태 복원
  authStore.restoreAuth();
});
</script>

<style>
.v-main {
  background-color: #f5f5f5;
}
</style>