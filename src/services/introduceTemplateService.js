import api from '@/apis/apiClient'
import { IntroduceAPI } from '@/apis/routes/introduce'

export const fetchTemplates = async () => {
  const res = await api.get(IntroduceAPI.GET_ALL_TEMPLATES)
  return res.data.data
}

export const createTemplate = async (title, memberId, itemIds) => {
  const res = await api.post(IntroduceAPI.CREATE_TEMPLATE, { title, memberId, itemIds })
  return res.data.data
}

export const fetchItems = async () => {
  const res = await api.get(IntroduceAPI.GET_ALL_TEMPLATE_ITEMS)
  return res.data.data
}

export const fetchTemplateDetail = async (id) => {
  const res = await api.get(IntroduceAPI.GET_TEMPLATE_BY_ID(id))
  return res.data
}

export const createItem = async ({ title, memberId, introduceTemplateId }) => {
  const res = await api.post(IntroduceAPI.CREATE_TEMPLATE_ITEM, { title, memberId, introduceTemplateId })
  return res.data.data
}

export const deleteTemplate = async (id) => {
  await api.delete(IntroduceAPI.DELETE_TEMPLATE(id))
}
