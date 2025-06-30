import { defineStore } from 'pinia'
import { ref } from 'vue'
import {
  fetchIntroduceItemsService,
  createIntroduceItemService,
  deleteIntroduceItemService,
  createIntroduceRatingResult,
  getIntroduceByIdService,
  getAllIntroduceService,
  getIntroduceByApplicationIdService,
  updateIntroduceService,
  createIntroduceService,
  createIntroduceTemplateItemResponse
} from '@/services/introduceService'
import { createTemplate } from '@/services/introduceTemplateService'

export const useIntroduceStore = defineStore('introduce', () => {
  // ===== Model: 상태 정의 =====
  const items = ref([])
  const loading = ref(false)
  const error = ref(null)

  // ===== Actions: 비즈니스 로직은 Service 계층을 통해서만 =====
  
  // 전체 항목 조회
  const fetchItems = async (templateId) => {
    loading.value = true
    error.value = null
    try {
      items.value = await fetchIntroduceItemsService(templateId)
    } catch (e) {
      error.value = e.message
    } finally {
      loading.value = false
    }
  }

  // 항목 추가
  const addItem = async (dto) => {
    loading.value = true
    error.value = null
    try {
      const newItem = await createIntroduceItemService(dto)
      items.value.push(newItem)
    } catch (e) {
      error.value = e.message
    } finally {
      loading.value = false
    }
  }

  // 항목 삭제
  const removeItem = async (id) => {
    loading.value = true
    error.value = null
    try {
      await deleteIntroduceItemService(id)
      items.value = items.value.filter(item => item.id !== id)
    } catch (e) {
      error.value = e.message
    } finally {
      loading.value = false
    }
  }

  // 🧹 시스템 전체 중복 데이터 정리
  const cleanupAllDuplicates = async () => {
    loading.value = true
    error.value = null
    try {
      console.log('🧹 사용자 요청으로 전체 중복 데이터 정리 시작...')
      const { cleanupDuplicateRatingResults } = await import('@/services/introduceService')
      const result = await cleanupDuplicateRatingResults()
      
      if (result.success) {
        console.log('🎉 중복 데이터 정리 완료:', result)
        return result
      } else {
        throw new Error(result.error || '중복 데이터 정리 실패')
      }
    } catch (e) {
      error.value = e.message
      throw e
    } finally {
      loading.value = false
    }
  }

  // 자기소개서 평가 결과 저장
  const saveIntroduceRatingResult = async (payload) => {
    loading.value = true
    error.value = null
    try {
      // 💡 저장 전 해당 introduce_id의 중복 데이터 미리 정리
      if (payload.introduceId) {
        try {
          console.log('🧹 저장 전 중복 데이터 사전 정리...')
          const { getIntroduceRatingResultByIntroduceId } = await import('@/services/introduceService')
          await getIntroduceRatingResultByIntroduceId(payload.introduceId) // 이 함수 안에서 중복 제거 로직이 실행됨
        } catch (cleanupError) {
          console.warn('⚠️ 사전 중복 정리 실패 (계속 진행):', cleanupError.message)
        }
      }
      
      const result = await createIntroduceRatingResult(payload)
      
      // 저장 성공 후 관련 스토어들 데이터 새로고침
      try {
        console.log('🔄 평가 결과 저장 후 데이터 새로고침...')
        
        // applicationStore에서 현재 지원서 정보 새로고침
        if (payload.applicationId) {
          const { useApplicationStore } = await import('@/stores/applicationStore')
          const applicationStore = useApplicationStore()
          if (applicationStore.fetchApplicationById) {
            await applicationStore.fetchApplicationById(payload.applicationId)
            console.log('✅ 지원서 정보 새로고침 완료')
          }
        }
        
        // 최신 평가 결과 재조회하여 캐시 업데이트
        if (payload.introduceId) {
          const { getIntroduceRatingResultByIntroduceId } = await import('@/services/introduceService')
          const latestEvaluation = await getIntroduceRatingResultByIntroduceId(payload.introduceId)
          if (latestEvaluation) {
            console.log('✅ 최신 평가 결과 캐시 업데이트:', latestEvaluation.id)
          }
        }
        
      } catch (refreshError) {
        console.warn('⚠️ 데이터 새로고침 실패 (평가 저장은 성공):', refreshError.message)
      }
      
      return result
    } catch (e) {
      error.value = e.message
      throw e
    } finally {
      loading.value = false
    }
  }

  // ID로 자기소개서 조회
  const fetchIntroduceById = async (introduceId) => {
    loading.value = true
    error.value = null
    try {
      console.log('🔍 자기소개서 상세 조회:', introduceId)
      return await getIntroduceByIdService(introduceId)
    } catch (e) {
      error.value = e.message
      console.error('❌ 자기소개서 조회 실패:', e)
      throw e
    } finally {
      loading.value = false
    }
  }

  // applicationId로 자기소개서 조회
  const getIntroduceByApplicationId = async (applicationId) => {
    loading.value = true
    error.value = null
    try {
      console.log('🔍 applicationId로 자기소개서 조회:', applicationId)
      const result = await getIntroduceByApplicationIdService(applicationId)
      console.log('✅ applicationId로 찾은 자기소개서:', result)
      return result
    } catch (e) {
      error.value = e.message
      console.error('❌ applicationId로 자기소개서 조회 실패:', e)
      return null
    } finally {
      loading.value = false
    }
  }

  // 자기소개서 업데이트
  const updateIntroduce = async (introduceId, updateData) => {
    loading.value = true
    error.value = null
    try {
      console.log('🔄 자기소개서 업데이트:', { introduceId, updateData })
      const result = await updateIntroduceService(introduceId, updateData)
      console.log('✅ 자기소개서 업데이트 성공:', result)
      return result
    } catch (e) {
      error.value = e.message
      console.error('❌ 자기소개서 업데이트 실패:', e)
      throw e
    } finally {
      loading.value = false
    }
  }

  // 자기소개서 생성
  const createIntroduce = async ({ applicantId, applicationId, introduceTemplateId, content }) => {
    loading.value = true
    error.value = null
    try {
      console.log('📝 자기소개서 생성 요청 데이터:', { applicantId, applicationId, introduceTemplateId, content })
      
      const payload = {
        applicantId: applicantId,
        applicationId: applicationId,
        introduceTemplateId: introduceTemplateId,
        content: content || ''
      }
      
      console.log('📤 자기소개서 생성 최종 요청:', payload)
      const result = await createIntroduceService(payload)
      console.log('✅ 자기소개서 생성 응답:', result)
      return result?.id || result
    } catch (e) {
      error.value = e.message
      console.error('❌ 자기소개서 생성 실패:', e)
      throw e
    } finally {
      loading.value = false
    }
  }

  // 전체 자기소개서 조회
  const getAllIntroduce = async () => {
    loading.value = true
    error.value = null
    try {
      console.log('🔍 전체 자기소개서 조회')
      const result = await getAllIntroduceService()
      console.log('✅ 전체 자기소개서 조회 성공:', result)
      return result
    } catch (e) {
      error.value = e.message
      console.error('❌ 전체 자기소개서 조회 실패:', e)
      throw e
    } finally {
      loading.value = false
    }
  }

  // 자기소개서 템플릿 항목별 응답 등록
  const createTemplateItemResponse = async (payload) => {
    loading.value = true
    error.value = null
    try {
      console.log('📝 자기소개서 템플릿 항목 응답 등록:', payload)
      const result = await createIntroduceTemplateItemResponse(payload)
      console.log('✅ 자기소개서 템플릿 항목 응답 등록 성공:', result)
      return result
    } catch (e) {
      error.value = e.message
      console.error('❌ 자기소개서 템플릿 항목 응답 등록 실패:', e)
      throw e
    } finally {
      loading.value = false
    }
  }

      return {
    // 상태
    items,
    loading,
    error,
    
    // Actions
    fetchItems,
    addItem,
    removeItem,
    cleanupAllDuplicates,
    saveIntroduceRatingResult,
    fetchIntroduceById,
    getIntroduceByApplicationId,
    updateIntroduce,
    createIntroduce,
    getAllIntroduce,
    createTemplateItemResponse
  }
})

export const useIntroduceTemplateStore = defineStore('introduceTemplate', {
  actions: {
    async addTemplate(title, memberId, itemIds) {
      return await createTemplate({ title, memberId, itemIds })
    }
  }
})

export const fetchTemplates = async () => {
  // 이 함수는 Service를 통해 호출되어야 함
  // TODO: introduceTemplateService.getTemplates() 로 변경 필요
  console.warn('⚠️ fetchTemplates는 Service 계층을 통해 호출되어야 합니다.')
}
