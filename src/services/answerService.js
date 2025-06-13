import api from '@/apis/apiClient';
import { JobtestAPI } from '@/apis/routes/jobtest';
import ApiResponseDTO from '@/dto/common/apiResponseDTO';
import { withErrorHandling, throwCustomApiError } from '@/utils/errorHandler';

import { AnswerResponseDTO } from '@/dto/employment/jobtest/answerResponseDTO';

/* 지원서별 답안 상세 조회 */
export const getAnswersByApplicationJobtestId = async (applicationJobtestId, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(JobtestAPI.ANSWER_DETAIL, applicationJobtestId);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        // 여러 답변일 수도, 단일 답변일 수도 있음
        if (Array.isArray(apiResponse.data)) {
            return apiResponse.data.map(item => AnswerQueryDTO.fromJSON(item));
        } else {
            return AnswerQueryDTO.fromJSON(apiResponse.data);
        }
    }, options);
};