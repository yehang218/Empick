import { defineStore } from 'pinia'
import { fetchIntroduceStandards, createIntroduceStandard, fetchIntroduceStandardDetail, deleteIntroduceStandard } from '@/services/introduceStandardService'
import { useMemberStore } from '@/stores/memberStore'

export const useIntroduceStandardStore = defineStore('introduceStandard', {
  state: () => ({
    standards: [],
    loading: false,
    error: null,
    standardDetail: null,
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
    async addStandard(content, itemIds) {
      this.loading = true
      this.error = null
      try {
        const memberStore = useMemberStore()
        const memberId = memberStore.user?.id || memberStore.form.id
        if (!memberId) {
          alert('로그인 정보가 올바르지 않습니다. 다시 로그인 해주세요.')
          throw new Error('No memberId')
        }
        const res = await createIntroduceStandard(content, memberId, itemIds)
        let standardId = res?.data?.id || res?.id
        if (!standardId) {
          await this.fetchStandards()
          if (this.standards.length > 0) {
            standardId = this.standards[this.standards.length - 1].id
          }
        }
        if (standardId) {
          const { patchStandardItemFk } = await import('@/services/introduceStandardItemService')
          for (const itemId of itemIds) {
            await patchStandardItemFk(itemId, standardId)
          }
        }
        this.standards.push({
          id: standardId || Date.now(),
          content,
          itemIds: [...itemIds],
        })
        await this.fetchStandards()
      } catch (e) {
        this.error = e
        throw e
      } finally {
        this.loading = false
      }
    },
    async fetchStandardDetail(id) {
      this.standardDetail = await fetchIntroduceStandardDetail(id)
    },
    async deleteStandard(id) {
      this.loading = true
      this.error = null
      try {
        await deleteIntroduceStandard(id)
        await this.fetchStandards() // 삭제 후 목록 새로고침
      } catch (e) {
        this.error = e
        throw e
      } finally {
        this.loading = false
      }
    }
  }
})
