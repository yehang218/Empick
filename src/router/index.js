import { createRouter, createWebHistory } from 'vue-router';
import { useAuthStore } from '@/stores/authStore';

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
        path: '/login',
        name: 'LoginPage',
        component: () => import('@/views/login/LoginPage.vue'),
        meta: {
            hideSidebar: true
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
    {
        path: '/employment/jobtests',
        name: 'EmploymentJobtests',
        component: () => import('@/views/test/EvaluationTestPage.vue'),
        props: true
    },
    // 신규 사원 등록 페이지
    {
        path: '/orgstructure/member-register',
        name: 'MemberRegisterPage',
        component: () => import('@/views/orgstructure/MemberRegisterPage.vue'),
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/orgstructure/profile',
        name: 'ProfilePage',
        component: () => import('@/views/orgstructure/ProfilePage.vue'),
        meta: {
            requiresAuth: true
        }
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

// 네비게이션 가드
router.beforeEach((to, from, next) => {
    const authStore = useAuthStore();
    const requiresAuth = to.matched.some(record => record.meta.requiresAuth);
    const isAuthenticated = authStore.isAuthenticated;

    if (requiresAuth && !isAuthenticated) {
        // 인증이 필요한 페이지인데 로그인하지 않은 경우
        next('/login');
    } else if (to.path === '/login' && isAuthenticated) {
        // 로그인 페이지인데 이미 로그인한 경우
        next('/');
    } else {
        next();
    }
});

export default router;