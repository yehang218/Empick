import { createRouter, createWebHistory } from 'vue-router';

const routes = [
    {
        path: '/employment/jobtests',
        name: 'EmploymentJobtests',
        component: () => import('../views/employment/JobtestPage.vue'),
        props: true
    },
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
        name: 'MJTestPage',
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
        path: '/',
        name: 'MainPage',
        component: () => import('@/views/MainPage.vue')
    },
    {
        path: '/counter',
        name: 'CounterPage',
        component: () => import('@/views/CounterPage.vue')
    },
    {
        path: '/employment/jobtests',
        name: 'EmploymentJobtests',
        component: () => import('@/views/test/JobTestPage.vue'),
        props: true
    },
    {
        path: '/employment/recruitment',
        name: 'RecruitmentPage',
        component: () => import('@/views/employment/RecruitmentPage.vue')
    },
    {
        path: '/WSPage',
        name: 'WSPage',
        component: () => import('@/views/WSPage.vue')
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;