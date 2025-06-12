import { createRouter, createWebHistory } from 'vue-router';
import { authRoutes } from './auth.routes';
import { employmentRoutes } from './employment.routes';
import { orgstructureRoutes } from './orgstructure.routes';
import { testRoutes } from './test.routes';
import { authGuard } from './middleware/auth.guard';

const routes = [
    {
        path: '/',
        name: 'MainPage',
        component: () => import('@/views/MainPage.vue'),
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/dashboard',
        name: 'DashboardPage',
        component: () => import('@/views/DashboardPage.vue'),
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/test/list-view-test-page',
        name: 'ListViewTestPage',
        component: () => import('@/views/test/ListViewTestPage.vue')
    },
    {
        path: '/employment/mail',
        name: 'MailPage',
        component: () => import('@/views/employment/MailPage.vue')
    },
    // 서민종 - 컴포넌트 테스트 페이지용 주소
    {
        path: '/test/mj',
        name: 'MJTestMainPage',
        component: () => import('../views/test/MJTestMainPage.vue'),
        props: true
    },
    {
        path: '/test/mj/calendar',
        name: 'MJCalendarTestPage',
        component: () => import('../views/test/MJCalendarTestPage.vue'),
        props: true
    },
    {
        path: '/test/mj/evaluationCriteriaList',
        name: 'MJEvaluationCriteriaListTestPage',
        component: () => import('../views/test/MJEvaluationCriteriaListTestPage.vue'),
        props: true
    },
    {
        path: '/test/mj/evaluationScore',
        name: 'MJEvaluationScoreTestPage',
        component: () => import('../views/test/MJEvaluationScoreTestPage.vue'),
    },
    {
        path: '/test/list',
        name: 'ListView',
        component: () => import('@/components/common/ListView.vue')
    },
    {
        path: '/test/mj/mailReceiverList',
        name: 'MJMailReceiverListTestPage',
        component: () => import('../views/test/MJMailReceiverListTestPage.vue'),
    },
    {
        path: '/test/mj/messageInputBox',
        name: 'MJMessageInputBoxTestPage',
        component: () => import('../views/test/MJMessageInputBoxTestPage.vue'),
    },
    {
        path: '/test/mj/oneColumnList',
        name: 'MJOneColumnListTestPage',
        component: () => import('../views/test/MJOneColumnListTestPage.vue'),
    },
    // {

    //     path: '/counter',
    //     name: 'CounterPage',
    //     component: () => import('@/views/test/CounterPage.vue')
    // },

    {
        path: '/employment/recruitment',
        name: 'RecruitmentPage',
        component: () => import('@/views/employment/RecruitmentPage.vue')
    },
    {
        path: '/WSPage',
        name: 'WSPage',
        component: () => import('@/views/test/WSPage.vue')
    },

    // <-------------------- 실무테스트 -------------------->
    {
        path: '/employment/jobtests',
        name: 'EmploymentJobtests',
        component: () => import('@/views/test/EvaluationTestPage.vue'),
        props: true
    },
    // {
    //     path: '/employment/jobtests/problems',
    //     name: 'JobtestProblemList',
    //     component: () => import('@/views/employment/JobtestProblemListPage.vue'),
    //     meta: {
    //         requiresAuth: true
    //     }
    // },
    ...authRoutes,
    ...employmentRoutes,
    ...orgstructureRoutes,
    ...(process.env.NODE_ENV === 'development' ? testRoutes : [])
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

// 인증 미들웨어 적용
router.beforeEach(authGuard);

export default router;