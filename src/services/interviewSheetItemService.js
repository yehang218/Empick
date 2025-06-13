import api from '@/apis/apiClient';

import { InterviewAPI } from '@/apis/routes/interview';

import InterviewSheetItemResponseDTO from '@/dto/employment/interview/interviewSheetItemResponseDTO';
import ApiResponseDTO from '@/dto/common/apiResponseDTO';

import { withErrorHandling, throwCustomApiError } from '@/utils/errorHandler';



// 🔹 생성/수정/삭제 (Command)

// 면접 평가표 항목을 등록하는 서비스
export const createSheetItemService = async (dto, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.post(InterviewAPI.CREATE_SHEET_ITEM, dto);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return InterviewSheetItemResponseDTO.fromJSON(apiResponse.data);
    }, options);
};

// 면접 평가표 항목을 삭제하는 서비스
export const deleteSheetItemService = async (id, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.delete(InterviewAPI.DELETE_SHEET_ITEM(id));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return InterviewSheetItemResponseDTO.fromJSON(apiResponse.data);
    }, options);
};



// 🔹 조회 (Query)

// 면접 평가표 항목 전체를 조회하는 서비스
export const findAllSheetItemsService = async (options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(InterviewAPI.FIND_ALL_SHEET_ITEM);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.data.map(item => InterviewSheetItemResponseDTO.fromJSON(item));
    }, options);
};


// id로 면접 평가표 항목을 조회하는 서비스
export const findSheetItemByIdService = async (id, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(InterviewAPI.FIND_SHEET_ITEM_BY_ID(id));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return InterviewSheetItemResponseDTO.fromJSON(apiResponse.data);
    }, options);
};


// 면접 평가표 id로 면접 평가표 항목을 조회하는 서비스
export const findSheetItemsBySheetIdService = async (sheetId, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(InterviewAPI.FIND_SHEET_ITEM_BY_SHEET_ID(sheetId));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.data.map(item => InterviewSheetItemResponseDTO.fromJSON(item));
    }, options);
};

// 면접 평가 기준 id로 면접 평가표 항목을 조회하는 서비스
export const findSheetItemsByCriteriaIdService = async (criteriaId, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(InterviewAPI.FIND_SHEET_ITEM_BY_CRITERIA_ID(criteriaId));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.data.map(item => InterviewSheetItemResponseDTO.fromJSON(item));
    }, options);
};
