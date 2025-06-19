<template>
    <!-- 다중 선택 시 네비게이션 버튼 -->
    <v-row v-if="selectedApplicants.length > 1" class="mt-4">
        <v-col cols="12" class="d-flex justify-center align-center">
            <v-btn @click="previousApplicant" :disabled="currentApplicantIndex === 0" variant="outlined" class="mr-4">
                <v-icon>mdi-chevron-left</v-icon>
                이전
            </v-btn>
            <span class="mx-4 text-body-1">
                {{ currentApplicantIndex + 1 }} / {{ selectedApplicants.length }}
            </span>
            <v-btn @click="nextApplicant" :disabled="currentApplicantIndex === selectedApplicants.length - 1"
                variant="outlined" class="ml-4">
                다음
                <v-icon>mdi-chevron-right</v-icon>
            </v-btn>
        </v-col>
    </v-row>
</template>

<script setup>
// Props
defineProps({
    selectedApplicants: {
        type: Array,
        default: () => []
    },
    currentApplicantIndex: {
        type: Number,
        default: 0
    }
})

// Emits
const emit = defineEmits([
    'previousApplicant',
    'nextApplicant'
])

// Methods
const previousApplicant = () => {
    emit('previousApplicant')
}

const nextApplicant = () => {
    emit('nextApplicant')
}
</script>

<style scoped>
/* 네비게이션 관련 스타일 */
</style>