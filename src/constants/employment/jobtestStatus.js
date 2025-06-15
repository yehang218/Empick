export const STATUS_LABEL_MAP = {
  'WAITING': '대기중',
  'IN_PROGRESS': '진행 중',
  'COMPLETED': '완료',
};

/**
 * 상태 코드 한글화
 * @param {keyof typeof STATUS_LABEL_MAP} status - 상태 코드
 * @returns {string} 한글화된 상태
 */
export const getStatusLabel = (status) => {
  return STATUS_LABEL_MAP[status] || status;
};