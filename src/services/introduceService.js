import api from '@/apis/apiClient'

export const fetchIntroduceItemsService = async (templateId) => {
  const res = await api.get(`/api/v1/introduce-template/${templateId}/items`)
  return res.data
}

export const createIntroduceItemService = async (dto) => {
  const res = await api.post('/api/v1/introduce-template/item', dto)
  return res.data
}

export const deleteIntroduceItemService = async (id) => {
  await api.delete(`/api/v1/introduce-template/item/${id}`)
}
