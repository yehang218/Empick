<template>
    <div class="attendance-page">
        <!-- 페이지 제목 -->
        <h1 class="page-title">근태현황</h1>

        <!-- 날짜 선택 스위치 및 출퇴근 버튼 -->
        <div class="date-selector-container">
            <div class="date-selector">
                <v-btn icon size="small" variant="text" @click="previousMonth">
                    <v-icon>mdi-chevron-left</v-icon>
                </v-btn>

                <div class="date-display">
                    <span class="year-month">{{ currentYear }}.{{ currentMonth.toString().padStart(2, '0') }}</span>
                </div>

                <v-btn icon size="small" variant="text" @click="nextMonth">
                    <v-icon>mdi-chevron-right</v-icon>
                </v-btn>

                <v-btn variant="text" size="small" class="today-btn" @click="goToToday">
                    오늘
                </v-btn>
            </div>

            <div class="attendance-buttons">
                <v-btn variant="outlined" @click="checkIn">출근</v-btn>
                <v-btn variant="outlined" @click="checkOut">퇴근</v-btn>
            </div>
        </div>

        <!-- 메인 콘텐츠 -->
        <div class="page-content">
            <MonthlyWorkSummaryCard :month="`${currentMonth.toString().padStart(2, '0')}월`"
                :working-hours="workingHours" :target-hours="targetHours" :remaining-work-days="remainingWorkDays" />

            <!-- 주차별 근태 상세 -->
            <div class="week-section">
                <WeekAccordionList :year="currentYear" :month="currentMonth" :attendance-data="attendanceData"
                    @approval-request="handleApprovalRequest" @time-edit="handleTimeEdit" />
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import MonthlyWorkSummaryCard from '@/components/attendance/MonthlyWorkSummaryCard.vue'
import WeekAccordionList from '@/components/attendance/WeekAccordionList.vue'

// 현재 날짜 상태
const currentDate = ref(new Date())

// 계산된 속성들
const currentYear = computed(() => currentDate.value.getFullYear())
const currentMonth = computed(() => currentDate.value.getMonth() + 1)

// 근무 시간 데이터 (예시)
const workingHours = ref({
    hours: 51,
    minutes: 45
})

const targetHours = ref({
    hours: 51,
    minutes: 24
})

const remainingWorkDays = ref(14)

// 출석 데이터 (예시)
const attendanceData = ref([
    {
        date: '05',
        startTime: '07:45:00',
        endTime: '17:50:00',
        startLocation: true,
        endLocation: true,
        totalDuration: '9h 5m 0s',
        regularHours: '9h 5m 0s',
        overtimeHours: '0h 0m 0s',
        nightHours: '0h 0m 0s',
        needsApproval: false,
        selected: true,
        breakTime: true
    }
])

// 날짜 네비게이션 메서드
const previousMonth = () => {
    const newDate = new Date(currentDate.value)
    newDate.setMonth(newDate.getMonth() - 1)
    currentDate.value = newDate
}

const nextMonth = () => {
    const newDate = new Date(currentDate.value)
    newDate.setMonth(newDate.getMonth() + 1)
    currentDate.value = newDate
}

const goToToday = () => {
    currentDate.value = new Date()
}

// 출퇴근 메서드
const checkIn = () => {
    console.log('출근 처리')
}

const checkOut = () => {
    console.log('퇴근 처리')
}

// 주차별 이벤트 처리
const handleApprovalRequest = (dayData) => {
    console.log('승인 요청:', dayData)
}

const handleTimeEdit = (dayData) => {
    console.log('시간 수정:', dayData)
}
</script>

<style scoped>
.attendance-page {
    padding: 24px;
    min-height: 100vh;
}

.page-title {
    font-size: 28px;
    font-weight: 700;
    color: #333;
    margin: 0 0 16px 0;
}

.date-selector-container {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 24px;
    max-width: 1200px;
    margin-left: auto;
    margin-right: auto;
    position: relative;
}

.date-selector {
    display: flex;
    align-items: center;
    gap: 8px;
    background: white;
    border-radius: 8px;
    padding: 8px 16px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    position: absolute;
    left: 50%;
    transform: translateX(-50%);
}

.attendance-buttons {
    display: flex;
    gap: 8px;
}

.attendance-buttons .v-btn {
    background: white;
    border: 1px solid #ddd;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    color: #333;
    font-weight: 600;
}

.attendance-buttons .v-btn:hover {
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.date-display {
    display: flex;
    align-items: center;
    min-width: 100px;
    justify-content: center;
}

.year-month {
    font-size: 18px;
    font-weight: 600;
    color: #333;
    letter-spacing: 0.5px;
}

.today-btn {
    margin-left: 8px;
    font-size: 14px;
    color: #666;
}

.today-btn:hover {
    color: #1976d2;
}



.page-content {
    max-width: 1200px;
    margin: 0 auto;
}

.week-section {
    margin-top: 24px;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
    .attendance-page {
        padding: 16px;
    }



    .page-title {
        font-size: 24px;
    }
}
</style>