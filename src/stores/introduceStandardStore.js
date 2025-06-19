import { defineStore } from 'pinia'
import { fetchIntroduceStandards } from '@/services/introduceStandardService'

export const useIntroduceStandardStore = defineStore('introduceStandard', {
  state: () => ({
    standards: [],
    loading: false,
    error: null,
  }),
  actions: {
    async fetchStandards() {
      this.loading = true
      this.error = null
      try {
        const data = await fetchIntroduceStandards()
        this.standards = Array.isArray(data) ? data : []
      } catch (e) {
        this.error = e
        this.standards = []
      } finally {
        this.loading = false
      }
    }
  }
})
