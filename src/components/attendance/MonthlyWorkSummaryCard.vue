<template>
    <div class="monthly-work-summary-card">
        <!-- 헤더 -->
        <div class="card-header">
            <div class="title-section">
                <h3 class="title">선택된 근무시간대</h3>
                <span class="time-range">06:00:00 ~ 22:00:00</span>
                <v-icon class="info-icon" size="small" color="grey">mdi-information-outline</v-icon>
            </div>
            <!-- <div class="action-buttons">
                <v-btn variant="outlined" size="small" prepend-icon="mdi-calendar-month">
                    월별근무상세
                </v-btn>
                <v-btn variant="outlined" size="small" prepend-icon="mdi-download">
                    엑셀 다운로드
                </v-btn>
            </div> -->
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
                    <!-- 📊 직관적 상태 배지 -->
                    <div class="status-badges">
                        <!-- 🚨 법적 위험: 주 52시간 초과 -->
                        <v-chip v-if="workTimeValidation.exceedsWeeklyLimit" color="error" size="small" variant="flat"
                            class="ml-2">
                            <v-icon start size="small">mdi-alert-circle</v-icon>
                            법정한도 초과 (+{{ workTimeValidation.weeklyExcessHours }}h)
                        </v-chip>

                        <!-- ⚠️ 연장근무 한도 초과: 12시간 초과 -->
                        <v-chip v-else-if="workTimeValidation.exceedsOvertimeLimit" color="warning" size="small"
                            variant="flat" class="ml-2">
                            <v-icon start size="small">mdi-clock-alert-outline</v-icon>
                            연장한도 초과 (+{{ workTimeValidation.overtimeHours - workTimeValidation.maxOvertimeHours }}h)
                        </v-chip>

                        <!-- 📘 정상 연장근무: 12시간 이내 -->
                        <v-chip v-else-if="workTimeValidation.hasOvertimeHours" color="info" size="small" variant="flat"
                            class="ml-2">
                            <v-icon start size="small">mdi-clock-plus-outline</v-icon>
                            연장근무 {{ workTimeValidation.overtimeHours }}h
                        </v-chip>

                        <!-- 🌙 야간근무 -->
                        <v-chip v-if="workTimeValidation.hasNightHours" color="deep-purple" size="small" variant="flat"
                            class="ml-1">
                            <v-icon start size="small">mdi-weather-night</v-icon>
                            야간근무 {{ workTimeValidation.nightHours }}h
                        </v-chip>

                        <!-- ✅ 기본시간 초과 (정상 범위) -->
                        <v-chip
                            v-if="workTimeValidation.exceedsBasicHours && !workTimeValidation.hasOvertimeHours && !workTimeValidation.exceedsWeeklyLimit"
                            color="success" size="small" variant="flat" class="ml-1">
                            <v-icon start size="small">mdi-check-circle-outline</v-icon>
                            기본시간 완료
                        </v-chip>
                    </div>
                </div>

                <!-- 진행률 바 -->
                <div class="progress-container">
                    <!-- 🔥 FIX: 단일 progress bar로 전체 진행률 표시 -->
                    <div class="custom-progress-bar">
                        <!-- 기본 근무시간 부분 (100%까지) -->
                        <div class="progress-segment basic-segment" :style="{
                            width: progressPercentage > 100 ?
                                `${(100 / progressPercentage) * 100}%` :
                                `${progressPercentage}%`,
                            backgroundColor: progressColor === 'success' ? '#4CAF50' :
                                progressColor === 'warning' ? '#FF9800' :
                                    progressColor === 'error' ? '#F44336' : '#2196F3'
                        }">
                        </div>

                        <!-- 연장근무 부분 (100% 초과분) -->
                        <div v-if="progressPercentage > 100" class="progress-segment overtime-segment" :style="{
                            width: `${((progressPercentage - 100) / progressPercentage) * 100}%`,
                            backgroundColor: '#2196F3'
                        }">
                        </div>
                    </div>

                    <span class="progress-text" :class="progressTextClass">{{ formattedProgressPercentage }}</span>
                </div>

                <!-- 통계 정보 -->
                <div class="stats-info">
                    <div class="stat-item">
                        <span class="label">남은 근무시간:</span>
                        <span class="value">{{ remainingTimeRange }}</span>
                    </div>
                    <div class="stat-item">
                        <span class="label">할당된 {{ month }} 근무시간:</span>
                        <span class="value">{{ targetHours.hours }}h {{ targetHours.minutes }}m</span>
                    </div>
                    <!-- 🔥 NEW: 휴게시간 정보 추가 -->
                    <div class="stat-item" v-if="breakTimeHours > 0">
                        <span class="label">
                            <v-icon size="small" class="mr-1">mdi-coffee</v-icon>
                            월간 총 휴게시간:
                        </span>
                        <span class="value info-text">{{ breakTimeHours }}h {{ breakTimeMinutes }}m</span>
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
import { computed, toRef } from 'vue'
import { useWorkTimeValidation } from '@/composables/useWorkTimeValidation'
import { useWorkTimeCalculation } from '@/composables/useWorkTimeCalculation'

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
    },
    // 🔥 NEW: 주간 근무 기록 데이터 (workTimeValidator 사용을 위해)
    weeklyRecords: {
        type: Array,
        default: () => []
    },
    overtimeHours: {
        type: Number,
        default: 0
    },
    nightHours: {
        type: Number,
        default: 0
    },
    // 🔥 NEW: 휴게시간 관련 props
    totalBreakMinutes: {
        type: Number,
        default: 0
    }
})

