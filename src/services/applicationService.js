import api from '@/apis/apiClient';
import { ApplicationAPI } from '@/apis/routes/application';
import ApiResponseDTO from '@/dto/common/apiResponseDTO';
import ApplicationResponseDTO from '@/dto/employment/application/applicationResponeDTO';
import { withErrorHandling, throwCustomApiError } from '@/utils/errorHandler';

export const getAllApplicationsService = async (options = {}) => {
  return withErrorHandling(async () => {
    const response = await api.get(ApplicationAPI.GET_ALL_APPLICATIONS);
    const apiResponse = ApiResponseDTO.fromJSON(response.data);

    if (!apiResponse.success) {
      throwCustomApiError(apiResponse.code, apiResponse.message);
    }

    return apiResponse.data.map(item => ApplicationResponseDTO.fromJSON(item));
  }, options);
};

export const getApplicationByIdService = async (id, options = {}) => {
  return withErrorHandling(async () => {
    const response = await api.get(ApplicationAPI.GET_APPLICATION_BY_ID(id));
    const apiResponse = ApiResponseDTO.fromJSON(response.data);

    if (!apiResponse.success) {
      throwCustomApiError(apiResponse.code, apiResponse.message);
    }

    return ApplicationResponseDTO.fromJSON(apiResponse.data);
  }, options);
};

export const createApplicationService = async (dto, options = {}) => {
  return withErrorHandling(async () => {
    const response = await api.post(ApplicationAPI.CREATE_APPLICATION, dto);
    const apiResponse = ApiResponseDTO.fromJSON(response.data);

    if (!apiResponse.success) {
      throwCustomApiError(apiResponse.code, apiResponse.message);
    }

    return ApplicationResponseDTO.fromJSON(apiResponse.data);
  }, options);
};

export const updateApplicationStatusService = async (id, dto, options = {}) => {
  return withErrorHandling(async () => {
    const response = await api.patch(ApplicationAPI.UPDATE_APPLICATION_STATUS(id), dto);
    const apiResponse = ApiResponseDTO.fromJSON(response.data);

    if (!apiResponse.success) {
      throwCustomApiError(apiResponse.code, apiResponse.message);
    }

    return ApplicationResponseDTO.fromJSON(apiResponse.data);
  }, options);
};

export const deleteApplicationService = async (id, options = {}) => {
  return withErrorHandling(async () => {
    const response = await api.delete(ApplicationAPI.DELETE_APPLICATION(id));
    const apiResponse = ApiResponseDTO.fromJSON(response.data);

    if (!apiResponse.success) {
      throwCustomApiError(apiResponse.code, apiResponse.message);
    }

    return ApplicationResponseDTO.fromJSON(apiResponse.data);
  }, options);
};

export const createApplicationResponseService = async (dto, options = {}) => {
  return withErrorHandling(async () => {
    const response = await api.post(ApplicationAPI.CREATE_APPLICATION_RESPONSE, dto);
    const apiResponse = ApiResponseDTO.fromJSON(response.data);

    if (!apiResponse.success) {
      throwCustomApiError(apiResponse.code, apiResponse.message);
    }

    return ApplicationResponseDTO.fromJSON(apiResponse.data);
  }, options);
};