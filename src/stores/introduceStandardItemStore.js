import { defineStore } from 'pinia'
import { fetchStandardItems, createStandardItem } from '@/services/introduceStandardItemService'
import { useMemberStore } from '@/stores/memberStore'

export const useIntroduceStandardItemStore = defineStore('introduceStandardItem', {
  state: () => ({
    items: [],
    loading: false,
    error: null,
  }),
  actions: {
    async fetchItems() {
      this.loading = true
      this.error = null
      try {
        this.items = await fetchStandardItems()
      } catch (e) {
        this.error = e
      } finally {
        this.loading = false
      }
    },
    async addItem(content) {
      try {
        const memberStore = useMemberStore()
        console.log('memberStore.user:', memberStore.user)
        console.log('memberStore.memberId:', memberStore.memberId)
        const memberId = memberStore.user?.id || memberStore.memberId // 실제 구조에 맞게 조정
        if (!memberId) {
          alert('로그인 정보가 올바르지 않습니다. 다시 로그인 해주세요.')
          return
        }
        await createStandardItem(content, memberId)
        await this.fetchItems()
      } catch (e) {
        this.error = e
      }
    }
  }
})
