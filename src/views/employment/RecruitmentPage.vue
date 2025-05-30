<template>
    <v-app>
        <!-- ✅ 공통 헤더 -->
        <Header />

        <!-- ✅ 페이지 본문 -->
        <v-main>
            <v-container>
                <h1 class="text-h5">채용 공고 테스트 페이지입니다.</h1>
            </v-container>
        </v-main>
    </v-app>
</template>

<script setup>
// 👇 경로 주의: 실제 파일 위치에 맞춰 작성
import Header from '@/components/common/Header.vue'
</script>

<style scoped></style>
