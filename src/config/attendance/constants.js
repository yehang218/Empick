/**
 * 근태 관련 상수 정의
 * 카테고리 ID, 상태 코드, 포맷 등의 상수값들을 정의
 */

export const ATTENDANCE_CONSTANTS = {
    // 근태 카테고리 ID
    CATEGORY_IDS: {
        CHECK_IN: 0,
        CHECK_OUT: 1,
        LATE: 2,
        EARLY_LEAVE: 3,
        BREAK_START: 4,
        BREAK_END: 5,
        OVERTIME_START: 6,
        OVERTIME_END: 7,
    },

    // 근태 카테고리 라벨
    CATEGORY_LABELS: {
        0: '출근',
        1: '퇴근',
        2: '지각',
        3: '조퇴',
        4: '휴게시작',
        5: '휴게종료',
        6: '연장시작',
        7: '연장종료',
    },

    // 상태 코드
    STATUS_CODES: {
        NORMAL: 0,      // 정상
        MODIFIED: 1,    // 수정됨
        PENDING_APPROVAL: 2, // 승인 대기중
        APPROVED: 3,    // 승인됨
        REJECTED: 4,    // 거부됨
    },

    // 상태 라벨
    STATUS_LABELS: {
        0: '정상',
        1: '수정됨',
        2: '승인대기',
        3: '승인됨',
        4: '거부됨',
    },

    // 시간 포맷
    TIME_FORMATS: {
        DISPLAY: 'HH:mm:ss',
        DISPLAY_SHORT: 'HH:mm',
        ISO: 'YYYY-MM-DDTHH:mm:ss',
        ISO_WITH_TIMEZONE: 'YYYY-MM-DDTHH:mm:ssZ',
        DATE_ONLY: 'YYYY-MM-DD',
        MONTH_YEAR: 'YYYY-MM',
    },

    // 근무 유형
    WORK_TYPES: {
        REGULAR: 'regular',
        OVERTIME: 'overtime',
        NIGHT: 'night',
        WEEKEND: 'weekend',
        HOLIDAY: 'holiday',
    },

    // 근무 유형 라벨
    WORK_TYPE_LABELS: {
        regular: '정규',
        overtime: '연장',
        night: '야간',
        weekend: '주말',
        holiday: '휴일',
    },

    // 색상 코드
    COLORS: {
        WORK_BAR: '#4caf50',
        OVERTIME_BAR: '#ff9800',
        NIGHT_BAR: '#3f51b5',
        BREAK_BAR: '#81c784',
        WEEKEND_BAR: '#9e9e9e',
    },

    // 캐시 만료 시간 (밀리초)
    CACHE_EXPIRY: {
        CATEGORIES: 10 * 60 * 1000,     // 10분
        RECENT_RECORDS: 3 * 60 * 1000,  // 3분
        TODAY_DATA: 24 * 60 * 60 * 1000, // 24시간 (날짜 기준)
        MONTHLY_DATA: 5 * 60 * 1000,    // 5분
    }
} 