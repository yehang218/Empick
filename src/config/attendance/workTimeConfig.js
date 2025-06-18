/**
 * 근무시간 관련 설정
 * 모든 근무시간 계산의 기준이 되는 설정값들을 정의
 */

export const WORK_TIME_CONFIG = {
    // 기본 근무시간
    STANDARD_WORK_HOURS: 8,
    STANDARD_WORK_MINUTES: 480, // 8시간 = 480분

    // 근무시간대
    WORK_START_TIME: '09:00:00',
    WORK_END_TIME: '18:00:00',

    // 야간 근무시간대 (22:00 ~ 06:00)
    NIGHT_WORK_START: '22:00:00',
    NIGHT_WORK_END: '06:00:00',

    // 휴게시간
    LUNCH_BREAK_START: '12:00:00',
    LUNCH_BREAK_END: '13:00:00',
    LUNCH_BREAK_DURATION: 60, // 60분

    // 연장근무 기준
    OVERTIME_THRESHOLD: 480, // 8시간(480분) 후 연장근무

    // 주말 정의 (0: 일요일, 6: 토요일)
    WEEKEND_DAYS: [0, 6],

    // 시간 계산 관련
    MINUTES_PER_HOUR: 60,
    HOURS_PER_DAY: 24,

    // 반올림 정책
    ROUNDING_POLICY: 'floor', // 'floor', 'ceil', 'round'
} 