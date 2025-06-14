import api from '@/apis/apiClient';
import { JobtestAPI } from '@/apis/routes/jobtest';
import ApiResponseDTO from '@/dto/common/apiResponseDTO';
import { withErrorHandling, throwCustomApiError } from '@/utils/errorHandler';

import QuestionResponseDTO from '@/dto/employment/jobtest/questionResponseDTO';
import QuestionDetailResponseDTO from '@/dto/employment/jobtest/questionDetailResponseDTO';

/**
 * 실무테스트 문제 목록을 조회하는 서비스
 */
export const getQuestionsService = async (options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(JobtestAPI.QUESTIONS);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        // 성공 상태로 오지 않았다면 에러 처리
        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.data.map(item => QuestionResponseDTO.fromJSON(item));
    }, options);
};

// 실무테스트 문제 상세 조회 서비스
export const getQuestionDetailService = async (options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(JobtestAPI.QUESTION_DETAIL);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.data.map(item => QuestionDetailResponseDTO.fromJSON(item));
    }, options);
}

// 실무테스트 문제 등록 서비스
export const createQuestionService = async (
    dto,
    options = {}
) => {
    return withErrorHandling(async () => {
        const response = await api.post(JobtestAPI.QUESTIONS, dto.toJSON());
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.message;
    }, options);
};