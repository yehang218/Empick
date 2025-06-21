import api from '@/apis/apiClient';

import { InterviewAPI } from '@/apis/routes/interview';

import InterviewerResponseDTO from '@/dto/employment/interview/interviewerResponseDTO';
import ApiResponseDTO from '@/dto/common/apiResponseDTO';

import { withErrorHandling, throwCustomApiError } from '@/utils/errorHandler';

// 🔹 생성/수정/삭제 (Command)

// 면접 담당자를 등록하는 서비스
export const createInterviewerService = async (dto, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.post(InterviewAPI.CREATE_INTERVIEWER, dto);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);
        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }
        return InterviewerResponseDTO.fromJSON(apiResponse.data);
    }, options);
};

// 면접 담당자의 점수를 수정하는 서비스
export const updateInterviewerScoreService = async (id, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.patch(InterviewAPI.UPDATE_INTERVIEWER_SCORE(id));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);
        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }
        return InterviewerResponseDTO.fromJSON(apiResponse.data);
    }, options);
};

// 면접 담당자의 평가를 수정하는 서비스
export const updateInterviewerReviewService = async (id, review, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.patch(InterviewAPI.UPDATE_INTERVIEWER_REVIEW(id, review));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);
        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }
        return InterviewerResponseDTO.fromJSON(apiResponse.data);
    }, options);
};

// 면접 담당자를 삭제하는 서비스
export const deleteInterviewerService = async (id, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.delete(InterviewAPI.DELETE_INTERVIEWER(id));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);
        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }
        return InterviewerResponseDTO.fromJSON(apiResponse.data);
    }, options);
};


// 🔹 조회 (Query)

// 면접 담당자 전체를 조회하는 서비스
export const getAllInterviewers = async (options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(InterviewAPI.GET_ALL_INTERVIEWERS);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }
        return apiResponse.data.map(item => InterviewerResponseDTO.fromJSON(item));
    }, options);
};

// id로 면접 담당자를 조회하는 서비스
export const getInterviewerByIdService = async (id, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(InterviewAPI.GET_INTERVIEWER_BY_ID(id));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);
        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }
        return InterviewerResponseDTO.fromJSON(apiResponse.data);
    }, options);
};

// 면접 id로 면접 담당자를 조회하는 서비스
export const getInterviewersByInterviewIdService = async (interviewId, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(InterviewAPI.GET_INTERVIEWERS_BY_INTERVIEW_ID(interviewId));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);
        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }
        return apiResponse.data.map(item => InterviewerResponseDTO.fromJSON(item));
    }, options);
};