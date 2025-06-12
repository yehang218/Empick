export const authRoutes = [
    {
        path: '/login',
        name: 'LoginPage',
        component: () => import('@/views/login/LoginPage.vue'),
        meta: {
            hideSidebar: true
        }
    }
]; 