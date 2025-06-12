import api from '@/apis/apiClient';
import { API } from '@/apis/routes';
import LoginResponseDTO from '@/dto/auth/loginResponseDTO';

/**
 * 로그인 서비스
 * @param {Object} loginRequest - 로그인 요청 데이터
 * @param {string} loginRequest.employeeNumber - 사원번호
 * @param {string} loginRequest.password - 비밀번호
 * @returns {Promise<LoginResponseDTO>} 로그인 응답 데이터
 * @throws {Error} 로그인 실패 시 에러
 */
export const loginService = async (loginRequest) => {
    console.log('loginService 시작:', loginRequest);
    try {
        // API 요청
        console.log('API 요청 시작:', API.AUTH.LOGIN);
        const response = await api.post(API.AUTH.LOGIN, loginRequest);
        console.log('API 응답:', response.data);

        // 응답 데이터 유효성 검사
        if (!response.data?.data?.accessToken) {
            console.error('토큰 없음:', response.data);
            throw new Error('서버 응답이 올바르지 않습니다.');
        }

        // 응답 DTO 생성
        const loginResponse = new LoginResponseDTO(
            response.data.data.accessToken,
            response.data.data.refreshToken,
            response.data.data.user
        );
        console.log('LoginResponseDTO 생성 완료:', loginResponse);
        return loginResponse;
    } catch (error) {
        console.error('loginService 에러:', error);
        // 에러 타입에 따른 처리
        if (error.response) {
            console.error('서버 응답 에러:', error.response);
            // 서버 응답 에러
            switch (error.response.status) {
                case 401:
                    throw new Error('사원번호 또는 비밀번호가 올바르지 않습니다.');
                case 403:
                    throw new Error('접근 권한이 없습니다.');
                case 429:
                    throw new Error('너무 많은 로그인 시도가 있었습니다. 잠시 후 다시 시도해주세요.');
                default:
                    throw new Error(error.response.data?.message || '로그인 중 오류가 발생했습니다.');
            }
        } else if (error.request) {
            console.error('네트워크 에러:', error.request);
            // 네트워크 에러
            throw new Error('서버에 연결할 수 없습니다. 네트워크 연결을 확인해주세요.');
        } else {
            console.error('기타 에러:', error);
            // 기타 에러
            throw error;
        }
    }
};

/**
 * 로그아웃 서비스
 * @returns {Promise<boolean>} 로그아웃 성공 여부
 */
export const logoutService = async () => {
    try {
        // 서버에 로그아웃 요청
        await api.post(API.AUTH.LOGOUT);

        // 로컬 스토리지의 토큰 제거
        localStorage.removeItem('auth_tokens');

        return true;
    } catch (error) {
        console.error('Logout failed:', error);
        // 서버 요청이 실패하더라도 로컬 토큰은 제거
        localStorage.removeItem('auth_tokens');
        return false;
    }
};

/**
 * 토큰 갱신 서비스
 * @param {string} refreshToken - 갱신 토큰
 * @returns {Promise<LoginResponseDTO>} 새로운 토큰 정보
 */
export const refreshTokenService = async (refreshToken) => {
    try {
        const response = await api.post(API.AUTH.REFRESH, { refreshToken });

        if (!response.data?.accessToken) {
            throw new Error('토큰 갱신 응답이 올바르지 않습니다.');
        }

        return new LoginResponseDTO(
            response.data.accessToken,
            response.data.refreshToken,
            response.data.user
        );
    } catch (error) {
        if (error.response?.status === 401) {
            throw new Error('토큰이 만료되었습니다. 다시 로그인해주세요.');
        }
        throw error;
    }
};

/**
 * 현재 사용자 정보 조회 서비스
 * @returns {Promise<Object>} 사용자 정보
 */
export const getCurrentUserService = async () => {
    try {
        const response = await api.get(API.AUTH.CURRENT_USER);
        return response.data;
    } catch (error) {
        if (error.response?.status === 401) {
            throw new Error('인증이 필요합니다.');
        }
        throw error;
    }
};