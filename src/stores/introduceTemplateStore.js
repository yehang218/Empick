import { defineStore } from 'pinia'
import { fetchTemplates, createTemplate } from '@/services/introduceTemplateService'

export const useIntroduceTemplateStore = defineStore('introduceTemplate', {
  state: () => ({
    templates: []
  }),
  actions: {
    async loadTemplates() {
      this.templates = await fetchTemplates()
    },
    async addTemplate(title, memberId, itemIds) {
      return await createTemplate({ title, memberId, itemIds })
    }
  }
})
