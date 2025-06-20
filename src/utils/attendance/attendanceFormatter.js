/**
 * 근태 데이터 포맷팅 유틸리티
 * 시간, 날짜, 근무시간 등의 데이터를 다양한 형태로 포맷팅
 */

import { WORK_TIME_CONFIG, ATTENDANCE_CONSTANTS } from '@/config/attendance'

/**
 * 분을 시간 문자열로 포맷팅 (예: 90 → "1h 30m")
 * @param {number} totalMinutes - 총 분 수
 * @param {string} format - 포맷 형태 ('short', 'long', 'colon')
 * @returns {string} 포맷된 시간 문자열
 */
export const formatMinutesToDuration = (totalMinutes, format = 'short') => {
    if (!totalMinutes || totalMinutes < 0) {
        return format === 'colon' ? '00:00' : '0h 0m'
    }

    const hours = Math.floor(totalMinutes / WORK_TIME_CONFIG.MINUTES_PER_HOUR)
    const minutes = totalMinutes % WORK_TIME_CONFIG.MINUTES_PER_HOUR

    switch (format) {
        case 'long':
            return `${hours}시간 ${minutes}분`
        case 'colon':
            return `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}`
        case 'short':
        default:
            return `${hours}h ${minutes}m`
    }
}

/**
 * 시간 문자열을 분으로 변환 (예: "1h 30m" → 90)
 * @param {string} timeString - 시간 문자열
 * @returns {number} 총 분 수
 */
export const parseDurationToMinutes = (timeString) => {
    if (!timeString) return 0

    // "1h 30m" 형태 파싱
    const hourMatch = timeString.match(/(\d+)h/)
    const minuteMatch = timeString.match(/(\d+)m/)

    const hours = hourMatch ? parseInt(hourMatch[1], 10) : 0
    const minutes = minuteMatch ? parseInt(minuteMatch[1], 10) : 0

    return hours * WORK_TIME_CONFIG.MINUTES_PER_HOUR + minutes
}

/**
 * 시간 문자열을 객체로 파싱 (예: "1h 30m" → {hours: 1, minutes: 30})
 * @param {string} timeString - 시간 문자열
 * @returns {Object} 시간 객체
 */
export const parseTimeString = (timeString) => {
    if (!timeString) return { hours: 0, minutes: 0 }

    const totalMinutes = parseDurationToMinutes(timeString)
    return {
        hours: Math.floor(totalMinutes / WORK_TIME_CONFIG.MINUTES_PER_HOUR),
        minutes: totalMinutes % WORK_TIME_CONFIG.MINUTES_PER_HOUR
    }
}

/**
 * 화면 표시용 시간 포맷팅
 * @param {string} timeString - 시간 문자열 (HH:MM:SS)
 * @param {boolean} includeSeconds - 초 포함 여부
 * @returns {string} 포맷된 시간
 */
export const formatTimeForDisplay = (timeString, includeSeconds = true) => {
    if (!timeString) return '--:--'

    const time = timeString.substring(0, includeSeconds ? 8 : 5)
    return time
}

/**
 * API 전송용 날짜 포맷팅
 * @param {Date|string} date - 날짜 객체 또는 문자열
 * @param {boolean} includeTime - 시간 포함 여부
 * @returns {string} ISO 형식 날짜 문자열
 */
export const formatDateForAPI = (date, includeTime = true) => {
    const dateObj = typeof date === 'string' ? new Date(date) : date

    if (includeTime) {
        return dateObj.toISOString().slice(0, 19) // YYYY-MM-DDTHH:mm:ss
    } else {
        return dateObj.toISOString().slice(0, 10) // YYYY-MM-DD
    }
}

/**
 * 사용자 친화적 날짜 포맷팅
 * @param {Date|string} date - 날짜 객체 또는 문자열
 * @param {string} format - 포맷 형태 ('short', 'long', 'relative')
 * @returns {string} 포맷된 날짜
 */
export const formatDateForDisplay = (date, format = 'short') => {
    const dateObj = typeof date === 'string' ? new Date(date) : date
    const now = new Date()

    switch (format) {
        case 'long':
            return dateObj.toLocaleDateString('ko-KR', {
                year: 'numeric',
                month: 'long',
                day: 'numeric',
                weekday: 'long'
            })
        case 'relative': {
            const diffDays = Math.floor((now - dateObj) / (1000 * 60 * 60 * 24))
            if (diffDays === 0) return '오늘'
            if (diffDays === 1) return '어제'
            if (diffDays === -1) return '내일'
            if (diffDays > 0) return `${diffDays}일 전`
            return `${Math.abs(diffDays)}일 후`
        }
        case 'short':
        default:
            return dateObj.toLocaleDateString('ko-KR', {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit'
            })
    }
}

