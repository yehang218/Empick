<template>
    <v-card variant="outlined" class="pa-4 mt-6">
        <h3 class="text-subtitle-1 font-weight-bold mb-4">지원자 현황</h3>

        <v-table dense>
            <thead>
                <tr>
                    <th>No</th>
                    <th>지원자</th>
                    <th>채용 공고</th>
                    <th>채점 상태</th>
                    <th>채점 점수</th>
                    <th>채점자</th>
                    <th>평가 상태</th>
                    <th>평가 점수</th>
                    <th>평가자</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="(app, index) in applications" :key="app.applicationId">
                    <td>{{ index + 1 }}</td>
                    <td>{{ app.applicantName }}</td>
                    <td>{{ app.recruitmentTitle }}</td>
                    <td>{{ statusLabel(app.gradingStatus) }}</td>
                    <td>{{ app.gradingScore ?? '-' }}</td>
                    <td>{{ app.gradingMemberName ?? '-' }}</td>
                    <td>{{ statusLabel(app.evaluationStatus) }}</td>
                    <td>{{ app.evaluationScore ?? '-' }}</td>
                    <td>{{ app.evaluationMemberName ?? '-' }}</td>
                </tr>
            </tbody>
        </v-table>
    </v-card>
</template>

<script setup>
defineProps({
    applications: Array
})

const statusLabel = (status) => {
    switch (status) {
        case 'COMPLETED': return '완료'
        case 'IN_PROGRESS': return '진행중'
        case 'NOT_STARTED': return '미시작'
        default: return '-'
    }
}
</script>
