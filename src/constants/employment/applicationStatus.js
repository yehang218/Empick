// 지원서 상태 관리
export const APPLICATION_STATUS = {
  WAITING: { code: 0, label: '서류검토대기중', color: 'orange' },
  PASSED_DOCS: { code: 1, label: '서류합격', color: 'blue' },
  PASSED_PRACTICAL: { code: 2, label: '실무합격', color: 'purple' },
  PASSED_INTERVIEW: { code: 3, label: '면접합격', color: 'teal' },
  PASSED_FINAL: { code: 4, label: '최종합격', color: 'green' },
  REJECTED: { code: 5, label: '불합격', color: 'red' }
}

// 코드로 상태 정보 조회
export const getStatusByCode = (code) => {
  const status = Object.values(APPLICATION_STATUS).find(s => s.code === code)
  return status || { code, label: '알 수 없음', color: 'grey' }
}

// 라벨로 상태 정보 조회
export const getStatusByLabel = (label) => {
  const status = Object.values(APPLICATION_STATUS).find(s => s.label === label)
  return status || null
}

// 기존 문자열 상태를 숫자 코드로 변환
export const getCodeByStringStatus = (stringStatus) => {
  const statusMap = {
    'WAITING': 0,
    'PASSED_DOCS': 1,
    'PASSED_PRACTICAL': 2,
    'PASSED_INTERVIEW': 3,
    'PASSED_INTERVIEW_1': 3, // 1차면접 합격도 면접합격으로 매핑
    'PASSED_INTERVIEW_2': 3, // 2차면접 합격도 면접합격으로 매핑
    'PASSED_FINAL': 4,
    'REJECTED': 5,
    'FAILED': 5 // FAILED도 불합격으로 매핑
  }
  
  return statusMap[stringStatus] !== undefined ? statusMap[stringStatus] : 0
}

// 문자열 상태를 숫자 코드와 라벨 정보로 변환
export const getStatusInfoByString = (stringStatus) => {
  const code = getCodeByStringStatus(stringStatus)
  return getStatusByCode(code)
}

// 모든 상태 목록 (선택 옵션용)
export const STATUS_OPTIONS = Object.values(APPLICATION_STATUS)

// 다음 단계 상태 조회
export const getNextStatus = (currentCode) => {
  const nextCode = currentCode + 1
  return Object.values(APPLICATION_STATUS).find(s => s.code === nextCode)
}

// 이전 단계 상태 조회  
export const getPrevStatus = (currentCode) => {
  const prevCode = currentCode - 1
  return Object.values(APPLICATION_STATUS).find(s => s.code === prevCode)
} 