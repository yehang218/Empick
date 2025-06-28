import { ref, computed, watch } from 'vue'
import { groupAttendanceByDate, calculateNightWorkHours, calculateTimeDifferenceInMinutes, calculateBreakTime } from '@/utils/attendance/attendanceCalculator'

/**
 * 출석 데이터 관리 및 계산 관련 로직을 담은 composable
 */
export const useAttendanceData = (attendanceStore, currentYear, currentMonth) => {

    // 기본 상태들
    const refreshKey = ref(0)
    const workingHours = ref({ hours: 0, minutes: 0 })
    const targetHours = ref({ hours: 0, minutes: 0 })
    const remainingWorkDays = ref(0)

    // 추가 계산 데이터
    const weeklyRecords = ref([])
    const overtimeHours = ref(0)
    const nightHours = ref(0)
    const totalBreakMinutes = ref(0)

    // Store에서 raw 데이터 가져오기
    const rawAttendanceRecords = computed(() => attendanceStore.myRecords)

    /**
     * 연장근무 및 야간근무 시간 계산
     */
    const calculateOvertimeAndNightHours = () => {
        if (!rawAttendanceRecords.value.length) {
            overtimeHours.value = 0
            nightHours.value = 0
            totalBreakMinutes.value = 0
            weeklyRecords.value = []
            return
        }

        // 날짜별로 그룹핑
        const dailyData = groupAttendanceByDate(rawAttendanceRecords.value)
        let totalMonthlyMinutes = 0
        let totalNightMinutes = 0
        let totalBreakTime = 0
        const records = []

        Object.values(dailyData).forEach(day => {
            if (day.checkIn && day.checkOut) {
                const startTime = day.checkIn.time
                const endTime = day.checkOut.time

                // 총 근무시간 계산
                const totalMinutes = calculateTimeDifferenceInMinutes(startTime, endTime)
                totalMonthlyMinutes += totalMinutes

                // 야간근무 시간 계산
                const nightMinutes = calculateNightWorkHours(startTime, endTime)
                totalNightMinutes += nightMinutes

                // 휴게시간 계산
                const breakMinutes = calculateBreakTime(totalMinutes)
                totalBreakTime += breakMinutes

                // weeklyRecords 형식으로 변환
                records.push({
                    date: day.date,
                    startTime: startTime,
                    endTime: endTime,
                    workMinutes: totalMinutes
                })
            }
        })

        // 2024년 새 기준 - 월별 연장근무 계산
        const daysInMonth = new Date(currentYear.value, currentMonth.value, 0).getDate()
        const weeksInMonth = Math.ceil(daysInMonth / 7)
        const monthlyBasicMinutes = 40 * 60 * weeksInMonth  // 40시간 × 주수
        const totalOvertimeMinutes = Math.max(0, totalMonthlyMinutes - monthlyBasicMinutes)

        // 시간 단위로 변환 (소수점 첫째자리 반올림)
        overtimeHours.value = Math.round((totalOvertimeMinutes / 60) * 10) / 10
        nightHours.value = Math.round((totalNightMinutes / 60) * 10) / 10
        totalBreakMinutes.value = totalBreakTime
        weeklyRecords.value = records
    }

    /**
     * 근무 시간 계산
     */
    const updateWorkingHours = async () => {
        if (!rawAttendanceRecords.value.length) {
            workingHours.value = { hours: 0, minutes: 0 }
            return
        }

        const stats = attendanceStore.calculateMonthlyStats(rawAttendanceRecords.value)
        workingHours.value = await attendanceStore.parseTimeString(stats.totalHours)
    }

    /**
     * 목표 시간 계산
     */
    const updateTargetHours = async () => {
        try {
            const result = await attendanceStore.calculateTargetHours(currentYear.value, currentMonth.value)
            targetHours.value = result
        } catch (error) {
            console.error('목표 시간 계산 에러:', error)
            targetHours.value = { hours: 0, minutes: 0 }
        }
    }

    /**
     * 남은 근무일 계산
     */
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

    /**
     * 근태 데이터 로드
     */
    const loadAttendanceData = async () => {
        try {
            await attendanceStore.loadMonthlyAttendanceData(currentYear.value, currentMonth.value)
        } catch (error) {
            console.error('근태 데이터 로드 실패:', error)
        }
    }

    /**
     * 모든 계산 데이터 업데이트
     */
    const updateAllCalculations = async () => {
        await Promise.all([
            updateWorkingHours(),
            updateTargetHours(),
            updateRemainingWorkDays()
        ])
        calculateOvertimeAndNightHours()
    }

    /**
     * 오늘 출근/퇴근 상태 확인
     */
    const getTodayAttendanceStatus = () => {
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

        return {
            todayData,
            hasCheckIn: todayData && todayData.checkIn !== null,
            hasCheckOut: todayData && todayData.checkOut !== null,
            todayKey: todayLocal
        }
    }

    /**
     * 출퇴근 처리
     */
    const processCheckIn = async () => {
        const { hasCheckIn } = getTodayAttendanceStatus()

        if (hasCheckIn) {
            throw new Error('오늘은 이미 출근하셨습니다.')
        }

        try {
            await attendanceStore.createCheckInRecord()
            await loadAttendanceData()
            await updateWorkingHours()
            refreshKey.value++
            console.log('출근 처리 완료')
        } catch (error) {
            console.error('출근 처리 실패:', error)
            throw error
        }
    }

    const processCheckOut = async () => {
        const { hasCheckIn, hasCheckOut } = getTodayAttendanceStatus()

        if (!hasCheckIn) {
            throw new Error('출근 기록이 없습니다. 먼저 출근해주세요.')
        }

        if (hasCheckOut) {
            throw new Error('오늘은 이미 퇴근하셨습니다.')
        }

        try {
            await attendanceStore.createCheckOutRecord()
            await loadAttendanceData()
            await updateWorkingHours()
            refreshKey.value++
            console.log('퇴근 처리 완료')
        } catch (error) {
            console.error('퇴근 처리 실패:', error)
            throw error
        }
    }

    // 출퇴근 상태를 computed로 제공
    const hasTodayCheckIn = computed(() => {
        refreshKey.value // 강제 업데이트
        const { hasCheckIn } = getTodayAttendanceStatus()
        return hasCheckIn
    })

    const hasTodayCheckOut = computed(() => {
        refreshKey.value // 강제 업데이트  
        const { hasCheckOut } = getTodayAttendanceStatus()
        return hasCheckOut
    })

    // rawAttendanceRecords 변경 감지
    watch(rawAttendanceRecords, () => {
        updateWorkingHours()
        calculateOvertimeAndNightHours()
    }, { immediate: true })

    // 년/월 변경 감지
    watch([currentYear, currentMonth], () => {
        updateTargetHours()
        updateRemainingWorkDays()
        loadAttendanceData()
    }, { immediate: true })

    return {
        // 상태
        workingHours,
        targetHours,
        remainingWorkDays,
        weeklyRecords,
        overtimeHours,
        nightHours,
        totalBreakMinutes,
        rawAttendanceRecords,
        refreshKey,

        // 출퇴근 상태
        hasTodayCheckIn,
        hasTodayCheckOut,

        // 메서드
        loadAttendanceData,
        updateAllCalculations,
        processCheckIn,
        processCheckOut,
        calculateOvertimeAndNightHours,
        updateWorkingHours,
        updateTargetHours,
        updateRemainingWorkDays,
        getTodayAttendanceStatus
    }
} 