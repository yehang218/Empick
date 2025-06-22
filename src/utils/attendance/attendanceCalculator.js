/**
 * 근태 데이터 계산 및 변환 유틸리티
 * 출퇴근 시간 계산, 근무시간 분류, 데이터 그룹핑 등의 기능을 제공
 */

import { WORK_TIME_CONFIG } from '@/config/attendance'
import { formatMinutesToDuration } from './attendanceFormatter.js'

// ==================== 시간 계산 함수들 ====================

// formatMinutesToDuration은 attendanceFormatter.js로 이동됨
export { formatMinutesToDuration } from './attendanceFormatter.js'

/**
 * 시간 차이 계산 (분 단위)
 * @param {string} startTime - 시작 시간 (HH:MM:SS)
 * @param {string} endTime - 종료 시간 (HH:MM:SS)
 * @returns {number} 시간 차이 (분)
 */
export const calculateTimeDifferenceInMinutes = (startTime, endTime) => {
    if (!startTime || !endTime) return 0;

    const start = new Date(`2024-01-01T${startTime}`);
    const end = new Date(`2024-01-01T${endTime}`);

    // 날짜가 바뀐 경우 처리 (야간 근무)
    if (end < start) {
        end.setDate(end.getDate() + 1);
    }

    // 소수점 버리기 (Math.floor 사용)
    return Math.max(0, Math.floor((end - start) / (1000 * 60)));
};

/**
 * 야간 근무시간 계산 (22:00-06:00)
 * @param {string} startTime - 시작 시간 (HH:MM:SS)
 * @param {string} endTime - 종료 시간 (HH:MM:SS)
 * @returns {number} 야간 근무시간 (분)
 */
export const calculateNightWorkHours = (startTime, endTime) => {
    if (!startTime || !endTime) return 0;

    const start = new Date(`2024-01-01T${startTime}`);
    const end = new Date(`2024-01-01T${endTime}`);

    // 날짜가 바뀐 경우 처리
    if (end < start) {
        end.setDate(end.getDate() + 1);
    }

    let nightMinutes = 0;

    // 22:00-24:00 구간
    const nightStart = new Date(`2024-01-01T${WORK_TIME_CONFIG.NIGHT_WORK_START}`);
    const nightEnd = new Date(`2024-01-02T${WORK_TIME_CONFIG.NIGHT_WORK_END}`);

    // 야간 시간대와 근무시간의 교집합 계산
    const overlapStart = new Date(Math.max(start.getTime(), nightStart.getTime()));
    const overlapEnd = new Date(Math.min(end.getTime(), nightEnd.getTime()));

    if (overlapStart < overlapEnd) {
        // 소수점 버리기 (Math.floor 사용)
        nightMinutes = Math.floor((overlapEnd - overlapStart) / (1000 * 60));
    }

    return Math.max(0, nightMinutes);
};

/**
 * 근무시간 분류 계산
 * @param {string} startTime - 시작 시간 (HH:MM:SS)
 * @param {string} endTime - 종료 시간 (HH:MM:SS)
 * @returns {Object} 근무시간 분류 결과
 */
export const categorizeWorkHours = (startTime, endTime) => {
    const totalMinutes = calculateTimeDifferenceInMinutes(startTime, endTime);

    if (totalMinutes === 0) {
        return {
            total: '0h 0m',
            regular: '0h 0m',
            overtime: '0h 0m',
            night: '0h 0m'
        };
    }

    // 기본 근무시간 (설정에서 가져옴)
    const regularLimit = WORK_TIME_CONFIG.STANDARD_WORK_MINUTES;
    const regularMinutes = Math.min(totalMinutes, regularLimit);
    const overtimeMinutes = Math.max(0, totalMinutes - regularLimit);

    // 야간 근무 계산 (22:00-06:00)
    const nightMinutes = calculateNightWorkHours(startTime, endTime);

    return {
        total: formatMinutesToDuration(totalMinutes),
        regular: formatMinutesToDuration(regularMinutes),
        overtime: formatMinutesToDuration(overtimeMinutes),
        night: formatMinutesToDuration(nightMinutes)
    };
};

// ==================== 데이터 그룹핑 함수들 ====================

/**
 * 날짜별 근태 데이터 그룹핑
 * @param {Array} records - 근태 기록 배열
 * @returns {Object} 날짜별로 그룹핑된 데이터
 */
export const groupAttendanceByDate = (records) => {
    const dailyData = {};

    records.forEach(record => {
        if (!record.recordTime) return;

        const date = record.recordTime.split('T')[0];
        const time = record.recordTime.split('T')[1].substring(0, 8); // HH:MM:SS

        if (!dailyData[date]) {
            dailyData[date] = {
                date: date,
                records: [],
                checkIn: null,
                checkOut: null
            };
        }

        dailyData[date].records.push(record);

        // 출근/퇴근 시간 설정 (categoryId로 판단)
        if (record.attendanceCategoryId === 1) {  // 출근
            dailyData[date].checkIn = { ...record, time };
        } else if (record.attendanceCategoryId === 2) {  // 퇴근
            dailyData[date].checkOut = { ...record, time };
        }
    });

    return dailyData;
};