// Composables
const { validateMonthlyWorkTime } = useWorkTimeValidation()
const {
    useProgressPercentage,
    useFormattedProgressPercentage,
    getProgressColor,
    getProgressTextClass,
    useRemainingTimeRange,
    useDailyAverageNeeded
} = useWorkTimeCalculation()

// Reactive refs for composables
const workingHoursRef = toRef(props, 'workingHours')
const targetHoursRef = toRef(props, 'targetHours')
const remainingWorkDaysRef = toRef(props, 'remainingWorkDays')

// 🔥 REFACTORED: 근무시간 검증 로직 (composable 사용)
const workTimeValidation = computed(() =>
    validateMonthlyWorkTime(props.workingHours, props.targetHours, props.nightHours)
)

// 🔥 REFACTORED: 진행률 계산 (composable 사용)
const progressPercentage = useProgressPercentage(workingHoursRef, targetHoursRef)
const formattedProgressPercentage = useFormattedProgressPercentage(progressPercentage)

// 🔥 REFACTORED: 진행률 색상 및 텍스트 클래스 (composable 사용)
const progressColor = computed(() =>
    getProgressColor(
        workTimeValidation.value.hasLegalIssues,
        workTimeValidation.value.exceedsWeeklyLimit,
        progressPercentage.value
    )
)

const progressTextClass = computed(() =>
    getProgressTextClass(
        workTimeValidation.value.hasLegalIssues,
        workTimeValidation.value.exceedsWeeklyLimit
    )
)

// 🔥 REFACTORED: 남은 시간 범위 및 일일 평균 (composable 사용)
const remainingTimeRange = useRemainingTimeRange(workingHoursRef, targetHoursRef)
const dailyAverageNeeded = useDailyAverageNeeded(workingHoursRef, targetHoursRef, remainingWorkDaysRef)

// 🔥 NEW: 휴게시간 계산
const breakTimeHours = computed(() => Math.floor(props.totalBreakMinutes / 60))
const breakTimeMinutes = computed(() => props.totalBreakMinutes % 60)

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
    flex-wrap: wrap;
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

/* 🔥 NEW: 상태 배지 스타일 */
.status-badges {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    margin-top: 8px;
    gap: 4px;
}

.progress-container {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-bottom: 20px;
}

.custom-progress-bar {
    flex: 1;
    height: 8px;
    background-color: #e0e0e0;
    border-radius: 4px;
    display: flex;
    overflow: hidden;
    position: relative;
}

.progress-segment {
    height: 100%;
    transition: width 0.3s ease;
}

.basic-segment {
    border-radius: 4px 0 0 4px;
}

.overtime-segment {
    border-radius: 0 4px 4px 0;
}

.progress-segment:only-child {
    border-radius: 4px;
}

.progress-text {
    font-size: 14px;
    font-weight: 600;
    color: #4CAF50;
    min-width: 50px;
}

/* 🔥 NEW: 진행률 텍스트 색상 클래스 */
.progress-text.warning-text {
    color: #FF9800 !important;
}

.progress-text.error-text {
    color: #F44336 !important;
}

.stats-info {
    display: flex;
    flex-direction: column;
    gap: 8px;
}

.stat-item {
    display: flex;
    gap: 8px;
    align-items: center;
}

.label {
    font-size: 14px;
    color: #666;
    min-width: 140px;
    display: flex;
    align-items: center;
}

.value {
    font-size: 14px;
    color: #333;
    font-weight: 500;
    flex: 1;
}

/* 🔥 NEW: 상태별 텍스트 색상 */
.warning-text {
    color: #FF9800 !important;
    font-weight: 600;
}

.error-text {
    color: #F44336 !important;
    font-weight: 600;
}

.info-text {
    color: #2196F3 !important;
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