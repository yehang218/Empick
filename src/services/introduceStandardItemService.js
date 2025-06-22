import apiClient from '@/apis/apiClient'
import { IntroduceAPI } from '@/apis/routes/introduce'

export const fetchStandardItems = async () => {
  const response = await apiClient.get(IntroduceAPI.GET_ALL_STANDARD_ITEMS)
  return Array.isArray(response.data?.data) ? response.data.data : []
}

export const createStandardItem = async (content, memberId) => {
  return apiClient.post(IntroduceAPI.CREATE_STANDARD_ITEM, { content, memberId })
}

export const updateStandardItemsFk = async (standardId, itemIds) => {
  // itemIds: number[], standardId: number
  return apiClient.post(IntroduceAPI.UPDATE_STANDARD_ITEMS_FK, { standardId, itemIds })
}

export const patchStandardItemFk = async (itemId, introduceStandardId) => {
  return apiClient.patch(`${IntroduceAPI.CREATE_STANDARD_ITEM}/${itemId}`, { introduceStandardId })
}

export async function fetchItemsByStandardId(standardId) {
  const response = await apiClient.get(`/api/v1/employment/introduce-standard/items/standard/${standardId}`)
  console.log('🔍 기준표별 항목 조회 API 응답:', response.data)
  
  // 백엔드에서 직접 배열을 반환하는 경우
  if (Array.isArray(response.data)) {
    return { data: response.data }
  }
  
  // CustomApiResponse 형태인 경우
  if (response.data?.data) {
    return response.data
  }
  
  return response
}

export const deleteStandardItem = async (id) => {
  return apiClient.delete(`${IntroduceAPI.DELETE_STANDARD_ITEM(id)}`)
}
