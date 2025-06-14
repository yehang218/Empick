<template>
    <div>
        <h2>지원서 목록</h2>
        <v-list v-if="applications.length">
            <v-list-item v-for="application in applications" :key="application.id" @click="goToDetail(application.id)"
                class="mb-4">
                <v-card class="w-100 pa-3" outlined>
                    <v-card-text>
                        <div><strong>지원서 ID:</strong> {{ application.id }}</div>
                        <div><strong>모집 공고 ID:</strong> {{ application.recruitment_id }}</div>
                        <div><strong>지원일:</strong> {{ formatDate(application.created_at) }}</div>
                        <div><strong>상태:</strong> {{ application.status }}</div>
                        <div><strong>지원자 ID:</strong> {{ application.applicant_id }}</div>
                        <div><strong>자기소개 평가 ID:</strong> {{ application.introduce_rating_result_id }}</div>
                        <div><strong>수정일:</strong> {{ application.updated_at }}</div>
                        <div><strong>수정자:</strong> {{ application.updated_by }}</div>
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
import axios from 'axios';

const applications = ref([]);
const router = useRouter();

const formatDate = (dateStr) => {
    const date = new Date(dateStr);
    return date.toLocaleString(); // 'YYYY-MM-DD HH:mm:ss'로 포맷됨
};

const fetchApplications = async () => {
    try {
        const res = await axios.get('http://localhost:5001/api/v1/employment/application');
        applications.value = res.data.data; // 데이터 형식에 따라 조정
        console.log('응답 데이터', res.data);
    } catch (error) {
        console.error('지원서 목록 조회 실패', error);
        applications.value = [];
    }
};

const goToDetail = (applicationId) => {
    router.push({ name: 'InterviewDetailPage', params: { applicationId } });
};

onMounted(fetchApplications);
</script>
