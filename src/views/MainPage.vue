<template>
    <div class="attendance-page">
        <!-- 🔥 NEW: 메인 컨테이너로 전체 콘텐츠 감싸기 -->
        <div class="main-container">
            <!-- 헤더 섹션 -->
            <div class="page-header">
                <!-- 페이지 제목 -->
                <h1 class="page-title">{{ userName }}님 로그인하셨습니다</h1>

                <!-- 날짜 선택 및 출퇴근 컨트롤 -->
                <div class="header-controls">
                    <div class="date-selector">
                        <v-btn icon size="small" variant="text" @click="previousMonth">
                            <v-icon>mdi-chevron-left</v-icon>
                        </v-btn>

                        <div class="date-display">
                            <span class="year-month">{{ currentYear }}.{{ currentMonth.toString().padStart(2, '0')
                                }}</span>
                        </div>

                        <v-btn icon size="small" variant="text" @click="nextMonth">
                            <v-icon>mdi-chevron-right</v-icon>
                        </v-btn>

                        <v-btn variant="text" size="small" class="today-btn" @click="goToToday">
                            오늘
                        </v-btn>
                    </div>

                    <div class="attendance-buttons">
                        <!-- 현재 달인 경우에만 출퇴근 버튼 표시 -->
                        <template v-if="isCurrentMonth">
                            <v-btn variant="outlined" :disabled="hasTodayCheckIn"
                                :color="hasTodayCheckIn ? 'grey' : 'primary'" @click="checkIn">
                                {{ hasTodayCheckIn ? '출근완료' : '출근' }}
                            </v-btn>
                            <v-btn variant="outlined" :disabled="!hasTodayCheckIn || hasTodayCheckOut"
                                :color="hasTodayCheckOut ? 'grey' : (!hasTodayCheckIn ? 'grey' : 'primary')"
                                @click="checkOut">
                                {{ hasTodayCheckOut ? '퇴근완료' : '퇴근' }}
                            </v-btn>
                        </template>
                    </div>
                </div>
            </div>

            <!-- 메인 콘텐츠 -->
            <div class="page-content">
                <MonthlyWorkSummaryCard :month="`${currentMonth.toString().padStart(2, '0')}월`"
                    :working-hours="workingHours" :target-hours="targetHours" :remaining-work-days="remainingWorkDays"
                    :weekly-records="weeklyRecords" :overtime-hours="overtimeHours" :night-hours="nightHours"
                    :total-break-minutes="totalBreakMinutes" />

                <!-- 주차별 근태 상세 -->
                <div class="week-section">
                    <WeekAccordionList :year="currentYear" :month="currentMonth"
                        :raw-attendance-records="rawAttendanceRecords" @approval-request="handleApprovalRequest"
                        @time-edit="handleTimeEdit" />
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { computed, onMounted, watch } from 'vue'
import MonthlyWorkSummaryCard from '@/components/attendance/MonthlyWorkSummaryCard.vue'
import WeekAccordionList from '@/components/attendance/WeekAccordionList.vue'
import { useAttendanceStore } from '@/stores/attendanceStore'
import { useAuthStore } from '@/stores/authStore'
import { useMemberStore } from '@/stores/memberStore'
import { useAttendanceData } from '@/composables/useAttendanceData'
import { useDateNavigation } from '@/composables/useDateNavigation'

// Stores
const attendanceStore = useAttendanceStore()
const authStore = useAuthStore()
const memberStore = useMemberStore()

// Composables
const {
    currentYear,
    currentMonth,
    isCurrentMonth,
    previousMonth,
    nextMonth,
    goToToday
} = useDateNavigation()

const {
    workingHours,
    targetHours,
    remainingWorkDays,
    weeklyRecords,
    overtimeHours,
    nightHours,
    totalBreakMinutes,
    rawAttendanceRecords,
    hasTodayCheckIn,
    hasTodayCheckOut,
    updateAllCalculations,
    processCheckIn,
    processCheckOut
} = useAttendanceData(attendanceStore, currentYear, currentMonth)

// 사용자 이름 가져오기
const userName = computed(() => {
    return memberStore.form.name || '사용자'
})

