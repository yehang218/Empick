import { useAuthStore } from '@/stores/authStore';

export const authGuard = (to, from, next) => {
    const authStore = useAuthStore();
    const requiresAuth = to.matched.some(record => record.meta.requiresAuth);
    const requiredRoles = to.matched.flatMap(record => record.meta.requiredRoles || []);
    const isAuthenticated = authStore.isAuthenticated;
    const userRoles = authStore.userInfo?.roles || [];

    // 🔐 인증 체크
    if (requiresAuth && !isAuthenticated) {
        next('/login');
        return;
    }

    // 🎭 역할 권한 체크
    if (requiredRoles.length > 0 && isAuthenticated) {
        const hasRequiredRole = requiredRoles.some(role => userRoles.includes(role));
        if (!hasRequiredRole) {
            // 권한 없음 - 메인 페이지로 리다이렉트 또는 403 페이지
            console.warn('접근 권한이 없습니다. 필요 권한:', requiredRoles, '사용자 권한:', userRoles);
            next('/'); // 또는 next('/403') 
            return;
        }
    }

    // 🏠 로그인 페이지 리다이렉트
    if (to.path === '/login' && isAuthenticated) {
        next('/');
        return;
    }

    // ✅ 정상 진행
    next();
}; 