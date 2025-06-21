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
  return apiClient.get(`/api/v1/employment/introduce-standard/items/standard/${standardId}`)
}
