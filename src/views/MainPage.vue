<template>
    <div class="attendance-page">
        <!-- 페이지 제목 -->
        <h1 class="page-title">{{ userName }}님 로그인하셨습니다</h1>

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
                <!-- 현재 달인 경우에만 출퇴근 버튼 표시 -->
                <template v-if="isCurrentMonth">
                    <v-btn variant="outlined" :disabled="hasTodayCheckIn" :color="hasTodayCheckIn ? 'grey' : 'primary'"
                        @click="checkIn">
                        {{ hasTodayCheckIn ? '출근완료' : '출근' }}
                    </v-btn>
                    <v-btn variant="outlined" :disabled="!hasTodayCheckIn || hasTodayCheckOut"
                        :color="hasTodayCheckOut ? 'grey' : (!hasTodayCheckIn ? 'grey' : 'primary')" @click="checkOut">
                        {{ hasTodayCheckOut ? '퇴근완료' : '퇴근' }}
                    </v-btn>
                </template>
                <!-- 개발용 캐시 초기화 버튼 -->
                <v-btn variant="text" size="small" color="orange" @click="clearCache" class="ml-2">
                    캐시 초기화
                </v-btn>
            </div>
        </div>

        <!-- 메인 콘텐츠 -->
        <div class="page-content">
            <MonthlyWorkSummaryCard :month="`${currentMonth.toString().padStart(2, '0')}월`"
                :working-hours="workingHours" :target-hours="targetHours" :remaining-work-days="remainingWorkDays" />

            <!-- 주차별 근태 상세 -->
            <div class="week-section">
                <WeekAccordionList :year="currentYear" :month="currentMonth"
                    :raw-attendance-records="rawAttendanceRecords" @approval-request="handleApprovalRequest"
                    @time-edit="handleTimeEdit" />
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import MonthlyWorkSummaryCard from '@/components/attendance/MonthlyWorkSummaryCard.vue'
import WeekAccordionList from '@/components/attendance/WeekAccordionList.vue'
import { useAttendanceStore } from '@/stores/attendanceStore'
import { useAuthStore } from '@/stores/authStore'
import { useMemberStore } from '@/stores/memberStore'
import { groupAttendanceByDate } from '@/utils/attendance/attendanceCalculator'

// 현재 날짜 상태
const currentDate = ref(new Date())
// 반응성 강제 업데이트를 위한 키
const refreshKey = ref(0)
const attendanceStore = useAttendanceStore()
const authStore = useAuthStore()
const memberStore = useMemberStore()

// 사용자 이름 가져오기
const userName = computed(() => {
    return memberStore.form.name || '사용자'
})

// 계산된 속성들
const currentYear = computed(() => currentDate.value.getFullYear())
const currentMonth = computed(() => currentDate.value.getMonth() + 1)

// 현재 달인지 확인하는 computed
const isCurrentMonth = computed(() => {
    const today = new Date()
    return currentYear.value === today.getFullYear() && currentMonth.value === (today.getMonth() + 1)
})

// Store에서 로딩 상태와 데이터 가져오기
const rawAttendanceRecords = computed(() => attendanceStore.myRecords)

// 계산된 근무 시간 데이터
const workingHours = ref({ hours: 0, minutes: 0 })

// 근무 시간 계산 함수
const updateWorkingHours = async () => {
    if (!rawAttendanceRecords.value.length) {
        workingHours.value = { hours: 0, minutes: 0 }
        return
    }

    const stats = attendanceStore.calculateMonthlyStats(rawAttendanceRecords.value)
    workingHours.value = await attendanceStore.parseTimeString(stats.totalHours)
}

// rawAttendanceRecords 변경 감지
watch(rawAttendanceRecords, () => {
    updateWorkingHours()
}, { immediate: true })

// 목표 근무 시간
const targetHours = ref({ hours: 0, minutes: 0 })

// 목표 시간 계산 함수
const updateTargetHours = async () => {
    try {
        const result = await attendanceStore.calculateTargetHours(currentYear.value, currentMonth.value)
        targetHours.value = result
    } catch (error) {
        console.error('MainPage - 목표 시간 계산 에러:', error)
        targetHours.value = { hours: 0, minutes: 0 }
    }
}

// 날짜 변경 시 목표 시간 업데이트
watch([currentYear, currentMonth], () => {
    updateTargetHours()
}, { immediate: true })

