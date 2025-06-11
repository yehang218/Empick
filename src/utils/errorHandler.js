import ApiResponseDTO from '@/dto/common/ApiResponseDTO';
import router from '@/router';
import { useToast } from 'vue-toastification';

const toast = useToast();

/**
 * API 에러를 처리하는 공통 핸들러
 * @param {Error} error - 발생한 에러 객체
 * @param {Object} options - 추가 옵션
 * @param {boolean} options.showToast - 토스트 메시지 표시 여부 (기본값: true)
 * @param {boolean} options.redirect - 리다이렉트 처리 여부 (기본값: true)
 * @returns {void}
 */
export const handleApiError = (error, options = { showToast: true, redirect: true }) => {
    const { showToast = true, redirect = true } = options;

    if (error.response) {
        // 백엔드에서 반환한 에러 응답 처리
        const apiResponse = ApiResponseDTO.fromJSON(error.response.data);

        // 에러 로깅
                console.error('API Error:', {
            code: apiResponse.code,
            message: apiResponse.message,
            status: error.response.status,
            path: error.config?.url
        });

        // HTTP 상태 코드에 따른 특별 처리
        if (redirect) {
            switch (error.response.status) {
                case 401:
                    // 인증 에러 - 로그인 페이지로 리다이렉트
                    router.push('/login');
                    break;
                case 403:
                    // 권한 없음 - 접근 거부 페이지로 리다이렉트
                    router.push('/access-denied');
                    break;
                case 404:
                    // 리소스 없음 - 404 페이지로 리다이렉트
                    router.push('/not-found');
                    break;
                // 필요한 경우 다른 상태 코드에 대한 처리 추가
            }
        }

        // 토스트 메시지 표시
        if (showToast) {
            toast.error(apiResponse.message);
        }

    } else if (error.request) {
        // 네트워크 오류 처리
        console.error('Network Error:', error.request);
        if (showToast) {
            toast.error('서버와의 통신에 실패했습니다.');
        }
    } else {
        // 기타 에러 처리
        console.error('Error:', error.message);
        if (showToast) {
            toast.error('알 수 없는 오류가 발생했습니다.');
        }
    }
};

/**
 * API 에러를 처리하고 결과를 반환하는 래퍼 함수
 * @param {Function} apiCall - API 호출 함수
 * @param {Object} options - 에러 처리 옵션
 * @returns {Promise<any>} API 호출 결과
 */
export const withErrorHandling = async (apiCall, options = {}) => {
    try {
        return await apiCall();
    } catch (error) {
        handleApiError(error, options);
        throw error; // 에러를 다시 throw하여 호출자가 처리할 수 있도록 함
    }
}; 