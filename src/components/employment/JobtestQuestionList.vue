<template>
    <v-card variant="outlined" class="pa-4">
        <h3 class="text-subtitle-1 font-weight-bold mb-4">문제 목록</h3>
        <v-table dense>
            <thead>
                <tr>
                    <th>번호</th>
                    <th>내용</th>
                    <th>배점</th>
                    <th>유형</th>
                    <th>난이도</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="(question, index) in validQuestions" :key="question.id">
                    <td>{{ index + 1 }}</td>
                    <td>{{ question.content }}</td>
                    <td>{{ question.score }}점</td>
                    <td>
                        <span :style="getQuestionTypeStyle(question.type)">
                            {{ getQuestionTypeLabel(question.type) }}
                        </span>
                    </td>
                    <td>
                        <span :style="getDifficultyStyle(question.difficulty)">
                            {{ getDifficultyLabel(question.difficulty) }}
                        </span>
                    </td>
                </tr>
            </tbody>
        </v-table>
    </v-card>
</template>

<script setup>
import { computed } from 'vue'
import { getDifficultyLabel, getDifficultyColors } from '@/constants/employment/difficulty.js'
import { getQuestionTypeLabel, getQuestionTypeColors } from '@/constants/employment/questionTypes.js'

const props = defineProps({
    questions: Array
})

const validQuestions = computed(() => {
    return (props.questions || []).filter(q => q);
});

const getQuestionTypeStyle = (type) => {
    const colors = getQuestionTypeColors(type);
    return {
        backgroundColor: colors.background,
        color: colors.text,
        padding: '4px 8px',
        borderRadius: '12px',
        fontSize: '0.75rem',
        fontWeight: '600',
        textTransform: 'uppercase',
        letterSpacing: '0.3px',
        boxShadow: '0 1px 3px rgba(0, 0, 0, 0.1)',
        transition: 'all 0.2s ease',
        display: 'inline-flex',
        alignItems: 'center'
    };
};

const getDifficultyStyle = (difficulty) => {
    const colors = getDifficultyColors(difficulty);
    return {
        backgroundColor: colors.background,
        color: colors.text,
        padding: '4px 8px',
        borderRadius: '12px',
        fontSize: '0.75rem',
        fontWeight: '600',
        textTransform: 'uppercase',
        letterSpacing: '0.3px',
        boxShadow: '0 1px 3px rgba(0, 0, 0, 0.1)',
        transition: 'all 0.2s ease',
        display: 'inline-flex',
        alignItems: 'center'
    };
};
</script>
