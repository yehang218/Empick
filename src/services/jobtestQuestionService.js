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
export const getQuestionDetailService = async (questionId, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(JobtestAPI.QUESTION_DETAIL(questionId));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return QuestionDetailResponseDTO.fromJSON(apiResponse.data);
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


// 실무테스트 문제 수정 서비스
export const updateQuestionService = async ( id, dto, options = {} ) => {
    return withErrorHandling(async () => {
        const response = await api.patch(JobtestAPI.QUESTION_DETAIL(id),dto);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if(!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return Question

    }, options);
}



// 실무테스트 문제 삭제
export const deleteQuestionService = async (
    questionId,
    options = {}
) => {
    return withErrorHandling(async () => {
        const response = await api.delete(JobtestAPI.QUESTION_DETAIL(questionId));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.message;
    }, options);
};

// 선택지 전체 삭제 (문제 ID 기준)
export const deleteQuestionOptionsByQuestionId = async (questionId, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.delete(JobtestAPI.QUESTION_OPTION_DETAIL(questionId));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);
        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }
        return apiResponse.message;
    }, options);
};