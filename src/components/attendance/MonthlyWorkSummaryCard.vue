<template>
    <div class="monthly-work-summary-card">
        <!-- 헤더 -->
        <div class="card-header">
            <div class="title-section">
                <h3 class="title">선택된 근무시간대</h3>
                <span class="time-range">06:00:00 ~ 22:00:00</span>
                <v-icon class="info-icon" size="small" color="grey">mdi-information-outline</v-icon>
            </div>
            <div class="action-buttons">
                <v-btn variant="outlined" size="small" prepend-icon="mdi-calendar-month">
                    월별근무상세
                </v-btn>
                <v-btn variant="outlined" size="small" prepend-icon="mdi-download">
                    엑셀 다운로드
                </v-btn>
            </div>
        </div>

        <!-- 메인 콘텐츠 -->
        <div class="card-content">
            <!-- 좌측: 진행률 및 시간 -->
            <div class="progress-section">
                <div class="month-info">
                    <span class="month">09월</span>
                    <span class="sub-text">(총 근무구간은 51h 24m 이상은 권장)</span>
                </div>

                <div class="time-display">
                    <span class="hours">51</span>
                    <span class="unit">h</span>
                    <span class="minutes">45</span>
                    <span class="unit">m</span>
                </div>

                <!-- 진행률 바 -->
                <div class="progress-container">
                    <v-progress-linear :model-value="progressPercentage" height="8" color="success"
                        bg-color="grey-lighten-3" rounded></v-progress-linear>
                    <span class="progress-text">{{ progressPercentage.toFixed(1) }}%</span>
                </div>

                <!-- 통계 정보 -->
                <div class="stats-info">
                    <div class="stat-item">
                        <span class="label">남은 근무시간:</span>
                        <span class="value">최소 11h 14m ~ 최대 16h 38m</span>
                    </div>
                    <div class="stat-item">
                        <span class="label">남은 09월 근무시간:</span>
                        <span class="value">51h 24m</span>
                    </div>
                    <div class="stat-item">
                        <span class="label">일일 예상 필요 평균:</span>
                        <span class="value">8h 18m 평균 (남은 근무일: 14 일)</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, computed } from 'vue'

// Props
const props = defineProps({
    workingHours: {
        type: Object,
        default: () => ({
            hours: 51,
            minutes: 45
        })
    },
    targetHours: {
        type: Object,
        default: () => ({
            hours: 51,
            minutes: 24
        })
    },
    month: {
        type: String,
        default: '09월'
    },
    remainingWorkDays: {
        type: Number,
        default: 14
    }
})

// 시간/분 옵션
const hours = computed(() => {
    return Array.from({ length: 24 }, (_, i) => ({
        title: i.toString().padStart(2, '0'),
        value: i.toString().padStart(2, '0')
    }))
})

const minutes = computed(() => {
    return Array.from({ length: 60 }, (_, i) => ({
        title: i.toString().padStart(2, '0'),
        value: i.toString().padStart(2, '0')
    }))
})

const statusOptions = [
    { title: '휴근', value: 'absent' },
    { title: '출근', value: 'present' },
    { title: '지각', value: 'late' },
    { title: '조퇴', value: 'early_leave' },
    { title: '반차', value: 'half_day' }
]

// 진행률 계산
const progressPercentage = computed(() => {
    const currentMinutes = props.workingHours.hours * 60 + props.workingHours.minutes
    const targetMinutes = props.targetHours.hours * 60 + props.targetHours.minutes
    return (currentMinutes / targetMinutes) * 100
})

// 메서드
const closeDetailModal = () => {
    // 모달 닫기 로직
    console.log('모달 닫기')
}

// Emits
defineEmits(['update:workingHours', 'downloadExcel', 'viewMonthlyDetail'])
</script>

<style scoped>
.monthly-work-summary-card {
    background: white;
    border-radius: 8px;
    padding: 24px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 24px;
}

.title-section {
    display: flex;
    align-items: center;
    gap: 8px;
}

.title {
    font-size: 18px;
    font-weight: 600;
    color: #333;
    margin: 0;
}

.time-range {
    font-size: 14px;
    color: #666;
}

.info-icon {
    cursor: pointer;
}

.action-buttons {
    display: flex;
    gap: 8px;
}

.card-content {
    display: flex;
    gap: 40px;
}

.progress-section {
    flex: 1;
}

.month-info {
    margin-bottom: 16px;
}

.month {
    font-size: 16px;
    font-weight: 600;
    color: #333;
    margin-right: 8px;
}

.sub-text {
    font-size: 12px;
    color: #666;
}

.time-display {
    display: flex;
    align-items: baseline;
    margin-bottom: 16px;
}

.hours,
.minutes {
    font-size: 48px;
    font-weight: 700;
    color: #333;
}

.unit {
    font-size: 24px;
    font-weight: 500;
    color: #666;
    margin-right: 8px;
}

.progress-container {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-bottom: 20px;
}

.progress-container .v-progress-linear {
    flex: 1;
}

.progress-text {
    font-size: 14px;
    font-weight: 600;
    color: #4CAF50;
    min-width: 50px;
}

.stats-info {
    display: flex;
    flex-direction: column;
    gap: 8px;
}

.stat-item {
    display: flex;
    gap: 8px;
}

.label {
    font-size: 14px;
    color: #666;
    min-width: 140px;
}

.value {
    font-size: 14px;
    color: #333;
    font-weight: 500;
}

.detail-section {
    width: 300px;
}

.detail-modal {
    width: 100%;
}

.modal-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px 20px 8px 20px;
    font-size: 16px;
    font-weight: 600;
}

.modal-content {
    padding: 0 20px 20px 20px;
}

.form-row {
    display: flex;
    align-items: center;
    margin-bottom: 16px;
}

.form-label {
    width: 60px;
    font-size: 14px;
    color: #333;
    font-weight: 500;
}

.form-controls {
    flex: 1;
    display: flex;
    align-items: center;
}

.form-controls .v-text-field,
.form-controls .v-select {
    margin-bottom: 0;
}

.form-controls .v-checkbox {
    margin-left: 0;
}
</style>