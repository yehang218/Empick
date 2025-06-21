import api from '@/apis/apiClient'
import { IntroduceAPI } from '@/apis/routes/introduce'

export const fetchItems = async () => {
  const res = await api.get(IntroduceAPI.GET_ALL_TEMPLATE_ITEMS)
  return res.data.data
}

export const createItem = async ({ title, memberId, introduceTemplateId }) => {
  const res = await api.post(IntroduceAPI.CREATE_TEMPLATE_ITEM, { title, memberId, introduceTemplateId })
  return res.data.data
}

export const deleteItem = async (id) => {
  await api.delete(IntroduceAPI.DELETE_TEMPLATE_ITEM(id))
}
