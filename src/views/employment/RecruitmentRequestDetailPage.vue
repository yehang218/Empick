<template>
    <v-container fluid class="pa-6">
        <v-row justify="space-between" align="center" class="mb-6">
            <h2 class="text-h5 font-weight-bold">
                {{ detail?.jobName }} 채용 요청서
            </h2>
            <v-btn color="success" class="text-white" prepend-icon="mdi-plus" @click="handleClick">
                채용 공고 작성하기
            </v-btn>
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
        <TemplateYNModal v-if="showModal" @confirm="goToTemplateSelect" @no="goToEditor" @close="closeModal" />
    </v-container>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useRecruitmentRequestStore } from '@/stores/recruitmentRequestStore';
import dayjs from 'dayjs';
import TemplateYNModal from '@/components/employment/TemplateYNModal.vue';

const route = useRoute();
const router = useRouter();
const store = useRecruitmentRequestStore();

const id = route.params.id;
const showModal = ref(false);

onMounted(() => {
    store.loadRecruitmentRequestDetail(id);
});
const handleClick = () => {
    showModal.value = true;
};

// '예' → 템플릿 선택 페이지
const goToTemplateSelect = () => {
    showModal.value = false;
    router.push(`/employment/recruitments/template-select?id=${id}`);
};

// '아니오' → 에디터가 있는 공고 작성 페이지
const goToEditor = () => {
    showModal.value = false;
    router.push(`/employment/recruitments/create?id=${id}`);
};

// 'X' → 모달 닫기
const closeModal = () => {
    showModal.value = false;
};

const detail = computed(() => store.recruitmentRequestDetail);
const loading = computed(() => store.loadingDetail);

// 날짜 포맷 함수
const formatDate = (date) => dayjs(date).format('YYYY-MM-DD HH:mm');

const fields = computed(() => {
    if (!detail.value) return [];

    return [
        { label: '모집 인원', value: `${detail.value.headcount}명` },
        { label: '모집 기간', value: `${formatDate(detail.value.startedAt)} ~ ${formatDate(detail.value.endedAt)}` },
        {
            label: '자격 요건',
            value: detail.value.qualification ? detail.value.qualification.split('\n') : []
        },
        {
            label: '우대 사항',
            value: detail.value.preference ? detail.value.preference.split('\n') : []
        },
        { label: '고용 형태', value: detail.value.employmentType },
        { label: '근무 지역', value: detail.value.workLocation },
        { label: '부서', value: detail.value.departmentName },
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