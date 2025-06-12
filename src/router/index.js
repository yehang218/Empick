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

    // <---------- 면         접 ---------->

    {
        path: '/employment/interview-criteria',
        name: 'InterviewSheetPage',
        component: () => import('@/views/employment/InterviewSheetPage.vue'),
        meta: {
            requiresAuth: true
        }
    },

    // <---------- 안내      메일 ---------->
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