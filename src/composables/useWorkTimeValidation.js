import { calculateTimeDifferenceInMinutes, calculateActualWorkMinutes, calculateBreakTime } from '@/utils/attendance/attendanceCalculator'

/**
 * 근무시간 검증 관련 로직을 담은 composable
 */
export const useWorkTimeValidation = () => {

    /**
     * 월간 근무시간 검증
     * @param {Object} workingHours - 현재 근무시간 {hours, minutes}
     * @param {Object} targetHours - 목표 근무시간 {hours, minutes}
     * @param {number} nightHours - 야간근무 시간
     * @returns {Object} 검증 결과
     */
    const validateMonthlyWorkTime = (workingHours, targetHours, nightHours = 0) => {
        // 현재 총 근무시간
        const currentHours = workingHours?.hours || 0
        const currentMins = workingHours?.minutes || 0
        const totalCurrentMinutes = currentHours * 60 + currentMins
        const totalCurrentHours = Math.floor(totalCurrentMinutes / 60)

        // 할당된 근무시간 (목표 시간)
        const targetH = targetHours?.hours || 0
        const targetM = targetHours?.minutes || 0
        const totalTargetMinutes = targetH * 60 + targetM
        const totalTargetHours = Math.floor(totalTargetMinutes / 60)

        // 월간 연장근무 계산 = 총 근무시간 - 할당 근무시간
        const monthlyOvertimeMinutes = Math.max(0, totalCurrentMinutes - totalTargetMinutes)
        const monthlyOvertimeHours = Math.floor(monthlyOvertimeMinutes / 60)
        const hasMonthlyOvertime = monthlyOvertimeHours > 0

        // 야간근무 정보
        const hasNightHours = nightHours > 0

        // 월별 법정 한도 계산 (6월 기준)
        const weeklyMaxHours = 52  // 주당 최대 52시간
        const weeklyBasicHours = 40  // 주당 기본 40시간
        const maxOvertimePerWeek = 12  // 주당 최대 연장 12시간

        // 6월의 주 수 계산 (대략적으로 4.3주 = 30일/7일)
        const weeksInMonth = Math.ceil(30 / 7)  // 약 4.3주
        const monthlyMaxHours = weeklyMaxHours * weeksInMonth  // 52 × 4.3 ≈ 224시간
        const monthlyBasicHours = weeklyBasicHours * weeksInMonth  // 40 × 4.3 ≈ 172시간

        // 월별 한도 초과 여부
        const exceedsMonthlyLimit = totalCurrentHours > monthlyMaxHours
        const exceedsMonthlyBasic = totalCurrentHours > monthlyBasicHours

        // 연장근무 한도 초과 여부 (주당 기준)
        const avgWeeklyHours = totalCurrentHours / weeksInMonth
        const avgWeeklyOvertime = Math.max(0, avgWeeklyHours - weeklyBasicHours)
        const exceedsOvertimeLimit = avgWeeklyOvertime > maxOvertimePerWeek

        // 전체 이슈 여부
        const hasIssues = hasMonthlyOvertime || hasNightHours || exceedsMonthlyLimit

        return {
            hasIssues,
            hasOvertimeHours: hasMonthlyOvertime,
            overtimeHours: monthlyOvertimeHours,
            hasNightHours,
            nightHours,
            nightRate: 150, // 150% 수당
            exceedsWeeklyLimit: exceedsMonthlyLimit,
            exceedsBasicHours: exceedsMonthlyBasic,
            exceedsOvertimeLimit,
            currentWeeklyHours: totalCurrentHours,
            maxWeeklyHours: monthlyMaxHours,
            basicWeeklyHours: monthlyBasicHours,
            maxOvertimeHours: maxOvertimePerWeek,
            weeklyExcessHours: Math.max(0, totalCurrentHours - monthlyMaxHours),
            hasLegalIssues: exceedsMonthlyLimit,
            legalIssueMessage: exceedsMonthlyLimit ? `월 ${Math.ceil(monthlyMaxHours)}시간 한도를 초과했습니다. 즉시 조치가 필요합니다.` : '',
            hasRecommendations: exceedsMonthlyBasic && !exceedsMonthlyLimit,
            recommendationMessage: exceedsMonthlyBasic && !exceedsMonthlyLimit ? `월 기본시간(${Math.ceil(monthlyBasicHours)}h)을 초과했습니다. 적절한 휴식을 권장합니다.` : '',
            // 디버그 정보
            weeksInMonth,
            monthlyMaxHours: Math.ceil(monthlyMaxHours),
            avgWeeklyHours: Math.ceil(avgWeeklyHours),
            totalCurrentHours,
            totalTargetHours,
            monthlyOvertimeMinutes: Math.floor(monthlyOvertimeMinutes)
        }
    }

    /**
     * 주간 근무시간 검증 (휴게시간 제외한 실제 근무시간 기준)
     * @param {Object} week - 주간 데이터 {days: []}
     * @returns {Object} 검증 결과
     */
    const validateWeeklyWorkTime = (week) => {
        if (!week.days || week.days.length === 0) {
            return {
                exceedsSpecialLimit: false,
                exceedsNormalLimit: false,
                hasOvertimeWork: false,
                meetsBasicHours: false,
                specialExcessHours: 0,
                normalExcessHours: 0,
                overtimeHours: 0,
                totalHours: 0,
                actualHours: 0
            }
        }

        let totalWeeklyMinutes = 0
        let actualWeeklyMinutes = 0

        // 각 일자별 근무시간 계산 (휴게시간 포함/제외)
        week.days.forEach(day => {
            if (day.startTime && day.endTime && day.startTime !== '-' && day.endTime !== '-') {
                // 총 재실시간
                const dailyTotalMinutes = calculateTimeDifferenceInMinutes(day.startTime, day.endTime)
                totalWeeklyMinutes += dailyTotalMinutes

                // 실제 근무시간 (휴게시간 제외)
                const dailyActualMinutes = calculateActualWorkMinutes(day.startTime, day.endTime)
                actualWeeklyMinutes += dailyActualMinutes
            }
        })

        const totalWeeklyHours = Math.floor(totalWeeklyMinutes / 60)
        const actualWeeklyHours = Math.floor(actualWeeklyMinutes / 60)

        // 2024년 새 기준 - 주 40시간 초과분이 연장근무 (실제 근무시간 기준)
        const weeklyBasicMinutes = 40 * 60  // 40시간 = 2400분
        const totalOvertimeMinutes = Math.max(0, actualWeeklyMinutes - weeklyBasicMinutes)
        const totalOvertimeHours = Math.floor(totalOvertimeMinutes / 60)

        // 한국 근로기준법 기준 (실제 근무시간 기준)
        const weeklyBasicHours = 40        // 주당 기본 40시간
        const weeklyMaxHours = 52          // 주당 최대 52시간 (40 + 12)
        const weeklySpecialMaxHours = 60   // 특별한 사정 시 60시간 (40 + 12 + 8)

        const exceedsSpecialLimit = actualWeeklyHours > weeklySpecialMaxHours
        const exceedsNormalLimit = actualWeeklyHours > weeklyMaxHours && !exceedsSpecialLimit
        const hasOvertimeWork = totalOvertimeHours > 0 && !exceedsNormalLimit && !exceedsSpecialLimit
        const meetsBasicHours = actualWeeklyHours >= weeklyBasicHours && !hasOvertimeWork && !exceedsNormalLimit && !exceedsSpecialLimit

        return {
            exceedsSpecialLimit,
            exceedsNormalLimit,
            hasOvertimeWork,
            meetsBasicHours,
            specialExcessHours: exceedsSpecialLimit ? actualWeeklyHours - weeklySpecialMaxHours : 0,
            normalExcessHours: exceedsNormalLimit ? actualWeeklyHours - weeklyMaxHours : 0,
            overtimeHours: totalOvertimeHours,
            totalHours: totalWeeklyHours,      // 총 재실시간
            actualHours: actualWeeklyHours     // 실제 근무시간
        }
    }

    /**
 * 일별 근무시간 검증 (휴게시간 포함)
 * @param {Object} day - 일별 데이터 {startTime, endTime}
 * @returns {Object} 검증 결과
 */
    const validateDailyWorkTime = (day) => {
        // 출퇴근 시간이 없는 경우 기본값 반환
        if (!day.startTime || !day.endTime || day.startTime === '-' || day.endTime === '-') {
            return {
                hasOvertime: false,
                exceedsLimit: false,
                isFullDay: false,
                isInsufficient: false,
                overtimeHours: 0,
                totalHours: 0,
                actualHours: 0,
                breakMinutes: 0
            }
        }

        // 총 재실시간 계산 (분 단위)
        const totalMinutes = calculateTimeDifferenceInMinutes(day.startTime, day.endTime)
        const totalHours = Math.floor(totalMinutes / 60)

        // 휴게시간 계산
        const breakMinutes = calculateBreakTime(totalMinutes)

        // 실제 근무시간 계산 (휴게시간 제외)
        const actualWorkMinutes = calculateActualWorkMinutes(day.startTime, day.endTime)
        const actualHours = Math.floor(actualWorkMinutes / 60)

        // 일별 기준시간 (실제 근무시간 기준)
        const dailyBasicHours = 8      // 일 8시간 기본 근무
        const dailyMaxHours = 12       // 일일 최대 실제 근무시간 (12시간)

        // 연장근무 계산 (실제 근무시간 기준)
        const overtimeHours = Math.max(0, actualHours - dailyBasicHours)

        return {
            hasOvertime: overtimeHours > 0 && actualHours <= dailyMaxHours,
            exceedsLimit: actualHours > dailyMaxHours,
            isFullDay: actualHours === dailyBasicHours,
            isInsufficient: actualHours > 0 && actualHours < dailyBasicHours,
            overtimeHours,
            totalHours,        // 총 재실시간 (시간)
            actualHours,       // 실제 근무시간 (시간)
            breakMinutes       // 휴게시간 (분)
        }
    }

    return {
        validateMonthlyWorkTime,
        validateWeeklyWorkTime,
        validateDailyWorkTime
    }
} 