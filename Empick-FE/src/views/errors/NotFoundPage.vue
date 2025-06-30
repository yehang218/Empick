<template>
    <v-container class="d-flex flex-column align-center justify-center text-center" fill-height>
        <v-icon size="100" color="warning" class="mb-6">mdi-alert-circle-outline</v-icon>

        <h1 class="text-h4 font-weight-bold mb-2">페이지를 찾을 수 없습니다</h1>
        <p class="text-subtitle-1 mb-6">요청하신 페이지가 존재하지 않거나 삭제되었습니다.</p>

        <v-btn color="primary" @click="goHome" class="px-6 py-3" size="large">
            홈으로 가기
        </v-btn>
    </v-container>
</template>

<script setup>
import { useRouter, useRoute } from 'vue-router';
import { onMounted } from 'vue';

const router = useRouter();
const route = useRoute();

const goHome = () => {
    router.push('/');
};

// 컴포넌트가 마운트될 때 경로 확인하여 지원자용 경로면 CareerError로 리다이렉트
onMounted(() => {
    const currentPath = route.path;
    const isCareerPath = currentPath.startsWith('/career') || currentPath.startsWith('/employment/jobtest/exam/');
    
    if (isCareerPath) {
        console.log('지원자용 경로에서 404 발생, CareerError 페이지로 리다이렉트');
        router.replace({ name: 'CareerError' });
    }
});
</script>