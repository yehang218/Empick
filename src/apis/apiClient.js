import axios from 'axios';

const api = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL,
    timeout: parseInt(import.meta.env.VITE_TIMEOUT) || 5000,
});

api.interceptors.response.use(
    response => response,
    error => {
        // 예: 토큰 만료 처리, 로깅, 공통 에러 메시지
        console.error(error);
        return Promise.reject(error);
    }
);

export default api;
