import { defineStore } from 'pinia'
import { fetchTemplates, createTemplate, fetchTemplateDetail, deleteTemplate } from '@/services/introduceTemplateService'

export const useIntroduceTemplateStore = defineStore('introduceTemplate', {
  state: () => ({
    templates: [],
    selectedTemplate: null,
  }),
  actions: {
    async loadTemplates() {
      this.templates = await fetchTemplates()
    },
    async addTemplate(title, memberId, itemIds) {
      const newTemplate = await createTemplate(title, memberId, itemIds)
      this.templates.push(newTemplate)
      return newTemplate
    },
    async loadTemplateDetail(id) {
      this.selectedTemplate = await fetchTemplateDetail(id)
    },
    async removeTemplate(id) {
      await deleteTemplate(id)
      this.templates = this.templates.filter(template => template.id !== id)
    }
  }
})
