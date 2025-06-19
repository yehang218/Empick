export const employmentRoutes = [

    //    <------------------- 실무테스트 -------------------->
    // 실무테스트 문제 목록 페이지
    {
        path: '/employment/jobtest-questions',
        name: 'JobtestQuestionList',
        component: () => import('@/views/employment/JobtestQuestionListPage.vue'),
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_HR_ACCESS']
        }
    },
    // 실무테스트 문제 등록 페이지
    {
        path: '/employment/jobtest-questions/create',
        name: 'JobtestQuestionCreate',
        component: () => import('@/views/employment/JobtestQuestionFormPage.vue'),
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_HR_ACCESS']
        }
    },
    // 실무테스트 문제 수정 페이지
    {
        path: '/employment/jobtest-questions/:id/edit',
        name: 'JobtestQuestionEdit',
        component: () => import('@/views/employment/JobtestQuestionFormPage.vue'),
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_HR_ACCESS']
        }
    },
    // 실무테스트 목록 페이지
    {
        path: '/employment/jobtests',
        name: 'JobtestList',
        component: () => import('@/views/employment/JobtestListPage.vue'),
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_HR_ACCESS']
        }
    },
    // 실무테스트 상세 조회 페이지
    {
        path: '/employment/jobtests/:jobtestId',
        name: 'JobtestDetail',
        component: () => import('@/views/employment/JobtestDetailPage.vue'),
        props: true,
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_HR_ACCESS']
        }
    },
    // 실무테스트 등록 페이지
    {
        path: '/employment/jobtests/create',
        name: 'JobtestCreate',
        component: () => import('@/views/employment/JobtestCreatePage.vue'),
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_HR_ACCESS']
        }
    },

    // 실무테스트 답안 목록 페이지
    {
        path: '/employment/jobtest-answers',
        name: 'JobtestAnswerList',
        component: () => import('@/views/employment/JobtestAnswerListPage.vue'),
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_HR_ACCESS']
        }
    },
    // 실무테스트 답안 상세 조회 페이지
    {
        path: '/employment/jobtest-answers/:applicationJobtestId',
        name: 'JobtestAnswerDetail',
        component: () => import('@/views/employment/JobtestAnswerDetailPage.vue'),
        props: true,
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_HR_ACCESS']
        }
    },
    // 실무테스트 답안 채점 페이지
    {
        path: '/employment/jobtest-answers/:answerId/grading',
        name: 'JobtestAnswerGrading',
        component: () => import('@/views/employment/JobtestAnswerGradingPage.vue'),
        props: true,
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_HR_ACCESS']
        }
    },
    // 실무테스트 답안 평가 등록 페이지
    {
        path: '/employment/jobtest-answers/:answerId/evaluation',
        name: 'JobtestAnswerEvaluation',
        component: () => import('@/views/employment/JobtestAnswerEvaluationPage.vue'),
        props: true,
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_HR_ACCESS']
        }
    },
    // 실무테스트 입장 페이지 (지원자용이므로 권한 제한 없음)
    {
        path: '/employment/jobtest/enter/:jobtestId',
        name: 'JobtestEnter',
        component: () => import('@/views/employment/JobtestEnterPage.vue'),
        props: true,
        meta: { requiresAuth: false }
    },

    //    <------------------- 채용공고 -------------------->
    // 채용 요청서 목록 페이지
    {
        path: '/employment/recruitment-requests',
        name: 'RecruitmentRequestList',
        component: () => import('@/views/employment/RecruitmentRequestListPage.vue'),
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_HR_ACCESS']
        }
    },
    // 채용 요청서 상세 조회 페이지
    {
        path: '/employment/recruitment-requests/:id',
        name: 'RecruitmentRequestDetail',
        component: () => import('@/views/employment/RecruitmentRequestDetailPage.vue'),
        props: true,
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_HR_ACCESS']
        }
    },
    // 채용 요청서 등록 페이지
    {
        path: '/employment/recruitment-requests/create',
        name: 'RecruitmentRequestCreate',
        component: () => import('@/views/employment/RecruitmentRequestCreatePage.vue'),
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_HR_ACCESS']
        }
    },


    // 채용 공고 목록 페이지
    {
        path: '/employment/recruitments',
        name: 'RecruitmentList',
        component: () => import('@/views/employment/RecruitmentListPage.vue'),
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_HR_ACCESS']
        }
    },
    // 채용 공고 상세 조회 페이지
    {
        path: '/employment/recruitments/:id',
        name: 'RecruitmentDetails',
        component: () => import('@/views/employment/RecruitmentDetailPage.vue'),
        props: true,
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_HR_ACCESS']
        }
    },
    // 채용 공고 등록 페이지
    {
        path: '/employment/recruitments/create',
        name: 'RecruitmentCreate',
        component: () => import('@/views/employment/RecruitmentCreatePage.vue'),
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_HR_ACCESS']
        }
    },
    // 지원서 항목 구성 페이지
    {
        path: '/employment/application-items/select',
        name: 'ApplicationItemSelectPage',
        component: () => import('@/views/employment/ApplicationItemSelectPage.vue'),
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_HR_ACCESS']
        }
    },

    // <---------- 면         접 ---------->

    {
        path: '/employment/interview-criteria',
        name: 'InterviewSheetPage',
        component: () => import('@/views/employment/InterviewSheetPage.vue'),
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_HR_ACCESS']
        }
    },
    {
        path: '/employment/interview-criteria/create',
        name: 'CreateInterviewSheetPage',
        component: () => import('@/views/employment/CreateInterviewSheetPage.vue'),
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_HR_ACCESS']
        }
    },
    {
        path: '/employment/interview-score',
        name: 'InputInterviewScorePage',
        component: () => import('@/views/employment/InputInterviewScorePage.vue'),
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_HR_ACCESS']
        }
    },
    {
        path: '/employment/interviews',
        name: 'InterviewSchedulePage',
        component: () => import('@/views/employment/InterviewSchedulePage.vue'),
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_HR_ACCESS']
        }
    },
    {
        path: '/employment/interviews/detail/:applicationId',
        name: 'InterviewDetailPage',
        component: () => import('@/views/employment/InterviewDetailPage.vue'),
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_HR_ACCESS']
        }
    },
    {
        path: '/employment/interviews/create',
        name: 'CreateInterviewPage',
        component: () => import('@/views/employment/CreateInterviewPage.vue'),
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_HR_ACCESS']
        }
    },

    // <---------- 지원서 / 지원자 ---------->
    {
        path: '/employment/applicant',
        name: 'ApplicantPage',
        component: () => import('@/views/employment/ApplicantPage.vue'),
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_HR_ACCESS']
        }
    },
    // 지원서 상세 조회 페이지 (기존 ApplicationPage를 상세 페이지로 사용)
    {
        path: '/employment/applications/:applicationId',
        name: 'ApplicationDetail',
        component: () => import('@/views/employment/ApplicationPage.vue'),
        props: true,
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_HR_ACCESS']
        }
    },

    // 채용공고별 지원자 목록 페이지
    {
        path: '/employment/applicant/recruitments/:recruitmentId',
        name: 'ApplicantRecruitmentPage',
        component: () => import('@/views/employment/ApplicantRecruitmentPage.vue'),
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_HR_ACCESS']
        }
    },
    // 지원자 인적사항 등록 페이지
    {
        path: '/employment/applicants/register',
        name: 'ApplicantRegistrationPage',
        component: () => import('@/views/employment/ApplicantRegistrationPage.vue'),
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_HR_ACCESS']
        }
    },

    {
        path: '/employment/introduce-standard-items/:templateId',
        name: 'IntroduceStandardItemPage',
        component: () => import('@/views/employment/IntroduceStandardItemPage.vue'),
        props: true,
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_HR_ACCESS']
        }
    },
    {
        path: '/employment/introduce-templates',
        name: 'IntroduceTemplateListPage',
        component: () => import('@/views/employment/IntroduceTemplateListPage.vue'),
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_HR_ACCESS']
        }
    },

    {
        path: '/employment/introduce-templates/create',
        name: 'IntroduceTemplateCreatePage',
        component: () => import('@/views/employment/IntroduceTemplateCreatePage.vue'),
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_HR_ACCESS']
        }
    },
    // 자기소개서
    {
        path: '/employment/introduce-standard-items/:templateId',
        name: 'IntroduceStandardItemPage',
        component: () => import('@/views/employment/IntroduceStandardItemPage.vue'),
            props: true
        },

    {
        path: '/employment/introduce-templates',
        name: 'IntroduceTemplateListPage',
        component: () => import('@/views/employment/IntroduceTemplateListPage.vue'),
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_HR_ACCESS']
        }
    },
    {
        path: '/employment/introduce-templates/create',
        name: 'IntroduceTemplateCreatePage',
        component: () => import('@/views/employment/IntroduceTemplateCreatePage.vue'),
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_HR_ACCESS']
        }
    },
    {
        path: '/employment/introduce-templates/:id',
        name: 'IntroduceTemplateDetailPage',
        component: () => import('@/views/employment/IntroduceTemplateDetailPage.vue'),
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_HR_ACCESS']
        }
    },
    {
        path: '/employment/introduce-standard-items/manage',
        name: 'IntroduceStandardItemCreatePage',
        component: () => import('@/views/employment/IntroduceStandardItemCreatePage.vue'),
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_HR_ACCESS']
        }
    },
    {
        path: '/employment/introduce-standard/create',
        name: 'IntroduceStandardCreatePage',
        component: () => import('@/views/employment/IntroduceStandardCreatePage.vue'),
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_HR_ACCESS']
        }
    },
    {
        path: '/employment/introduce-standard/list',
        name: 'IntroduceStandardListPage',
        component: () => import('@/views/employment/IntroduceStandardListPage.vue'),
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_HR_ACCESS']
        }
    },
    {
        path: '/employment/introduce-items/manage',
        name: 'IntroduceTemplateItemManagePage',
        component: () => import('@/views/employment/IntroduceTemplateItemManagePage.vue'),
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_HR_ACCESS']
        }
    },
    {
        path: '/employment/introduce-standard/:id',
        name: 'IntroduceStandardDetail',
        component: () => import('@/views/employment/IntroduceStandardDetailPage.vue'),
        meta: { requiresAuth: true }
    }

];
