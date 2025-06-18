import api from '@/apis/apiClient'

export const fetchItems = async () => {
  const res = await api.get('/api/v1/employment/introduce-template/item')
  return res.data.data
}

export const createItem = async ({ title, memberId, introduceTemplateId }) => {
  const res = await api.post('/api/v1/employment/introduce-template-item', { title, memberId, introduceTemplateId })
  return res.data.data
}

export const deleteItem = async (id) => {
  await api.delete(`/api/v1/employment/introduce-template-item/${id}`)
}
