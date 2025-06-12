import api from '@/apis/apiClient';

import { InterviewAPI } from '@/apis/routes/interview';

import InterviewSheetResponseDTO from '@/dto/employment/interview/interviewSheetResponseDTO';
import ApiResponseDTO from '@/dto/common/ApiResponseDTO';

import { withErrorHandling, throwCustomApiError } from '@/utils/errorHandler';


// 🔹 생성/수정/삭제 (Command)

// 면접 평가표를 등록하는 서비스
export const createSheetService = async (dto, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.post(InterviewAPI.CREATE_SHEET, dto);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return InterviewSheetResponseDTO.fromJSON(apiResponse.data);
    }, options);
};

// 면접 평가표를 수정하는 서비스
export const updateSheetService = async (id, dto, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.patch(InterviewAPI.UPDATE_SHEET(id), dto);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return InterviewSheetResponseDTO.fromJSON(apiResponse.data);
    }, options);
};

// 면접 평가표를 삭제하는 서비스
export const deleteSheetService = async (id, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.delete(InterviewAPI.DELETE_SHEET(id));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return InterviewSheetResponseDTO.fromJSON(apiResponse.data);
    }, options);
};


// 🔹 조회 (Query)

// 면접 평가표 전체를 조회하는 서비스
export const getAllSheetsService = async (options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(InterviewAPI.GET_ALL_SHEETS);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.data.map(item => InterviewSheetResponseDTO.fromJSON(item));
    }, options);
};


// id로 면접 평가표를 조회하는 서비스
export const getSheetByIdService = async (id, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(InterviewAPI.GET_SHEET_BY_ID(id));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return InterviewSheetResponseDTO.fromJSON(apiResponse.data);
    }, options);
};

// 이름으로 면접 평가표를 검색하는 서비스
export const searchSheetByNameService = async (name, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(InterviewAPI.SEARCH_SHEET_BY_NAME(name));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.data.map(item => InterviewSheetResponseDTO.fromJSON(item));
    }, options);
};
