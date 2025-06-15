import { ApplicantAPI } from '@/apis/routes/applicant';
import api from '@/apis/apiClient';
import ApiResponseDTO from '@/dto/common/apiResponseDTO';
import ApplicantCommandDTO from '@/dto/employment/applicant/applicantCommandDTO';
import ApplicantResponseDTO from '@/dto/employment/applicant/applicantResponseDTO';
import { withErrorHandling, throwCustomApiError } from '@/utils/errorHandler';

export const getAllApplicantsService = async (options = {}) => {
  return withErrorHandling(async () => {
    const response = await api.get(ApplicantAPI.GET_ALL_APPLICANT);
    const apiResponse = ApiResponseDTO.fromJSON(response.data);
    
    if (!apiResponse.success) {
      throwCustomApiError(apiResponse.code, apiResponse.message);
    }

    return apiResponse.data.map(ApplicantResponseDTO.fromJSON);
  }, options);
};

export const getApplicantByIdService = async (id, options = {}) => {
  return withErrorHandling(async () => {
    const response = await api.get(ApplicantAPI.GET_APPLICANT_BY_ID(id));
    const apiResponse = ApiResponseDTO.fromJSON(response.data);

    if (!apiResponse.success) {
      throwCustomApiError(apiResponse.code, apiResponse.message);
    }

    return ApplicantResponseDTO.fromJSON(apiResponse.data);
  }, options);
};

export const searchApplicantsByNameService = async (name, options = {}) => {
  return withErrorHandling(async () => {
    const response = await api.get(ApplicantAPI.SEARCH_APPLICANT_BY_NAME(name));
    const apiResponse = ApiResponseDTO.fromJSON(response.data);
    
    if (!apiResponse.success) {
      throwCustomApiError(apiResponse.code, apiResponse.message);
    }

    return apiResponse.data.map(ApplicantResponseDTO.fromJSON);
  }, options);
};

export const createApplicantService = async (dto, options = {}) => {
  return withErrorHandling(async () => {
    const response = await api.post(ApplicantAPI.CREATE_APPLICANT, dto);
    const apiResponse = ApiResponseDTO.fromJSON(response.data);
    
    if (!apiResponse.success) {
      throwCustomApiError(apiResponse.code, apiResponse.message);
    }

    return ApplicantCommandDTO.fromJSON(apiResponse.data);
  }, options);
};

export const getBookmarksByMemberIdService = async (memberId, options = {}) => {
  return withErrorHandling(async () => {
    const response = await api.get(ApplicantAPI.GET_BOOKMARK_BY_MEMBER_ID(memberId));
    const apiResponse = ApiResponseDTO.fromJSON(response.data);
    
    if (!apiResponse.success) {
      throwCustomApiError(apiResponse.code, apiResponse.message);
    }

    return apiResponse.data;
  }, options);
};

export const addBookmarkService = async (dto, options = {}) => {
  return withErrorHandling(async () => {
    const response = await api.post(ApplicantAPI.ADD_BOOKMARK, dto);
    const apiResponse = ApiResponseDTO.fromJSON(response.data);

    if (!apiResponse.success) {
      throwCustomApiError(apiResponse.code, apiResponse.message);
    }

    return apiResponse.data;
  }, options);
};

export const removeBookmarkService = async (memberId, applicantId, options = {}) => {
  return withErrorHandling(async () => {
    const response = await api.delete(ApplicantAPI.REMOVE_BOOKMARK(memberId, applicantId));
    const apiResponse = ApiResponseDTO.fromJSON(response.data);

    if (!apiResponse.success) {
      throwCustomApiError(apiResponse.code, apiResponse.message);
    }

    return apiResponse.data;
  }, options);
};