// 남은 근무일 계산
const remainingWorkDays = ref(0)

// 남은 근무일 계산 함수
const updateRemainingWorkDays = async () => {
    const today = new Date()

    // 현재 월이 아니면 0 반환
    if (today.getFullYear() !== currentYear.value || today.getMonth() + 1 !== currentMonth.value) {
        remainingWorkDays.value = 0
        return
    }

    try {
        remainingWorkDays.value = await attendanceStore.getWorkDaysRemaining(currentYear.value, currentMonth.value)
    } catch (error) {
        console.error('남은 근무일 계산 실패:', error)
        remainingWorkDays.value = 0
    }
}

// 날짜 변경 시 남은 근무일 업데이트
watch([currentYear, currentMonth], () => {
    updateRemainingWorkDays()
}, { immediate: true })

// 이제 WeekAccordionList가 직접 Store에서 데이터를 변환하므로 제거

// 근태 데이터 로드 (Store 함수 사용)
const loadAttendanceData = async () => {
    try {
        await attendanceStore.loadMonthlyAttendanceData(currentYear.value, currentMonth.value)
    } catch (error) {
        console.error('근태 데이터 로드 실패:', error)
    }
}

// 날짜 변경 시 데이터 재로드
watch([currentYear, currentMonth], () => {
    loadAttendanceData()
}, { immediate: false })

// 사용자 변경 감지 (로그인/로그아웃 시)
watch(() => authStore.userInfo, async (newUser, oldUser) => {
    // 사용자가 변경되었을 때 (로그인 또는 다른 사용자로 변경)
    if (newUser !== oldUser) {
        await Promise.all([
            memberStore.getMyInfo(), // 사용자 정보 다시 로드
            loadAttendanceData(),
            updateTargetHours(),
            updateRemainingWorkDays(),
            updateWorkingHours()
        ])
    }
}, { deep: true })

// 컴포넌트 마운트 시 데이터 로드
onMounted(async () => {
    await Promise.all([
        memberStore.getMyInfo(), // 사용자 정보 로드
        loadAttendanceData(),
        updateTargetHours(),
        updateRemainingWorkDays(),
        updateWorkingHours()
    ])
})

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

// 오늘 출근 기록 확인 (근태 카테고리 ID 1=출근 기준)
const hasTodayCheckIn = computed(() => {
    // refreshKey를 의존성에 포함하여 강제 업데이트
    refreshKey.value

    // 여러 방법으로 오늘 날짜 확인
    const now = new Date()
    const today = new Date().toISOString().split('T')[0] // YYYY-MM-DD
    const todayKST = new Date(now.getTime() + (9 * 60 * 60 * 1000)).toISOString().split('T')[0] // KST
    const todayLocal = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}-${String(now.getDate()).padStart(2, '0')}`

    // 실제 데이터 구조 확인을 위한 로그
    console.log('=== 날짜 및 데이터 구조 디버깅 ===')
    console.log('현재 시스템 시간:', now)
    console.log('today (UTC):', today)
    console.log('todayKST:', todayKST)
    console.log('todayLocal:', todayLocal)
    console.log('rawAttendanceRecords 전체 샘플 (처음 5개):', rawAttendanceRecords.value.slice(0, 5))

    // 모든 레코드의 날짜 추출해서 확인
    const allDates = rawAttendanceRecords.value.map(record => {
        const recordTime = record.recordTime?.split('T')[0]
        const createdAt = record.createdAt?.split('T')[0]
        return {
            id: record.id,
            recordTime: record.recordTime,
            recordTimeDate: recordTime,
            createdAt: record.createdAt,
            createdAtDate: createdAt,
            categoryId: record.attendanceCategoryId
        }
    })
    console.log('모든 레코드의 날짜 정보:', allDates)

    const dailyData = groupAttendanceByDate(rawAttendanceRecords.value)

    console.log('groupAttendanceByDate 결과 키들:', Object.keys(dailyData))
    console.log('groupAttendanceByDate 전체 결과:', dailyData)

    // 여러 날짜로 시도해보기
    const todayDataUTC = dailyData[today]
    const todayDataKST = dailyData[todayKST]
    const todayDataLocal = dailyData[todayLocal]

    console.log('UTC 날짜로 찾은 데이터:', todayDataUTC)
    console.log('KST 날짜로 찾은 데이터:', todayDataKST)
    console.log('Local 날짜로 찾은 데이터:', todayDataLocal)

    // 가장 적절한 todayData 선택
    const todayData = todayDataLocal || todayDataKST || todayDataUTC

    console.log('최종 선택된 todayData:', todayData)
    console.log('출근 상태 체크:', {
        today: todayLocal,
        recordsCount: rawAttendanceRecords.value.length,
        todayData,
        hasCheckIn: todayData && todayData.checkIn !== null,
        refreshKey: refreshKey.value
    })

    return todayData && todayData.checkIn !== null
})

// 오늘 퇴근 기록 확인 (근태 카테고리 ID 2=퇴근 기준)
const hasTodayCheckOut = computed(() => {
    // refreshKey를 의존성에 포함하여 강제 업데이트
    refreshKey.value

    // 여러 방법으로 오늘 날짜 확인
    const now = new Date()
    const today = new Date().toISOString().split('T')[0] // YYYY-MM-DD
    const todayKST = new Date(now.getTime() + (9 * 60 * 60 * 1000)).toISOString().split('T')[0] // KST
    const todayLocal = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}-${String(now.getDate()).padStart(2, '0')}`

    const dailyData = groupAttendanceByDate(rawAttendanceRecords.value)

    // 여러 날짜로 시도해보기
    const todayDataUTC = dailyData[today]
    const todayDataKST = dailyData[todayKST]
    const todayDataLocal = dailyData[todayLocal]

    // 가장 적절한 todayData 선택
    const todayData = todayDataLocal || todayDataKST || todayDataUTC

    console.log('퇴근 상태 체크:', {
        today: todayLocal,
        recordsCount: rawAttendanceRecords.value.length,
        todayData,
        hasCheckOut: todayData && todayData.checkOut !== null,
        refreshKey: refreshKey.value
    })
    return todayData && todayData.checkOut !== null
})

