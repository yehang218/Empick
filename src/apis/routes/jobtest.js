export const JobtestAPI = {
    // 문제
    QUESTIONS: '/api/v1/employment/questions',
    QUESTION_DETAIL: (id) => `/api/v1/employment/questions/${id}`,

    // 문제 선택지
    QUESTION_OPTIONS: '/api/v1/employment/question-options',
    QUESTION_OPTION_DETAIL: (id) => `/api/v1/employment/question-options/${id}`,

    // 실무테스트
    JOBTESTS: '/api/v1/employment/jobtests',
    JOBTEST_DETAIL: (jobtestId) => `/api/v1/employment/jobtests/${jobtestId}`,

    // 실무테스트별 문제
    JOBTEST_QUESTIONS: '/api/v1/employment/jobtest-questions',
    JOBTEST_QUESTION_DETAIL: (id) => `/api/v1/employment/jobtest-questions/${id}`,

    // 채점 기준
    GRADING_CRITERIA: '/api/v1/employment/grading-criteria',
    GRADING_CRITERIA_DETAIL: (id) => `/api/v1/employment/grading-criteria/${id}`,

    // 채점 결과
    GRADING_RESULTS: '/api/v1/employment/grading-results',
    GRADING_RESULT_DETAIL: (id) => `/api/v1/employment/grading-results/${id}`,

    // 평가 기준
    EVALUATION_CRITERIA: '/api/v1/employment/evaluation-criteria',
    EVALUATION_CRITERIA_DETAIL: (id) => `/api/v1/employment/evaluation-criteria/${id}`,
    EVALUATION_CRITERIA_BY_JOBTEST: (jobtestId) =>
        `/api/v1/employment/jobtests/${jobtestId}/evaluation-criteria`,

    // 평가 결과
    EVALUATION_RESULTS: '/api/v1/employment/evaluation-results',
    EVALUATION_RESULT_DETAIL: (id) => `/api/v1/employment/evaluation-results/${id}`,
    EVALUATION_RESULTS_BY_APPLICATION: (applicationId) =>
        `/api/v1/employment/applications/${applicationId}/evaluation-results`,

    // 지원서별 실무테스트
    APPLICATION_JOBTESTS: '/api/v1/employment/application-jobtests',
    APPLICATION_JOBTEST_DETAIL: (id) => `/api/v1/employment/application-jobtests/${id}`,
    APPLICATION_JOBTEST_BY_APPLICATION: (applicationId) =>
        `/api/v1/employment/application-jobtests/application/${applicationId}`,

    // 답변
    ANSWERS: '/api/v1/employment/answers',
    ANSWER_DETAIL: (id) => `/api/v1/employment/answers/${id}`,
    GRADE_ANSWER: (applicationJobtestId) =>
        `/api/v1/employment/answers/${applicationJobtestId}/grade`,

    // 실무테스트 응시
    JOBTEST_ENTER: (jobtestId) => `/api/v1/employment/jobtests/exam/enter/${jobtestId}`,
};