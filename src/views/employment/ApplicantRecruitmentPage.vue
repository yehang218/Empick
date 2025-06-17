<template>
    <v-container>
        <h2>지원서 목록</h2>
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
                <tr v-for="app in store.applicationList" :key="app.id">
                    <td>{{ app.name }}</td>
                    <td>{{ app.email }}</td>
                    <td>{{ app.phone }}</td>
                    <td>{{ app.status }}</td>
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

onMounted(async () => {
    const recruitmentId = route.params.recruitmentId;
    await store.loadApplicationsByRecruitmentId(recruitmentId);
});
</script>