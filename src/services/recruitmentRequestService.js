import api from '@/apis/apiClient';
import { API } from '@/apis/routes';
import ApiResponseDTO from '@/dto/common/apiResponseDTO';
import RecruitmentRequestResponseDTO from '@/dto/employment/recruitment/RecruitmentRequestResponseDTO';
import { withErrorHandling, throwCustomApiError } from '@/utils/errorHandler';

// 채용 요청서 목록 조회
export const fetchRecruitmentRequestList = async (options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(API.RECRUITMENT.REQUEST_LIST);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.data.map(item => RecruitmentRequestResponseDTO.fromJSON(item));
    }, options);
};

// 채용 요청서 상세 조회
export const fetchRecruitmentRequestDetail = async (id, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(API.RECRUITMENT.REQUEST_DETAIL(id));
        console.log('📡 요청서 응답', response.data);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return RecruitmentRequestResponseDTO.fromJSON(apiResponse.data);
    }, options);
};

// 채용 요청서 생성
export const createRecruitmentRequest = async (payload, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.post(API.RECRUITMENT.REQUEST_CREATE, payload);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.data;
    }, options);
};