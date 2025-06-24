import { useAuthStore } from '@/stores/authStore';
import { throwCustomApiError } from '@/utils/errorHandler';

export const authGuard = async (to, from, next) => {
    const authStore = useAuthStore();
    const requiresAuth = to.matched.some(record => record.meta.requiresAuth);
    const requiredRoles = to.matched.flatMap(record => record.meta.requiredRoles || []);
    const isAuthenticated = authStore.isAuthenticated;
    const userRoles = authStore.userInfo?.roles || [];

    // 🔐 인증 체크
    if (requiresAuth && !isAuthenticated) {
        console.warn('로그인이 필요합니다.');
        // 로그아웃 처리 (필요시)
        await authStore.logout(); 
        return next({ name: 'LoginPage' });
    }

    // 🎭 역할 권한 체크
    if (requiredRoles.length > 0 && isAuthenticated) {
        const hasRequiredRole = requiredRoles.some(role => userRoles.includes(role));
        if (!hasRequiredRole) {
            console.warn('접근 권한이 없습니다. 필요 권한:', requiredRoles, '사용자 권한:', userRoles);
            return next({ name: 'Forbidden' });
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