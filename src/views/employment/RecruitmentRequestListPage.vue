<template>
    <v-app>
        <Header />

        <!-- 헤더 높이 + 2단 메뉴 고려하여 padding-top 넉넉히 확보 -->
        <v-main class="pa-4" style="padding-top: 140px;">
            <v-container fluid>
                <v-row>
                    <v-col cols="12">
                        <v-card>
                            <v-card-title class="text-h6 font-weight-bold">채용 요청서 목록</v-card-title>
                            <ListView :headers="headers" :data="store.list" />
                        </v-card>
                    </v-col>
                </v-row>
            </v-container>
        </v-main>
    </v-app>
</template>

<script setup>
import { onMounted } from 'vue';
import { useRecruitmentRequestStore } from '@/stores/recruitmentRequestStore';
import ListView from '@/components/common/ListView.vue';
import Header from '@/components/common/Header.vue';

const store = useRecruitmentRequestStore();
onMounted(() => {
    store.loadList();
});

// 채용 요청서 테이블 헤더 정의
const headers = [
    { key: 'positionName', label: '포지션명' },
    { key: 'headcount', label: '인원' },
    { key: 'requestedAt', label: '요청일' },
    { key: 'departmentName', label: '부서' },
    { key: 'memberName', label: '작성자' },
    { key: 'status', label: '상태' }
];
</script>