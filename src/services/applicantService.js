import { ApplicantAPI, ApplicantBookmarkAPI } from '@/apis/routes/application';
import api from '@/apis/apiClient';
import ApiResponseDTO from '@/dto/common/apiResponseDTO';
import ApplicantResponseDTO from '@/dto/employment/application/applicantResponseDTO';
import { withErrorHandling, throwCustomApiError } from '@/utils/errorHandler';
// import ApplicantFullInfoListDTO from '@/dto/employment/applicant/applicantFullInfoListDTO';

export const getAllApplicantsService = async (options = {}) => {
  return withErrorHandling(async () => {
    const response = await api.get(ApplicantAPI.GET_ALL_APPLICANTS);
    const apiResponse = ApiResponseDTO.fromJSON(response.data);

    if (!apiResponse.success) {
      throwCustomApiError(apiResponse.code, apiResponse.message);
    }

    return apiResponse.data.map(item => ApplicantResponseDTO.fromJSON(item));
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

export const getApplicantFullInfoListService = async (options = {}) => {
  return withErrorHandling(async () => {
    const response = await api.get(ApplicantAPI.APPLICANT_LIST);
    const apiResponse = ApiResponseDTO.fromJSON(response.data);

    if (!apiResponse.success) {
      throwCustomApiError(apiResponse.code, apiResponse.message);
    }

    return apiResponse.data.map(item => ApplicantResponseDTO.fromJSON(item));
  }, options);
};

export const searchApplicantsByNameService = async (name, options = {}) => {
  return withErrorHandling(async () => {
    const response = await api.get(ApplicantAPI.SEARCH_APPLICANTS_BY_NAME(name));
    const apiResponse = ApiResponseDTO.fromJSON(response.data);

    if (!apiResponse.success) {
      throwCustomApiError(apiResponse.code, apiResponse.message);
    }

    return apiResponse.data.map(item => ApplicantResponseDTO.fromJSON(item));
  }, options);
};

export const createApplicantService = async (dto, options = {}) => {
  return withErrorHandling(async () => {
    const response = await api.post(ApplicantAPI.CREATE_APPLICANT, dto);
    const apiResponse = ApiResponseDTO.fromJSON(response.data);

    if (!apiResponse.success) {
      throwCustomApiError(apiResponse.code, apiResponse.message);
    }

    return ApplicantResponseDTO.fromJSON(apiResponse.data);
  }, options);
};

export const getBookmarksByMemberIdService = async (memberId, options = {}) => {
  return withErrorHandling(async () => {
    const response = await api.get(ApplicantBookmarkAPI.GET_BOOKMARK_BY_MEMBER_ID(memberId));
    const apiResponse = ApiResponseDTO.fromJSON(response.data);

    if (!apiResponse.success) {
      throwCustomApiError(apiResponse.code, apiResponse.message);
    }

    return apiResponse.data;
  }, options);
};

export const addApplicantBookmarkService = async (dto, options = {}) => {
  return withErrorHandling(async () => {
    const response = await api.post(ApplicantBookmarkAPI.ADD_BOOKMARK, dto);
    const apiResponse = ApiResponseDTO.fromJSON(response.data);

    if (!apiResponse.success) {
      throwCustomApiError(apiResponse.code, apiResponse.message);
    }

    return apiResponse.data;
  }, options);
};

export const removeApplicantBookmarkService = async (memberId, applicantId, options = {}) => {
  return withErrorHandling(async () => {
    const response = await api.delete(ApplicantBookmarkAPI.REMOVE_BOOKMARK(memberId, applicantId));
    const apiResponse = ApiResponseDTO.fromJSON(response.data);

    if (!apiResponse.success) {
      throwCustomApiError(apiResponse.code, apiResponse.message);
    }

    return apiResponse.data;
  }, options);
};
