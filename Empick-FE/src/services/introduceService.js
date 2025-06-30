import api from '@/apis/apiClient'
import { IntroduceAPI } from '@/apis/routes/introduce'
import ApiResponseDTO from '@/dto/common/apiResponseDTO'

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
  console.log('📤 자기소개서 평가 결과 저장 요청:', payload)
  console.log('🔍 payload.introduceId 상세:', {
    value: payload.introduceId,
    type: typeof payload.introduceId,
    isNull: payload.introduceId === null,
    isUndefined: payload.introduceId === undefined
  })
  
  // 🔒 중복 방지: introduce_id로 기존 평가 결과 확인
  let existingRatingResult = null
  if (!payload.introduceId) {
    throw new Error('introduceId가 필요합니다. 자기소개서 정보를 확인해주세요.')
  }

  try {
    console.log('🔍 introduce_id로 기존 평가 결과 조회:', payload.introduceId)
    
    // 최대 3번 재시도로 안정성 확보
    let retryCount = 0
    const maxRetries = 3
    
    while (retryCount < maxRetries) {
      try {
        existingRatingResult = await getIntroduceRatingResultByIntroduceId(payload.introduceId)
        break // 성공하면 반복 종료
      } catch (retryError) {
        retryCount++
        console.warn(`⚠️ 평가 결과 조회 실패 (${retryCount}/${maxRetries}):`, retryError.message)
        
        if (retryCount >= maxRetries) {
          console.error('❌ 최대 재시도 횟수 초과')
          break
        }
        
        // 100ms 대기 후 재시도
        await new Promise(resolve => setTimeout(resolve, 100))
      }
    }
    
    if (existingRatingResult) {
      console.log('✅ 기존 평가 결과 발견 - UPDATE 모드:', {
        id: existingRatingResult.id,
        introduce_id: existingRatingResult.introduce_id,
        rating_score: existingRatingResult.rating_score,
        member_id: existingRatingResult.member_id
      })
    } else {
      console.log('ℹ️ 기존 평가 결과 없음 - CREATE 모드')
    }
  } catch (error) {
    console.warn('⚠️ 기존 평가 결과 조회 전체 실패:', error.message)
    existingRatingResult = null
  }
  
  // 백엔드가 기대하는 필드명으로 변환 (snake_case와 camelCase 모두 시도)
  const requestData = {
    content: payload.content,
    ratingScore: payload.ratingScore,
    rating_score: payload.ratingScore, // snake_case 버전
    introduceId: payload.introduceId,
    introduce_id: payload.introduceId, // snake_case 버전 (중요!)
    introduceStandardId: payload.introduceStandardId || payload.standardId || null,
    introduce_standard_id: payload.introduceStandardId || payload.standardId || null, // snake_case 버전
    memberId: payload.memberId || 1,
    member_id: payload.memberId || 1 // snake_case 버전
  }
  
  console.log('📤 백엔드 전송 데이터:', requestData)
  console.log('🔍 requestData.introduce_id 상세:', {
    value: requestData.introduce_id,
    type: typeof requestData.introduce_id,
    isNull: requestData.introduce_id === null,
    isUndefined: requestData.introduce_id === undefined
  })
  
  if (!requestData.introduceId && !requestData.introduce_id) {
    throw new Error('introduceId가 필요합니다. 자기소개서 정보를 확인해주세요.')
  }
  
  try {
    let response
    let isUpdated = false
    
    // 기존 평가 결과가 있으면 UPDATE 시도
    if (existingRatingResult && existingRatingResult.id) {
      console.log('🔄 기존 평가 결과 업데이트 시도... (ID:', existingRatingResult.id, ')')
      
      try {
        response = await api.patch(IntroduceAPI.UPDATE_RATING_RESULT(existingRatingResult.id), requestData)
        console.log('✅ 평가 결과 업데이트 성공:', response.data)
        isUpdated = true
      } catch (updateError) {
        console.warn('⚠️ 평가 결과 업데이트 실패, CREATE로 fallback:', updateError.message)
        console.log('🔄 기존 평가 결과 삭제 후 새로 생성...')
        
        // 기존 평가 결과 삭제 시도
        try {
          await api.delete(IntroduceAPI.DELETE_RATING_RESULT(existingRatingResult.id))
          console.log('✅ 기존 평가 결과 삭제 성공:', existingRatingResult.id)
        } catch (deleteError) {
          console.warn('⚠️ 기존 평가 결과 삭제 실패:', deleteError.message)
        }
        
        // 새로 생성
        response = await api.post(IntroduceAPI.CREATE_RATING_RESULT, requestData)
        console.log('✅ 평가 결과 새로 생성 성공:', response.data)
        isUpdated = false
      }
    } else {
      // CREATE 전에 한 번 더 중복 체크 (race condition 방지)
      console.log('🔍 CREATE 직전 중복 재확인...')
      const lastCheckResult = await getIntroduceRatingResultByIntroduceId(payload.introduceId)
      
      if (lastCheckResult && lastCheckResult.id) {
        console.log('⚠️ CREATE 직전에 다른 평가 결과 발견! UPDATE로 전환:', lastCheckResult.id)
        
        try {
          response = await api.patch(IntroduceAPI.UPDATE_RATING_RESULT(lastCheckResult.id), requestData)
          console.log('✅ 긴급 UPDATE 성공:', response.data)
          isUpdated = true
          existingRatingResult = lastCheckResult // 나중에 ID 추출용
        } catch (emergencyUpdateError) {
          console.warn('⚠️ 긴급 UPDATE 실패, 기존 결과 삭제 후 CREATE:', emergencyUpdateError.message)
          
          // 기존 결과 삭제 후 CREATE
          try {
            await api.delete(IntroduceAPI.DELETE_RATING_RESULT(lastCheckResult.id))
            console.log('✅ 중복 평가 결과 삭제 성공:', lastCheckResult.id)
          } catch (deleteError) {
            console.warn('⚠️ 중복 평가 결과 삭제 실패:', deleteError.message)
          }
          
          response = await api.post(IntroduceAPI.CREATE_RATING_RESULT, requestData)
          console.log('✅ 삭제 후 새로운 평가 결과 생성 성공:', response.data)
          isUpdated = false
        }
      } else {
        console.log('🔄 중복 없음 확인 - 새로운 평가 결과 생성 시도...')
        response = await api.post(IntroduceAPI.CREATE_RATING_RESULT, requestData)
        console.log('✅ 평가 결과 생성 성공:', response.data)
        isUpdated = false
      }
    }
    
    // 2. 저장된 평가 결과의 ID 추출
    let ratingResultId = null
    
    if (isUpdated && existingRatingResult?.id) {
      // UPDATE의 경우: 기존 평가 결과 ID 사용
      ratingResultId = existingRatingResult.id
      console.log('✅ UPDATE - 기존 평가 결과 ID 사용:', ratingResultId)
    } else {
      // CREATE의 경우: 응답에서 ID 추출
      console.log('🔍 CREATE - 응답에서 ID 추출 시도...')
      
      // 여러 가능한 구조에서 ID 추출 시도
      if (response.data?.data?.id) {
        ratingResultId = response.data.data.id
        console.log('✅ response.data.data.id에서 ID 추출:', ratingResultId)
      } else if (response.data?.id) {
        ratingResultId = response.data.id
        console.log('✅ response.data.id에서 ID 추출:', ratingResultId)
      } else if (response.data?.data) {
        // data 객체 전체 구조 확인
        console.log('🔍 data 객체 전체 구조 확인:', response.data.data)
        
        // 가능한 ID 필드들 시도
        ratingResultId = response.data.data.ratingResultId || 
                        response.data.data.rating_result_id ||
                        response.data.data.introduceRatingResultId ||
                        response.data.data.introduce_rating_result_id ||
                        response.data.data.resultId ||
                        response.data.data.result_id
        
        if (ratingResultId) {
          console.log('✅ 대체 필드에서 ID 추출:', ratingResultId)
        }
      }
    }
    
    console.log('🔍 최종 추출된 평가 결과 ID:', ratingResultId)
    console.log('🔍 전체 응답 구조 확인:', JSON.stringify(response.data, null, 2))
    
    // 3. application 테이블의 introduce_rating_result_id 업데이트
    // ID를 찾지 못한 경우에만 fallback으로 재조회
    if (!ratingResultId && payload.introduceId) {
      console.log('🔄 ID 추출 실패 - fallback으로 최근 평가 결과 재조회 시도...')
      try {
        // introduceId로 방금 저장한 평가 결과 재조회
        const recentEvaluation = await getIntroduceRatingResultByIntroduceId(payload.introduceId)
        if (recentEvaluation && recentEvaluation.id) {
          ratingResultId = recentEvaluation.id
          console.log('✅ fallback으로 평가 결과 ID 발견:', ratingResultId)
        } else {
          console.warn('⚠️ fallback 재조회에서도 평가 결과를 찾을 수 없습니다.')
        }
      } catch (fallbackError) {
        console.warn('⚠️ fallback 재조회 실패:', fallbackError.message)
      }
    }
    
    if (ratingResultId && payload.applicationId) {
      try {
        console.log('🔄 application.introduce_rating_result_id 업데이트 시작:', {
          applicationId: payload.applicationId,
          ratingResultId: ratingResultId,
          ratingResultIdType: typeof ratingResultId,
          applicationIdType: typeof payload.applicationId
        })
        
        // 수정된 ApplicationCommandDTO를 사용하는 업데이트 서비스 호출
        const { updateApplicationIntroduceRatingResultService } = await import('@/services/applicationService')
        const updateResult = await updateApplicationIntroduceRatingResultService(payload.applicationId, ratingResultId)
        
        console.log('✅ application.introduce_rating_result_id 업데이트 완료:', updateResult)
        console.log('🔍 업데이트된 application 정보:', {
          id: updateResult?.id,
          introduceRatingResultId: updateResult?.introduceRatingResultId,
          introduce_rating_result_id: updateResult?.introduce_rating_result_id
        })
      } catch (updateError) {
        console.error('❌ application.introduce_rating_result_id 업데이트 실패:', updateError)
        console.error('❌ 업데이트 에러 상세:', {
          message: updateError.message,
          response: updateError.response?.data,
          status: updateError.response?.status,
          config: updateError.config
        })
        // 평가 결과는 이미 저장되었으므로 업데이트 실패는 경고로만 처리
        console.warn('⚠️ 평가 결과는 저장되었지만 application 연결 업데이트에 실패했습니다.')
      }
    } else {
      console.warn('⚠️ ratingResultId 또는 applicationId가 없어 application 업데이트를 건너뜁니다.', {
        ratingResultId,
        ratingResultIdType: typeof ratingResultId,
        applicationId: payload.applicationId,
        applicationIdType: typeof payload.applicationId,
        ratingResultIdTruthy: !!ratingResultId,
        applicationIdTruthy: !!payload.applicationId
      })
    }
    
    return response
  } catch (error) {
    console.error('❌ 평가 결과 저장 실패:', error)
    console.error('❌ 에러 응답:', error.response?.data)
    throw error
  }
}

