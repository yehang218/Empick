import api from '@/apis/apiClient';

import { InterviewAPI } from '../apis/routes/interview';

import InterviewCriteriaResponseDTO from '@/dto/employment/interview/interviewCriteriaResponseDTO';
import ApiResponseDTO from '@/dto/common/ApiResponseDTO';

import { withErrorHandling, throwCustomApiError } from '@/utils/errorHandler';




// 🔹 생성/수정/삭제 (Command)

// 면접 평가 기준을 등록하는 서비스
export const createCriteriaService = async (dto, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.post(InterviewAPI.CREATE_CRITERIA, dto);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return InterviewCriteriaResponseDTO.fromJSON(apiResponse.data);
    }, options);
};


// 면접 평가 기준을 수정하는 서비스
export const updateCriteriaService = async (id, dto, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.patch(InterviewAPI.UPDATE_CRITERIA(id), dto);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return InterviewCriteriaResponseDTO.fromJSON(apiResponse.data);
    }, options);
};


// 면접 평가 기준을 삭제하는 서비스
export const deleteCriteriaService = async (id, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.delete(InterviewAPI.DELETE_CRITERIA(id));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return InterviewCriteriaResponseDTO.fromJSON(apiResponse.data);
    }, options);
};


// 🔹 조회 (Query)

// 면접 평가 기준 전체를 조회하는 서비스
export const getAllCriteriaService = async (options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(InterviewAPI.GET_ALL_CRITERIA);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.data.map(item => InterviewCriteriaResponseDTO.fromJSON(item));
    }, options);
};

// id로 면접 평가 기준을 조회하는 서비스
export const getCriteriaByIdService = async (id, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(InterviewAPI.GET_CRITERIA_BY_ID(id));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);
        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }
        return InterviewCriteriaResponseDTO.fromJSON(apiResponse.data);
    }, options);
}

// 내용으로 면접 평가 기준을 검색하는 서비스
export const searchCriteriaByTitleService = async (title, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(InterviewAPI.SEARCH_CRITERIA_BY_TITLE(title));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);
        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }
        return apiResponse.data.map(item => InterviewCriteriaResponseDTO.fromJSON(item));
    }, options);
}

