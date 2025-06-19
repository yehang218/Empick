<template>
    <v-card v-if="selectedApplicants.length > 0" class="mb-6" elevation="2">
        <v-card-title class="modern-card-title">
            <div class="title-content">
                <div class="title-icon-wrapper">
                    <v-icon class="title-icon">mdi-account-multiple</v-icon>
                </div>
                <div class="title-text">
                    <h3 class="title-main">선택된 지원자 정보</h3>
                    <p class="title-sub">{{ selectedApplicants.length }}명의 지원자가 선택되었습니다</p>
                </div>
            </div>
            <!-- 다중 선택 시 전체 선택/해제 및 일괄 등록 버튼 -->
            <div v-if="selectedApplicants.length > 1" class="action-buttons">
                <v-checkbox :model-value="selectAllForRegistration" :indeterminate="isIndeterminate" label="전체 선택"
                    hide-details density="compact" class="modern-checkbox"
                    @update:model-value="toggleSelectAllForRegistration" />
                <v-btn color="success" variant="tonal" size="small" class="modern-btn-primary"
                    :disabled="selectedForRegistration.length === 0" @click="onBulkRegister">
                    <v-icon size="16" class="mr-1">mdi-account-plus</v-icon>
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
/* 모던 카드 타이틀 */
.modern-card-title {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 1.5rem !important;
    background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%) !important;
    border-bottom: 1px solid rgba(226, 232, 240, 0.3) !important;
}

.title-content {
    display: flex;
    align-items: center;
    gap: 1rem;
}

.title-icon-wrapper {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 48px;
    height: 48px;
    background: linear-gradient(135deg, #ffffff 0%, #f8fafc 100%);
    border-radius: 12px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
    border: 1px solid rgba(226, 232, 240, 0.3);
}

.title-icon {
    color: #64748b;
    font-size: 24px;
}

.title-text {
    flex: 1;
}

.title-main {
    font-size: 1.25rem;
    font-weight: 600;
    color: #334155;
    margin: 0 0 0.25rem 0;
    line-height: 1.3;
}

.title-sub {
    font-size: 0.875rem;
    color: #64748b;
    margin: 0;
    font-weight: 400;
}

.action-buttons {
    display: flex;
    align-items: center;
    gap: 1rem;
}

.modern-checkbox {
    margin-right: 0 !important;
}

.modern-btn-primary {
    background: linear-gradient(135deg, #10b981 0%, #059669 100%) !important;
    color: white !important;
    border: none !important;
    box-shadow: 0 4px 12px rgba(16, 185, 129, 0.3) !important;
}

.modern-btn-primary:hover {
    background: linear-gradient(135deg, #059669 0%, #047857 100%) !important;
    transform: translateY(-1px) !important;
    box-shadow: 0 6px 16px rgba(16, 185, 129, 0.4) !important;
}

.modern-btn-primary:disabled {
    background: linear-gradient(135deg, #e2e8f0 0%, #cbd5e1 100%) !important;
    color: #94a3b8 !important;
    transform: none !important;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05) !important;
}

/* 스크롤 영역 개선 */
.selected-applicants-scroll {
    border: 1px solid rgba(226, 232, 240, 0.5);
    border-radius: 16px;
    background: linear-gradient(135deg, #ffffff 0%, #f8fafc 100%);
    box-shadow: inset 0 2px 8px rgba(0, 0, 0, 0.05);
    backdrop-filter: blur(10px);
}

.selected-applicant {
    background: linear-gradient(135deg, #f1f5f9 0%, #e2e8f0 100%) !important;
    border-left: 4px solid #94a3b8;
    border-radius: 12px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
    margin: 8px;
}

.selected-applicant:hover {
    background: linear-gradient(135deg, #e2e8f0 0%, #cbd5e1 100%) !important;
    transform: translateY(-1px);
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;
}

.v-list-item {
    border-radius: 12px;
    margin-bottom: 8px;
    transition: all 0.3s ease;
    background: rgba(255, 255, 255, 0.7);
    backdrop-filter: blur(5px);
}

.v-list-item:hover {
    background: rgba(248, 250, 252, 0.9);
    transform: translateY(-1px);
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
}

/* 아바타 스타일 개선 */
:deep(.v-avatar) {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1) !important;
    border: 2px solid rgba(255, 255, 255, 0.8) !important;
    background: linear-gradient(135deg, #64748b 0%, #475569 100%) !important;
}

/* 칩 스타일 개선 */
:deep(.v-chip) {
    border-radius: 8px !important;
    background: linear-gradient(135deg, #f1f5f9 0%, #e2e8f0 100%) !important;
    color: #64748b !important;
    border: 1px solid rgba(226, 232, 240, 0.3) !important;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05) !important;
    font-weight: 500 !important;
}

:deep(.v-chip--variant-tonal) {
    background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%) !important;
}

/* 성공 칩 */
:deep(.v-chip.bg-success) {
    background: linear-gradient(135deg, #10b981 0%, #059669 100%) !important;
    color: white !important;
}

/* 프라이머리 칩 */
:deep(.v-chip.bg-primary) {
    background: linear-gradient(135deg, #64748b 0%, #475569 100%) !important;
    color: white !important;
}

/* 진행 바 스타일 개선 */
:deep(.v-progress-linear) {
    border-radius: 8px !important;
    overflow: hidden !important;
    background: rgba(226, 232, 240, 0.3) !important;
    box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.1) !important;
}

/* 체크박스 스타일 개선 */
:deep(.v-checkbox .v-selection-control__wrapper) {
    border-radius: 6px !important;
    background: rgba(255, 255, 255, 0.8) !important;
    backdrop-filter: blur(5px) !important;
}

/* 구분선 스타일 개선 */
:deep(.v-divider) {
    border-color: rgba(226, 232, 240, 0.3) !important;
    margin: 1.5rem 0 !important;
}

/* 카드 텍스트 패딩 조정 */
:deep(.v-card-text) {
    padding: 1.5rem !important;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
    .modern-card-title {
        flex-direction: column;
        align-items: flex-start;
        gap: 1rem;
    }

    .action-buttons {
        width: 100%;
        justify-content: space-between;
    }

    .title-content {
        width: 100%;
    }
}
</style>