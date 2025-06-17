<script setup>
import { onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { useApplicantStore } from '@/stores/applicantStore';

const route = useRoute();
const store = useApplicantStore();

onMounted(async () => {
    const recruitmentId = route.params.recruitmentId;
    await store.loadApplicantsByRecruitmentId(recruitmentId);
});
</script>

<template>
    <v-container>
        <h2>지원자 목록</h2>
        <v-table>
            <thead>
                <tr>
                    <th>이름</th>
                    <th>이메일</th>
                    <th>전화번호</th>
                    <th>생년월일</th>
                    <th>주소</th>
                    <th>상태</th>
                    <th>프로필</th>
                    <th>지원일</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="app in store.applicantList" :key="app.applicationId">
                    <td>{{ app.name }}</td>
                    <td>{{ app.email }}</td>
                    <td>{{ app.phone }}</td>
                    <td>{{ app.birth }}</td>
                    <td>{{ app.address }}</td>
                    <td>{{ app.status }}</td>
                    <td>
                        <img v-if="app.profileUrl" :src="app.profileUrl" alt="profile" width="32" height="32" />
                    </td>
                    <td>{{ app.createdAt }}</td>
                </tr>
            </tbody>
        </v-table>
    </v-container>
</template>