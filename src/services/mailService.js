import api from '@/apis/apiClient';
import { MailAPI } from '@/apis/routes/mail';
import ApiResponseDTO from '@/dto/common/apiResponseDTO';
import MailDTO from '@/dto/employment/mail/mailResponseDTO';
import { withErrorHandling, throwCustomApiError } from '@/utils/errorHandler';

// Command
// DB에 안내 메일을 등록하는 서비스
export const createMailService = async (dto, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.post(MailAPI.CREATE, dto);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);
        if (!apiResponse.success) throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        return apiResponse.data.map(item => MailDTO.fromJSON(item));
    }, options);
};

// 단순 메일을 전송하는 서비스(DB 등록 포함)
export const sendMailService = async (dto, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.post(MailAPI.SEND, dto);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);
        if (!apiResponse.success) throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        return apiResponse.data;
    }, options);
};

// 실무 테스트 안내 메일을 전송하는 서비스(DB 등록 포함)
export const sendJobtestMailService = async (id, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.post(MailAPI.SEND_JOBTEST(id));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);
        if (!apiResponse.success) throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        return apiResponse.data.map(item => MailDTO.fromJSON(item));
    }, options);
};

// 면접 안내 메일을 전송하는 서비스(DB 등록 포함)
export const sendInterviewMailService = async (id, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.post(MailAPI.SEND_INTERVIEW(id));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);
        if (!apiResponse.success) throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        return apiResponse.data.map(item => MailDTO.fromJSON(item));
    }, options);
};

// Query
// 모든 안내 메일을 조회하는 서비스
export const findAllMailsService = async (options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(MailAPI.GET_ALL);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);
        if (!apiResponse.success) throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        return apiResponse.data.map(item => MailDTO.fromJSON(item));
    }, options);
};

// id로 안내 메일을 조회하는 서비스
export const findMailByIdService = async (id, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(MailAPI.GET_BY_ID(id));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);
        if (!apiResponse.success) throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        return apiResponse.data.map(item => MailDTO.fromJSON(item));
    }, options);
};

// 발송된 이메일로 안내 메일을 조회하는 서비스
export const findMailsByEmailService = async (email, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(MailAPI.GET_BY_EMAIL(email));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);
        if (!apiResponse.success) throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        return apiResponse.data.map(item => MailDTO.fromJSON(item));
    }, options);
};
