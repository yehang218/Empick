import api from '@/apis/apiClient';
import { JobtestAPI } from '@/apis/routes/jobtest';
import ApiResponseDTO from '@/dto/common/apiResponseDTO';
import { withErrorHandling, throwCustomApiError } from '@/utils/errorHandler';

import  AnswerResponseDTO  from '@/dto/employment/jobtest/answerResponseDTO';

/* 지원서별 답안 상세 조회 */
export const getAnswersByApplicationJobtestId = async (applicationJobtestId, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(JobtestAPI.ANSWER_DETAIL(applicationJobtestId));

        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        if (Array.isArray(apiResponse.data)) {
            return apiResponse.data.map(item => AnswerResponseDTO.fromJSON(item));
        } else {
            return AnswerResponseDTO.fromJSON(apiResponse.data);
        }
    }, options);
};