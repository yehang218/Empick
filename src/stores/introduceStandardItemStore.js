import { defineStore } from 'pinia'
import { fetchStandardItems, createStandardItem, fetchItemsByStandardId, deleteStandardItem } from '@/services/introduceStandardItemService'
import { IntroduceAPI } from '@/apis/routes/introduce'
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
    async fetchItemsByStandardId(standardId) {
      this.loading = true
      this.error = null
      try {
        console.log('🔍 기준표별 항목 조회:', standardId)
        const response = await fetchItemsByStandardId(standardId)
        
        const data = response.data?.data || response.data || response || []
        this.items = Array.isArray(data) ? data : []
        
        console.log('✅ 기준표별 항목 조회 결과:', this.items)
        console.log('✅ 조회된 항목 수:', this.items.length)
        
        return this.items
      } catch (e) {
        console.error('❌ 기준표별 항목 조회 실패:', e)
        this.error = e
        this.items = []
        throw e
      } finally {
        this.loading = false
      }
    },
    async addItem(content) {
      try {
        const memberStore = useMemberStore()
        const memberId = memberStore.user?.id || memberStore.memberId || memberStore.form.id
        if (!memberId) {
          alert('로그인 정보가 올바르지 않습니다. 다시 로그인 해주세요.')
          return
        }
        await createStandardItem(content, memberId)
        await this.fetchItems()
      } catch (e) {
        this.error = e
      }
    },
    async removeItem(id) {
      try {
        await deleteStandardItem(id)
        await this.fetchItems()
      } catch (e) {
        this.error = e
      }
    }
  }
})
