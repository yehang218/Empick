import api from '@/apis/apiClient';

import { InterviewAPI } from '@/apis/routes/interview';

import InterviewScoreResponseDTO from '@/dto/employment/interview/interviewScoreResponseDTO';
import ApiResponseDTO from '@/dto/common/apiResponseDTO';

import { withErrorHandling, throwCustomApiError } from '@/utils/errorHandler';



// 🔹 생성/수정/삭제 (Command)

// 면접 평가 점수를 등록하는 서비스
export const createInterviewScoreService = async (dto, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.post(InterviewAPI.CREATE_INTERVIEW_SCORE, dto);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return InterviewScoreResponseDTO.fromJSON(apiResponse.data);
    }, options);
};

// 면접 평가 점수를 수정하는 서비스
export const updateInterviewScoreService = async (id, dto, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.patch(InterviewAPI.UPDATE_INTERVIEW_SCORE(id), dto);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return InterviewScoreResponseDTO.fromJSON(apiResponse.data);
    }, options);
};

// 면접 평가 점수를 삭제하는 서비스
export const deleteInterviewScoreService = async (id, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.delete(InterviewAPI.DELETE_INTERVIEW_SCORE(id));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return InterviewScoreResponseDTO.fromJSON(apiResponse.data);
    }, options);
};



// 🔹 조회 (Query)

// 면접 평가 점수 전체를 조회하는 서비스
export const findAllInterviewScoresService = async (options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(InterviewAPI.FIND_ALL_INTERVIEW_SCORE);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.data.map(item => InterviewScoreResponseDTO.fromJSON(item));
    }, options);
};

// id로 면접 평가 점수를 조회하는 서비스
export const findInterviewScoreByIdService = async (id, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(InterviewAPI.FIND_INTERVIEW_SCORE_BY_ID(id));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return InterviewScoreResponseDTO.fromJSON(apiResponse.data);
    }, options);
};

// 면접 담당자 id로 담당자가 입력한 면접 평가 점수를 조회하는 서비스
export const findInterviewScoresByInterviewerIdService = async (interviewerId, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(InterviewAPI.FIND_INTERVIEW_SCORE_BY_INTERVIEWER_ID(interviewerId));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.data.map(item => InterviewScoreResponseDTO.fromJSON(item));
    }, options);
};

