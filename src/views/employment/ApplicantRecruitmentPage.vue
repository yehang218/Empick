<template>
    <v-container>
        <h2>지원자 목록</h2>
        <v-table>
            <thead>
                <tr>
                    <th>이름</th>
                    <th>이메일</th>
                    <th>전화번호</th>
                    <th>상태</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="applicant in applicantList" :key="applicant.id">
                    <td>{{ applicant.name }}</td>
                    <td>{{ applicant.email }}</td>
                    <td>{{ applicant.phone }}</td>
                    <td>{{ applicant.status }}</td>
                </tr>
            </tbody>
        </v-table>
    </v-container>
</template>

<script setup>
import { onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { useApplicationStore } from '@/stores/applicationStore';

const route = useRoute();
const store = useApplicationStore();
const applicantList = ref([]);

onMounted(async () => {
    const recruitmentId = route.params.recruitmentId;
    applicantList.value = await store.loadApplicantByRecruitmentId(recruitmentId) || [];
});
</script>