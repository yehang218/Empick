import { defineStore } from 'pinia';
import { ref } from 'vue';
import { loginService } from '@/services/authService';

export const useAuthStore = defineStore('auth', () => {
    // 상태 정의
    const isAuthenticated = ref(false);
    const accessToken = ref('');
    const refreshToken = ref('');
    const userInfo = ref(null);
    const error = ref(null);
    const loading = ref(false);

    // 로그인 액션
    const login = async (loginRequest) => {
        loading.value = true;
        error.value = null;

        try {
            const response = await loginService(loginRequest);

            accessToken.value = response.accessToken;
            refreshToken.value = response.refreshToken;
            isAuthenticated.value = true;

            if (response.user) {
                userInfo.value = response.user;
            }
        } catch (err) {
            error.value = err?.response?.data?.message || '로그인 실패';
            isAuthenticated.value = false;
            accessToken.value = '';
            refreshToken.value = '';
            userInfo.value = null;
        } finally {
            loading.value = false;
        }
    };

    // 로그아웃 액션
    const logout = () => {
        isAuthenticated.value = false;
        accessToken.value = '';
        refreshToken.value = '';
        userInfo.value = null;
    };

    const getAuthHeaders = () => {
        return {
            Authorization: `Bearer ${accessToken.value}`,
        };
    };

    return {
        isAuthenticated,
        accessToken,
        refreshToken,
        userInfo,
        error,
        loading,
        login,
        logout,
        getAuthHeaders,
    };
});
