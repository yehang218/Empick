import { createRouter, createWebHistory } from 'vue-router';

const routes = [
    {
        path: '/test/mj',
        name: 'MJTestPage',
        component: () => import('../views/test/MJTestMainPage.vue'),
        props: true
    },
    {
        path: '/test/mj/evaluationScore',
        name: 'MJEvaluationScoreTestPage',
        component: () => import('../views/test/MJEvaluationScoreTestPage.vue'),
        props: true
    },
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
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;