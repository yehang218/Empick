<template>
    <v-card variant="outlined" class="pa-4 mb-4">
        <div class="d-flex justify-space-between">
            <div>
                <div class="text-h6 font-weight-bold mb-2">{{ jobtest.title }}</div>
                <div class="text-subtitle-2">난이도: {{ difficultyLabel }}</div>
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

const props = defineProps({
    jobtest: Object
})

const difficultyLabel = computed(() => {
    switch (props.jobtest.difficulty) {
        case 'EASY': return '쉬움'
        case 'MEDIUM': return '보통'
        case 'HARD': return '어려움'
        default: return '알 수 없음'
    }
})

const formatDate = (dateStr) => {
    if (!dateStr) return '-'
    const date = new Date(dateStr)
    return `${date.toLocaleDateString()} ${date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}`
}
</script>