// 출퇴근 메서드 (근태 카테고리 ID 1=출근, 2=퇴근으로 기록)
const checkIn = async () => {
    // 이미 오늘 출근한 경우 중복 출근 방지
    if (hasTodayCheckIn.value) {
        alert('오늘은 이미 출근하셨습니다.')
        return
    }

    try {
        await attendanceStore.createCheckInRecord()  // 근태 카테고리 ID 1로 출근 기록

        // 현재 보고 있는 월의 데이터를 다시 로드
        await loadAttendanceData()

        // 추가로 workingHours도 즉시 업데이트
        await updateWorkingHours()

        // 강제로 computed 업데이트
        refreshKey.value++

        console.log('출근 처리 완료')
        console.log('출근 상태 확인:', hasTodayCheckIn.value)
    } catch (error) {
        console.error('출근 처리 실패:', error)
        alert('출근 처리에 실패했습니다. 다시 시도해주세요.')
    }
}

const checkOut = async () => {
    // 아직 출근하지 않은 경우 퇴근 불가
    if (!hasTodayCheckIn.value) {
        alert('출근 기록이 없습니다. 먼저 출근해주세요.')
        return
    }

    // 이미 오늘 퇴근한 경우 중복 퇴근 방지
    if (hasTodayCheckOut.value) {
        alert('오늘은 이미 퇴근하셨습니다.')
        return
    }

    try {
        await attendanceStore.createCheckOutRecord() // 근태 카테고리 ID 2로 퇴근 기록

        // 현재 보고 있는 월의 데이터를 다시 로드
        await loadAttendanceData()

        // 추가로 workingHours도 즉시 업데이트
        await updateWorkingHours()

        // 강제로 computed 업데이트
        refreshKey.value++

        console.log('퇴근 처리 완료')
        console.log('퇴근 상태 확인:', hasTodayCheckOut.value)
    } catch (error) {
        console.error('퇴근 처리 실패:', error)
        alert('퇴근 처리에 실패했습니다. 다시 시도해주세요.')
    }
}

// 주차별 이벤트 처리
const handleApprovalRequest = (dayData) => {
    console.log('승인 요청:', dayData)
}

const handleTimeEdit = (dayData) => {
    console.log('시간 수정:', dayData)
}

// 캐시 초기화 함수
const clearCache = () => {
    attendanceStore.resetAllData()
    alert('캐시가 초기화되었습니다.')
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