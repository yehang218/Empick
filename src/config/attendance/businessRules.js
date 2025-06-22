/**
 * 근태 관련 비즈니스 규칙 (한국 근로기준법 기준)
 * 출퇴근, 승인, 지각/조퇴 등의 비즈니스 로직 규칙을 정의
 * 근태 카테고리 ID: 1=출근, 2=퇴근, 3=지각, 4=조퇴
 */

export const BUSINESS_RULES = {
    // 출퇴근 규칙 (근태 카테고리 ID 기준)
    ATTENDANCE_RULES: {
        MAX_CHECK_IN_PER_DAY: 1,                       // 하루 최대 출근 기록 수
        MAX_CHECK_OUT_PER_DAY: 1,                      // 하루 최대 퇴근 기록 수  
        REQUIRE_CHECK_IN_BEFORE_CHECK_OUT: true,       // 퇴근 전 출근 필수 여부
        ALLOW_RETROACTIVE_ENTRY: false, // 소급 입력 허용 여부
        FLEXIBLE_TIME_START: '08:00:00', // 유연근무제 시작 가능 시간
        FLEXIBLE_TIME_END: '10:00:00', // 유연근무제 종료 시간
    },

    // 지각/조퇴 기준
    TARDINESS_RULES: {
        LATE_THRESHOLD_MINUTES: 1, // 1분 후 지각 (법적으로는 즉시)
        EARLY_LEAVE_THRESHOLD_MINUTES: 1, // 1분 전 조퇴
        GRACE_PERIOD_MINUTES: 0, // 유예시간 없음 (법적 기준)
        SERIOUS_LATE_THRESHOLD_MINUTES: 30, // 심각한 지각 기준
    },

    // 승인 요청 규칙 (근로기준법 준수)
    APPROVAL_RULES: {
        REQUIRE_APPROVAL_FOR_OVERTIME: true, // 연장근무는 반드시 사전 승인
        REQUIRE_APPROVAL_FOR_NIGHT_WORK: true, // 야간근무는 반드시 사전 승인
        REQUIRE_APPROVAL_FOR_WEEKEND_WORK: true, // 주말근무는 반드시 사전 승인
        REQUIRE_APPROVAL_FOR_HOLIDAY_WORK: true, // 공휴일근무는 반드시 사전 승인
        AUTO_APPROVE_THRESHOLD_MINUTES: 0, // 자동 승인 없음 (모든 연장근무 승인 필요)
        ADVANCE_APPROVAL_HOURS: 24, // 24시간 전 사전 승인 필요
    },

    // 데이터 유효성 규칙 (근로기준법 제53조 기준)
    VALIDATION_RULES: {
        MAX_WORK_HOURS_PER_DAY: 20, // 하루 최대 근무시간 (8 + 12시간 연장)
        MAX_WORK_HOURS_PER_DAY_SPECIAL: 28, // 특별한 사정 시 최대 (8 + 12 + 8시간)
        MIN_WORK_HOURS_PER_DAY: 0.5, // 하루 최소 근무시간 (30분)
        MAX_CONTINUOUS_WORK_HOURS: 12, // 연속 근무 최대시간 (휴게시간 제외)
        REQUIRE_BREAK_AFTER_HOURS: 4, // 4시간 후 휴게시간 필수 (근로기준법 제54조)
        MIN_BREAK_DURATION_4HOURS: 30, // 4시간 근무 시 최소 30분 휴게
        MIN_BREAK_DURATION_8HOURS: 60, // 8시간 근무 시 최소 1시간 휴게
        MAX_WEEKLY_HOURS: 52, // 주 최대 근무시간 (40 + 12시간)
        MAX_WEEKLY_HOURS_SPECIAL: 60, // 특별한 사정 시 주 최대 (40 + 12 + 8시간)
    },

    // 휴식 관련 규칙 (근로기준법 제54조, 제55조)
    REST_RULES: {
        WEEKLY_REST_DAYS: 1, // 주 1일 휴일 보장
        CONSECUTIVE_WORK_LIMIT_DAYS: 6, // 연속 근무 최대 6일
        MIN_REST_BETWEEN_SHIFTS_HOURS: 11, // 교대근무 간 최소 11시간 휴식
        MONTHLY_REST_DAYS: 4, // 월 최소 4일 휴일
    },

    // 특수 근무 규칙
    SPECIAL_WORK_RULES: {
        NIGHT_WORK_MAX_HOURS: 8, // 야간근무 최대 시간
        PREGNANT_WORKER_OVERTIME_PROHIBITED: true, // 임산부 연장근무 금지
        MINOR_WORKER_OVERTIME_PROHIBITED: true, // 미성년자 연장근무 금지
        HOLIDAY_WORK_COMPENSATION_RATE: 1.5, // 휴일근무 보상 비율
    },

    // 알림 규칙
    NOTIFICATION_RULES: {
        REMIND_CHECK_OUT_AFTER_HOURS: 8, // 8시간 후 퇴근 알림
        WARN_OVERTIME_BEFORE_MINUTES: 60, // 연장근무 1시간 전 경고
        WARN_WEEKLY_LIMIT_HOURS: 35, // 주 35시간 도달 시 경고
        DAILY_SUMMARY_TIME: '18:00', // 일일 요약 시간
        BREAK_REMINDER_HOURS: 4, // 4시간 근무 시 휴게 알림
    },

    // 법정 의무 사항
    LEGAL_REQUIREMENTS: {
        WORK_TIME_RECORD_RETENTION_YEARS: 3, // 근로시간 기록 보존 3년
        OVERTIME_AGREEMENT_REQUIRED: true, // 연장근무 협정 필요 (근로기준법 제53조)
        NIGHT_WORK_HEALTH_CHECK_REQUIRED: true, // 야간근무자 건강검진 필요
        WEEKLY_OVERTIME_LIMIT_ENFORCEMENT: true, // 주간 연장근무 한도 강제
    }
} 