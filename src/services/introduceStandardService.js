import apiClient from '@/apis/apiClient'
import { IntroduceAPI } from '@/apis/routes/introduce'

export const fetchIntroduceStandards = async () => {
  const response = await apiClient.get(IntroduceAPI.GET_ALL_STANDARDS)
  console.log('introduce-standard API 응답:', response.data)
  if (Array.isArray(response.data?.data)) return response.data.data
  return []
}

export const deleteIntroduceStandard = async (id) => {
  return apiClient.delete(IntroduceAPI.DELETE_STANDARD(id))
}

export const createIntroduceStandard = async (title, memberId, itemIds) => {
  return apiClient.post(IntroduceAPI.CREATE_STANDARD, { content:title, memberId, itemIds })
}
