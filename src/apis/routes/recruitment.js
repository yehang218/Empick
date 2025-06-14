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
};