// 사용자 변경 감지 (로그인/로그아웃 시)
watch(() => authStore.userInfo, async (newUser, oldUser) => {
    // 로그아웃으로 인해 사용자가 null이 된 경우는 무시
    if (!newUser && oldUser) {
        console.log('사용자 로그아웃 감지, API 호출 생략')
        return
    }

    // 사용자가 변경되었을 때 (로그인 또는 다른 사용자로 변경)
    // 그리고 인증된 상태인 경우에만 API 호출
    if (newUser !== oldUser && newUser && authStore.isAuthenticated) {
        try {
            console.log('사용자 정보 변경 감지, 데이터 재로드 시작')
            await Promise.all([
                memberStore.getMyInfo(), // 사용자 정보 다시 로드
                updateAllCalculations()
            ])
            console.log('사용자 정보 변경에 따른 데이터 재로드 완료')
        } catch (error) {
            console.error('사용자 정보 변경 시 데이터 로드 실패:', error)
            // 오류가 발생해도 계속 진행 (사용자 경험 향상)
        }
    }
}, { deep: true })

// 컴포넌트 마운트 시 데이터 로드
onMounted(async () => {
    await Promise.all([
        memberStore.getMyInfo(), // 사용자 정보 로드
        updateAllCalculations()
    ])
})

// 🔥 REFACTORED: 출퇴근 처리 로직 (composable 사용)
const checkIn = async () => {
    try {
        await processCheckIn()
        console.log('출근 상태 확인:', hasTodayCheckIn.value)
    } catch (error) {
        alert(error.message || '출근 처리에 실패했습니다. 다시 시도해주세요.')
    }
}

const checkOut = async () => {
    try {
        await processCheckOut()
        console.log('퇴근 상태 확인:', hasTodayCheckOut.value)
    } catch (error) {
        alert(error.message || '퇴근 처리에 실패했습니다. 다시 시도해주세요.')
    }
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

/* 🔥 NEW: 메인 컨테이너 - 전체 콘텐츠를 일관되게 중앙 정렬 */
.main-container {
    max-width: 1200px;
    margin: 0 auto;
    background: white;
    border-radius: 8px;
    border: 1px solid #e0e0e0;
    overflow: hidden;
}

/* 🔥 NEW: 헤더 섹션 */
.page-header {
    background: #fafafa;
    border-bottom: 1px solid #e0e0e0;
    color: #333;
    padding: 32px 40px;
    position: relative;
}

.page-title {
    font-size: 28px;
    font-weight: 700;
    margin: 0 0 24px 0;
    color: #333;
}

/* 🔥 NEW: 헤더 컨트롤 영역 */
.header-controls {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 24px;
}

.date-selector {
    display: flex;
    align-items: center;
    gap: 8px;
    background: white;
    border: 1px solid #e0e0e0;
    border-radius: 8px;
    padding: 8px 16px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.date-selector:hover {
    border-color: #d0d0d0;
}

.date-selector .v-btn {
    color: #666 !important;
}

.date-display {
    display: flex;
    align-items: center;
    min-width: 120px;
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
    color: #666 !important;
}

.today-btn:hover {
    color: #333 !important;
    background: rgba(0, 0, 0, 0.05) !important;
}

.attendance-buttons {
    display: flex;
    gap: 12px;
}

.attendance-buttons .v-btn {
    background: white;
    border: 1px solid #ddd;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
    color: #333;
    font-weight: 600;
    border-radius: 8px;
    padding: 12px 24px;
}

.attendance-buttons .v-btn:hover {
    border-color: #ccc;
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

/* 🔥 NEW: 메인 콘텐츠 영역 */
.page-content {
    padding: 40px;
}

.week-section {
    margin-top: 32px;
}

/* 반응형 디자인 */
@media (max-width: 1024px) {
    .main-container {
        margin: 0 16px;
        border-radius: 8px;
    }

    .page-header {
        padding: 24px 32px;
    }

    .page-content {
        padding: 32px;
    }
}

@media (max-width: 768px) {
    .attendance-page {
        padding: 16px;
    }

    .main-container {
        margin: 0;
        border-radius: 0;
        box-shadow: none;
    }

    .page-header {
        padding: 20px 24px;
    }

    .page-title {
        font-size: 24px;
        margin-bottom: 16px;
    }

    .header-controls {
        flex-direction: column;
        gap: 16px;
        align-items: stretch;
    }

    .date-selector {
        justify-content: center;
    }

    .attendance-buttons {
        justify-content: center;
        gap: 8px;
    }

    .attendance-buttons .v-btn {
        flex: 1;
        padding: 10px 16px;
    }

    .page-content {
        padding: 24px;
    }

    .week-section {
        margin-top: 24px;
    }
}

@media (max-width: 480px) {
    .page-header {
        padding: 16px 20px;
    }

    .page-title {
        font-size: 20px;
    }

    .page-content {
        padding: 20px;
    }
}
</style>