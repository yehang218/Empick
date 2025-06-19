<template>
    <v-row class="mt-8">
        <v-col cols="12" class="d-flex flex-column align-center">
            <v-btn color="success" @click="onRegister" size="large">
                {{ currentApplicant?.name || '지원자' }} 등록
            </v-btn>
            <div v-if="selectedApplicants.length > 1" class="text-caption text-grey mt-2">
                현재 편집 중인 지원자만 등록됩니다 ({{ currentApplicantIndex + 1 }}/{{ selectedApplicants.length }})
            </div>
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
    },
    currentApplicant: {
        type: Object,
        default: null
    }
})

// Emits
const emit = defineEmits(['register'])

// Methods
const onRegister = () => {
    emit('register')
}
</script>

<style scoped>
/* 액션 관련 스타일 */
</style>