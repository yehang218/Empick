export const employmentRoutes = [

    //    <------------------- 실무테스트 -------------------->
    // 실무테스트 문제 목록 페이지
    {
        path: '/employment/jobtest-questions',
        name: 'JobtestQuestionList',
        component: () => import('@/views/employment/JobtestQuestionListPage.vue'),
        meta: {
            requiresAuth: true
        }
    },
    // 실무테스트 문제 등록 페이지
    {
        path: '/employment/jobtest-questions/create',
        name: 'JobtestQuestionCreate',
        component: () => import('@/views/employment/JobtestQuestionCreatePage.vue'),
        meta: {
            requiresAuth: true
        }
    },
    // 실무테스트 목록 페이지
    {
        path: '/employment/jobtests',
        name: 'JobtestList',
        component: () => import('@/views/employment/JobtestListPage.vue'),
        meta: { requiresAuth: true }
    },
    // 실무테스트 상세 조회 페이지
    {
        path: '/employment/jobtests/:id',
        name: 'JobtestDetail',
        component: () => import('@/views/employment/JobtestDetailPage.vue'),
        props: true,
        meta: { requiresAuth: true }
    },
    // 실무테스트 등록 페이지
    {
        path: '/employment/jobtests/create',
        name: 'JobtestCreate',
        component: () => import('@/views/employment/JobtestCreatePage.vue'),
        meta: { requiresAuth: true }
    },

    // 실무테스트 답안 목록 페이지
    {
        path: '/employment/jobtest-answers',
        name: 'JobtestAnswerList',
        component: () => import('@/views/employment/JobtestAnswerListPage.vue'),
        meta: { requiresAuth: true }
    },
    // 실무테스트 답안 상세 조회 페이지
    {
        path: '/employment/jobtest-answers/:applicationJobtestId',
        name: 'JobtestAnswerDetail',
        component: () => import('@/views/employment/JobtestAnswerDetailPage.vue'),
        props: true,
        meta: { requiresAuth: true }
    },
    // 실무테스트 답안 채점 페이지
    {
        path: '/employment/jobtest-answers/:answerId/grading',
        name: 'JobtestAnswerGrading',
        component: () => import('@/views/employment/JobtestAnswerGradingPage.vue'),
        props: true,
        meta: { requiresAuth: true }
    },
    // 실무테스트 답안 평가 등록 페이지
    {
        path: '/employment/jobtest-answers/:answerId/evaluation',
        name: 'JobtestAnswerEvaluation',
        component: () => import('@/views/employment/JobtestAnswerEvaluationPage.vue'),
        props: true,
        meta: { requiresAuth: true }
    },

    //    <------------------- 채용공고 -------------------->
    // 채용 요청서 목록 페이지
    {
        path: '/employment/recruitment-requests',
        name: 'RecruitmentRequestList',
        component: () => import('@/views/employment/RecruitmentRequestListPage.vue'),
        meta: {
            requiresAuth: true
        }
    },
    // 채용 요청서 상세 조회 페이지
    {
        path: '/employment/recruitment-requests/:id',
        name: 'RecruitmentRequestDetail',
        component: () => import('@/views/employment/RecruitmentRequestDetailPage.vue'),
        props: true,
        meta: {
            requiresAuth: true
        }
    },
    // 채용 요청서 등록 페이지
    {
        path: '/employment/recruitment-requests/create',
        name: 'RecruitmentRequestCreate',
        component: () => import('@/views/employment/RecruitmentRequestCreateView.vue'),
        meta: { 
            requiresAuth: true 
        }
    },
    
    // 채용 공고 목록 페이지
    {
        path: '/employment/recruitments',
        name: 'RecruitmentList',
        component: () => import('@/views/employment/RecruitmentListPage.vue'),
        meta: {
            requiresAuth: true
        }
    },
    // 채용 공고 상세 조회 페이지
    {
        path: '/employment/recruitments/:id',
        name: 'RecruitmentDetail',
        component: () => import('@/views/employment/RecruitmentDetailPage.vue'),
        props: true,
        meta: {
            requiresAuth: true
        }
    },
    // 채용 공고 등록 페이지
    {
        path: '/employment/recruitments/create',
        name: 'RecruitmentCreate',
        component: () => import('@/views/employment/RecruitmentCreatePage.vue'),
        meta: { 
            requiresAuth: true 
        }
    },
    // 지원서 항목 구성 페이지
    {
        path: '/employment/application-items/select',
        name: 'ApplicationItemSelectPage',
        component: () => import('@/views/employment/ApplicationItemSelectPage.vue'),
        meta: { requiresAuth: true }
    }
]; 