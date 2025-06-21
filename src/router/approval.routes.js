export const approvalRoutes = [
    {
        path: '/approval/my-documents',
        name: 'MyApprovalDocumentsPage',
        component: () => import('@/views/approval/MyApprovalDocumentsPage.vue'),
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_USER', 'ROLE_HR_ACCESS', 'ROLE_APPROVAL_PROCESSOR'],
            menu: {
                title: '내가 작성한 결재 문서',
                icon: 'mdi-file-document-outline',
                group: '결재',
                subGroup: '결재 문서'
            }
        }
    },
    // 받은 결재 목록 페이지 (인사팀, 팀장급만)
    {
        path: '/approval/inbox',
        name: 'ApprovalInboxList',
        component: () => import('@/views/approval/ApprovalInboxPage.vue'),
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_HR_ACCESS', 'ROLE_APPROVAL_PROCESSOR']
        }
    },

    // 요청한 결재 목록 페이지
    {
        path: '/approval/sent',
        name: 'ApprovalSentList',
        component: () => import('@/views/approval/ApprovalSentPage.vue'),
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_USER', 'ROLE_HR_ACCESS', 'ROLE_APPROVAL_PROCESSOR']
        }
    },

    // 결재 문서 작성 페이지
    {
        path: '/approval/create',
        name: 'ApprovalCreate',
        component: () => import('@/views/approval/ApprovalCreatePage.vue'),
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_USER', 'ROLE_HR_ACCESS', 'ROLE_APPROVAL_PROCESSOR']
        }
    },

    // 결재 상세 조회 페이지
    {
        path: '/approval/:id',
        name: 'ApprovalDetail',
        component: () => import('@/views/approval/ApprovalDetailPage.vue'),
        props: true,
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_USER', 'ROLE_HR_ACCESS', 'ROLE_APPROVAL_PROCESSOR']
        }
    }

];
