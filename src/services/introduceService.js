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

// ID로 자기소개서 조회
export const getIntroduceByIdService = async (introduceId) => {
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
}

// 전체 자기소개서 조회
export const getAllIntroduceService = async () => {
  const res = await api.get(IntroduceAPI.GET_ALL_INTRODUCE)
  return res.data?.data || res.data || []
}

// applicationId로 자기소개서 조회
export const getIntroduceByApplicationIdService = async (applicationId) => {
  // 전체 조회 후 applicationId로 필터링
  const allRes = await api.get(IntroduceAPI.GET_ALL_INTRODUCE)
  const allIntroduces = allRes.data?.data || allRes.data || []
  
  console.log('📋 전체 자기소개서 목록:', allIntroduces)
  
  // applicationId로 필터링
  const targetIntroduce = allIntroduces.find(item => 
    item.applicationId == applicationId
  )
  
  return targetIntroduce || null
}

// 자기소개서 업데이트
export const updateIntroduceService = async (introduceId, updateData) => {
  const res = await api.patch(`${IntroduceAPI.UPDATE_INTRODUCE(introduceId)}`, updateData)
  console.log('✅ 자기소개서 업데이트 성공:', res.data)
  return res.data?.data || res.data
}

// 자기소개서 생성
export const createIntroduceService = async (payload) => {
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
  return res.data?.data || res.data
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
    
    // 🔍 디버깅: 전체 자기소개서 데이터 구조 확인
    console.log('📊 전체 자기소개서 데이터:', allIntroduces)
    console.log('🔍 찾고 있는 applicationId:', applicationId, '(타입:', typeof applicationId, ')')
    
    // 각 자기소개서의 applicationId 확인
    allIntroduces.forEach((item, index) => {
      console.log(`📋 자기소개서 ${index + 1}:`, {
        id: item.id,
        applicationId: item.applicationId,
        application_id: item.application_id,
        applicantId: item.applicantId,
        applicant_id: item.applicant_id,
        introduceTemplateId: item.introduceTemplateId,
        introduce_template_id: item.introduce_template_id,
        content: item.content?.substring(0, 50) + '...',
        전체_데이터: item
      })
    })
    
    const introduce = allIntroduces.find(item => {
      // application_id (snake_case) 우선으로 매칭 시도
      const match = item.application_id == applicationId || 
                   item.applicationId == applicationId ||
                   String(item.application_id) === String(applicationId) ||
                   String(item.applicationId) === String(applicationId)
      
      if (match) {
        console.log('✅ 매칭된 자기소개서 (applicationId):', item)
        return true
      }
      
      // 📍 Fallback: applicantId로 매칭 시도 (API에서 application_id가 undefined인 경우)
      // URL에서 applicantId 가져오기
      const urlParams = new URLSearchParams(window.location.search)
      const applicantIdFromUrl = urlParams.get('applicantId')
      
      if (applicantIdFromUrl && (item.applicantId == applicantIdFromUrl || item.applicant_id == applicantIdFromUrl)) {
        console.log('✅ 매칭된 자기소개서 (applicantId fallback):', item)
        console.log('🔍 매칭 조건:', { 
          itemApplicantId: item.applicantId, 
          urlApplicantId: applicantIdFromUrl,
          applicationId: applicationId 
        })
        return true
      }
      
      return false
    })
    
    if (!introduce) {
      console.log('❌ 자기소개서가 없습니다.')
      console.log('🔍 매칭 시도한 조건들:')
      console.log('- item.application_id == applicationId (주요)')
      console.log('- item.applicationId == applicationId') 
      console.log('- String(item.application_id) === String(applicationId)')
      console.log('- String(item.applicationId) === String(applicationId)')
      return { introduce: null, templateItems: [], responses: [] }
    }
    
    console.log('✅ 자기소개서 발견:', introduce)
    
    // 2. recruitment 테이블에서 introduce_template_id 조회
    let introduceTemplateId = null
    try {
      // application → recruitment 관계를 통해 template ID 찾기
      const applicationRes = await api.get(`/api/v1/employment/application/${applicationId}`)
      const application = applicationRes.data?.data || applicationRes.data
      
      if (application && application.recruitmentId) {
        const recruitmentRes = await api.get(`/api/v1/employment/recruitments/${application.recruitmentId}`)
        const recruitment = recruitmentRes.data?.data || recruitmentRes.data
        
        if (recruitment && recruitment.introduceTemplateId) {
          introduceTemplateId = recruitment.introduceTemplateId
          console.log('✅ recruitment에서 introduce_template_id 발견:', introduceTemplateId)
        }
      }
    } catch (recruitmentError) {
      console.warn('recruitment 정보 조회 실패:', recruitmentError)
    }
    
    // 3. 템플릿 항목들 조회
    let templateItems = []
    if (introduceTemplateId) {
      try {
        const itemsRes = await api.get(IntroduceAPI.GET_ALL_TEMPLATE_ITEMS)
        const allItems = itemsRes.data?.data || itemsRes.data || []
        templateItems = allItems.filter(item => 
          item.introduceTemplateId == introduceTemplateId
        )
        console.log('✅ 템플릿 항목들:', templateItems)
      } catch (templateError) {
        console.warn('템플릿 항목 조회 실패:', templateError)
      }
    }
    
    // 4. 자기소개서 템플릿 항목별 응답 조회 (전체 조회 후 필터링)
    let responses = []
    try {
      const responsesRes = await api.get(IntroduceAPI.GET_ALL_TEMPLATE_ITEM_RESPONSES)
      const allResponses = responsesRes.data?.data || responsesRes.data || []
      
      // introduce.id로 필터링 (introduce_template_item_response 테이블의 introduce_id와 매칭)
      responses = allResponses.filter(response => {
        // introduce_id (snake_case) 우선으로 매칭 시도
        return response.introduce_id == introduce.id || 
               response.introduceId == introduce.id ||
               // 또는 application_id로 직접 매칭 (만약 response에 application_id가 있다면)
               response.application_id == applicationId ||
               response.applicationId == applicationId
      })
      
      console.log('✅ 필터링된 템플릿 항목 응답들:', responses)
      console.log('🔍 필터링 조건:', { 
        introduceId: introduce.id, 
        applicationId: applicationId,
        totalResponses: allResponses.length,
        filteredResponses: responses.length 
      })
      
      // 디버깅을 위해 전체 응답 데이터의 구조 확인
      if (allResponses.length > 0) {
        console.log('📊 전체 응답 데이터 샘플:', allResponses[0])
      }
      
    } catch (responseError) {
      console.warn('템플릿 항목 응답 조회 실패:', responseError)
    }
    
    return { introduce, templateItems, responses }
  } catch (error) {
    console.error('자기소개서 조회 실패:', error)
    throw error
  }
}
