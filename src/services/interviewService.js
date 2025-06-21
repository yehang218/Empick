import api from '@/apis/apiClient';

import { InterviewAPI } from '../apis/routes/interview';

import InterviewResponseDTO from '@/dto/employment/interview/interviewResponseDTO';
import ApiResponseDTO from '@/dto/common/apiResponseDTO';

import { withErrorHandling, throwCustomApiError } from '@/utils/errorHandler';


// 🔹 생성/수정/삭제 (Command)

// 면접을 등록하는 서비스
export const createInterviewService = async (dto, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.post(InterviewAPI.CREATE_INTERVIEW, dto);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return InterviewResponseDTO.fromJSON(apiResponse.data);
    }, options);
};

// 면접을 수정하는 서비스
export const updateInterviewService = async (id, dto, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.patch(InterviewAPI.UPDATE_INTERVIEW(id), dto);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return InterviewResponseDTO.fromJSON(apiResponse.data);
    }, options);
};

// 면접 시간을 수정하는 서비스
export const updateInterviewDatetimeService = async (id, datetime, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.patch(InterviewAPI.UPDATE_DATETIME(id, datetime));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return InterviewResponseDTO.fromJSON(apiResponse.data);
    }, options);
};

// 면접 주소를 수정하는 서비스
export const updateInterviewAddressService = async (id, address, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.patch(InterviewAPI.UPDATE_ADDRESS(id, address));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return InterviewResponseDTO.fromJSON(apiResponse.data);
    }, options);
};

// 면접을 삭제하는 서비스
export const deleteInterviewService = async (id, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.delete(InterviewAPI.DELETE_INTERVIEW(id));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return InterviewResponseDTO.fromJSON(apiResponse.data);
    }, options);
};



// 🔹 조회 (Query)

// 면접 전체 목록을 조회하는 서비스
export const getAllInterviewsService = async (options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(InterviewAPI.GET_ALL_INTERVIEWS);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        // 성공 상태로 오지 않았다면 에러 처리
        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.data.map(item => InterviewResponseDTO.fromJSON(item));
    }, options);
};

// 면접을 id로 조회하는 서비스
export const getInterviewByIdService = async (id, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(InterviewAPI.GET_INTERVIEW_BY_ID(id));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return InterviewResponseDTO.fromJSON(apiResponse.data);
    }, options);
};

// 면접을 지원서id로 조회하는 서비스
export const getInterviewByApplicationIdService = async (applicationId, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(InterviewAPI.GET_INTERVIEW_BY_APPLICATION_ID(applicationId));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return InterviewResponseDTO.fromJSON(apiResponse.data);
    }, options);
};

// 면접을 날짜로 조회하는 서비스
export const getInterviewsByDateService = async (date, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(InterviewAPI.GET_INTERVIEWS_BY_DATE(date));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.data.map(item => InterviewResponseDTO.fromJSON(item));
    }, options);
};

// 면접을 등록 가능한지 조회하는 서비스
export const checkAvailableDatetimeService = async (datetime, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(InterviewAPI.CHECK_AVAILABLE_DATETIME(datetime));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.data; // Boolean (true or false)
    }, options);
};

