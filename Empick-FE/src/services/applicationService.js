import api from '../apis/apiClient'
import { ApplicationAPI, ApplicationResponseAPI } from '@/apis/routes/application';
import ApiResponseDTO from '@/dto/common/apiResponseDTO';
import ApplicationResponseDTO from '@/dto/employment/application/applicationResponeDTO';
import ApplicationItemResponseDTO from '@/dto/employment/application/applicationItemResponseDTO';
import { withErrorHandling, throwCustomApiError } from '@/utils/errorHandler';
import { fetchApplicationItemCategories, fetchApplicationItemsByRecruitment } from './applicationItemService';

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

    const applicationDto = ApplicationResponseDTO.fromJSON(apiResponse.data);
    if (!applicationDto) {
      console.warn('⚠️ 지원서 데이터 변환 실패. API 응답:', apiResponse);
      throw new Error(`지원서 데이터를 변환할 수 없습니다. (ID: ${id})`);
    }
    return applicationDto;
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

      const applicationDto = ApplicationResponseDTO.fromJSON(apiResponse.data);
      if (!applicationDto) {
        throw new Error('지원서 데이터를 변환할 수 없습니다.');
      }
      return applicationDto;
    } catch (error) {
      console.warn('⚠️ applicantId 전용 엔드포인트 실패, 기본 엔드포인트 시도:', error.message);
      
      // 실패하면 기본 엔드포인트 사용 (applicantId를 applicationId로 사용)
      const response = await api.get(ApplicationAPI.GET_APPLICATION_BY_ID(applicantId));
      const apiResponse = ApiResponseDTO.fromJSON(response.data);
      
      if (!apiResponse.success) {
        throwCustomApiError(apiResponse.code, apiResponse.message);
      }

      const fallbackDto = ApplicationResponseDTO.fromJSON(apiResponse.data);
      if (!fallbackDto) {
        throw new Error('지원서 데이터를 변환할 수 없습니다.');
      }
      return fallbackDto;
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

export const updateApplicationStatusService = async (id, statusCode, options = {}) => {
  return withErrorHandling(async () => {
    console.log('🔄 지원서 상태 변경:', { applicationId: id, statusCode });
    
    const updateData = {
      status: statusCode
    };
    
    const response = await api.patch(ApplicationAPI.UPDATE_APPLICATION_STATUS(id), updateData);
    const apiResponse = ApiResponseDTO.fromJSON(response.data);

    if (!apiResponse.success) {
      throwCustomApiError(apiResponse.code, apiResponse.message);
    }

    console.log('✅ 지원서 상태 변경 성공:', apiResponse.data);
    
    // 클라이언트에서 상태 설명 추가 (백엔드 수정 전 임시 해결책)
    const result = ApplicationResponseDTO.fromJSON(apiResponse.data);
    if (result && typeof result.status === 'number') {
      const { getStatusByCode } = await import('@/constants/employment/applicationStatus');
      const statusInfo = getStatusByCode(result.status);
      result.statusDescription = statusInfo.label;
      console.log('✅ 클라이언트에서 상태 설명 추가:', statusInfo.label);
    }
    
    return result;
  }, options);
};

// application의 introduce_rating_result_id 업데이트 전용 서비스
export const updateApplicationIntroduceRatingResultService = async (applicationId, ratingResultId, options = {}) => {
  return withErrorHandling(async () => {
    console.log('🔄 application introduce_rating_result_id 업데이트 시도:', {
      applicationId,
      ratingResultId
    });
    
    // 방법 1: 단순 필드 업데이트 시도
    try {
      const updateData = {
        introduceRatingResultId: ratingResultId,
        introduce_rating_result_id: ratingResultId
      };
      
      console.log('📤 방법 1 - 단순 필드 업데이트:', updateData);
      const response = await api.patch(ApplicationAPI.UPDATE_APPLICATION_STATUS(applicationId), updateData);
      const apiResponse = ApiResponseDTO.fromJSON(response.data);

      if (apiResponse.success) {
        console.log('✅ 방법 1 성공 - application introduce_rating_result_id 업데이트 완료:', apiResponse.data);
        return ApplicationResponseDTO.fromJSON(apiResponse.data);
      }
    } catch (method1Error) {
      console.warn('⚠️ 방법 1 실패:', method1Error.message);
    }

    // 방법 2: 현재 상태 포함한 업데이트 시도
    try {
      const currentApp = await getApplicationByIdService(applicationId);
      const updateData = {
        id: applicationId,
        status: currentApp.status,
        introduceRatingResultId: ratingResultId,
        introduce_rating_result_id: ratingResultId
      };
      
      console.log('📤 방법 2 - 전체 정보 포함 업데이트:', updateData);
      const response = await api.patch(ApplicationAPI.UPDATE_APPLICATION_STATUS(applicationId), updateData);
      const apiResponse = ApiResponseDTO.fromJSON(response.data);

      if (apiResponse.success) {
        console.log('✅ 방법 2 성공 - application introduce_rating_result_id 업데이트 완료:', apiResponse.data);
        return ApplicationResponseDTO.fromJSON(apiResponse.data);
      }
    } catch (method2Error) {
      console.warn('⚠️ 방법 2 실패:', method2Error.message);
    }

    // 방법 3: PUT 요청 시도
    try {
      const currentApp = await getApplicationByIdService(applicationId);
      const updateData = {
        ...currentApp,
        introduceRatingResultId: ratingResultId,
        introduce_rating_result_id: ratingResultId
      };
      
      console.log('📤 방법 3 - PUT 요청:', updateData);
      const response = await api.put(ApplicationAPI.UPDATE_APPLICATION_STATUS(applicationId), updateData);
      const apiResponse = ApiResponseDTO.fromJSON(response.data);

      if (apiResponse.success) {
        console.log('✅ 방법 3 성공 - application introduce_rating_result_id 업데이트 완료:', apiResponse.data);
        return ApplicationResponseDTO.fromJSON(apiResponse.data);
      }
    } catch (method3Error) {
      console.warn('⚠️ 방법 3 실패:', method3Error.message);
    }

    // 모든 방법 실패 시 에러 로깅
    console.error('❌ 모든 업데이트 방법 실패 - introduce_rating_result_id 업데이트 불가');
    console.error('🔍 백엔드 API 스키마 확인 필요');
    
    // 실패해도 예외를 던지지 않고 null 반환 (평가 저장은 성공했으므로)
    return null;
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
    return ApplicationItemResponseDTO.fromJSON(apiResponse.data);
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

    console.log('✅ 원본 API 응답 데이터:', apiResponse.data);
    
    // 배열 형태의 이력서 응답 데이터를 DTO로 변환
    if (Array.isArray(apiResponse.data)) {
      const responses = apiResponse.data
        .map(item => {
          console.log('🔍 개별 응답 원본 데이터:', item);
          return ApplicationItemResponseDTO.fromJSON(item);
        })
        .filter(item => item !== null); // null 값 제거
      
      console.log('📋 DTO 변환 후 데이터:', responses);
      
      // categoryName이 없는 경우 applicationItemId를 통해 항목 정보 매핑
      try {
        // 1. 먼저 지원서 정보를 통해 recruitmentId 확인
        const applicationResponse = await api.get(ApplicationAPI.GET_APPLICATION_BY_ID(applicationId));
        const applicationData = ApiResponseDTO.fromJSON(applicationResponse.data);
        
        if (applicationData.success && applicationData.data.recruitmentId) {
          const recruitmentId = applicationData.data.recruitmentId;
          console.log('🔍 recruitmentId 확인:', recruitmentId);
          
          // 2. 해당 채용공고의 모든 항목 정보 조회
          const { fetchApplicationItemsByRecruitment } = await import('./applicationItemService');
          const applicationItems = await fetchApplicationItemsByRecruitment(recruitmentId);
          console.log('📋 채용공고 항목들:', applicationItems);
          
                     // 3. applicationItemId를 통해 항목명 매핑
           responses.forEach(response => {
             if (!response.categoryName && response.applicationItemId) {
               const matchedItem = applicationItems.find(item => item.id === response.applicationItemId);
               if (matchedItem && matchedItem.categoryName) {
                 response.categoryName = matchedItem.categoryName;
                 console.log(`🔗 항목명 매핑 성공: ${response.applicationItemId} -> ${response.categoryName}`);
               } else {
                 console.warn(`⚠️ 매칭되는 항목을 찾을 수 없거나 categoryName이 없음: applicationItemId=${response.applicationItemId}`, matchedItem);
               }
             }
           });
        }
      } catch (mappingError) {
        console.warn('⚠️ 항목명 매핑 실패, 원본 데이터 사용:', mappingError.message);
      }
      
      // 최종 데이터 로깅
      responses.forEach((response, index) => {
        console.log(`📋 응답 ${index + 1}:`, {
          id: response.id,
          applicationItemId: response.applicationItemId,
          categoryName: response.categoryName,
          content: response.content?.substring(0, 50) + (response.content?.length > 50 ? '...' : ''),
          inputType: response.inputType,
          required: response.required
        });
      });
      
      console.log('🎯 최종 반환 데이터 (항목명 매핑 완료):', responses);
      return responses;
    }
    
    return [];
  }, options);
};