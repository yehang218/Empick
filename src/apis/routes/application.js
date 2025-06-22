export const ApplicantAPI = {
    // 🔹 조회
    GET_ALL_APPLICANTS: '/api/v1/employment/applicant',
    GET_APPLICANT_BY_ID: (id) =>
        `/api/v1/employment/applicant/${id}`,
    SEARCH_APPLICANTS_BY_NAME: (name) =>
        `/api/v1/employment/applicant/search?name=${encodeURIComponent(name)}`,
    GET_APPLICANTS_BY_RECRUITMENT_ID: (recruitmentId) =>
        `/api/v1/employment/recruitments/${recruitmentId}/applicants`,

    // 🔸 생성
    CREATE_APPLICANT: '/api/v1/employment/applicant/create',
    APPLICANT_LIST: '/api/v1/employment/applicant/applicant-list',
}

export const ApplicantBookmarkAPI = {
    // 🔹 조회
    GET_ALL_BOOKMARKS: '/api/v1/employment/applicant-bookmark',
    GET_BOOKMARKS_BY_MEMBER_ID: (memberId) =>
        `/api/v1/employment/applicant-bookmark/${memberId}`,

    // 🔸 생성
    ADD_BOOKMARK: '/api/v1/employment/applicant-bookmark',

    // 🔸 삭제
    REMOVE_BOOKMARK: (memberId, applicantId) =>
        `/api/v1/employment/applicant-bookmark/${memberId}/${applicantId}`,
};


export const ApplicationAPI = {
    // 🔹 조회
    GET_ALL_APPLICATIONS: '/api/v1/employment/application',
    GET_APPLICATION_BY_ID: (id) =>
        `/api/v1/employment/application/${id}`,
    GET_APPLICATION_BY_APPLICANT_ID: (applicantId) =>
        `/api/v1/employment/application/applicant/${applicantId}`,

    // 🔸 생성
    CREATE_APPLICATION: '/api/v1/employment/application',

    // 🔸 수정 (지원서 상태 변경)
    UPDATE_APPLICATION_STATUS: (id) =>
        `/api/v1/employment/application/${id}`,

    // 🔸 삭제
    DELETE_APPLICATION: (id) =>
        `/api/v1/employment/application/${id}`,

    GET_APPLICATION_BY_RECRUITMENT_ID: (recruitmentId) =>
        `/api/v1/employment/recruitments/${recruitmentId}/applications`,

};

export const ApplicationResponseAPI = {
    // 🔹 조회
    GET_ALL_APPLICATION_RESPONSES: '/api/v1/employment/application-response',
    GET_APPLICATION_RESPONSES_BY_APPLICATION_ID: (applicationId) =>
        `/api/v1/employment/application-response/application/${applicationId}`,

    // 🔸 생성
    CREATE_APPLICATION_RESPONSE: '/api/v1/employment/application-response',
};

export const ApplicationItemAPI = {
    // 🔹 조회
    GET_APPLICATION_ITEM_BY_ID: (id) =>
        `/api/v1/employment/application-item/${id}`,
    GET_ALL_APPLICATION_ITEMS: '/api/v1/employment/application-item',
};