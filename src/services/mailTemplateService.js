import api from '@/apis/apiClient';
import { MailTemplateAPI } from '@/apis/routes/mail';
import ApiResponseDTO from '@/dto/common/apiResponseDTO';
import MailTemplateDTO from '@/dto/employment/mail/mailTemplateDTO';
import { withErrorHandling, throwCustomApiError } from '@/utils/errorHandler';

// Command
// 안내 메일 템플릿을 등록하는 서비스
export const createTemplateService = async (dto, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.post(MailTemplateAPI.CREATE, dto);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);
        if (!apiResponse.success) throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        return apiResponse.data.map(item => MailTemplateDTO.fromJSON(item));
    }, options);
};

// 안내 메일 템플릿을 수정하는 서비스
export const updateTemplateService = async (id, dto, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.patch(MailTemplateAPI.UPDATE(id), dto);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);
        if (!apiResponse.success) throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        return apiResponse.data.map(item => MailTemplateDTO.fromJSON(item));
    }, options);
};

// 안내 메일 템플릿을 삭제하는 서비스
export const deleteTemplateService = async (id, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.delete(MailTemplateAPI.DELETE(id));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);
        if (!apiResponse.success) throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        return apiResponse.data.map(item => MailTemplateDTO.fromJSON(item));
    }, options);
};

// 모든 안내 메일 템플릿을 조회하는 서비스
export const findAllTemplatesService = async (options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(MailTemplateAPI.GET_ALL);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);
        if (!apiResponse.success) throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        return apiResponse.data.map(item => MailTemplateDTO.fromJSON(item));
    }, options);
};

// id로 안내 메일 템플릿을 조회하는 서비스
export const findTemplateByIdService = async (id, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(MailTemplateAPI.GET_BY_ID(id));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);
        if (!apiResponse.success) throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        return apiResponse.data.map(item => MailTemplateDTO.fromJSON(item));
    }, options);
};

// 제목으로 안내 메일 템플릿을 조회하는 서비스
export const searchTemplateByTitleService = async (title, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(MailTemplateAPI.SEARCH_BY_TITLE(title));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);
        if (!apiResponse.success) throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        return apiResponse.data.map(item => MailTemplateDTO.fromJSON(item));
    }, options);
};
