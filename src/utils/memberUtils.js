/**
 * 멤버 관련 유틸리티 함수들
 * 데이터 변환, 포맷팅, 상태 처리
 */

import dayjs from 'dayjs'

/**
 * 멤버 상태에 따른 색상 반환
 * @param {number} status - 멤버 상태 (1: 재직중, 0: 휴직중, -1: 퇴직)
 * @returns {string} Vuetify 색상 클래스
 */
export const getStatusColor = (status) => {
    switch (status) {
        case 1: return 'success'
        case 0: return 'warning'
        case -1: return 'error'
        default: return 'grey'
    }
}

/**
 * 멤버 상태에 따른 텍스트 반환
 * @param {number} status - 멤버 상태
 * @returns {string} 상태 텍스트
 */
export const getStatusText = (status) => {
    switch (status) {
        case 1: return '재직중'
        case 0: return '휴직중'
        case -1: return '퇴직'
        default: return '알 수 없음'
    }
}

/**
 * 날짜를 한국어 형식으로 포맷팅
 * @param {string|Date} date - 포맷팅할 날짜
 * @returns {string} 포맷팅된 날짜 문자열
 */
export const formatHireDate = (date) => {
    if (!date) return ''
    return new Date(date).toLocaleDateString('ko-KR')
}

/**
 * 사원번호 유효성 검사
 * @param {string} employeeNumber - 사원번호
 * @returns {boolean} 유효성 여부
 */
export const isValidEmployeeNumber = (employeeNumber) => {
    return employeeNumber && employeeNumber.trim().length > 0
}

/**
 * 연락처 포맷팅 (하이픈 추가)
 * @param {string} phone - 연락처 (숫자만)
 * @returns {string} 포맷팅된 연락처
 */
export const formatPhoneNumber = (phone) => {
    if (!phone) return ''
    const cleaned = phone.replace(/\D/g, '')
    if (cleaned.length === 11) {
        return `${cleaned.slice(0, 3)}-${cleaned.slice(3, 7)}-${cleaned.slice(7)}`
    }
    return phone
}

/**
 * 이메일 도메인 추출
 * @param {string} email - 이메일 주소
 * @returns {string} 도메인 부분
 */
export const getEmailDomain = (email) => {
    if (!email || !email.includes('@')) return ''
    return email.split('@')[1]
}

// 상태 관련 유틸리티 (근태 카테고리와는 별개의 멤버 출근 상태)
export const getStatusClass = (status) => {
    switch (status) {
        case 1: return 'status-present'     // 출근
        case 0: return 'status-absent'      // 미출근
        case 2: return 'status-weekend'     // 휴무일
        case -1: return 'status-no-record'  // 기록없음
        default: return 'status-unknown'
    }
}

export const getStatusLabel = (status) => {
    switch (status) {
        case 1: return '출근'               // 출근
        case 0: return '미출근'             // 미출근
        case 2: return '휴무일'             // 휴무일 (주말)
        case -1: return '기록없음'          // 기록없음
        default: return '알 수 없음'
    }
}

// 날짜 포맷팅
export const formatDate = (dateString) => {
    if (!dateString) return '-'
    return dayjs(dateString).format('YYYY-MM-DD')
}

// 테이블 헤더 정의
export const TABLE_HEADERS = [
    { title: '이름', key: 'name', sortable: true, width: '200px' },
    { title: '사번', key: 'employeeNumber', sortable: true, width: '120px' },
    { title: '이메일', key: 'email', sortable: true, width: '200px' },
    { title: '연락처', key: 'phone', sortable: true, width: '150px' },
    { title: '부서', key: 'departmentName', sortable: true, width: '150px' },
    { title: '상태', key: 'status', sortable: true, width: '100px' },
    { title: '입사일시', key: 'hireAt', sortable: true, width: '120px' }
]

// 상태 옵션 (멤버 출근 상태)
export const STATUS_OPTIONS = [
    { title: '전체', value: '전체' },
    { title: '출근', value: 1 },
    { title: '미출근', value: 0 },
    { title: '휴무일', value: 2 },
    { title: '기록없음', value: -1 }
] 