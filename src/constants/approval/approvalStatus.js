export const APPROVAL_STATUS_MAP = {
    CANCELED: { code: -2, label: '결재 취소' },
    REJECTED: { code: -1, label: '반려' },
    IN_PROGRESS: { code: 0, label: '결재 진행중' },
    APPROVED: { code: 1, label: '결재 완료' }
};

export const getApprovalStatusLabel = (status) => {
    // status는 code(-2, -1, 0, 1) 또는 문자열 키일 수 있음
    if (typeof status === 'number') {
        const found = Object.values(APPROVAL_STATUS_MAP).find(s => s.code === status);
        return found ? found.label : '알 수 없음';
    }
    if (typeof status === 'string' && APPROVAL_STATUS_MAP[status]) {
        return APPROVAL_STATUS_MAP[status].label;
    }
    return '알 수 없음';
};
