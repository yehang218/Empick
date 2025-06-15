import { defineStore } from 'pinia'
import { ref } from 'vue'
import {
  fetchIntroduceItemsService,
  createIntroduceItemService,
  deleteIntroduceItemService
} from '@/services/introduceService'
import { createTemplate } from '@/services/introduceTemplateService'
import api from '@/apis/apiClient'

export const useIntroduceStore = defineStore('introduce', () => {
  const items = ref([])
  const loading = ref(false)
  const error = ref(null)

  // 전체 항목 조회
  const fetchItems = async (templateId) => {
    loading.value = true
    error.value = null
    try {
      items.value = await fetchIntroduceItemsService(templateId)
    } catch (e) {
      error.value = e.message
    } finally {
      loading.value = false
    }
  }

  // 항목 추가
  const addItem = async (dto) => {
    loading.value = true
    error.value = null
    try {
      const newItem = await createIntroduceItemService(dto)
      items.value.push(newItem)
    } catch (e) {
      error.value = e.message
    } finally {
      loading.value = false
    }
  }

  // 항목 삭제
  const removeItem = async (id) => {
    loading.value = true
    error.value = null
    try {
      await deleteIntroduceItemService(id)
      items.value = items.value.filter(item => item.id !== id)
    } catch (e) {
      error.value = e.message
    } finally {
      loading.value = false
    }
  }

  return {
    items, loading, error,
    fetchItems, addItem, removeItem
  }
})

export const useIntroduceTemplateStore = defineStore('introduceTemplate', {
  actions: {
    async addTemplate(title, memberId, itemIds) {
      return await createTemplate({ title, memberId, itemIds })
    }
  }
})

export const fetchTemplates = async () => {
  const res = await api.get('/api/v1/employment/introduce-template')
  return res.data.data
}
