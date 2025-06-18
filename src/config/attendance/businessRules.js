/**
 * 근태 관련 비즈니스 규칙
 * 출퇴근, 승인, 지각/조퇴 등의 비즈니스 로직 규칙을 정의
 */

export const BUSINESS_RULES = {
    // 출퇴근 규칙
    ATTENDANCE_RULES: {
        MAX_CHECK_IN_PER_DAY: 1,
        MAX_CHECK_OUT_PER_DAY: 1,
        REQUIRE_CHECK_IN_BEFORE_CHECK_OUT: true,
        ALLOW_RETROACTIVE_ENTRY: false, // 소급 입력 허용 여부
    },

    // 지각/조퇴 기준
    TARDINESS_RULES: {
        LATE_THRESHOLD_MINUTES: 10, // 10분 후 지각
        EARLY_LEAVE_THRESHOLD_MINUTES: 30, // 30분 전 조퇴
        GRACE_PERIOD_MINUTES: 5, // 유예시간 5분
    },

    // 승인 요청 규칙
    APPROVAL_RULES: {
        REQUIRE_APPROVAL_FOR_OVERTIME: true,
        REQUIRE_APPROVAL_FOR_NIGHT_WORK: true,
        REQUIRE_APPROVAL_FOR_WEEKEND_WORK: true,
        REQUIRE_APPROVAL_FOR_HOLIDAY_WORK: true,
        AUTO_APPROVE_THRESHOLD_MINUTES: 30, // 30분 이하는 자동 승인
    },

    // 데이터 유효성 규칙
    VALIDATION_RULES: {
        MAX_WORK_HOURS_PER_DAY: 16, // 하루 최대 근무시간
        MIN_WORK_HOURS_PER_DAY: 1,  // 하루 최소 근무시간
        MAX_CONTINUOUS_WORK_HOURS: 12, // 연속 근무 최대시간
        REQUIRE_BREAK_AFTER_HOURS: 6, // 6시간 후 휴게시간 필수
    },

    // 알림 규칙
    NOTIFICATION_RULES: {
        REMIND_CHECK_OUT_AFTER_HOURS: 10, // 10시간 후 퇴근 알림
        WARN_OVERTIME_BEFORE_MINUTES: 30, // 연장근무 30분 전 경고
        DAILY_SUMMARY_TIME: '18:00', // 일일 요약 시간
    }
} 