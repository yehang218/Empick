import { defineStore } from 'pinia'
import { fetchIntroduceStandards, createIntroduceStandard } from '@/services/introduceStandardService'
import { useMemberStore } from '@/stores/memberStore'

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
    },
    async addStandard(title, itemIds) {
      this.loading = true
      this.error = null
      try {
        const memberStore = useMemberStore()
        const memberId = memberStore.user?.id || memberStore.form.id
        if (!memberId) {
          alert('로그인 정보가 올바르지 않습니다. 다시 로그인 해주세요.')
          throw new Error('No memberId')
        }
        await createIntroduceStandard(title, memberId, itemIds)
        await this.fetchStandards()
      } catch (e) {
        this.error = e
        throw e
      } finally {
        this.loading = false
      }
    }
  }
})
