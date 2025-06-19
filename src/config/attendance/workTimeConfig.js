/**
 * 근무시간 관련 설정 (한국 근로기준법 기준)
 * 모든 근무시간 계산의 기준이 되는 설정값들을 정의
 */

export const WORK_TIME_CONFIG = {
    // 기본 근무시간 (근로기준법 제50조: 1주 40시간, 1일 8시간)
    STANDARD_WORK_HOURS: 8,
    STANDARD_WORK_MINUTES: 480, // 8시간 = 480분
    WEEKLY_STANDARD_HOURS: 40, // 주 40시간
    WEEKLY_STANDARD_MINUTES: 2400, // 40시간 = 2400분

    // 근무시간대
    WORK_START_TIME: '09:00:00',
    WORK_END_TIME: '18:00:00',

    // 야간 근무시간대 (근로기준법 제56조: 22:00 ~ 06:00)
    NIGHT_WORK_START: '22:00:00',
    NIGHT_WORK_END: '06:00:00',

    // 휴게시간 (근로기준법 제54조: 4시간 근무 시 30분, 8시간 근무 시 1시간)
    LUNCH_BREAK_START: '12:00:00',
    LUNCH_BREAK_END: '13:00:00',
    LUNCH_BREAK_DURATION: 60, // 60분
    REQUIRED_BREAK_4HOURS: 30, // 4시간 근무 시 30분 휴게
    REQUIRED_BREAK_8HOURS: 60, // 8시간 근무 시 1시간 휴게

    // 연장근무 기준 (근로기준법 제53조)
    OVERTIME_THRESHOLD: 480, // 8시간(480분) 후 연장근무
    MAX_OVERTIME_DAILY: 12, // 1일 최대 연장근무 12시간 (평상시)
    MAX_OVERTIME_WEEKLY: 12, // 1주 최대 연장근무 12시간

    // 특별연장근무 (근로기준법 제53조 제3항)
    MAX_SPECIAL_OVERTIME_DAILY: 8, // 특별한 사정 시 1일 최대 8시간 추가
    MAX_SPECIAL_OVERTIME_WEEKLY: 8, // 특별한 사정 시 1주 최대 8시간 추가

    // 주말 정의 (0: 일요일, 6: 토요일)
    WEEKEND_DAYS: [0, 6],

    // 공휴일 근무 (근로기준법 제55조)
    HOLIDAY_WORK_RATE: 1.5, // 공휴일 근무 시 150% 수당

    // 시간 계산 관련
    MINUTES_PER_HOUR: 60,
    HOURS_PER_DAY: 24,

    // 반올림 정책
    ROUNDING_POLICY: 'floor', // 'floor', 'ceil', 'round'

    // 수당 계산 기준 (근로기준법 제56조)
    OVERTIME_RATE: 1.5, // 연장근무 150% 수당
    NIGHT_WORK_RATE: 1.5, // 야간근무 150% 수당
    WEEKEND_WORK_RATE: 1.5, // 주말근무 150% 수당
} 