// ID로 평가 결과 직접 조회 (가장 효율적인 방법)
export const getIntroduceRatingResultById = async (ratingResultId) => {
  try {
    console.log('🔍 평가 결과 ID로 직접 조회:', ratingResultId)
    const response = await api.get(IntroduceAPI.GET_RATING_RESULT_BY_ID(ratingResultId))
    const apiResponse = ApiResponseDTO.fromJSON(response.data)
    
    if (!apiResponse.success) {
      console.warn('⚠️ 평가 결과 조회 실패:', apiResponse.message)
      return null
    }
    
    console.log('✅ 평가 결과 ID 직접 조회 성공:', apiResponse.data)
    return apiResponse.data
  } catch (error) {
    console.warn('⚠️ 평가 결과 ID 직접 조회 실패:', error.message)
    console.log('🔄 전체 조회 후 ID 필터링으로 Fallback 시도...')
    
    // Fallback: 전체 조회 후 ID로 필터링
    try {
      const allResults = await getAllIntroduceRatingResults()
      const result = allResults.find(item => {
        const match = item.id == ratingResultId || 
                     String(item.id) === String(ratingResultId)
        
        if (match) {
          console.log('✅ 전체 조회에서 평가 결과 ID 매칭 성공:', item)
        }
        
        return match
      })
      
      if (result) {
        console.log('✅ Fallback으로 평가 결과 조회 성공:', result)
        return result
      } else {
        console.log('ℹ️ 해당 ID의 평가 결과가 없습니다:', ratingResultId)
        return null
      }
    } catch (fallbackError) {
      console.error('❌ Fallback 조회도 실패:', fallbackError.message)
      return null
    }
  }
}

