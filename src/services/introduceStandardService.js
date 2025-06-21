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

export const createIntroduceStandard = async (content, memberId, itemIds) => {
  return apiClient.post(IntroduceAPI.CREATE_STANDARD, { content, memberId, itemIds })
}

export const fetchIntroduceStandardDetail = async (id) => {
  const response = await apiClient.get(IntroduceAPI.GET_STANDARD_BY_ID(id))
  return response.data
}
