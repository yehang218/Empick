// ✅ 파일: stores/applicantListStore.js
import { defineStore } from 'pinia'
import { ref } from 'vue'

import {
  fetchApplicants,
  assignTestToApplicants,
  updateApplicantStatus
} from '@/services/applicantListService'

export const useApplicantListStore = defineStore('applicantList', () => {
  // 상태
  const applicantList = ref([])
  const selectedApplicant = ref(null)
  const selectedApplicantIds = ref([])
  const loading = ref(false)
  const error = ref(null)

  // 🔍 전체 조회
  const fetchAllApplicants = async () => {
    loading.value = true
    error.value = null
    try {
      const result = await fetchApplicants()
      applicantList.value = result
    } catch (err) {
      error.value = err.message
      throw err
    } finally {
      loading.value = false
    }
  }

  // ✍️ 상태 업데이트
  const updateStatus = async (id, newStatus) => {
    const result = await updateApplicantStatus(id, newStatus)
    await fetchAllApplicants()
    return result
  }

  // ✍️ 테스트 할당
  const assignTest = async (ids) => {
    const result = await assignTestToApplicants(ids)
    await fetchAllApplicants()
    return result
  }

  // ✅ 선택된 지원자 ID 토글
  const toggleSelection = (id) => {
    if (selectedApplicantIds.value.includes(id)) {
      selectedApplicantIds.value = selectedApplicantIds.value.filter(i => i !== id)
    } else {
      selectedApplicantIds.value.push(id)
    }
  }

  return {
    applicantList,
    selectedApplicant,
    selectedApplicantIds,
    loading,
    error,
    fetchAllApplicants,
    updateStatus,
    assignTest,
    toggleSelection
  }
})
