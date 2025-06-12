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
    }
]; 