<template>
    <div class="action-container">
        <div class="action-card">
            <div class="action-header">
                <div class="action-icon-wrapper">
                    <v-icon class="action-icon">mdi-account-plus</v-icon>
                </div>
                <div class="action-content">
                    <h3 class="action-title">등록 준비 완료</h3>
                    <p class="action-subtitle">
                        {{ currentApplicant?.name || '지원자' }}님의 정보를 확인하고 등록하세요
                    </p>
                </div>
            </div>

            <div class="action-body">
                <v-btn class="register-btn" color="success" size="large" variant="flat" @click="onRegister">
                    <v-icon size="20" class="mr-2">mdi-check-circle</v-icon>
                    {{ currentApplicant?.name || '지원자' }} 등록하기
                </v-btn>

                <div v-if="selectedApplicants.length > 1" class="progress-info">
                    <div class="progress-text">
                        <v-icon size="16" class="mr-1">mdi-information-outline</v-icon>
                        현재 편집 중인 지원자만 등록됩니다
                    </div>
                    <v-chip size="small" variant="tonal" color="primary" class="mt-2">
                        {{ currentApplicantIndex + 1 }} / {{ selectedApplicants.length }}
                    </v-chip>
                </div>
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
const emit = defineEmits(['register'])

// Methods
const onRegister = () => {
    emit('register')
}
</script>

<style scoped>
/* 액션 컨테이너 */
.action-container {
    margin-top: 2rem;
    display: flex;
    justify-content: center;
}

.action-card {
    background: linear-gradient(135deg, #ffffff 0%, #f8fafc 100%);
    border-radius: 20px;
    padding: 2rem;
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
    border: 1px solid rgba(255, 255, 255, 0.2);
    backdrop-filter: blur(10px);
    max-width: 500px;
    width: 100%;
    text-align: center;
    position: relative;
    overflow: hidden;
}

.action-card::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 4px;
    background: linear-gradient(90deg, #10b981 0%, #059669 50%, #10b981 100%);
}

.action-header {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 1rem;
    margin-bottom: 2rem;
    flex-direction: column;
}

.action-icon-wrapper {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 64px;
    height: 64px;
    background: linear-gradient(135deg, #10b981 0%, #059669 100%);
    border-radius: 16px;
    box-shadow: 0 8px 24px rgba(16, 185, 129, 0.3);
    border: 3px solid rgba(255, 255, 255, 0.8);
}

.action-icon {
    color: white;
    font-size: 28px;
}

.action-content {
    text-align: center;
}

.action-title {
    font-size: 1.5rem;
    font-weight: 700;
    color: #1e293b;
    margin: 0 0 0.5rem 0;
    line-height: 1.2;
}

.action-subtitle {
    font-size: 1rem;
    color: #64748b;
    margin: 0;
    font-weight: 400;
    line-height: 1.5;
}

.action-body {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 1rem;
}

.register-btn {
    min-width: 240px;
    height: 56px;
    border-radius: 16px !important;
    background: linear-gradient(135deg, #10b981 0%, #059669 100%) !important;
    color: white !important;
    font-weight: 700 !important;
    font-size: 1.125rem !important;
    text-transform: none !important;
    box-shadow: 0 8px 24px rgba(16, 185, 129, 0.4) !important;
    border: none !important;
    transition: all 0.3s ease !important;
}

.register-btn:hover {
    background: linear-gradient(135deg, #059669 0%, #047857 100%) !important;
    transform: translateY(-2px) !important;
    box-shadow: 0 12px 32px rgba(16, 185, 129, 0.5) !important;
}

.register-btn:active {
    transform: translateY(0) !important;
}

.progress-info {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 0.5rem;
    padding: 1rem;
    background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
    border-radius: 12px;
    border: 1px solid rgba(226, 232, 240, 0.3);
    box-shadow: inset 0 2px 8px rgba(0, 0, 0, 0.05);
}

.progress-text {
    display: flex;
    align-items: center;
    color: #64748b;
    font-size: 0.875rem;
    font-weight: 500;
}

/* 칩 스타일 개선 */
:deep(.v-chip) {
    border-radius: 8px !important;
    background: linear-gradient(135deg, #64748b 0%, #475569 100%) !important;
    color: white !important;
    border: 1px solid rgba(255, 255, 255, 0.2) !important;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1) !important;
    font-weight: 600 !important;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
    .action-card {
        margin: 0 1rem;
        padding: 1.5rem;
    }

    .register-btn {
        min-width: 200px;
        height: 48px;
        font-size: 1rem !important;
    }

    .action-title {
        font-size: 1.25rem;
    }

    .action-subtitle {
        font-size: 0.875rem;
    }
}

/* 애니메이션 */
@keyframes pulse {

    0%,
    100% {
        transform: scale(1);
    }

    50% {
        transform: scale(1.05);
    }
}

.action-icon-wrapper {
    animation: pulse 2s infinite;
}

.register-btn {
    animation: fadeInUp 0.6s ease-out 0.3s both;
}

@keyframes fadeInUp {
    from {
        opacity: 0;
        transform: translateY(20px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}
</style>