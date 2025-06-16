export const approvalRoutes = [

    // 받은 결재 목록 페이지
    {
        path: '/approval/inbox',
        name: 'ApprovalInboxList',
        component: () => import('@/views/approval/ApprovalInboxPage.vue'),
        meta: { requiresAuth: true }
    },

    // 요청한 결재 목록 페이지
    {
        path: '/approval/sent',
        name: 'ApprovalSentList',
        component: () => import('@/views/approval/ApprovalSentPage.vue'),
        meta: { requiresAuth: true }
    },

    // 결재 문서 작성 페이지
    {
        path: '/approval/create',
        name: 'ApprovalCreate',
        component: () => import('@/views/approval/ApprovalCreatePage.vue'),
        meta: { requiresAuth: true }
    },

    // 결재 상세 조회 페이지
    {
        path: '/approval/:id',
        name: 'ApprovalDetail',
        component: () => import('@/views/approval/ApprovalDetailPage.vue'),
        props: true,
        meta: { requiresAuth: true }
    }

];
