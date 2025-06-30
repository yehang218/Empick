/**
 * 근태 관련 상수 정의
 * 카테고리 ID, 상태 코드, 포맷 등의 상수값들을 정의
 */

export const ATTENDANCE_CONSTANTS = {
    // 근태 카테고리 ID (DB의 실제 ID에 맞춤)
    CATEGORY_IDS: {
        CHECK_IN: 1,        // 출근 (DB의 실제 ID)
        CHECK_OUT: 2,       // 퇴근 (DB의 실제 ID)
        LATE: 3,            // 지각 (DB의 실제 ID)
        EARLY_LEAVE: 4,     // 조퇴 (DB의 실제 ID)
        BREAK_START: 5,     // 휴게시작
        BREAK_END: 6,       // 휴게종료
        OVERTIME_START: 7,  // 연장시작
        OVERTIME_END: 8,    // 연장종료
    },

    // 근태 카테고리 라벨 (DB ID에 맞춤)
    CATEGORY_LABELS: {
        1: '출근',
        2: '퇴근',
        3: '지각',
        4: '조퇴',
        5: '휴게시작',
        6: '휴게종료',
        7: '연장시작',
        8: '연장종료',
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