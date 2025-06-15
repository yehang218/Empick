export const approvalRoutes = [
    {
        path: '/approval/my-documents',
        name: 'MyApprovalDocumentsPage',
        component: () => import('@/views/approval/MyApprovalDocumentsPage.vue'),
        meta: {
            requiresAuth: true,
            menu: {
                title: '내가 작성한 결재 문서',
                icon: 'mdi-file-document-outline',
                group: '결재',
                subGroup: '결재 문서'
            }
        }
    },
]; 