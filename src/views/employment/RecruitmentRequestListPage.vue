<template>
    <v-container fluid class="pa-4">
        <v-row justify="center">
            <v-col cols="12" md="10" lg="8">
                <v-card class="pa-6">
                    <v-card-title class="text-h6 font-weight-bold mb-4">채용 요청서 목록</v-card-title>
                    <ListView :headers="headers" :data="formattedList" />
                </v-card>
            </v-col>
        </v-row>
    </v-container>
</template>

<script setup>
import { onMounted, computed } from 'vue';
import { useRecruitmentStore } from '@/stores/recruitmentStore';
import ListView from '@/components/common/ListView.vue';
import dayjs from 'dayjs';

const store = useRecruitmentStore();

onMounted(() => {
    store.loadRecruitmentRequestList();
});

// 가공된 데이터: headcount에 '명', createdAt 포맷 변경
const formattedList = computed(() =>
    store.recruitmentRequestList.map(item => ({
        ...item,
        headcount: `${item.headcount} 명`,
        createdAt: dayjs(item.createdAt).format('YYYY-MM-DD HH:mm:ss')
    }))
);

// 헤더 정의
const headers = [
    { key: 'jobName', label: '포지션명' },
    { key: 'headcount', label: '인원' },
    { key: 'createdAt', label: '요청일' },
    { key: 'departmentName', label: '부서' },
    { key: 'memberName', label: '작성자' }
];
</script>