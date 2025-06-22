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
  createIntroduceService
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

  // 자기소개서 평가 결과 저장
  const saveIntroduceRatingResult = async (payload) => {
    loading.value = true
    error.value = null
    try {
      return await createIntroduceRatingResult(payload)
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

      return {
    // 상태
    items,
    loading,
    error,
    
    // Actions
    fetchItems,
    addItem,
    removeItem,
    saveIntroduceRatingResult,
    fetchIntroduceById,
    getIntroduceByApplicationId,
    updateIntroduce,
    createIntroduce,
    getAllIntroduce
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
