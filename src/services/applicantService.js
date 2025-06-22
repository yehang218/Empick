import { ApplicantAPI, ApplicantBookmarkAPI } from '@/apis/routes/application';
import api from '@/apis/apiClient';
import ApiResponseDTO from '@/dto/common/apiResponseDTO';
import ApplicantResponseDTO from '@/dto/employment/application/applicantResponseDTO';
// import ApplicantCommandDTO from '@/dto/employment/employment/applicant/applicantCommandDTO';
import { withErrorHandling, throwCustomApiError } from '@/utils/errorHandler';
// import ApplicantFullInfoListDTO from '@/dto/employment/applicant/applicantFullInfoListDTO';
// import axios from '@/apis/axios';

const APPLICANT_API_BASE_URL = '/api/v1/employment/applicant';

import ApplicantFullInfoListDTO from '@/dto/employment/applicant/applicantFullInfoListDTO';

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

    // 🔍 원본 API 응답 데이터 로깅
    console.log('🔍 API 원본 응답 데이터:', apiResponse.data);
    if (apiResponse.data.length > 0) {
      console.log('🔍 첫 번째 API 응답 항목:', apiResponse.data[0]);
      console.log('🔍 API 응답 키들:', Object.keys(apiResponse.data[0]));
    }

    const mappedData = apiResponse.data.map(item => ApplicantFullInfoListDTO.fromJSON(item));
    console.log('🔍 DTO 매핑 후 데이터:', mappedData);
    if (mappedData.length > 0) {
      console.log('🔍 첫 번째 매핑된 데이터:', mappedData[0]);
    }

    return mappedData;
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
  }, options)
};

const applicantService = {
  /**
   * 새로운 지원자를 등록합니다.
   * @param {Object} applicantData - 등록할 지원자 데이터 (이름, 전화번호 등)
   * @returns {Promise<Object>} API 응답 데이터 (CustomApiResponse 구조)
   */
  async registerApplicant(applicantData) {
    try {
      const response = await api.post(ApplicantAPI.CREATE_APPLICANT, applicantData);
      return response.data; // CustomApiResponse 구조를 따른 응답
    } catch (error) {
      console.error('지원자 등록 실패:', error);
      throw error; // 에러를 호출자에게 다시 던집니다.
    }
  },

  /**
   * 모든 지원자 목록을 조회합니다.
   * @returns {Promise<Object>} API 응답 데이터 (CustomApiResponse 구조, 지원자 목록 포함)
   */
  async fetchAllApplicants() {
    try {
      const response = await api.get(APPLICANT_API_BASE_URL);
      return response.data; // CustomApiResponse 구조를 따른 응답
    } catch (error) {
      console.error('지원자 목록 조회 실패:', error);
      throw error; // 에러를 호출자에게 다시 던집니다.
    }
  }
};

export default applicantService;
