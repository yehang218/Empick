<template>
    <v-card v-if="selectedApplicants.length > 0" class="mb-6" elevation="2">
        <v-card-title class="d-flex align-center justify-space-between">
            <div class="d-flex align-center">
                <v-icon class="mr-2" color="primary">mdi-account-multiple</v-icon>
                <span>선택된 지원자 정보 ({{ selectedApplicants.length }}명)</span>
            </div>
            <!-- 다중 선택 시 전체 선택/해제 및 일괄 등록 버튼 -->
            <div v-if="selectedApplicants.length > 1" class="d-flex align-center">
                <v-checkbox :model-value="selectAllForRegistration" :indeterminate="isIndeterminate" label="전체 선택"
                    hide-details density="compact" class="mr-4" @update:model-value="toggleSelectAllForRegistration" />
                <v-btn color="success" variant="tonal" size="small" :disabled="selectedForRegistration.length === 0"
                    @click="onBulkRegister">
                    일괄 등록 ({{ selectedForRegistration.length }}명)
                </v-btn>
            </div>
        </v-card-title>

        <!-- 다중 선택 시 스크롤박스 표시 -->
        <v-card-text v-if="selectedApplicants.length > 1">
            <div class="selected-applicants-scroll" style="max-height: 200px; overflow-y: auto;">
                <v-list density="compact">
                    <v-list-item v-for="(applicant, index) in selectedApplicants" :key="applicant.applicantId"
                        class="mb-2" :class="{ 'selected-applicant': currentApplicantIndex === index }"
                        style="cursor: pointer;">
                        <template v-slot:prepend>
                            <v-checkbox :model-value="isSelectedForRegistration(applicant)"
                                @update:model-value="toggleRegistrationSelection(applicant)" hide-details
                                density="compact" class="mr-2" @click.stop />
                            <v-avatar size="40" color="primary" @click="selectCurrentApplicant(index)">
                                <span class="text-white">{{ applicant.name?.charAt(0) || '?' }}</span>
                            </v-avatar>
                        </template>
                        <div @click="selectCurrentApplicant(index)" class="flex-grow-1">
                            <v-list-item-title>{{ applicant.name || '이름 없음' }}</v-list-item-title>
                            <v-list-item-subtitle>
                                {{ applicant.email || '이메일 없음' }} | {{ applicant.phone || '연락처 없음' }}
                            </v-list-item-subtitle>
                            <!-- 등록 진행 상황 프로그레스바 -->
                            <div v-if="registrationProgress[applicant.applicantId]" class="mt-2">
                                <v-progress-linear :model-value="registrationProgress[applicant.applicantId].progress"
                                    :color="getProgressColor(registrationProgress[applicant.applicantId].status)"
                                    height="4" rounded />
                                <div class="text-caption mt-1"
                                    :class="getProgressTextColor(registrationProgress[applicant.applicantId].status)">
                                    {{ registrationProgress[applicant.applicantId].message }}
                                </div>
                            </div>
                        </div>
                        <template v-slot:append>
                            <div class="d-flex flex-column align-center">
                                <v-chip size="small" :color="currentApplicantIndex === index ? 'primary' : 'grey'"
                                    variant="tonal" class="mb-1">
                                    {{ currentApplicantIndex === index ? '현재 편집중' : '대기' }}
                                </v-chip>
                                <v-chip v-if="isSelectedForRegistration(applicant)" size="x-small" color="success"
                                    variant="tonal">
                                    등록 대상
                                </v-chip>
                                <!-- 등록 상태 칩 -->
                                <v-chip v-if="registrationProgress[applicant.applicantId]" size="x-small"
                                    :color="getStatusChipColor(registrationProgress[applicant.applicantId].status)"
                                    variant="tonal" class="mt-1">
                                    {{ getStatusText(registrationProgress[applicant.applicantId].status) }}
                                </v-chip>
                            </div>
                        </template>
                    </v-list-item>
                </v-list>
            </div>
            <v-divider class="my-4"></v-divider>
            <div class="d-flex align-center justify-space-between">
                <span class="text-subtitle-2 text-grey">현재 편집중인 지원자:</span>
                <v-chip color="primary" variant="tonal">
                    {{ currentApplicant?.name || '선택된 지원자 없음' }}
                </v-chip>
            </div>
            <div v-if="selectedForRegistration.length > 0" class="mt-2 d-flex align-center justify-space-between">
                <span class="text-subtitle-2 text-success">등록 대상 지원자:</span>
                <v-chip color="success" variant="tonal">
                    {{selectedForRegistration.map(a => a.name).join(', ')}}
                </v-chip>
            </div>
        </v-card-text>

        <!-- 단일 선택 시 간단한 정보만 표시 -->
        <v-card-text v-else>
            <div class="d-flex align-center">
                <v-avatar size="48" color="primary" class="mr-4">
                    <span class="text-white text-h6">{{ selectedApplicants[0]?.name?.charAt(0) || '?'
                        }}</span>
                </v-avatar>
                <div>
                    <div class="text-h6">{{ selectedApplicants[0]?.name || '이름 없음' }}</div>
                    <div class="text-body-2 text-grey">
                        {{ selectedApplicants[0]?.email || '이메일 없음' }} | {{ selectedApplicants[0]?.phone ||
                            '연락처 없음' }}
                    </div>
                </div>
            </div>
        </v-card-text>
    </v-card>
</template>

<script setup>
// Props
const props = defineProps({
    selectedApplicants: {
        type: Array,
        default: () => []
    },
    currentApplicantIndex: {
        type: Number,
        default: 0
    },
    selectedForRegistration: {
        type: Array,
        default: () => []
    },
    selectAllForRegistration: {
        type: Boolean,
        default: false
    },
    isIndeterminate: {
        type: Boolean,
        default: false
    },
    registrationProgress: {
        type: Object,
        default: () => ({})
    },
    currentApplicant: {
        type: Object,
        default: null
    }
})

// Emits
const emit = defineEmits([
    'toggleSelectAllForRegistration',
    'bulkRegister',
    'toggleRegistrationSelection',
    'selectCurrentApplicant'
])

// Methods
const toggleSelectAllForRegistration = (value) => {
    emit('toggleSelectAllForRegistration', value)
}

const onBulkRegister = () => {
    emit('bulkRegister')
}

const toggleRegistrationSelection = (applicant) => {
    emit('toggleRegistrationSelection', applicant)
}

const selectCurrentApplicant = (index) => {
    emit('selectCurrentApplicant', index)
}

const isSelectedForRegistration = (applicant) => {
    return props.selectedForRegistration.some(selected =>
        selected.applicantId === applicant.applicantId
    )
}

// Import helper functions from composable
import { useRegistrationProgress } from '@/composables/useRegistrationProgress'

const { getProgressColor, getProgressTextColor, getStatusChipColor, getStatusText } = useRegistrationProgress()
</script>

<style scoped>
.selected-applicants-scroll {
    border: 1px solid #e0e0e0;
    border-radius: 8px;
    background-color: #fafafa;
}

.selected-applicant {
    background-color: #e3f2fd !important;
    border-left: 4px solid #1976d2;
}

.selected-applicant:hover {
    background-color: #bbdefb !important;
}

.v-list-item {
    border-radius: 6px;
    margin-bottom: 4px;
}

.v-list-item:hover {
    background-color: #f5f5f5;
}
</style>