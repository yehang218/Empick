import api from '@/apis/apiClient';
import { API } from '@/apis/routes';
import ApiResponseDTO from '@/dto/common/apiResponseDTO';
import recruitmentProcessDTO from '@/dto/employment/recruitment/recruitmentProcessDTO';
import { withErrorHandling, throwCustomApiError } from '@/utils/errorHandler';

export const fetchRecruitmentProcesses = async (recruitmentId, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(API.RECRUITMENT.PROCESS_LIST(recruitmentId));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.data.map(item => recruitmentProcessDTO.fromJSON(item));
    }, options);
};
