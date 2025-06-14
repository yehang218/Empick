// src/stores/applicantStore.js
import { defineStore } from 'pinia'
import { fetchApplicantsService } from '@/services/applicantService'

export const useApplicantStore = defineStore('applicant', {
  state: () => ({
    applicants: [],
    loading: false
  }),

  actions: {
    async fetchApplicants() {
      this.loading = true
      try {
        const res = await fetchApplicantsService()
        this.applicants = res.data.result // ← 백엔드 응답 구조에 따라 조정
      } catch (e) {
        console.error('지원자 목록 조회 실패:', e)
      } finally {
        this.loading = false
      }
    }
  }
})
