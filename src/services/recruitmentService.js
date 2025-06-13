import api from '@/apis/apiClient'
import { API } from '@/apis/routes'
import ApiResponseDTO from '@/dto/common/apiResponseDTO'
import recruitmentResponseDTO from '@/dto/employment/recruitment/recruitmentResponseDTO'
import { withErrorHandling, throwCustomApiError } from '@/utils/errorHandler'

// 채용 공고 목록 조회
export const fetchRecruitmentList = async (options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(API.RECRUITMENT.RECRUITMENT_LIST)
        const apiResponse = ApiResponseDTO.fromJSON(response.data)

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400)
        }

        return apiResponse.data.map(item => recruitmentResponseDTO.fromJSON(item))
    }, options)
}
