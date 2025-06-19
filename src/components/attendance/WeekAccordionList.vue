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
import { ref, watch } from 'vue'
import WeekSummaryCard from './WeekSummaryCard.vue'
import { useAttendanceStore } from '@/stores/attendanceStore'

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

// 아코디언 상태를 관리하는 반응형 데이터
const weekList = ref([])

// 현재 주차 계산
const getCurrentWeekNumber = () => {
    const today = new Date()

    // 현재 월이 아니면 -1 반환 (아무 주차도 열지 않음)
    if (today.getFullYear() !== props.year || today.getMonth() + 1 !== props.month) {
        return -1
    }

    const currentDate = today.getDate()
    const firstDay = new Date(props.year, props.month - 1, 1)
    const firstDayOfWeek = firstDay.getDay()

    // 첫 주의 시작을 월요일로 맞추기
    let weekStart = 1
    if (firstDayOfWeek !== 1) {
        weekStart = 1 - (firstDayOfWeek === 0 ? 6 : firstDayOfWeek - 1)
    }

    // 현재 날짜가 속한 주차 계산
    let weekNumber = 1
    while (weekStart <= currentDate) {
        const weekEnd = weekStart + 6
        if (currentDate >= weekStart && currentDate <= weekEnd) {
            return weekNumber
        }
        weekStart += 7
        weekNumber++
    }

    return 1 // 기본값
}

// Store에서 주차별 데이터 가져와서 상태 업데이트
const updateWeekList = () => {
    if (!props.rawAttendanceRecords.length) {
        weekList.value = []
        return
    }

    const newWeekData = attendanceStore.groupAttendanceByWeek(
        props.year,
        props.month,
        props.rawAttendanceRecords
    )

    const currentWeek = getCurrentWeekNumber()

    // 기존 expanded 상태 보존하면서 업데이트
    weekList.value = newWeekData.map((newWeek, index) => {
        const existingWeek = weekList.value[index]

        // 기존 상태가 있으면 보존, 없으면 현재 주차인지 확인
        const shouldExpand = existingWeek
            ? existingWeek.expanded
            : newWeek.weekNumber === currentWeek

        return {
            ...newWeek,
            expanded: shouldExpand
        }
    })
}

// Props 변경 감지
watch([() => props.year, () => props.month, () => props.rawAttendanceRecords], () => {
    updateWeekList()
}, { immediate: true, deep: true })

// 주차 범위 포맷팅
const formatWeekRange = (startDate, endDate) => {
    const start = startDate.getDate()
    const end = endDate.getDate()
    return `${start}일 ~ ${end}일`
}

// 주차 토글
const toggleWeek = (index) => {
    weekList.value[index].expanded = !weekList.value[index].expanded
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