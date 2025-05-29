import { createRouter, createWebHistory } from 'vue-router';

const routes = [
    // TODO: 이 형식으로 라우터 추가
    // {
    //     // 상담 일지 페이지
    //     path: '/counsel/:counselId',
    //     name: 'CounselingReport',
    //     component: () => import('../views/CounselingReport.vue'),
    //     props: true
    // },
    {
        path: '/test/mj',
        name: 'MJTestPage',
        component: () => import('../views/test/MJTestPage.vue'),
        props: true
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;