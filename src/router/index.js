import { createRouter, createWebHashHistory, createWebHistory } from 'vue-router';

const routes = [
    // TODO: 이 형식으로 라우터 추가
    // {
    //     // 상담 일지 페이지
    //     path: '/counsel/:counselId',
    //     name: 'CounselingReport',
    //     component: () => import('../views/CounselingReport.vue'),
    //     props: true
    // },
];

const router = createRouter({
    history: createWebHashHistory(),
    routes
});

export default router;