export const RecruitmentAPI = {
    // 채용 요청서
    REQUEST_LIST: '/api/v1/employment/recruitments/requests',
    REQUEST_DETAIL: (id) => `/api/v1/employment/recruitments/requests/${id}`,
    REQUEST_CREATE: '/api/v1/employment/recruitments/requests',

    // 채용 공고
    RECRUITMENT_LIST: '/api/v1/employment/recruitments',
    RECRUITMENT_DETAIL: (id) => `/api/v1/employment/recruitments/${id}`,
    RECRUITMENT_CREATE: '/api/v1/employment/recruitments',
    RECRUITMENT_UPDATE: (id) => `/api/v1/employment/recruitments/${id}`,
    RECRUITMENT_DELETE: (id) => `/api/v1/employment/recruitments/${id}`,

    // 채용 프로세스
    PROCESS_LIST: (recruitmentId) => `/api/v1/employment/recruitments/processes/${recruitmentId}`,

    // 지원서 항목 카테고리
    APPLICATION_ITEM_CATEGORIES: '/api/v1/employment/applications/item-categories',
    APPLICATION_ITEMS_BY_RECRUITMENT: (id) => `/api/v1/employment/applications/items/${id}`,

};