/**
 * 근무시간 분류별 포맷팅
 * @param {Object} workHours - 근무시간 분류 객체
 * @param {string} format - 포맷 형태
 * @returns {Object} 포맷된 근무시간 객체
 */
export const formatWorkHours = (workHours, format = 'short') => {
    return {
        total: formatMinutesToDuration(workHours.totalMinutes || 0, format),
        regular: formatMinutesToDuration(workHours.regularMinutes || 0, format),
        overtime: formatMinutesToDuration(workHours.overtimeMinutes || 0, format),
        night: formatMinutesToDuration(workHours.nightMinutes || 0, format)
    }
}

/**
 * 근태 상태 라벨 포맷팅
 * @param {number} statusCode - 상태 코드
 * @returns {string} 상태 라벨
 */
export const formatStatusLabel = (statusCode) => {
    return ATTENDANCE_CONSTANTS.STATUS_LABELS[statusCode] || '알 수 없음'
}

/**
 * 근태 카테고리 라벨 포맷팅
 * @param {number} categoryId - 카테고리 ID
 * @returns {string} 카테고리 라벨
 */
export const formatCategoryLabel = (categoryId) => {
    return ATTENDANCE_CONSTANTS.CATEGORY_LABELS[categoryId] || '알 수 없음'
}

/**
 * 근무 유형 라벨 포맷팅
 * @param {string} workType - 근무 유형
 * @returns {string} 근무 유형 라벨
 */
export const formatWorkTypeLabel = (workType) => {
    return ATTENDANCE_CONSTANTS.WORK_TYPE_LABELS[workType] || workType
}

/**
 * 월간 요약 데이터 포맷팅
 * @param {Object} monthlyStats - 월간 통계 데이터
 * @returns {Object} 포맷된 월간 요약
 */
export const formatMonthlySummary = (monthlyStats) => {
    const totalMinutes = parseDurationToMinutes(monthlyStats.totalHours)
    const avgMinutes = parseDurationToMinutes(monthlyStats.averageHoursPerDay)

    return {
        totalHours: formatMinutesToDuration(totalMinutes, 'long'),
        totalHoursShort: formatMinutesToDuration(totalMinutes, 'short'),
        totalHoursColon: formatMinutesToDuration(totalMinutes, 'colon'),
        workDays: monthlyStats.workDays,
        averageHoursPerDay: formatMinutesToDuration(avgMinutes, 'short'),
        averageHoursPerDayColon: formatMinutesToDuration(avgMinutes, 'colon'),
        totalMinutes,
        averageMinutesPerDay: avgMinutes
    }
}

/**
 * 주차별 요약 데이터 포맷팅
 * @param {Array} weekData - 주차별 데이터 배열
 * @returns {Array} 포맷된 주차별 요약
 */
export const formatWeeklySummary = (weekData) => {
    return weekData.map(week => ({
        ...week,
        totalHoursFormatted: formatMinutesToDuration(parseDurationToMinutes(week.totalHours), 'short'),
        dateRange: `${formatDateForDisplay(week.startDate, 'short')} ~ ${formatDateForDisplay(week.endDate, 'short')}`,
        workDaysCount: week.days.length
    }))
}

/**
 * 진행률 계산 및 포맷팅
 * @param {number} current - 현재 값
 * @param {number} target - 목표 값
 * @param {string} unit - 단위 ('minutes', 'hours', 'days')
 * @returns {Object} 진행률 정보
 */
export const formatProgress = (current, target, unit = 'minutes') => {
    if (!target || target === 0) {
        return {
            percentage: 0,
            remaining: 0,
            isComplete: false,
            isOverTarget: false
        }
    }

    const percentage = Math.min(100, Math.round((current / target) * 100))
    const remaining = Math.max(0, target - current)

    return {
        percentage,
        remaining,
        isComplete: current >= target,
        isOverTarget: current > target,
        remainingFormatted: unit === 'minutes' ? formatMinutesToDuration(remaining) : remaining,
        currentFormatted: unit === 'minutes' ? formatMinutesToDuration(current) : current,
        targetFormatted: unit === 'minutes' ? formatMinutesToDuration(target) : target
    }
} 