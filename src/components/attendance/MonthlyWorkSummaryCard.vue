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
                    <span class="month">{{ month }}</span>
                    <span class="sub-text">(총 근무구간은 {{ targetHours.hours }}h {{ targetHours.minutes }}m 이상은 권장)</span>
                </div>

                <div class="time-display">
                    <span class="hours">{{ workingHours.hours }}</span>
                    <span class="unit">h</span>
                    <span class="minutes">{{ workingHours.minutes }}</span>
                    <span class="unit">m</span>
                </div>

                <!-- 진행률 바 -->
                <div class="progress-container">
                    <v-progress-linear :model-value="progressPercentage" height="8" color="success"
                        bg-color="grey-lighten-3" rounded></v-progress-linear>
                    <span class="progress-text">{{ formattedProgressPercentage }}</span>
                </div>

                <!-- 통계 정보 -->
                <div class="stats-info">
                    <div class="stat-item">
                        <span class="label">남은 근무시간:</span>
                        <span class="value">{{ remainingTimeRange }}</span>
                    </div>
                    <div class="stat-item">
                        <span class="label">남은 {{ month }} 근무시간:</span>
                        <span class="value">{{ targetHours.hours }}h {{ targetHours.minutes }}m</span>
                    </div>
                    <div class="stat-item">
                        <span class="label">일일 예상 필요 평균:</span>
                        <span class="value">{{ dailyAverageNeeded }} 평균 (남은 근무일: {{ remainingWorkDays }} 일)</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { computed } from 'vue'

// Props
const props = defineProps({
    workingHours: {
        type: Object,
        default: () => ({
            hours: 0,
            minutes: 0
        })
    },
    targetHours: {
        type: Object,
        default: () => ({
            hours: 0,
            minutes: 0
        })
    },
    month: {
        type: String,
        default: '월'
    },
    remainingWorkDays: {
        type: Number,
        default: 0
    }
})

// 진행률 계산
const progressPercentage = computed(() => {
    // 안전한 값 추출
    const currentHours = props.workingHours?.hours || 0
    const currentMins = props.workingHours?.minutes || 0
    const targetHours = props.targetHours?.hours || 0
    const targetMins = props.targetHours?.minutes || 0

    const currentMinutes = currentHours * 60 + currentMins
    const targetMinutes = targetHours * 60 + targetMins

    if (targetMinutes === 0) return 0

    const percentage = (currentMinutes / targetMinutes) * 100
    return isNaN(percentage) ? 0 : percentage
})

// 포맷팅된 진행률
const formattedProgressPercentage = computed(() => {
    const percentage = progressPercentage.value
    return `${percentage.toFixed(1)}%`
})

// 남은 근무시간 범위 계산
const remainingTimeRange = computed(() => {
    // 안전한 값 추출
    const currentHours = props.workingHours?.hours || 0
    const currentMins = props.workingHours?.minutes || 0
    const targetHours = props.targetHours?.hours || 0
    const targetMins = props.targetHours?.minutes || 0

    const currentMinutes = currentHours * 60 + currentMins
    const targetMinutes = targetHours * 60 + targetMins
    const remainingMinutes = Math.max(0, targetMinutes - currentMinutes)

    // 최소: 남은 시간 그대로, 최대: 남은 시간 + 여유시간(5시간)
    const minHours = Math.floor(remainingMinutes / 60)
    const minMins = remainingMinutes % 60
    const maxTotalMinutes = remainingMinutes + (5 * 60) // 5시간 여유
    const maxHours = Math.floor(maxTotalMinutes / 60)
    const maxMins = maxTotalMinutes % 60

    return `최소 ${minHours}h ${minMins}m ~ 최대 ${maxHours}h ${maxMins}m`
})

// 일일 평균 필요 시간 계산
const dailyAverageNeeded = computed(() => {
    if (!props.remainingWorkDays || props.remainingWorkDays === 0) return '0h 0m'

    // 안전한 값 추출
    const currentHours = props.workingHours?.hours || 0
    const currentMins = props.workingHours?.minutes || 0
    const targetHours = props.targetHours?.hours || 0
    const targetMins = props.targetHours?.minutes || 0

    const currentMinutes = currentHours * 60 + currentMins
    const targetMinutes = targetHours * 60 + targetMins
    const remainingMinutes = Math.max(0, targetMinutes - currentMinutes)

    const avgMinutesPerDay = Math.ceil(remainingMinutes / props.remainingWorkDays)
    const hours = Math.floor(avgMinutesPerDay / 60)
    const minutes = avgMinutesPerDay % 60

    return `${hours}h ${minutes}m`
})

// Emits
defineEmits(['downloadExcel', 'viewMonthlyDetail'])
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