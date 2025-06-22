import { defineStore } from 'pinia'
import { ref } from 'vue'
import {
  fetchIntroduceItemsService,
  createIntroduceItemService,
  deleteIntroduceItemService,
  createIntroduceRatingResult
} from '@/services/introduceService'
import { createTemplate } from '@/services/introduceTemplateService'
import api from '@/apis/apiClient'
import { IntroduceAPI } from '@/apis/routes/introduce'

export const useIntroduceStore = defineStore('introduce', () => {
  const items = ref([])
  const loading = ref(false)
  const error = ref(null)

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

  async function saveIntroduceRatingResult(payload) {
    // payload: { content, ratingScore, ... }
    return createIntroduceRatingResult(payload)
  }

  // ID로 자기소개서 조회 (전체 조회 후 필터링)
  const fetchIntroduceById = async (introduceId) => {
    loading.value = true
    error.value = null
    try {
      console.log('🔍 자기소개서 상세 조회:', introduceId)
      
      // 단건 조회 API가 없으므로 전체 조회 후 필터링
      try {
        // 먼저 단건 조회 API 시도
        const res = await api.get(`${IntroduceAPI.GET_INTRODUCE_BY_ID(introduceId)}`)
        console.log('📋 자기소개서 단건 조회 성공:', res.data)
        return res.data?.data || res.data
      } catch (singleError) {
        console.warn('⚠️ 단건 조회 API 없음, 전체 조회 후 필터링 시도')
        
        // 전체 조회 후 클라이언트에서 필터링
        const allRes = await api.get(IntroduceAPI.GET_ALL_INTRODUCE)
        const allIntroduces = allRes.data?.data || allRes.data || []
        
        console.log('📋 전체 자기소개서 목록:', allIntroduces)
        
        // introduceId로 필터링
        const targetIntroduce = allIntroduces.find(item => 
          item.id == introduceId || item.introduceId == introduceId
        )
        
        if (!targetIntroduce) {
          throw new Error(`자기소개서를 찾을 수 없습니다. ID: ${introduceId}`)
        }
        
        console.log('✅ 필터링으로 자기소개서 발견:', targetIntroduce)
        return targetIntroduce
      }
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
      
      // 전체 조회 후 applicationId로 필터링
      const allRes = await api.get(IntroduceAPI.GET_ALL_INTRODUCE)
      const allIntroduces = allRes.data?.data || allRes.data || []
      
      console.log('📋 전체 자기소개서 목록:', allIntroduces)
      
      // applicationId로 필터링
      const targetIntroduce = allIntroduces.find(item => 
        item.applicationId == applicationId
      )
      
      console.log('✅ applicationId로 찾은 자기소개서:', targetIntroduce)
      return targetIntroduce || null
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
      
      const res = await api.patch(`${IntroduceAPI.UPDATE_INTRODUCE(introduceId)}`, updateData)
      console.log('✅ 자기소개서 업데이트 성공:', res.data)
      return res.data?.data || res.data
    } catch (e) {
      error.value = e.message
      console.error('❌ 자기소개서 업데이트 실패:', e)
      throw e
    } finally {
      loading.value = false
    }
  }

  return {
    items, loading, error,
    fetchItems, addItem, removeItem,
    saveIntroduceRatingResult,
    fetchIntroduceById,
    getIntroduceByApplicationId,
    updateIntroduce,
    async createIntroduce({ applicantId, applicationId, introduceTemplateId, content }) {
      console.log('📝 자기소개서 생성 요청 데이터:', { applicantId, applicationId, introduceTemplateId, content })
      
      // 백엔드 IntroduceCommandDTO 스펙에 맞는 필드명
      const payload = {
        applicantId: applicantId,
        applicationId: applicationId,
        introduceTemplateId: introduceTemplateId,
        content: content || ''
      }
      
      console.log('📤 자기소개서 생성 최종 요청:', payload)
      console.log('📤 각 필드 상세 확인:', {
        applicantId: payload.applicantId,
        applicantIdType: typeof payload.applicantId,
        applicationId: payload.applicationId,
        applicationIdType: typeof payload.applicationId,
        introduceTemplateId: payload.introduceTemplateId,
        introduceTemplateIdType: typeof payload.introduceTemplateId
      })
      
      const res = await api.post(IntroduceAPI.CREATE_INTRODUCE, payload)
      
      console.log('✅ 자기소개서 생성 응답:', res.data)
      return res.data?.data?.id || res.data?.id
    }
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
  const res = await api.get('/api/v1/employment/introduce-template')
  return res.data.data
}
