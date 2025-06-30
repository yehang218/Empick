<template>
    <v-container fluid class="pa-6">

        <v-row justify="space-between" align="center" class="mb-6">
            <v-col cols="auto" class="d-flex align-center">
                <v-icon @click="$router.back()" class="me-2 cursor-pointer" size="28" color="black">
                    mdi-arrow-left
                </v-icon>
                <h2 class="text-h5 font-weight-bold">
                    {{ detail?.jobName }} 채용 요청서
                </h2>
            </v-col>

            <v-col cols="auto">
                <v-btn color="success" class="text-white" prepend-icon="mdi-plus" @click="handleClick">
                    채용 공고 작성하기
                </v-btn>
            </v-col>
        </v-row>


        <v-skeleton-loader v-if="loading" type="article" />

        <v-row v-else dense>
            <v-col cols="12" v-for="(item, index) in fields" :key="index">
                <v-card outlined rounded="lg" class="pa-4">
                    <div class="text-subtitle-2 font-weight-bold mb-2">{{ item.label }}</div>
                    <div v-if="Array.isArray(item.value)">
                        <v-list class="pa-0">
                            <v-list-item v-for="(v, i) in item.value" :key="i" class="pl-0">
                                <v-icon size="16" class="mr-1" color="green">mdi-circle-small</v-icon>
                                <span class="text-body-2">{{ v }}</span>
                            </v-list-item>
                        </v-list>
                    </div>
                    <div v-else class="text-body-2">{{ item.value }}</div>
                </v-card>
            </v-col>
        </v-row>
    </v-container>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useRecruitmentRequestStore } from '@/stores/recruitmentRequestStore';
import dayjs from 'dayjs';

const route = useRoute();
const router = useRouter();
const store = useRecruitmentRequestStore();

const id = route.params.id;

onMounted(() => {
    store.loadRecruitmentRequestDetail(id);
});

const handleClick = () => {
    router.push(`/employment/recruitments/create?id=${id}`);
};

const detail = computed(() => store.recruitmentRequestDetail);
const loading = computed(() => store.loadingDetail);

// 날짜 포맷 함수
const formatDate = (date) => dayjs(date).format('YYYY-MM-DD HH:mm');

const fields = computed(() => {
    if (!detail.value) return [];

    return [
        { label: '포지션', value: detail.value.jobName },
        { label: '부서', value: detail.value.departmentName },
        { label: '모집 인원', value: `${detail.value.headcount}명` },
        { label: '고용 형태', value: detail.value.employmentType },
        { label: '근무 지역', value: detail.value.workLocation },
        {
            label: '모집 기간',
            value: `${formatDate(detail.value.startedAt)} ~ ${formatDate(detail.value.endedAt)}`
        },
        {
            label: '주요 업무',
            value: detail.value.responsibility ? detail.value.responsibility.split('\n') : []
        },
        {
            label: '자격 요건',
            value: detail.value.qualification ? detail.value.qualification.split('\n') : []
        },
        {
            label: '우대 사항',
            value: detail.value.preference ? detail.value.preference.split('\n') : []
        },
        { label: '담당자', value: detail.value.memberName }
    ];
});


const goToRecruitmentCreate = () => {
    router.push({
        path: '/employment/recruitments/create',
        query: { requestId: id }
    });
};
</script>

<style scoped>
.v-card {
    border-color: #B5DAB0;
}

.text-subtitle-2 {
    color: #2f6f3e;
}

.text-body-2 {
    color: #333;
}
</style>