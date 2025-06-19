import ApiResponseDTO from '@/dto/common/apiResponseDTO';
import router from '@/router';
import { useToast } from 'vue-toastification';

const toast = useToast();

const ERROR_MESSAGES = {
    UNKNOWN: '알 수 없는 오류가 발생했습니다.',
    NETWORK: '서버와의 통신에 실패했습니다.',
    EMPTY_DATA: '데이터가 존재하지 않습니다.',
};

// 로그아웃 진행 중인지 추적하는 플래그
let isLoggingOut = false;

/**
 * 로그아웃 상태 설정
 */
export const setLoggingOut = (status) => {
    isLoggingOut = status;
};

/**
 * API 에러를 처리하는 공통 핸들러
 */
export const handleApiError = (error, options = { showToast: true, redirect: true }) => {
    const { showToast = true, redirect = true } = options;

    let apiResponse;
    try {
        apiResponse = ApiResponseDTO.fromJSON(error.response?.data || {});
    } catch {
        apiResponse = new ApiResponseDTO(false, 'UNKNOWN', ERROR_MESSAGES.UNKNOWN, null);
    }

    console.error('API Error:', {
        status: error.response?.status,
        path: error.config?.url,
        code: apiResponse.code,
        message: apiResponse.message,
    });

    // 현재 경로가 로그인 페이지인지 확인
    const currentPath = router.currentRoute.value.path;
    const isLoginPage = currentPath === '/login';

    if (redirect && error.response) {
        switch (error.response.status) {
            case 401:
                // 로그아웃 중이거나 이미 로그인 페이지에 있을 때는 추가 처리하지 않음
                if (!isLoggingOut && !isLoginPage) {
                    import('@/stores/authStore').then(({ useAuthStore }) => useAuthStore().logout());
                    router.push('/login');
                }
                break;
            case 403:
                router.push('/access-denied');      // 🚩 TODO : 권한이 필요하다고 뜨는 페이지
                break;
            case 404:
                router.push('/not-found');          // 🚩 TODO : 404 페이지
                break;
        }
    }

    // 로그아웃 중이거나 401 에러이거나 로그인 페이지에 있을 때는 토스트 메시지 표시하지 않음
    if (showToast && !isLoggingOut && !(error.response?.status === 401) && !isLoginPage) {
        toast.error(apiResponse.message);
    }
};

/**
 * try-catch 없이 API 호출을 감싸는 공통 유틸
 * 각 서비스에서 호출해서 사용
 */
export const withErrorHandling = async (apiCall, options = {}) => {
    try {
        return await apiCall();
    } catch (error) {
        handleApiError(error, options);
        throw error;
    }
};

/**
 * 사용자 정의 에러를 API 응답 형식으로 강제 throw
 */
export const throwCustomApiError = (code, message, status = 400) => {
    const error = new Error(message);
    error.response = {
        status,
        data: {
            success: false,
            code,
            message,
            data: null,
        }
    };
    throw error;
};
