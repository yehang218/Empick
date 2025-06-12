export const employmentRoutes = [
    {
        path: '/employment/recruitment',
        name: 'RecruitmentPage',
        component: () => import('@/views/employment/RecruitmentPage.vue'),
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/employment/jobtests',
        name: 'EmploymentJobtests',
        component: () => import('@/views/test/EvaluationTestPage.vue'),
        props: true,
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/employment/jobtests/questions',
        name: 'JobtestQuestionList',
        component: () => import('@/views/employment/JobtestQuestionListPage.vue'),
        meta: {
            requiresAuth: true
        }
    },
    // 채용 공고 관련
    {
        path: '/employment/recruitments/requests',
        name: 'RecruitmentRequestList',
        component: () => import('@/views/employment/RecruitmentRequestListPage.vue'),
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/employment/recruitments/requests/:id',
        name: 'RecruitmentRequestDetail',
        component: () => import('@/views/employment/RecruitmentRequestDetailPage.vue'),
        props: true,
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/employment/recruitments/requests',
        name: 'RecruitmentRequestList',
        component: () => import('@/views/employment/RecruitmentRequestListPage.vue'),
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/employment/recruitments/requests/create',
        name: 'RecruitmentRequestCreate',
        component: () => import('@/views/employment/RecruitmentRequestCreateView.vue'),
        meta: { requiresAuth: true }
    }
]; 