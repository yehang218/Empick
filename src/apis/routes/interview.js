export const InterviewAPI = {

    // interview
    // 🔹 생성/수정/삭제 (Command)
    CREATE_INTERVIEW: '/api/v1/employment/interview',
    UPDATE_INTERVIEW: (id) => `/api/v1/employment/interview/${id}`,
    UPDATE_DATETIME: (id, datetime) =>
        `/api/v1/employment/interview/${id}/datetime?datetime=${encodeURIComponent(datetime)}`,
    UPDATE_ADDRESS: (id, address) =>
        `/api/v1/employment/interview/${id}/address?address=${encodeURIComponent(address)}`,
    DELETE_INTERVIEW: (id) => `/api/v1/employment/interview/${id}`,

    // 🔹 조회 (Query)
    GET_ALL_INTERVIEWS: '/api/v1/employment/interview',
    GET_INTERVIEW_BY_ID: (id) => `/api/v1/employment/interview/${id}`,
    GET_INTERVIEWS_BY_DATE: (date) =>
        `/api/v1/employment/interview/date?date=${encodeURIComponent(date)}`,
    CHECK_AVAILABLE_DATETIME: (datetime) =>
        `/api/v1/employment/interview/checkAvailable?datetime=${encodeURIComponent(datetime)}`,


    // interview_criteria
    // 🔹 생성/수정/삭제 (Command)
    CREATE_CRITERIA: '/api/v1/employment/interviewCriteria',
    UPDATE_CRITERIA: (id) => `/api/v1/employment/interviewCriteria/${id}`,
    DELETE_CRITERIA: (id) => `/api/v1/employment/interviewCriteria/${id}`,

    // 🔹 조회 (Query)
    GET_ALL_CRITERIA: '/api/v1/employment/interviewCriteria',
    GET_CRITERIA_BY_ID: (id) => `/api/v1/employment/interviewCriteria/${id}`,
    SEARCH_CRITERIA_BY_CONTENT: (content) => `/api/v1/employment/interviewCriteria/content?content=${encodeURIComponent(content)}`,

    
    // interviewer
    // 🔹 생성/수정/삭제 (Command)
    CREATE_INTERVIEWER: '/api/v1/employment/interviewer',
    UPDATE_INTERVIEWER_SCORE: (id) => `/api/v1/employment/interviewer/${id}/score`,
    UPDATE_INTERVIEWER_REVIEW: (id, review) => `/api/v1/employment/interviewer/${id}/review?review=${encodeURIComponent(review)}`,
    DELETE_INTERVIEWER: (id) => `/api/v1/employment/interviewer/${id}`,
    // 🔹 조회 (Query)
    GET_ALL_INTERVIEWERS: '/api/v1/employment/interviewer',
    GET_INTERVIEWER_BY_ID: (id) => `/api/v1/employment/interviewer/${id}`,
    GET_INTERVIEWERS_BY_INTERVIEW_ID: (interviewId) => `/api/v1/employment/interviewer/interview/${interviewId}`,


    // interview_sheet
    // 🔹 생성/수정/삭제 (Command)
    CREATE_SHEET: '/api/v1/employment/interviewSheet',
    UPDATE_SHEET: (id) => `/api/v1/employment/interviewSheet/${id}`,
    DELETE_SHEET: (id) => `/api/v1/employment/interviewSheet/${id}`,

    // 🔹 조회 (Query)
    GET_ALL_SHEETS: '/api/v1/employment/interviewSheet',
    GET_SHEET_BY_ID: (id) => `/api/v1/employment/interviewSheet/${id}`,
    SEARCH_SHEET_BY_NAME: (name) => `/api/v1/employment/interviewSheet/name?name=${encodeURIComponent(name)}`,


    // interview_sheet_item
    // 🔹 생성/수정/삭제 (Command)
    CREATE_SHEET_ITEM: '/api/v1/employment/interviewSheetItem',
    DELETE_SHEET_ITEM: (id) => `/api/v1/employment/interviewSheetItem/${id}`,

    // 🔹 조회 (Query)
    FIND_ALL_SHEET_ITEM: '/api/v1/employment/interviewSheetItem',
    FIND_SHEET_ITEM_BY_ID: (id) => `/api/v1/employment/interviewSheetItem/${id}`,
    FIND_SHEET_ITEM_BY_SHEET_ID: (sheetId) => `/api/v1/employment/interviewSheetItem/sheet/${sheetId}`,
    FIND_SHEET_ITEM_BY_CRITERIA_ID: (criteriaId) => `/api/v1/employment/interviewSheetItem/criteria/${criteriaId}`,


    // interview_score
    // 🔹 생성/수정/삭제 (Command)
    CREATE_INTERVIEW_SCORE: '/api/v1/employment/interviewScore',
    UPDATE_INTERVIEW_SCORE: (id) => `/api/v1/employment/interviewScore/${id}`,
    DELETE_INTERVIEW_SCORE: (id) => `/api/v1/employment/interviewScore/${id}`,

    // 🔹 조회 (Query)
    FIND_ALL_INTERVIEW_SCORE: '/api/v1/employment/interviewScore',
    FIND_INTERVIEW_SCORE_BY_ID: (id) => `/api/v1/employment/interviewScore/${id}`,
    FIND_INTERVIEW_SCORE_BY_INTERVIEWER_ID: (interviewerId) => `/api/v1/employment/interviewScore/interview/${interviewerId}`,
};