// 전체 평가 결과 조회 (Fallback용)
export const getAllIntroduceRatingResults = async () => {
  try {
    console.log('🔍 전체 자기소개서 평가 결과 조회')
    const response = await api.get(IntroduceAPI.GET_ALL_RATING_RESULTS)
    const apiResponse = ApiResponseDTO.fromJSON(response.data)
    
    if (!apiResponse.success) {
      console.warn('⚠️ 전체 평가 결과 조회 실패:', apiResponse.message)
      return []
    }
    
    console.log('✅ 전체 평가 결과 조회 성공:', apiResponse.data?.length || 0, '개')
    return apiResponse.data || []
  } catch (error) {
    console.warn('⚠️ 전체 평가 결과 조회 실패:', error.message)
    return []
  }
}

// 🧹 전체 시스템 중복 데이터 정리 (introduce_id 기준)
export const cleanupDuplicateRatingResults = async () => {
  try {
    console.log('🧹 시스템 전체 중복 평가 결과 정리 시작...')
    
    const allResults = await getAllIntroduceRatingResults()
    console.log('📊 전체 평가 결과 개수:', allResults.length)
    
    // introduce_id별로 그룹화
    const groupedByIntroduceId = {}
    allResults.forEach(result => {
      const introduceId = result.introduce_id || result.introduceId
      if (introduceId) {
        if (!groupedByIntroduceId[introduceId]) {
          groupedByIntroduceId[introduceId] = []
        }
        groupedByIntroduceId[introduceId].push(result)
      }
    })
    
    let totalDuplicatesRemoved = 0
    
    // 각 introduce_id별로 중복 제거
    for (const [introduceId, results] of Object.entries(groupedByIntroduceId)) {
      if (results.length > 1) {
        console.log(`🔍 introduce_id ${introduceId}에서 ${results.length}개 중복 발견`)
        
        // ID 기준 내림차순 정렬 (최신 데이터 우선)
        const sortedResults = results.sort((a, b) => b.id - a.id)
        const latestResult = sortedResults[0]
        const duplicates = sortedResults.slice(1)
        
        console.log(`✅ 최신 결과 유지: ID ${latestResult.id}`)
        console.log(`🗑️ 삭제 대상: ${duplicates.map(d => d.id).join(', ')}`)
        
        // 중복 데이터 삭제
        for (const duplicate of duplicates) {
          try {
            await api.delete(IntroduceAPI.DELETE_RATING_RESULT(duplicate.id))
            console.log(`✅ 중복 데이터 삭제 성공: ID ${duplicate.id}`)
            totalDuplicatesRemoved++
          } catch (deleteError) {
            console.warn(`⚠️ 중복 데이터 삭제 실패: ID ${duplicate.id}`, deleteError.message)
          }
        }
      }
    }
    
    console.log(`🎉 중복 데이터 정리 완료! 총 ${totalDuplicatesRemoved}개 삭제됨`)
    
    return {
      success: true,
      totalChecked: allResults.length,
      duplicatesRemoved: totalDuplicatesRemoved,
      groupCount: Object.keys(groupedByIntroduceId).length
    }
    
  } catch (error) {
    console.error('❌ 중복 데이터 정리 실패:', error.message)
    return {
      success: false,
      error: error.message
    }
  }
}

