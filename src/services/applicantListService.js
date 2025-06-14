// ✅ 파일: src/services/applicantListService.js

import api from '@/apis/apiClient'
import { withErrorHandling } from '@/utils/errorHandler'
import { ApplicantAPI } from '@/apis/routes/applicant'
import ApiResponseDTO from '@/dto/common/apiResponseDTO'
import ApplicantListResponseDTO from '@/dto/employment/applicant/applicantListResponseDTO'

/** 지원자 전체 조회 */
export const fetchApplicants = async (options = {}) => {
  return withErrorHandling(async () => {
    const response = await api.get(ApplicantAPI.FETCH_ALL)
    const apiResponse = ApiResponseDTO.fromJSON(response.data)
    if (!apiResponse.success) {
      throw new Error(apiResponse.message || '지원자 목록 조회 실패')
    }
    return apiResponse.data.map(item => ApplicantListResponseDTO.fromJSON(item))
  }, options)
}

/** 선택된 지원자에게 테스트 할당 */

export const assignTestToApplicants = async (applicantIds, options = {}) => {
  return withErrorHandling(async () => {
    const response = await api.post(ApplicantAPI.ASSIGN_TEST, {
      applicantIds
    })
    const apiResponse = ApiResponseDTO.fromJSON(response.data)
    if (!apiResponse.success) {
      throw new Error(apiResponse.message || '테스트 할당 실패')
    }
    return apiResponse.data
  }, options)
}

/** 지원자 상태 업데이트 */
export const updateApplicantStatus = async (applicantId, newStatus, options = {}) => {
  return withErrorHandling(async () => {
    const response = await api.patch(ApplicantAPI.UPDATE_STATUS(applicantId), {
      status: newStatus
    })
    const apiResponse = ApiResponseDTO.fromJSON(response.data)
    if (!apiResponse.success) {
      throw new Error(apiResponse.message || '상태 업데이트 실패')
    }
    return apiResponse.data
  }, options)
}
