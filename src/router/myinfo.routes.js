export const myinfoRoutes = [
    {
        path: '/myinfo/profile',
        name: 'MyInfoProfilePage',
        component: () => import('@/views/orgstructure/ProfilePage.vue'),
        meta: {
            requiresAuth: true,
            requiredRoles: ['ROLE_USER', 'ROLE_HR_ACCESS']
        }
    }
]; 