// introduceId로 평가 결과 조회 (전체 조회 후 필터링)
export const getIntroduceRatingResultByIntroduceId = async (introduceId) => {
  try {
    console.log('🔍 자기소개서 평가 결과 조회 (introduceId):', introduceId, '타입:', typeof introduceId)
    
    // 전체 평가 결과 조회
    const allResults = await getAllIntroduceRatingResults()
    console.log('🔍 전체 평가 결과 개수:', allResults.length)
    console.log('🔍 전체 평가 결과 목록:', allResults.map(item => ({
      id: item.id,
      introduce_id: item.introduce_id,
      introduceId: item.introduceId,
      content: item.content?.substring(0, 30) + '...'
    })))
    
    // introduceId로 필터링 (다양한 형태로 매칭 시도)
    const matchingResults = allResults.filter(item => {
      const itemIntroduceId1 = item.introduce_id
      const itemIntroduceId2 = item.introduceId
      const targetId = introduceId
      
      // 숫자/문자열 변환하여 비교
      const match = 
        itemIntroduceId1 == targetId || 
        itemIntroduceId2 == targetId ||
        String(itemIntroduceId1) === String(targetId) ||
        String(itemIntroduceId2) === String(targetId) ||
        Number(itemIntroduceId1) === Number(targetId) ||
        Number(itemIntroduceId2) === Number(targetId)
      
      if (match) {
        console.log('🎯 매칭된 평가 결과:', {
          id: item.id,
          introduce_id: itemIntroduceId1,
          introduceId: itemIntroduceId2,
          rating_score: item.rating_score,
          content: item.content?.substring(0, 50) + '...',
          매칭조건: {
            'item.introduce_id == targetId': itemIntroduceId1 == targetId,
            'item.introduceId == targetId': itemIntroduceId2 == targetId,
            'String 비교1': String(itemIntroduceId1) === String(targetId),
            'String 비교2': String(itemIntroduceId2) === String(targetId)
          }
        })
      }
      
      return match
    })
    
    console.log('🔍 매칭된 평가 결과 개수:', matchingResults.length)
    
    if (matchingResults.length > 1) {
      console.warn('⚠️ 여러 개의 평가 결과가 매칭됨:', matchingResults.length, '개')
      console.log('⚠️ 중복 평가 결과들:', matchingResults.map(item => ({
        id: item.id,
        introduce_id: item.introduce_id,
        rating_score: item.rating_score,
        content: item.content?.substring(0, 30)
      })))
      
      // 🗑️ 중복 제거: 가장 최근 것만 남기고 나머지는 삭제
      const sortedResults = matchingResults.sort((a, b) => b.id - a.id) // ID 내림차순 정렬
      const latestResult = sortedResults[0] // 가장 최근 결과
      const duplicateResults = sortedResults.slice(1) // 중복된 결과들
      
      console.log('🗑️ 중복 평가 결과 삭제 시작...', duplicateResults.length, '개')
      
      // 중복된 평가 결과들 삭제
      for (const duplicate of duplicateResults) {
        try {
          console.log('🗑️ 중복 평가 결과 삭제 시도:', duplicate.id)
          await api.delete(IntroduceAPI.DELETE_RATING_RESULT(duplicate.id))
          console.log('✅ 중복 평가 결과 삭제 성공:', duplicate.id)
        } catch (deleteError) {
          console.warn('⚠️ 중복 평가 결과 삭제 실패:', duplicate.id, deleteError.message)
        }
      }
      
      console.log('✅ 최신 평가 결과 선택:', latestResult.id)
      return latestResult
      
    } else if (matchingResults.length === 1) {
      const result = matchingResults[0]
      console.log('✅ 평가 결과 조회 성공:', result.id)
      return result
    } else {
      console.log('ℹ️ 해당 introduceId의 평가 결과가 없습니다:', introduceId)
      console.log('🔍 확인된 introduce_id 값들:', allResults.map(item => ({
        id: item.id,
        introduce_id: item.introduce_id,
        introduceId: item.introduceId
      })))
      return null
    }
  } catch (error) {
    console.error('❌ 평가 결과 조회 실패:', error.message)
    return null
  }
}

