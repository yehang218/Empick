import api from '@/apis/apiClient';
import { API } from '@/apis/routes';
import ApiResponseDTO from '@/dto/common/apiResponseDTO';
import { withErrorHandling, throwCustomApiError } from '@/utils/errorHandler';

// 직무(포지션) 목록 조회
export const fetchJobList = async (options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(API.RECRUITMENT.JOB_LIST);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.data;
    }, options);
}; 