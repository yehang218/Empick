import { computed } from 'vue'

/**
 * 근무시간 계산 관련 로직을 담은 composable
 */
export const useWorkTimeCalculation = () => {

    /**
     * 진행률 계산
     * @param {Object} workingHours - 현재 근무시간 {hours, minutes}
     * @param {Object} targetHours - 목표 근무시간 {hours, minutes}
     * @returns {number} 진행률 (백분율)
     */
    const calculateProgressPercentage = (workingHours, targetHours) => {
        // 안전한 값 추출
        const currentHours = workingHours?.hours || 0
        const currentMins = workingHours?.minutes || 0
        const targetH = targetHours?.hours || 0
        const targetM = targetHours?.minutes || 0

        const currentMinutes = currentHours * 60 + currentMins
        const targetMinutes = targetH * 60 + targetM

        if (targetMinutes === 0) return 0

        const percentage = (currentMinutes / targetMinutes) * 100
        return isNaN(percentage) ? 0 : percentage
    }

    /**
     * 진행률 색상 결정
     * @param {boolean} hasLegalIssues - 법적 문제 여부
     * @param {boolean} exceedsWeeklyLimit - 주간 한도 초과 여부
     * @param {number} progressPercentage - 진행률
     * @returns {string} 색상 코드
     */
    const getProgressColor = (hasLegalIssues, exceedsWeeklyLimit, progressPercentage) => {
        if (hasLegalIssues) return 'error'
        if (exceedsWeeklyLimit) return 'warning'
        if (progressPercentage >= 100) return 'success'
        return 'primary'
    }

    /**
     * 진행률 텍스트 클래스 결정
     * @param {boolean} hasLegalIssues - 법적 문제 여부
     * @param {boolean} exceedsWeeklyLimit - 주간 한도 초과 여부
     * @returns {string} CSS 클래스명
     */
    const getProgressTextClass = (hasLegalIssues, exceedsWeeklyLimit) => {
        if (hasLegalIssues) return 'error-text'
        if (exceedsWeeklyLimit) return 'warning-text'
        return ''
    }

    /**
     * 남은 근무시간 범위 계산
     * @param {Object} workingHours - 현재 근무시간 {hours, minutes}
     * @param {Object} targetHours - 목표 근무시간 {hours, minutes}
     * @returns {string} 범위 문자열
     */
    const calculateRemainingTimeRange = (workingHours, targetHours) => {
        // 안전한 값 추출
        const currentHours = workingHours?.hours || 0
        const currentMins = workingHours?.minutes || 0
        const targetH = targetHours?.hours || 0
        const targetM = targetHours?.minutes || 0

        const currentMinutes = currentHours * 60 + currentMins
        const targetMinutes = targetH * 60 + targetM
        const remainingMinutes = Math.max(0, targetMinutes - currentMinutes)

        // 최소: 남은 시간 그대로, 최대: 남은 시간 + 여유시간(5시간)
        const minHours = Math.floor(remainingMinutes / 60)
        const minMins = remainingMinutes % 60
        const maxTotalMinutes = remainingMinutes + (5 * 60) // 5시간 여유
        const maxHours = Math.floor(maxTotalMinutes / 60)
        const maxMins = maxTotalMinutes % 60

        return `최소 ${minHours}h ${minMins}m ~ 최대 ${maxHours}h ${maxMins}m`
    }

    /**
     * 일일 평균 필요 시간 계산
     * @param {Object} workingHours - 현재 근무시간 {hours, minutes}
     * @param {Object} targetHours - 목표 근무시간 {hours, minutes}
     * @param {number} remainingWorkDays - 남은 근무일
     * @returns {string} 평균 시간 문자열
     */
    const calculateDailyAverageNeeded = (workingHours, targetHours, remainingWorkDays) => {
        if (!remainingWorkDays || remainingWorkDays === 0) return '0h 0m'

        // 안전한 값 추출
        const currentHours = workingHours?.hours || 0
        const currentMins = workingHours?.minutes || 0
        const targetH = targetHours?.hours || 0
        const targetM = targetHours?.minutes || 0

        const currentMinutes = currentHours * 60 + currentMins
        const targetMinutes = targetH * 60 + targetM
        const remainingMinutes = Math.max(0, targetMinutes - currentMinutes)

        const avgMinutesPerDay = Math.ceil(remainingMinutes / remainingWorkDays)
        const hours = Math.floor(avgMinutesPerDay / 60)
        const minutes = avgMinutesPerDay % 60

        return `${hours}h ${minutes}m`
    }

    /**
     * 진행률 포맷팅
     * @param {number} percentage - 진행률
     * @returns {string} 포맷된 진행률
     */
    const formatProgressPercentage = (percentage) => {
        return `${percentage.toFixed(1)}%`
    }

    /**
     * reactive computed를 이용한 진행률 계산
     * @param {Object} workingHours - 반응형 근무시간 객체
     * @param {Object} targetHours - 반응형 목표시간 객체
     * @returns {ComputedRef} computed 진행률
     */
    const useProgressPercentage = (workingHours, targetHours) => {
        return computed(() => calculateProgressPercentage(workingHours.value, targetHours.value))
    }

    /**
     * reactive computed를 이용한 진행률 포맷팅
     * @param {ComputedRef} progressPercentage - 진행률 computed
     * @returns {ComputedRef} 포맷된 진행률
     */
    const useFormattedProgressPercentage = (progressPercentage) => {
        return computed(() => formatProgressPercentage(progressPercentage.value))
    }

    /**
     * reactive computed를 이용한 남은 시간 범위 계산
     * @param {Object} workingHours - 반응형 근무시간 객체
     * @param {Object} targetHours - 반응형 목표시간 객체
     * @returns {ComputedRef} computed 남은 시간 범위
     */
    const useRemainingTimeRange = (workingHours, targetHours) => {
        return computed(() => calculateRemainingTimeRange(workingHours.value, targetHours.value))
    }

    /**
     * reactive computed를 이용한 일일 평균 계산
     * @param {Object} workingHours - 반응형 근무시간 객체
     * @param {Object} targetHours - 반응형 목표시간 객체
     * @param {Ref} remainingWorkDays - 반응형 남은 근무일
     * @returns {ComputedRef} computed 일일 평균
     */
    const useDailyAverageNeeded = (workingHours, targetHours, remainingWorkDays) => {
        return computed(() => calculateDailyAverageNeeded(
            workingHours.value,
            targetHours.value,
            remainingWorkDays.value
        ))
    }

    return {
        // 단순 계산 함수들
        calculateProgressPercentage,
        getProgressColor,
        getProgressTextClass,
        calculateRemainingTimeRange,
        calculateDailyAverageNeeded,
        formatProgressPercentage,

        // reactive computed 함수들
        useProgressPercentage,
        useFormattedProgressPercentage,
        useRemainingTimeRange,
        useDailyAverageNeeded
    }
} 