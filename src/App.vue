<template>
  <v-app v-if="isReady">
    <Header v-if="showHeader" />
    <Sidebar v-if="showSidebar" />

    <!-- ✅ 스크롤되는 영역 -->
    <v-main class="main-content">
      <router-view />
    </v-main>
  </v-app>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import Sidebar from '@/components/common/MainSidebar.vue'
import Header from '@/components/common/Header.vue'
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

// 헤더 표시 여부 결정
const showHeader = computed(() => {
  // 로그인 페이지나 인증이 필요한 페이지가 아닌 경우 헤더 숨김
  return !route.meta.hideHeader;
});

onMounted(async () => {
  // 앱 시작 시 저장된 인증 상태 복원
  authStore.restoreAuth();
  await router.isReady(); // 라우터가 준비될 때까지 대기
  isReady.value = true;
});
</script>

<style>
.layout-row {
  display: flex;
}

.sidebar {
  width: 260px;
}

.v-main {
  padding-top: 0 !important;
  margin-top: 0 !important;
}

.main-content {
  margin-left: 260px; /* 사이드바 폭 */
  padding: 24px;
  padding-top: 70px; /* ✅ AppBar height 만큼 보정 */
  background-color: #fff;
  min-height: 100vh;
  overflow-x: hidden;
}
</style>