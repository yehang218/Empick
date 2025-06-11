<template>
    <v-container fluid class="pa-0 mt-0 pt-0">
        <v-row class="ma-0 pa-0">
            <v-col cols="12" class="pa-0">
                <v-card class="mt-0 pa-0">
                    <v-card-title class="text-h6 font-weight-bold">채용 요청서 목록</v-card-title>
                    <ListView :headers="headers" :data="store.list" />
                </v-card>
            </v-col>
        </v-row>
    </v-container>
</template>

<script setup>
import { onMounted } from 'vue';
import { useRecruitmentStore } from '@/stores/recruitmentStore';
import ListView from '@/components/common/ListView.vue';

const store = useRecruitmentStore();

onMounted(() => {
    store.loadRecruitmentRequestList();
});

// 테이블 헤더 정의
const headers = [
    { key: 'positionName', label: '포지션명' },
    { key: 'headcount', label: '인원' },
    { key: 'requestedAt', label: '요청일' },
    { key: 'departmentName', label: '부서' },
    { key: 'memberName', label: '작성자' },
    { key: 'status', label: '상태' }
];
</script>