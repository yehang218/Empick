import { createRouter, createWebHistory } from 'vue-router';

const routes = [
    {
        path: '/test/list',
        name: 'ListView',
        component: () => import('@/components/common/ListView.vue')
    },
    {
        path: '/',
        name: 'MainPage',
        component: () => import('@/views/MainPage.vue')
    },
    {
        path: '/counter',
        name: 'CounterPage',
        component: () => import('@/views/CounterPage.vue')
    },
    // 테스트용 채용 페이지
    {
        path: '/employment/recruitment',
        name: 'RecruitmentPage',
        component: () => import('@/views/employment/RecruitmentPage.vue')
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;