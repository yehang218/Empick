<template>
    <v-card variant="outlined" class="pa-4 mb-4">
        <div class="d-flex justify-space-between">
            <div>
                <div class="text-h6 font-weight-bold mb-2">{{ jobtest.title }}</div>
                <div class="text-subtitle-2">
                    난이도: 
                    <span class="difficulty-tag" :style="getDifficultyStyle(jobtest.difficulty)">
                        {{ getDifficultyLabel(jobtest.difficulty) }}
                    </span>
                </div>
                <div class="text-subtitle-2">시험 시간: {{ jobtest.testTime }}분</div>
                <div class="text-subtitle-2">시작: {{ formatDate(jobtest.startedAt) }}</div>
                <div class="text-subtitle-2">종료: {{ formatDate(jobtest.endedAt) }}</div>
            </div>
            <div class="text-right">
                <div class="text-subtitle-2">생성자: {{ jobtest.createdName }} ({{ formatDate(jobtest.createdAt) }})</div>
                <div class="text-subtitle-2">수정자: {{ jobtest.updatedName }} ({{ formatDate(jobtest.updatedAt) }})</div>
            </div>
        </div>
    </v-card>
</template>

<script setup>
import { computed } from 'vue'
import { getDifficultyLabel, getDifficultyColors } from '@/constants/employment/difficulty.js'

const props = defineProps({
    jobtest: Object
})

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

const formatDate = (dateStr) => {
    if (!dateStr) return '-'
    const date = new Date(dateStr)
    return `${date.toLocaleDateString()} ${date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}`
}
</script>
