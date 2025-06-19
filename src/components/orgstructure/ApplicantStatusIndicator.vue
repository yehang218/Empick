<template>
    <div class="status-indicator-container">
        <div class="status-card">
            <div class="status-content">
                <div class="status-label">현재 지원자 상태</div>
                <div class="status-display">
                    <div class="status-dot" :class="getStatusClass(currentApplicant)">
                        <v-icon v-if="getStatusIcon(currentApplicant)" size="12" class="status-icon">
                            {{ getStatusIcon(currentApplicant) }}
                        </v-icon>
                    </div>
                    <div class="status-text">{{ getStatusText(currentApplicant) }}</div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
// Props
defineProps({
    currentApplicant: {
        type: Object,
        default: null
    }
})

// 상태 관련 메서드들
const getStatusClass = (applicant) => {
    const status = applicant?.status || applicant?.recruitmentStatus

    switch (status) {
        case 'PASSED_DOCS':
            return 'status-document-pass'
        case 'PASSED_INTERVIEW_2':
        case 'PASSED_FINAL':
            return 'status-second-pass'
        case 'WAITING':
            return 'status-waiting'
        default:
            return 'status-no-info'
    }
}

const getStatusIcon = (applicant) => {
    const status = applicant?.status || applicant?.recruitmentStatus

    switch (status) {
        case 'PASSED_DOCS':
            return 'mdi-file-check'
        case 'PASSED_INTERVIEW_2':
        case 'PASSED_FINAL':
            return 'mdi-check-all'
        case 'WAITING':
            return 'mdi-clock-outline'
        default:
            return 'mdi-help'
    }
}

const getStatusText = (applicant) => {
    const status = applicant?.status || applicant?.recruitmentStatus

    switch (status) {
        case 'PASSED_DOCS':
            return '서류합격'
        case 'PASSED_INTERVIEW_2':
            return '2차합격'
        case 'PASSED_FINAL':
            return '최종합격'
        case 'WAITING':
            return '대기중'
        default:
            return '정보없음'
    }
}
</script>

<style scoped>
.status-indicator-container {
    display: flex;
    margin-bottom: 1rem;
    flex: 0 0 180px;
}

.status-card {
    background: rgba(255, 255, 255, 0.9);
    border-radius: 16px;
    padding: 1rem 1.25rem;
    border: 1px solid rgba(226, 232, 240, 0.3);
    backdrop-filter: blur(10px);
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.06);
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
}

.status-content {
    text-align: center;
    width: 100%;
}

.status-label {
    font-size: 0.7rem;
    font-weight: 500;
    color: #64748b;
    margin-bottom: 0.5rem;
}

.status-display {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 0.5rem;
}

.status-dot {
    width: 28px;
    height: 28px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
    border: 2px solid #ffffff;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.status-document-pass {
    background: linear-gradient(135deg, #10b981 0%, #059669 100%);
}

.status-second-pass {
    background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
}

.status-waiting {
    background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
}

.status-no-info {
    background: linear-gradient(135deg, #64748b 0%, #475569 100%);
}

.status-icon {
    color: white;
    font-weight: bold;
}

.status-text {
    font-size: 0.75rem;
    font-weight: 600;
    color: #334155;
    text-align: center;
    line-height: 1.2;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
    .status-indicator-container {
        justify-content: center;
        margin-bottom: 1.5rem;
    }

    .status-card {
        max-width: 100%;
        margin: 0 1rem;
        padding: 1rem;
        height: auto;
    }

    .status-dot {
        width: 28px;
        height: 28px;
    }

    .status-text {
        font-size: 0.75rem;
    }
}

@media (max-width: 480px) {
    .status-card {
        margin: 0 1rem;
    }

    .status-dot {
        width: 24px;
        height: 24px;
    }

    .status-text {
        font-size: 0.7rem;
    }
}
</style>