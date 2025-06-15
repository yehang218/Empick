<template>
    <div>
        <h2>지원서 목록</h2>
        <v-list v-if="applications.length">
            <v-list-item v-for="application in applications" :key="application.id" @click="goToDetail(application.id)"
                class="mb-4">
                <v-card class="w-100 pa-3" outlined>
                    <v-card-text>
                        <div><strong>지원서 ID:</strong> {{ application.id }}</div>
                        <div><strong>모집 공고 제목:</strong> {{ getRecruitmentTitle(application.recruitmentId) }}</div>
                        <div><strong>지원일:</strong> {{ formatDate(application.createdAt) }}</div>
                        <div><strong>상태:</strong> {{ application.status }}</div>
                        <div><strong>지원자 이름:</strong> {{ getApplicantName(application.applicantId) }}</div>
                        <div><strong>자기소개 평가 ID:</strong> {{ application.introduceRatingResultId }}</div>
                        <div><strong>수정일:</strong> {{ formatDate(application.updatedAt) }}</div>
                        <div><strong>수정자:</strong> {{ application.updatedBy }}</div>
                    </v-card-text>
                </v-card>
            </v-list-item>
        </v-list>

        <v-alert v-else type="info" class="mt-4">
            등록된 지원서가 없습니다.
        </v-alert>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useApplicationStore } from '@/stores/applicationStore';
import { useApplicantStore } from '@/stores/applicantStore';
import { useRecruitmentRequestStore } from '@/stores/recruitmentRequestStore';

const applicationStore = useApplicationStore();
const applicantStore = useApplicantStore();
const recruitmentStore = useRecruitmentRequestStore();

const applications = ref([]);
const applicantMap = ref({});
const recruitmentMap = ref({});
const router = useRouter();

const formatDate = (dateStr) => {
    const date = new Date(dateStr);
    return date.toLocaleString();
};

const fetchApplications = async () => {
    console.log('[fetchApplications] 호출됨');
    try {
        const res = await applicationStore.fetchAllApplications();
        console.log('[fetchApplications] 결과:', res); // 👈 확인
        applications.value = res;
        
        const applicantIds = [...new Set(res.map(a => a.applicantId))];
        const recruitmentIds = [...new Set(res.map(a => a.recruitmentId))];

        await Promise.all(
            applicantIds.map(async (id) => {
                await applicantStore.fetchApplicantById(id);
                applicantMap.value[id] = applicantStore.selectedApplicant?.name || '이름 없음';
            })
        );

        await Promise.all(
            recruitmentIds.map(async (id) => {
                try {
                    await recruitmentStore.fetchRecruitmentRequestDetail(id);
                    recruitmentMap.value[id] = recruitmentStore.recruitmentRequestDetail?.title || '제목 없음';
                } catch {
                    recruitmentMap.value[id] = '제목 없음';
                }
            })
        );

    } catch (error) {
        console.error('지원서 목록 조회 실패', error);
        applications.value = [];
    }
};

const getApplicantName = (id) => applicantMap.value[id] || '로딩 중...';
const getRecruitmentTitle = (id) => recruitmentMap.value[id] || '로딩 중...';

const goToDetail = (applicationId) => {
    router.push({ name: 'InterviewDetailPage', params: { applicationId } });
};

onMounted(fetchApplications);
</script>
