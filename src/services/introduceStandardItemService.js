import apiClient from '@/apis/apiClient'
import { IntroduceAPI } from '@/apis/routes/introduce'

export const fetchStandardItems = async () => {
  const response = await apiClient.get(IntroduceAPI.GET_ALL_STANDARD_ITEMS)
  return Array.isArray(response.data?.data) ? response.data.data : []
}

export const createStandardItem = async (content, memberId) => {
  return apiClient.post(IntroduceAPI.CREATE_STANDARD_ITEM, { content, memberId })
}
