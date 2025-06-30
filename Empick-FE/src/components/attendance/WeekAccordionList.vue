<template>
    <div class="week-accordion-list">
        <div v-for="(week, index) in weekList" :key="index" class="week-accordion-item">
            <!-- 주차 헤더 -->
            <div class="week-header" @click="toggleWeek(index)" :class="{ 'expanded': week.expanded }">
                <div class="week-info">
                    <v-icon class="expand-icon">
                        {{ week.expanded ? 'mdi-chevron-up' : 'mdi-chevron-down' }}
                    </v-icon>
                    <span class="week-title">{{ week.weekNumber }}주차</span>

                    <!-- 🔥 NEW: 주차별 법정 한도 초과 배지 -->
                    <div class="week-status-badges">
                        <v-chip v-if="getWeeklyValidation(week).exceedsSpecialLimit" color="error" size="small"
                            variant="flat" class="ml-2">
                            <v-icon start size="small">mdi-alert-circle</v-icon>
                            특별한도 초과 (+{{ getWeeklyValidation(week).specialExcessHours }}h)
                        </v-chip>
                        <v-chip v-else-if="getWeeklyValidation(week).exceedsNormalLimit" color="warning" size="small"
                            variant="flat" class="ml-2">
                            <v-icon start size="small">mdi-alert</v-icon>
                            주간한도 초과 (+{{ getWeeklyValidation(week).normalExcessHours }}h)
                        </v-chip>
                        <v-chip v-else-if="getWeeklyValidation(week).hasOvertimeWork" color="info" size="small"
                            variant="flat" class="ml-2">
                            <v-icon start size="small">mdi-clock-plus-outline</v-icon>
                            연장근무 {{ getWeeklyValidation(week).overtimeHours }}h
                        </v-chip>
                        <v-chip v-else-if="getWeeklyValidation(week).meetsBasicHours" color="success" size="small"
                            variant="flat" class="ml-2">
                            <v-icon start size="small">mdi-check-circle</v-icon>
                            기본시간 완료
                        </v-chip>

                        <!-- 🔥 NEW: 휴게시간 정보 배지 -->
                        <v-chip v-if="getWeeklyValidation(week).totalHours > getWeeklyValidation(week).actualHours"
                            color="purple" size="small" variant="outlined" class="ml-2">
                            <v-icon start size="small">mdi-coffee</v-icon>
                            휴게 {{ formatBreakTime(getWeeklyValidation(week).totalHours -
                                getWeeklyValidation(week).actualHours) }}
                        </v-chip>
                    </div>
                </div>

                <div class="week-summary">
                    <span class="week-dates">
                        {{ formatWeekRange(week.startDate, week.endDate) }}
                    </span>
                    <span class="week-total-hours">
                        총 {{ week.totalHours }}
                    </span>
                </div>
            </div>

            <!-- 주차 내용 (WeekSummaryCard) -->
            <div v-if="week.expanded" class="week-content">
                <WeekSummaryCard :week-data="week.days" @request-approval="handleApprovalRequest"
                    @edit-time="handleTimeEdit" />
            </div>
        </div>
    </div>
</template>

<script setup>
import WeekSummaryCard from './WeekSummaryCard.vue'
import { useAttendanceStore } from '@/stores/attendanceStore'
import { useWorkTimeValidation } from '@/composables/useWorkTimeValidation'
import { useWeekManagement } from '@/composables/useWeekManagement'

// Props
const props = defineProps({
    year: {
        type: Number,
        default: () => new Date().getFullYear()
    },
    month: {
        type: Number,
        default: () => new Date().getMonth() + 1
    },
    rawAttendanceRecords: {
        type: Array,
        default: () => []
    }
})

const attendanceStore = useAttendanceStore()

// Composables
const { validateWeeklyWorkTime } = useWorkTimeValidation()
const { formatWeekRange, useWeekListState } = useWeekManagement()

// 🔥 REFACTORED: 주차 관리 로직 (composable 사용)
const { weekList, toggleWeek } = useWeekListState(props, attendanceStore)

// 🔥 REFACTORED: 주차별 근무시간 검증 함수 (composable 사용)
const getWeeklyValidation = (week) => validateWeeklyWorkTime(week)

// 휴게시간 포맷팅 함수
const formatBreakTime = (hours) => {
    if (hours <= 0) return '0h'

    const wholeHours = Math.floor(hours)
    const minutes = Math.round((hours - wholeHours) * 60)

    if (minutes === 0) {
        return `${wholeHours}h`
    } else if (wholeHours === 0) {
        return `${minutes}m`
    } else {
        return `${wholeHours}h ${minutes}m`
    }
}

// 승인 요청 처리
const handleApprovalRequest = (dayData) => {
    emit('approval-request', dayData)
}

// 시간 수정 처리
const handleTimeEdit = (dayData) => {
    emit('time-edit', dayData)
}

// Props 변경 감지는 watch에서 처리됨

// Emits
const emit = defineEmits(['approval-request', 'time-edit'])
</script>

<style lang="scss" scoped>
.week-accordion-list {
    display: flex;
    flex-direction: column;
    gap: 8px;
}

.week-accordion-item {
    background: white;
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.08);
}

.week-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 16px 20px;
    background: white;
    cursor: pointer;
    transition: all 0.2s ease;

    &:hover {
        background: #f9f9f9;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    }

    &.expanded {
        background: white;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.08);
    }

    .week-info {
        display: flex;
        align-items: center;
        gap: 12px;

        .expand-icon {
            color: #666;
            transition: transform 0.2s ease;
        }

        .week-title {
            font-size: 16px;
            font-weight: 600;
            color: #333;
        }

        // 🔥 NEW: 주차별 배지 스타일
        .week-status-badges {
            display: flex;
            align-items: center;
            gap: 8px;
            flex-wrap: wrap;
        }
    }

    .week-summary {
        display: flex;
        align-items: center;
        gap: 16px;

        .week-dates {
            font-size: 14px;
            color: #666;
        }

        .week-total-hours {
            font-size: 14px;
            font-weight: 600;
            color: #1976d2;
            background: #f5f5f5;
            padding: 4px 8px;
            border-radius: 4px;
        }
    }
}

.week-content {
    padding: 0;
    animation: slideDown 0.3s ease-out;
}

@keyframes slideDown {
    from {
        opacity: 0;
        max-height: 0;
    }

    to {
        opacity: 1;
        max-height: 500px;
    }
}

// 반응형 디자인
@media (max-width: 768px) {
    .week-header {
        padding: 12px 16px;
        flex-direction: column;
        align-items: flex-start;
        gap: 8px;

        .week-summary {
            align-self: stretch;
            justify-content: space-between;
        }
    }

    .week-accordion-list {
        gap: 4px;
    }
}
</style>