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
        path: '/employment/jobtests/problems',
        name: 'JobtestProblemList',
        component: () => import('@/views/employment/JobtestProblemListPage.vue'),
        meta: {
            requiresAuth: true
        }
    }
]; 