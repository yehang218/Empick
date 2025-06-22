import api from '@/apis/apiClient'
import { IntroduceAPI } from '@/apis/routes/introduce'

export const fetchIntroduceItemsService = async (templateId) => {
  const res = await api.get(`${IntroduceAPI.GET_ALL_TEMPLATES}/${templateId}/items`)
  return res.data
}

export const createIntroduceItemService = async (dto) => {
  const res = await api.post(IntroduceAPI.CREATE_TEMPLATE_ITEM, dto)
  return res.data
}

export const deleteIntroduceItemService = async (id) => {
  await api.delete(`${IntroduceAPI.DELETE_TEMPLATE_ITEM(id)}`)
}

export const createIntroduceRatingResult = async (payload) => {
  // payload: { content, ratingScore, ... }
  return api.post(IntroduceAPI.CREATE_RATING_RESULT, payload)
}

// 자기소개서 템플릿 항목별 응답 등록
export const createIntroduceTemplateItemResponse = async (payload) => {
  // payload: { introduceId, introduceTemplateItemId, content }
  return api.post(IntroduceAPI.CREATE_TEMPLATE_ITEM_RESPONSE, payload)
}

// 자기소개서 템플릿 항목별 응답 조회
export const getIntroduceTemplateItemResponses = async (introduceId) => {
  try {
    const res = await api.get(`${IntroduceAPI.GET_ALL_TEMPLATE_ITEM_RESPONSES}?introduceId=${introduceId}`)
    return res.data?.data || res.data || []
  } catch (error) {
    console.warn('자기소개서 템플릿 항목 응답 조회 실패:', error)
    return []
  }
}

// applicationId로 자기소개서와 템플릿 항목 응답 조회
export const getIntroduceWithTemplateResponses = async (applicationId) => {
  try {
    console.log('🔍 applicationId로 자기소개서 조회:', applicationId)
    
    // 1. introduce 테이블에서 applicationId로 자기소개서 조회
    const introduceRes = await api.get(`${IntroduceAPI.GET_ALL_INTRODUCE}`)
    const allIntroduces = introduceRes.data?.data || introduceRes.data || []
    
    const introduce = allIntroduces.find(item => 
      item.applicationId == applicationId
    )
    
    if (!introduce) {
      console.log('자기소개서가 없습니다.')
      return { introduce: null, templateItems: [], responses: [] }
    }
    
    console.log('✅ 자기소개서 발견:', introduce)
    
    // 2. 템플릿 정보 조회
    let templateItems = []
    if (introduce.introduceTemplateId) {
      try {
        const templateRes = await api.get(IntroduceAPI.GET_TEMPLATE_BY_ID(introduce.introduceTemplateId))
        const template = templateRes.data?.data || templateRes.data
        
        if (template?.items) {
          templateItems = template.items
        } else {
          // 템플릿 항목들을 별도 조회
          const itemsRes = await api.get(IntroduceAPI.GET_ALL_TEMPLATE_ITEMS)
          const allItems = itemsRes.data?.data || itemsRes.data || []
          templateItems = allItems.filter(item => 
            item.introduceTemplateId == introduce.introduceTemplateId
          )
        }
        console.log('✅ 템플릿 항목들:', templateItems)
      } catch (templateError) {
        console.warn('템플릿 조회 실패:', templateError)
      }
    }
    
    // 3. 템플릿 항목별 응답 조회
    const responses = await getIntroduceTemplateItemResponses(introduce.id)
    console.log('✅ 템플릿 항목 응답들:', responses)
    
    return { introduce, templateItems, responses }
  } catch (error) {
    console.error('자기소개서 조회 실패:', error)
    return { introduce: null, templateItems: [], responses: [] }
  }
}
