<template>
    <div>
        <CareerHeader />
        <v-container class="d-flex flex-column align-center justify-center text-center" fill-height>
            <v-icon size="100" color="error" class="mb-6">mdi-alert-circle-outline</v-icon>

            <h1 class="text-h4 font-weight-bold mb-2">오류가 발생했습니다</h1>
            <p class="text-subtitle-1 mb-6">요청하신 페이지에 접근할 수 없거나 오류가 발생했습니다.<br> 채용공고 목록으로 돌아가서 다시 시도해주세요.</p>

            <v-btn color="primary" @click="goToCareer" class="px-6 py-3" size="large">
                채용공고 목록으로 가기
            </v-btn>
        </v-container>
    </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import CareerHeader from '@/components/career/CareerHeader.vue';

const router = useRouter();

const goToCareer = () => {
    router.push('/career/recruitments');
};
</script> 