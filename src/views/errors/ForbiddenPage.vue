<template>
    <v-container class="d-flex flex-column align-center justify-center text-center" fill-height>
        <v-icon size="100" color="error" class="mb-6">mdi-shield-off</v-icon>

        <h1 class="text-h4 font-weight-bold mb-2">접근이 거부되었습니다</h1>
        <p class="text-subtitle-1 mb-6">이 페이지에 접근할 권한이 없습니다.<br> 관리자에게 문의하거나 홈으로 돌아가세요.</p>

        <v-btn color="primary" @click="goHome" class="px-6 py-3" size="large">
            홈으로 가기
        </v-btn>
    </v-container>
</template>

<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();

const goHome = () => {
    router.push('/');
};
</script>