import { defineStore } from 'pinia';
import { ref, computed } from 'vue';

import {
  getAllApplicationsService,
  getApplicationByIdService,
  getApplicationByApplicantIdService,
  createApplicationService,
  updateApplicationStatusService,
  deleteApplicationService,
  createApplicationResponseService,
  getApplicationResponsesByApplicationIdService
} from '@/services/applicationService';

export const useApplicationStore = defineStore('application', () => {
  // ===== Model (상태) =====
  const applicationList = ref([]);
  const selectedApplication = ref(null);
  const applicationResponses = ref([]); // 이력서 응답 데이터
  const introduceData = ref(null); // 자기소개서 데이터
  const loading = ref(false);
  const error = ref(null);

  // ===== ViewModel (computed properties) =====
  // 현재 지원서의 평가 통계
  const evaluationStats = computed(() => {
    if (!selectedApplication.value) return []
    
    return [
      {
        type: '자기소개서',
        score: introduceData.value?.score || selectedApplication.value?.introduceScore || 0,
        average: 75,
        result: (introduceData.value?.score || selectedApplication.value?.introduceScore || 0) >= 70 ? '합격' : '불합격'
      },
      {
        type: '실무 테스트',
        score: selectedApplication.value?.jobtestTotalScore || 0,
        average: 80,
        result: (selectedApplication.value?.jobtestTotalScore || 0) >= 70 ? '합격' : '불합격'
      },
      {
        type: '면접',
        score: selectedApplication.value?.interviewScore || 0,
        average: 85,
        result: (selectedApplication.value?.interviewScore || 0) >= 70 ? '합격' : '불합격'
      }
    ]
  })

  // 지원자 기본 정보 (computed)
  const applicantInfo = computed(() => {
    if (!selectedApplication.value) return null
    
    return {
      ...selectedApplication.value,
      name: selectedApplication.value.name || '지원자',
      profileUrl: selectedApplication.value.profileUrl || '/assets/empick_logo.png',
      jobName: selectedApplication.value.jobName || '백엔드 개발자',
      status: selectedApplication.value.status || 'WAITING'
    }
  })

  // 이력서 응답 요약 (computed)
  const resumeSummary = computed(() => {
    console.log('📊 resumeSummary computed - applicationResponses:', applicationResponses.value)
    
    if (!applicationResponses.value || applicationResponses.value.length === 0) {
      return []
    }
    
    return applicationResponses.value.map(response => {
      console.log('📋 개별 응답 매핑:', response)
      return {
        id: response.id,
        itemName: response.categoryName || response.itemName || response.applicationItemCategoryName || '항목명 없음',
        content: response.content || response.answer || response.responseContent || '응답 없음',
        inputType: response.inputType,
        isRequired: response.isRequired
      }
    })
  })

  // 자기소개서 항목들 (computed)
  const introduceItems = computed(() => {
    if (!introduceData.value) return []
    
    if (introduceData.value.items) {
      return introduceData.value.items
    }
    
    // 단일 내용인 경우
    if (introduceData.value.content) {
      return [{
        id: 1,
        title: '자기소개서',
        content: introduceData.value.content
      }]
    }
    
    return []
  })

  // 🔍 전체 지원서 목록 조회
  const fetchAllApplications = async () => {
    loading.value = true;
    error.value = null;
    try {
      const result = await getAllApplicationsService();
      applicationList.value = result;
      return result;
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  // 🔍 ID로 단일 지원서 조회
  const fetchApplicationById = async (id) => {
    loading.value = true;
    error.value = null;
    try {
      console.log('🔍 ApplicationStore: 지원서 조회 시작:', id)
      const result = await getApplicationByIdService(id);
      selectedApplication.value = result;
      console.log('✅ ApplicationStore: 지원서 조회 성공:', result)
      return result;
    } catch (err) {
      console.error('❌ ApplicationStore: 지원서 조회 실패:', err)
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  // 🔍 applicantId로 단일 지원서 조회
  const fetchApplicationByApplicantId = async (applicantId) => {
    loading.value = true;
    error.value = null;
    try {
      console.log('🔍 ApplicationStore: 지원자ID로 지원서 조회 시작:', applicantId)
      const result = await getApplicationByApplicantIdService(applicantId);
      selectedApplication.value = result;
      console.log('✅ ApplicationStore: 지원자ID로 지원서 조회 성공:', result)
      return result;
    } catch (err) {
      console.error('❌ ApplicationStore: 지원자ID로 지원서 조회 실패:', err)
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  // ✍️ 지원서 생성
  const createApplication = async (dto) => {
    loading.value = true;
    error.value = null;
    try {
      console.log('✍️ ApplicationStore: 지원서 생성 시작:', dto)
      const result = await createApplicationService(dto);
      console.log('✅ ApplicationStore: 지원서 생성 성공:', result)
      return result;
    } catch (err) {
      console.error('❌ ApplicationStore: 지원서 생성 실패:', err)
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  // 🔁 지원서 상태 업데이트
  const updateApplicationStatus = async (id, dto) => {
    const result = await updateApplicationStatusService(id, dto);
    await fetchAllApplications();
    return result;
  };

  // ❌ 지원서 삭제
  const deleteApplication = async (id) => {
    const result = await deleteApplicationService(id);
    await fetchAllApplications();
    return result;
  };

  // 📄 지원서 응답 생성
  const createApplicationResponse = async (dto) => {
    return await createApplicationResponseService(dto);
  };

  // 📄 지원서 ID로 이력서 응답 조회 (기존 메서드 - 호환성 유지)
  const getApplicationResponsesByApplicationId = async (applicationId) => {
    loading.value = true;
    error.value = null;
    try {
      const result = await getApplicationResponsesByApplicationIdService(applicationId);
      applicationResponses.value = result || []; // Store에 저장
      return result;
    } catch (err) {
      error.value = err.message;
      applicationResponses.value = []; // 에러 시 초기화
      throw err;
    } finally {
      loading.value = false;
    }
  };

  // 📝 자기소개서 데이터 설정 (외부에서 호출)
  const setIntroduceData = (data) => {
    introduceData.value = data;
  };

  // 📄 이력서 응답 데이터 조회 (ApplicationPage에서 사용)
  const fetchApplicationResponses = async (applicationId) => {
    loading.value = true;
    error.value = null;
    try {
      console.log('📄 ApplicationStore: 이력서 응답 조회 시작:', applicationId)
      const result = await getApplicationResponsesByApplicationIdService(applicationId);
      applicationResponses.value = result || [];
      console.log('✅ ApplicationStore: 이력서 응답 조회 성공:', result)
      
      // 데이터가 없으면 직접 API 호출 시도 (fallback)
      if (!result || result.length === 0) {
        console.warn('⚠️ 이력서 응답 데이터가 없어서 직접 API 호출 시도')
        try {
          const { default: api } = await import('@/apis/apiClient')
          const directResponse = await api.get(`/api/v1/employment/application-response/application/${applicationId}`)
          console.log('🔄 직접 API 호출 결과:', directResponse.data)
          
          const directData = directResponse.data?.data || directResponse.data || []
          if (directData.length > 0) {
            applicationResponses.value = directData
            console.log('✅ 직접 API 호출로 이력서 데이터 확보:', directData)
            return directData
          }
        } catch (directError) {
          console.error('❌ 직접 API 호출도 실패:', directError)
        }
      }
      
      return result;
    } catch (err) {
      console.error('❌ ApplicationStore: 이력서 응답 조회 실패:', err)
      error.value = err.message;
      applicationResponses.value = [];
      // 에러를 throw하지 않고 빈 배열로 처리
      return [];
    } finally {
      loading.value = false;
    }
  };

  // 📝 자기소개서 데이터 조회 (ApplicationPage에서 사용)
  const fetchIntroduceData = async (applicationId) => {
    loading.value = true;
    error.value = null;
    try {
      console.log('📝 ApplicationStore: 자기소개서 조회 시작:', applicationId)
      
      // 새로운 효율적인 API 사용
      const { getIntroduceWithTemplateResponses } = await import('@/services/introduceService')
      const result = await getIntroduceWithTemplateResponses(applicationId)
      
      if (result.introduce) {
        // 템플릿 항목과 응답을 결합하여 표시용 데이터 생성
        const combinedItems = result.templateItems.map(templateItem => {
          const response = result.responses.find(r => 
            r.introduceTemplateItemId == templateItem.id
          )
          return {
            id: templateItem.id,
            title: templateItem.title,
            content: response?.content || '응답이 없습니다.',
            templateItemId: templateItem.id,
            responseId: response?.id
          }
        })
        
        introduceData.value = {
          ...result.introduce,
          items: combinedItems,
          templateItems: result.templateItems,
          responses: result.responses,
          content: result.introduce.content
        }
        
        console.log('✅ ApplicationStore: 새로운 API로 자기소개서 조회 성공:', introduceData.value)
      } else {
        console.warn('⚠️ 자기소개서 데이터가 없습니다.')
        introduceData.value = {
          items: [],
          templateItems: [],
          responses: [],
          content: null
        }
      }
      
      return introduceData.value;
    } catch (err) {
      console.error('❌ ApplicationStore: 자기소개서 조회 실패:', err)
      error.value = err.message;
      introduceData.value = {
        items: [],
        templateItems: [],
        responses: [],
        content: null
      };
      return introduceData.value;
    } finally {
      loading.value = false;
    }
  };

  // 📋 지원서 직접 설정 (URL 파라미터나 임시 데이터 사용 시)
  const setApplication = (application) => {
    selectedApplication.value = application;
    console.log('📋 ApplicationStore: 지원서 직접 설정:', application)
  };

  // 🔄 상태 초기화
  const resetApplicationData = () => {
    selectedApplication.value = null;
    applicationResponses.value = [];
    introduceData.value = null;
    error.value = null;
    console.log('🧹 ApplicationStore: 데이터 초기화 완료')
  };

  return {
    // ===== Model (상태) =====
    applicationList,
    selectedApplication,
    applicationResponses,
    introduceData,
    loading,
    error,

    // ===== ViewModel (computed) =====
    evaluationStats,
    applicantInfo,
    resumeSummary,
    introduceItems,

    // ===== Actions (비즈니스 로직) =====
    fetchAllApplications,
    fetchApplicationById,
    fetchApplicationByApplicantId,
    createApplication,
    updateApplicationStatus,
    deleteApplication,
    createApplicationResponse,
    getApplicationResponsesByApplicationId,
    fetchApplicationResponses,
    fetchIntroduceData,
    setIntroduceData,
    setApplication,
    resetApplicationData,
  };
});