/**
 * 주차별 데이터 그룹핑
 * @param {number} year - 연도
 * @param {number} month - 월 (1-12)
 * @param {Array} records - 근태 기록 배열
 * @returns {Array} 주차별 데이터 배열
 */
export const groupAttendanceByWeek = (year, month, records) => {
    const dailyData = groupAttendanceByDate(records);
    const weeks = [];

    const firstDay = new Date(year, month - 1, 1);
    const lastDay = new Date(year, month, 0);

    let currentWeekStart = new Date(firstDay);
    let weekNumber = 1;

    // 주차 계산 (월요일 기준)
    const firstDayOfWeek = firstDay.getDay();
    if (firstDayOfWeek !== 1) {
        currentWeekStart.setDate(firstDay.getDate() - (firstDayOfWeek === 0 ? 6 : firstDayOfWeek - 1));
    }

    while (currentWeekStart <= lastDay) {
        const weekEnd = new Date(currentWeekStart);
        weekEnd.setDate(currentWeekStart.getDate() + 6);

        const weekDays = [];
        let weekTotalMinutes = 0;

        // 해당 주의 일자들 처리
        for (let i = 0; i < 7; i++) {
            const currentDate = new Date(currentWeekStart);
            currentDate.setDate(currentWeekStart.getDate() + i);

            // 해당 월의 날짜만 포함
            if (currentDate.getMonth() === month - 1) {
                const dateStr = currentDate.toISOString().split('T')[0];
                const dayData = dailyData[dateStr];

                if (dayData && (dayData.checkIn || dayData.checkOut)) {
                    const startTime = dayData.checkIn?.time || '';
                    const endTime = dayData.checkOut?.time || '';

                    // 출근만 있고 퇴근이 없는 경우 처리
                    let workHours;
                    let totalMinutes = 0;

                    if (startTime && endTime) {
                        // 출근과 퇴근이 모두 있는 경우
                        workHours = categorizeWorkHours(startTime, endTime);
                        totalMinutes = calculateTimeDifferenceInMinutes(startTime, endTime);
                    } else {
                        // 출근만 있거나 퇴근만 있는 경우
                        workHours = {
                            total: '0h 0m',
                            regular: '0h 0m',
                            overtime: '0h 0m',
                            night: '0h 0m'
                        };
                    }

                    // 타임존 문제 해결: dateStr에서 직접 날짜 추출
                    const dayOfMonth = dateStr.split('-')[2]; // "2024-12-18" -> "18"

                    const dayInfo = {
                        date: dayOfMonth,
                        startTime: startTime || '-',
                        endTime: endTime || '-',
                        totalDuration: workHours.total,
                        regularHours: workHours.regular,
                        overtimeHours: workHours.overtime,
                        nightHours: workHours.night,
                        needsApproval: dayData.records.some(r => r.status === 2),
                        selected: false,
                        breakTime: true,
                        rawRecords: dayData.records
                    };

                    weekDays.push(dayInfo);

                    // 주차별 총 근무시간 계산 (출근과 퇴근이 모두 있는 경우만)
                    weekTotalMinutes += totalMinutes;
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
                totalHours: formatMinutesToDuration(weekTotalMinutes)
            });
            weekNumber++;
        }

        currentWeekStart.setDate(currentWeekStart.getDate() + 7);
    }

    return weeks;
};

/**
 * 월간 근무 통계 계산
 * @param {Array} records - 근태 기록 배열
 * @returns {Object} 월간 통계 데이터
 */
export const calculateMonthlyStats = (records) => {
    const dailyData = groupAttendanceByDate(records);
    let totalMinutes = 0;
    let workDays = 0;

    Object.values(dailyData).forEach(day => {
        if (day.checkIn && day.checkOut) {
            const minutes = calculateTimeDifferenceInMinutes(day.checkIn.time, day.checkOut.time);
            totalMinutes += minutes;
            workDays++;
        }
    });

    // 평균 계산 시 소수점 버리기
    const avgMinutesPerDay = workDays > 0 ? Math.floor(totalMinutes / workDays) : 0;

    return {
        totalHours: formatMinutesToDuration(totalMinutes),
        workDays,
        averageHoursPerDay: formatMinutesToDuration(avgMinutesPerDay)
    };
};

/**
 * UI용 데이터 변환
 * @param {number} year - 연도
 * @param {number} month - 월 (1-12)
 * @param {Array} records - 근태 기록 배열
 * @returns {Array} UI에서 사용할 수 있는 주차별 데이터
 */
export const transformAttendanceDataForUI = (year, month, records) => {
    return groupAttendanceByWeek(year, month, records);
}; 