import api from '@/apis/apiClient';
import { JobtestAPI } from '@/apis/routes/jobtest';
import ApiResponseDTO from '@/dto/common/apiResponseDTO';
import { withErrorHandling, throwCustomApiError } from '@/utils/errorHandler';

import AnswerResponseDTO from '@/dto/employment/jobtest/answerResponseDTO';

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

// 답안 제출 (응시 페이지에서 다음 버튼을 누를 때 호출)
export const postAnswer = async (answerDTO, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.post(JobtestAPI.ANSWERS, answerDTO);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.message;
    }, options);
}

// 답안 채점(제출)
export const gradeAnswersByApplicationJobtestId = async (applicationJobtestId, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.patch(JobtestAPI.GRADE_ANSWER(applicationJobtestId));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);
        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }
        return apiResponse.message;
    }, options);
}