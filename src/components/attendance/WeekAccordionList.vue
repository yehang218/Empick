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
    attendanceData: {
        type: Array,
        default: () => []
    }
})

// 주차별 데이터
const weekList = ref([])

// 주차 계산 함수
const calculateWeeks = (year, month) => {
    const weeks = []
    const firstDay = new Date(year, month - 1, 1)
    const lastDay = new Date(year, month, 0)

    let currentWeekStart = new Date(firstDay)
    let weekNumber = 1

    // 첫 주의 시작을 월요일로 맞추기 (선택사항)
    const firstDayOfWeek = firstDay.getDay()
    if (firstDayOfWeek !== 1) { // 월요일이 아니면
        currentWeekStart.setDate(firstDay.getDate() - (firstDayOfWeek === 0 ? 6 : firstDayOfWeek - 1))
    }

    while (currentWeekStart <= lastDay) {
        const weekEnd = new Date(currentWeekStart)
        weekEnd.setDate(currentWeekStart.getDate() + 6)

        // 해당 주의 일자들 계산
        const weekDays = []
        for (let i = 0; i < 7; i++) {
            const currentDate = new Date(currentWeekStart)
            currentDate.setDate(currentWeekStart.getDate() + i)

            // 해당 월의 날짜만 포함
            if (currentDate.getMonth() === month - 1) {
                const dayData = findAttendanceData(currentDate)
                if (dayData) {
                    weekDays.push(dayData)
                }
            }
        }

        if (weekDays.length > 0) {
            weeks.push({
                weekNumber,
                startDate: new Date(currentWeekStart),
                endDate: new Date(weekEnd),
                days: weekDays,
                expanded: weekNumber === 2, // 기본적으로 2주차만 펼쳐짐
                totalHours: calculateWeekTotalHours(weekDays)
            })
            weekNumber++
        }

        currentWeekStart.setDate(currentWeekStart.getDate() + 7)
    }

    return weeks
}

// 출석 데이터 찾기
const findAttendanceData = (date) => {
    const dateStr = date.getDate().toString().padStart(2, '0')

    // 실제 데이터가 있으면 사용, 없으면 기본 데이터
    const existingData = props.attendanceData.find(data =>
        data.date === dateStr
    )

    if (existingData) {
        return existingData
    }

    // 기본 데이터 (예시)
    return {
        date: dateStr,
        startTime: '09:00:00',
        endTime: '18:00:00',
        startLocation: true,
        endLocation: true,
        totalDuration: '8h 0m 0s',
        regularHours: '8h 0m 0s',
        overtimeHours: '0h 0m 0s',
        nightHours: '0h 0m 0s',
        needsApproval: false,
        selected: false,
        breakTime: true
    }
}

// 주차별 총 근무시간 계산
const calculateWeekTotalHours = (days) => {
    let totalMinutes = 0

    days.forEach(day => {
        const duration = day.totalDuration
        const match = duration.match(/(\d+)h\s*(\d+)m/)
        if (match) {
            totalMinutes += parseInt(match[1]) * 60 + parseInt(match[2])
        }
    })

    const hours = Math.floor(totalMinutes / 60)
    const minutes = totalMinutes % 60

    return `${hours}h ${minutes}m`
}

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

// 주차 데이터 초기화
const initializeWeeks = () => {
    weekList.value = calculateWeeks(props.year, props.month)
}

// Props 변경 감지
watch([() => props.year, () => props.month], () => {
    initializeWeeks()
}, { immediate: true })

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