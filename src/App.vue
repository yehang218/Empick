<template>
  <v-app v-if="isReady">
    <!-- 헤더 -->
    <v-app-bar app height="70" flat v-if="showHeader">
      <Header />
    </v-app-bar>

    <div class="layout-wrapper">
      <!-- 사이드바 -->
      <div v-if="showSidebar" class="custom-sidebar">
        <Sidebar />
      </div>

      <!-- 본문 -->
      <div class="main-content">
        <router-view />
      </div>
    </div>
  </v-app>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import Header from '@/components/common/Header.vue';
import Sidebar from '@/components/common/MainSidebar.vue';
import { useAuthStore } from '@/stores/authStore';
import { useMemberStore } from '@/stores/memberStore';

const route = useRoute();
const router = useRouter();
const authStore = useAuthStore();
const memberStore = useMemberStore()
const isReady = ref(false);

const showHeader = computed(() => !route.meta.hideHeader);
const showSidebar = computed(() => !route.meta.hideSidebar);

onMounted(async () => {
  authStore.restoreAuth();
  await router.isReady();
  isReady.value = true;
});

watch(
  () => authStore.isAuthenticated,
  async (isAuth) => {
    if (isAuth) {
      try {
        await memberStore.getMyInfo();
      } catch (e) {
        console.error('멤버 정보 조회 실패', e);
        // 토큰이 만료되었거나 기타 오류인 경우, authStore에서 이미 처리됨
      }
    } else {
      // 로그아웃 시에는 API 호출 없이 스토어만 리셋
      console.log('로그아웃 감지, 멤버 스토어 리셋');
      memberStore.reset();
    }
  },
  { immediate: true }
);
</script>

<style scoped>
.layout-wrapper {
  display: flex;
  margin-top: 70px;
  /* 헤더 높이만큼 */
}

.custom-sidebar {
  width: 260px;
  min-height: calc(100vh - 70px);
  background-color: #fafcfb;
  border-right: 1px solid #e0e0e0;
}

.main-content {
  flex: 1;
  background-color: #fff;
  padding: 24px;
  min-height: calc(100vh - 70px);
  overflow-x: hidden;
}
</style>
