import api from '../apis/apiClient'
import { ApplicationAPI, ApplicationResponseAPI } from '@/apis/routes/application';
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
    console.log('🔍 API 호출 - applicationId:', id);
    const response = await api.get(ApplicationAPI.GET_APPLICATION_BY_ID(id));
    const apiResponse = ApiResponseDTO.fromJSON(response.data);
    
    if (!apiResponse.success) {
      throwCustomApiError(apiResponse.code, apiResponse.message);
    }

    return ApplicationResponseDTO.fromJSON(apiResponse.data);
  }, options);
};

// applicantId로 application 조회하는 함수 추가
export const getApplicationByApplicantIdService = async (applicantId, options = {}) => {
  return withErrorHandling(async () => {
    console.log('🔍 API 호출 - applicantId:', applicantId);
    
    try {
      // 먼저 applicantId 전용 엔드포인트 시도
      const response = await api.get(ApplicationAPI.GET_APPLICATION_BY_APPLICANT_ID(applicantId));
      const apiResponse = ApiResponseDTO.fromJSON(response.data);
      
      if (!apiResponse.success) {
        throwCustomApiError(apiResponse.code, apiResponse.message);
      }

      return ApplicationResponseDTO.fromJSON(apiResponse.data);
    } catch (error) {
      console.warn('⚠️ applicantId 전용 엔드포인트 실패, 기본 엔드포인트 시도:', error.message);
      
      // 실패하면 기본 엔드포인트 사용 (applicantId를 applicationId로 사용)
      const response = await api.get(ApplicationAPI.GET_APPLICATION_BY_ID(applicantId));
      const apiResponse = ApiResponseDTO.fromJSON(response.data);
      
      if (!apiResponse.success) {
        throwCustomApiError(apiResponse.code, apiResponse.message);
      }

      return ApplicationResponseDTO.fromJSON(apiResponse.data);
    }
  }, options);
};

export const createApplicationService = async (dto, options = {}) => {
  return withErrorHandling(async () => {
    const response = await api.post(ApplicationAPI.CREATE_APPLICATION, dto);
    const apiResponse = ApiResponseDTO.fromJSON(response.data);

    if (!apiResponse.success) {
      throwCustomApiError(apiResponse.code, apiResponse.message);
    }

    return ApplicationResponseDTO.fromJSON(apiResponse.data); // ✅ 여기
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
    console.log('📝 이력서 응답 생성 요청:', dto);
    
    const response = await api.post(ApplicationResponseAPI.CREATE_APPLICATION_RESPONSE, dto);
    const apiResponse = ApiResponseDTO.fromJSON(response.data);

    if (!apiResponse.success) {
      throwCustomApiError(apiResponse.code, apiResponse.message);
    }

    console.log('✅ 이력서 응답 생성 성공:', apiResponse.data);
    return ApplicationResponseDTO.fromJSON(apiResponse.data);
  }, options);
};

// applicationId로 application response들을 조회하는 서비스
export const getApplicationResponsesByApplicationIdService = async (applicationId, options = {}) => {
  return withErrorHandling(async () => {
    console.log('🔍 이력서 응답 조회 - applicationId:', applicationId);
    const response = await api.get(ApplicationResponseAPI.GET_APPLICATION_RESPONSES_BY_APPLICATION_ID(applicationId));
    const apiResponse = ApiResponseDTO.fromJSON(response.data);

    if (!apiResponse.success) {
      throwCustomApiError(apiResponse.code, apiResponse.message);
    }

    console.log('✅ 이력서 응답 조회 성공:', apiResponse.data);
    return apiResponse.data; // 배열 형태의 이력서 응답 데이터
  }, options);
};