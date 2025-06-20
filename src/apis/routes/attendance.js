export const AttendanceAPI = {
    // DELETE - 근태 기록 삭제
    DELETE_RECORD: (recordId) => `/api/v1/attendance-records/${recordId}`,

    // GET - 모든 근태 기록 조회
    ALL_RECORDS: '/api/v1/attendance/records',

    // GET - 근태 기록 상세 조회
    RECORD_DETAIL: (id) => `/api/v1/attendance/records/${id}`,

    // GET - 회원별 근태 기록 조회
    MEMBER_RECORDS: (memberId) => `/api/v1/attendance/records/member/${memberId}`,

    // GET - 날짜 범위별 근태 기록 조회
    RECORDS_BY_DATE_RANGE: '/api/v1/attendance/records/date-range',

    // GET - 내 모든 근태 기록 조회
    MY_RECORDS: '/api/v1/attendance/my',

    // GET - 내 최근 근태 기록 조회
    MY_RECENT_RECORDS: '/api/v1/attendance/my/recent',

    // GET - 내 특정 기간 근태 기록 조회
    MY_RECORDS_BY_DATE_RANGE: '/api/v1/attendance/my/date-range',

    // GET - 모든 근태 카테고리 조회
    ALL_CATEGORIES: '/api/v1/attendance/categories',

    // GET - 근태 카테고리 상세 조회
    CATEGORY_DETAIL: (id) => `/api/v1/attendance/categories/${id}`,

    // GET - 근태 카테고리 status별 조회
    CATEGORIES_BY_STATUS: (status) => `/api/v1/attendance/categories/status/${status}`,

    // GET - 근태 카테고리 총 개수 조회
    CATEGORIES_COUNT: '/api/v1/attendance/categories/count',

    // GET - 복수 ID로 근태 카테고리 조회
    CATEGORIES_BULK: '/api/v1/attendance/categories/bulk',

    // GET - 오늘 퇴근 기록 확인
    TODAY_CHECKOUT: '/api/v1/attendance-records/check-out/today',

    // GET - 오늘 출근 기록 확인
    TODAY_CHECKIN: '/api/v1/attendance-records/check-in/today',

    // POST - 근태 기록 생성
    CREATE_RECORD: '/api/v1/attendance-records',

    // PUT - 근태 기록 수정
    UPDATE_RECORD: (recordId) => `/api/v1/attendance-records/${recordId}`,
}