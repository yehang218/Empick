import axios from 'axios';

const instance = axios.create({
    baseURL: 'http://localhost:8080', // 백엔드 기본 URL 설정
    timeout: 5000, // 요청 타임아웃 설정
    headers: {
        'Content-Type': 'application/json',
    },
    withCredentials: true, // CORS 문제 해결을 위해 쿠키 및 인증 헤더 전송
});

// 요청 인터셉터 (선택 사항: 토큰 추가 등)
instance.interceptors.request.use(
    config => {
        // 예: localStorage.getItem('accessToken')으로 토큰을 가져와 헤더에 추가
        // const token = localStorage.getItem('accessToken');
        // if (token) {
        //     config.headers.Authorization = `Bearer ${token}`;
        // }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

// 응답 인터셉터 (선택 사항: 에러 처리, 토큰 갱신 등)
instance.interceptors.response.use(
    response => {
        return response;
    },
    error => {
        // 예: 401 Unauthorized 시 로그인 페이지로 리다이렉트
        // if (error.response && error.response.status === 401) {
        //     window.location.href = '/login';
        // }
        return Promise.reject(error);
    }
);

export default instance; 