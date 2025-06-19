<template>
    <div class="navigation-container">
        <div class="nav-card">
            <!-- 현재 지원자 정보 -->
            <div class="current-applicant-info">
                <div class="applicant-avatar">
                    <v-avatar size="40" color="primary">
                        <span class="text-white font-weight-bold">{{ currentApplicant?.name?.charAt(0) || '?' }}</span>
                    </v-avatar>
                </div>
                <div class="applicant-details">
                    <h4 class="applicant-name">{{ currentApplicant?.name || '지원자' }}</h4>
                    <p class="applicant-progress">{{ currentApplicantIndex + 1 }} / {{ selectedApplicants.length }}</p>
                </div>
            </div>

            <!-- 네비게이션 버튼 -->
            <div class="nav-buttons">
                <v-btn @click="previousApplicant" :disabled="currentApplicantIndex === 0" variant="outlined"
                    size="small" class="nav-btn">
                    <v-icon size="16">mdi-chevron-left</v-icon>
                    이전
                </v-btn>
                <v-btn @click="nextApplicant" :disabled="currentApplicantIndex === selectedApplicants.length - 1"
                    variant="outlined" size="small" class="nav-btn">
                    다음
                    <v-icon size="16">mdi-chevron-right</v-icon>
                </v-btn>
            </div>
        </div>
    </div>
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
/* 네비게이션 컨테이너 */
.navigation-container {
    margin-bottom: 1.5rem;
    display: flex;
    justify-content: center;
}

.nav-card {
    background: rgba(255, 255, 255, 0.9);
    border-radius: 16px;
    padding: 1rem 1.5rem;
    border: 1px solid rgba(226, 232, 240, 0.3);
    backdrop-filter: blur(10px);
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.06);
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 1.5rem;
    max-width: 500px;
    width: 100%;
}

/* 현재 지원자 정보 */
.current-applicant-info {
    display: flex;
    align-items: center;
    gap: 0.75rem;
    flex: 1;
}

.applicant-avatar {
    flex-shrink: 0;
}

.applicant-details {
    flex: 1;
    min-width: 0;
}

.applicant-name {
    font-size: 1rem;
    font-weight: 600;
    color: #334155;
    margin: 0 0 0.25rem 0;
    line-height: 1.2;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.applicant-progress {
    font-size: 0.75rem;
    color: #64748b;
    margin: 0;
    font-weight: 500;
}

/* 네비게이션 버튼 */
.nav-buttons {
    display: flex;
    gap: 0.5rem;
    flex-shrink: 0;
}

.nav-btn {
    border-radius: 10px !important;
    font-weight: 500 !important;
    text-transform: none !important;
    background: rgba(255, 255, 255, 0.8) !important;
    border: 1px solid rgba(226, 232, 240, 0.5) !important;
    color: #64748b !important;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05) !important;
    transition: all 0.3s ease !important;
    min-width: 70px !important;
}

.nav-btn:hover:not(:disabled) {
    background: rgba(248, 250, 252, 0.9) !important;
    border-color: rgba(148, 163, 184, 0.5) !important;
    transform: translateY(-1px) !important;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08) !important;
}

.nav-btn:disabled {
    background: rgba(241, 245, 249, 0.5) !important;
    color: #cbd5e1 !important;
    border-color: rgba(226, 232, 240, 0.3) !important;
    box-shadow: none !important;
}

/* 아바타 스타일 개선 */
:deep(.v-avatar) {
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1) !important;
    border: 2px solid rgba(255, 255, 255, 0.8) !important;
    background: linear-gradient(135deg, #64748b 0%, #475569 100%) !important;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
    .nav-card {
        flex-direction: column;
        gap: 1rem;
        padding: 1rem;
    }

    .current-applicant-info {
        justify-content: center;
        text-align: center;
    }

    .applicant-name {
        font-size: 0.875rem;
    }

    .applicant-progress {
        font-size: 0.7rem;
    }

    .nav-buttons {
        justify-content: center;
        width: 100%;
    }

    .nav-btn {
        flex: 1;
        max-width: 100px;
    }
}

@media (max-width: 480px) {
    .nav-card {
        margin: 0 1rem;
    }

    .nav-btn {
        min-width: 60px !important;
        font-size: 0.75rem !important;
    }
}
</style>