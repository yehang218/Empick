import axios from 'axios';
import { useAuthStore } from '@/stores/authStore';
import { setLoggingOut } from '@/utils/errorHandler';

const api = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL,
    timeout: parseInt(import.meta.env.VITE_TIMEOUT) || 60000, // 파일 업로드를 위해 60초로 증가
    // responseType: 'json', // 기본값은 json, profile-image 등은 개별 요청에서 blob으로 지정
});

// 요청 인터셉터
api.interceptors.request.use(
    config => {
        console.log('API 요청:', {
            url: config.url,
            method: config.method,
            data: config.data,
            headers: config.headers,
            responseType: config.responseType // responseType이 blob인지 확인
        });
        const authStore = useAuthStore();
        if (authStore.accessToken) {
            config.headers.Authorization = `Bearer ${authStore.accessToken}`;
        }
        // responseType은 개별 요청에서 지정된 값을 그대로 사용
        return config;
    },
    error => {
        console.error('API 요청 에러:', error);
        return Promise.reject(error);
    }
);

// 응답 인터셉터
api.interceptors.response.use(
    response => {
        console.log('API 응답 성공:', {
            url: response.config.url,
            status: response.status,
            data: response.data,
            responseType: response.config.responseType // responseType이 blob인지 확인
        });
        return response;
    },
    async error => {
        console.error('API 응답 에러:', {
            url: error.config?.url,
            status: error.response?.status,
            data: error.response?.data,
            message: error.message
        });
        const originalRequest = error.config;
        const authStore = useAuthStore();

        // 토큰 만료 에러이고, 재시도하지 않은 요청인 경우
        if (error.response?.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;

            try {
                // 토큰 갱신 시도
                const response = await api.post('/api/v1/auth/refresh', {
                    refreshToken: authStore.refreshToken
                });

                const { accessToken, refreshToken } = response.data;

                // 새 토큰 저장
                authStore.accessToken = accessToken;
                authStore.refreshToken = refreshToken;

                // 로컬 스토리지에도 저장
                localStorage.setItem('auth_tokens', JSON.stringify({
                    accessToken,
                    refreshToken
                }));

                // 원래 요청 재시도
                originalRequest.headers.Authorization = `Bearer ${accessToken}`;
                return api(originalRequest);
            } catch (refreshError) {
                // 토큰 갱신 실패 시 로그아웃 (플래그 설정)
                setLoggingOut(true);
                authStore.logout();
                return Promise.reject(refreshError);
            }
        }

        return Promise.reject(error);
    }
);

export default api;
