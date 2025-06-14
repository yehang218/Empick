export const RecruitmentAPI = {
    // 채용 요청서
    REQUEST_LIST: '/api/v1/employment/recruitments/requests',
    REQUEST_DETAIL: (id) => `/api/v1/employment/recruitments/requests/${id}`,
    REQUEST_CREATE: '/api/v1/employment/recruitments/requests'
};