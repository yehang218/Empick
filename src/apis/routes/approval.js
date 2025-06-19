export const ApprovalAPI = {
    // 결재 유형
    CATEGORIES: '/api/v1/approval/categories',
    CATEGORY_DETAIL: (categoryId) => `/api/v1/approval/categories/${categoryId}`,
    CATEGORY_CHILDREN: (parentId) => `/api/v1/approval/categories/${parentId}/children`,

    // 결재 항목 (유형별 항목)
    CATEGORY_ITEMS: (categoryId) => `/api/v1/approval/categories/${categoryId}/items`,
    CATEGORY_ITEM_DETAIL: (categoryId, itemId) =>
        `/api/v1/approval/categories/${categoryId}/items/${itemId}`,

    // 결재 문서
    DOCUMENTS: '/api/v1/approval/documents',
    DOCUMENT_DETAIL: (approvalId) => `/api/v1/approval/documents/${approvalId}`,

    // 결재 문서의 항목 내용
    DOCUMENT_CONTENTS: (approvalId) => `/api/v1/approval/documents/${approvalId}/contents`,
    DOCUMENT_CONTENT_DETAIL: (approvalId, contentId) =>
        `/api/v1/approval/documents/${approvalId}/contents/${contentId}`,

    // 결재 승인/반려 등 상태 변경
    APPROVE: (approvalId) => `/api/v1/approval/documents/${approvalId}/approve`,
    REJECT: (approvalId) => `/api/v1/approval/documents/${approvalId}/reject`,

    // 작성자 or 결재자 기준 목록 조회
    DOCUMENTS_BY_WRITER: (writerId) => `/api/v1/approval/writer/${writerId}`,
    DOCUMENTS_BY_APPROVER: (approverId) => `/api/v1/approval/documents?approverId=${approverId}`,
    
    // 자신이 결재자인 결재문서 목록 조회
    RECEIVED_DOCUMENTS_LIST: (memberId) => `/api/v1/approval/documents/received?memberId=${memberId}`,
    // 자신이 요청한 결재문서 목록 조회
    REQUESTED_DOCUMENTS_LIST: (memberId) => `/api/v1/approval/documents/requested?memberId=${memberId}`,
};