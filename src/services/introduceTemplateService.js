import api from '@/apis/apiClient'

export const fetchTemplates = async () => {
  const res = await api.get('/api/v1/employment/introduce-template')
  return res.data.data
}

export const createTemplate = async (title, memberId, itemIds) => {
  const res = await api.post('/api/v1/employment/introduce-template', { title, memberId, itemIds })
  return res.data.data
}

export const fetchItems = async () => {
  const res = await api.get('/api/v1/employment/introduce-template/item')
  return res.data.data
}

export const fetchTemplateDetail = async (id) => {
  const res = await api.get(`/api/v1/employment/introduce-template/${id}`)
  return res.data
}

export const createItem = async (content, memberId) => {
  const res = await api.post('/api/v1/employment/introduce-template-item', { content, memberId })
  return res.data.data
}

export const deleteTemplate = async (id) => {
  await api.delete(`/api/v1/employment/introduce-template/${id}`)
}