// applicationId로 평가 결과 조회 (전체 조회 후 필터링)
export const getIntroduceRatingResultByApplicationId = async (applicationId) => {
  try {
    console.log('🔍 자기소개서 평가 결과 조회 (applicationId):', applicationId)
    
    // 1. 먼저 applicationId로 introduce를 찾기
    let introduceId = null
    try {
      const introduceData = await getIntroduceByApplicationIdService(applicationId)
      if (introduceData && introduceData.id) {
        introduceId = introduceData.id
        console.log('✅ applicationId로 introduceId 발견:', introduceId)
      }
    } catch (introduceError) {
      console.warn('⚠️ applicationId로 introduce 조회 실패:', introduceError.message)
    }
    
    // 2. introduceId가 있으면 평가 결과 조회
    if (introduceId) {
      return await getIntroduceRatingResultByIntroduceId(introduceId)
    }
    
    // 3. Fallback: 전체 조회 후 application_id 필드로 직접 매칭 시도
    console.log('🔄 Fallback: 전체 평가 결과에서 applicationId 직접 매칭 시도')
    const allResults = await getAllIntroduceRatingResults()
    
    const result = allResults.find(item => {
      const match = item.application_id == applicationId || 
                   item.applicationId == applicationId ||
                   String(item.application_id) === String(applicationId) ||
                   String(item.applicationId) === String(applicationId)
      
      if (match) {
        console.log('✅ 매칭된 평가 결과 (applicationId):', item)
      }
      
      return match
    })
    
    if (result) {
      console.log('✅ 평가 결과 조회 성공:', result)
      return result
    } else {
      console.log('ℹ️ 해당 applicationId의 평가 결과가 없습니다:', applicationId)
      return null
    }
  } catch (error) {
    console.warn('⚠️ 평가 결과 조회 실패:', error.message)
    return null
  }
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
  try {
    console.log('🔍 새로운 API로 자기소개서 조회:', applicationId)
    const response = await api.get(IntroduceAPI.GET_INTRODUCE_BY_APPLICATION_ID(applicationId))
    const apiResponse = ApiResponseDTO.fromJSON(response.data)
    
    if (!apiResponse.success) {
      throw new Error(apiResponse.message || '자기소개서 조회 실패')
    }
    
    console.log('✅ 새로운 API로 자기소개서 조회 성공:', apiResponse.data)
    return apiResponse.data
  } catch (error) {
    console.error('❌ 새로운 API 자기소개서 조회 실패:', error)
    throw error
  }
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
    
    // 1. 새로운 API를 사용해서 applicationId로 직접 자기소개서 조회
    let introduce = null
    try {
      const introduceRes = await api.get(IntroduceAPI.GET_INTRODUCE_BY_APPLICATION_ID(applicationId))
      const apiResponse = ApiResponseDTO.fromJSON(introduceRes.data)
      
      if (apiResponse.success && apiResponse.data) {
        introduce = apiResponse.data
        console.log('✅ 새로운 API로 자기소개서 발견:', introduce)
      }
    } catch (directError) {
      console.warn('⚠️ 새로운 API 조회 실패, 기존 방식으로 fallback:', directError.message)
      
      // Fallback: 기존 방식 (전체 조회 후 필터링)
    const introduceRes = await api.get(`${IntroduceAPI.GET_ALL_INTRODUCE}`)
    const allIntroduces = introduceRes.data?.data || introduceRes.data || []
    
      // 🔍 디버깅: 전체 자기소개서 데이터 구조 확인
      console.log('📊 Fallback - 전체 자기소개서 데이터:', allIntroduces)
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
      
      introduce = allIntroduces.find(item => {
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
    }
    
    if (!introduce) {
      console.log('❌ 자기소개서가 없습니다.')
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
    
    // 3. 자기소개서 템플릿 항목별 응답을 먼저 조회해서 필요한 템플릿 항목 ID들을 파악
    let responses = []
    let templateItemIds = []
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
      
      // 응답에서 필요한 템플릿 항목 ID들 추출
      templateItemIds = responses.map(response => response.introduceTemplateItemId).filter(Boolean)
      
      console.log('✅ 필터링된 템플릿 항목 응답들:', responses)
      console.log('🔍 필요한 템플릿 항목 ID들:', templateItemIds)
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

    // 4. 응답에서 나온 템플릿 항목 ID들로 템플릿 항목들 조회
    let templateItems = []
    if (templateItemIds.length > 0) {
      try {
        const itemsRes = await api.get(IntroduceAPI.GET_ALL_TEMPLATE_ITEMS)
        const allItems = itemsRes.data?.data || itemsRes.data || []
        
        // 응답에서 나온 템플릿 항목 ID들로 필터링
        templateItems = allItems.filter(item => 
          templateItemIds.includes(item.id)
        )
        console.log('✅ 응답 기반 템플릿 항목들:', templateItems)
      } catch (templateError) {
        console.warn('템플릿 항목 조회 실패:', templateError)
        
        // Fallback: introduceTemplateId로 필터링 시도 (기존 방식)
        if (introduceTemplateId) {
          try {
          const itemsRes = await api.get(IntroduceAPI.GET_ALL_TEMPLATE_ITEMS)
          const allItems = itemsRes.data?.data || itemsRes.data || []
          templateItems = allItems.filter(item => 
              item.introduceTemplateId == introduceTemplateId
          )
            console.log('✅ Fallback 템플릿 항목들:', templateItems)
          } catch (fallbackError) {
            console.warn('Fallback 템플릿 항목 조회도 실패:', fallbackError)
          }
        }
      }
    }
    
    // 5. 최종 결과 반환 (responses는 이미 위에서 조회됨)
    
    return { introduce, templateItems, responses }
  } catch (error) {
    console.error('자기소개서 조회 실패:', error)
    throw error
  }
}

// 🔍 디버깅: 평가 저장 과정 상세 분석
export const debugIntroduceRatingProcess = async (introduceId) => {
  console.log('🔍 === 평가 저장 과정 디버깅 시작 ===')
  console.log('🔍 대상 introduce_id:', introduceId)
  
  try {
    // 1. 전체 평가 결과 조회
    const allResults = await getAllIntroduceRatingResults()
    console.log('📊 전체 평가 결과 개수:', allResults.length)
    
    // 2. 해당 introduce_id의 평가 결과들 찾기
    const targetResults = allResults.filter(result => {
      const itemIntroduceId = result.introduce_id || result.introduceId
      return itemIntroduceId == introduceId || String(itemIntroduceId) === String(introduceId)
    })
    
    console.log(`🎯 introduce_id ${introduceId}의 평가 결과:`, targetResults.length, '개')
    
    if (targetResults.length === 0) {
      console.log('✅ 중복 없음 - CREATE 해야 함')
      return { 
        duplicates: [],
        shouldCreate: true,
        shouldUpdate: false
      }
    } else if (targetResults.length === 1) {
      console.log('✅ 기존 평가 1개 발견 - UPDATE 해야 함')
      console.log('📋 기존 평가 정보:', {
        id: targetResults[0].id,
        introduce_id: targetResults[0].introduce_id,
        rating_score: targetResults[0].rating_score,
        content: targetResults[0].content?.substring(0, 50) + '...'
      })
      return {
        duplicates: [],
        existing: targetResults[0],
        shouldCreate: false,
        shouldUpdate: true
      }
    } else {
      console.warn('⚠️ 중복 평가 결과 발견:', targetResults.length, '개')
      targetResults.forEach((result, index) => {
        console.log(`📋 중복 ${index + 1}:`, {
          id: result.id,
          introduce_id: result.introduce_id,
          rating_score: result.rating_score,
          created_at: result.created_at || '알 수 없음'
        })
      })
      
      // 최신 것 선택
      const sorted = targetResults.sort((a, b) => b.id - a.id)
      const latest = sorted[0]
      const duplicates = sorted.slice(1)
      
      console.log('✅ 최신 평가 결과 선택:', latest.id)
      console.log('🗑️ 삭제할 중복 평가:', duplicates.map(d => d.id))
      
      return {
        duplicates: duplicates,
        existing: latest,
        shouldCreate: false,
        shouldUpdate: true
      }
    }
    
  } catch (error) {
    console.error('❌ 디버깅 과정 중 오류:', error.message)
    return {
      error: error.message,
      duplicates: [],
      shouldCreate: true,
      shouldUpdate: false
    }
  }
}

// 🧪 간단한 평가 저장 테스트 (실제 저장 안함)
export const testIntroduceRatingProcess = async (introduceId) => {
  console.log('🧪 === 평가 저장 테스트 (실제 저장 안함) ===')
  
  const debugResult = await debugIntroduceRatingProcess(introduceId)
  
  console.log('🔍 디버깅 결과:', debugResult)
  
  if (debugResult.shouldUpdate && debugResult.existing) {
    console.log('📝 UPDATE 시뮬레이션:')
    console.log('- URL:', IntroduceAPI.UPDATE_RATING_RESULT(debugResult.existing.id))
    console.log('- 기존 ID:', debugResult.existing.id)
    console.log('- introduce_id:', debugResult.existing.introduce_id)
    
    // 중복 삭제 시뮬레이션
    if (debugResult.duplicates.length > 0) {
      console.log('🗑️ 중복 삭제 시뮬레이션:')
      debugResult.duplicates.forEach(duplicate => {
        console.log('- 삭제 URL:', IntroduceAPI.DELETE_RATING_RESULT(duplicate.id))
        console.log('- 삭제 ID:', duplicate.id)
      })
    }
  } else if (debugResult.shouldCreate) {
    console.log('📝 CREATE 시뮬레이션:')
    console.log('- URL:', IntroduceAPI.CREATE_RATING_RESULT)
  }
  
  return debugResult
}
