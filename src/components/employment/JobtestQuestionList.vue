<template>
    <v-card variant="outlined" class="pa-4">
        <h3 class="text-subtitle-1 font-weight-bold mb-4">문제 목록</h3>
        <v-table dense>
            <thead>
                <tr>
                    <th>번호</th>
                    <th>내용</th>
                    <th>배점</th>
                    <th>난이도</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="(question, index) in questions" :key="question.id">
                    <td>{{ index + 1 }}</td>
                    <td>{{ question.content }}</td>
                    <td>{{ question.score }}점</td>
                    <td>{{ difficultyLabel(question.difficulty) }}</td>
                </tr>
            </tbody>
        </v-table>
    </v-card>
</template>

<script setup>
defineProps({
    questions: Array
})

const difficultyLabel = (level) => {
    switch (level) {
        case 'EASY': return '쉬움'
        case 'MEDIUM': return '보통'
        case 'HARD': return '어려움'
        default: return '알 수 없음'
    }
}
</script>
