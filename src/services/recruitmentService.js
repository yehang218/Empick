import api from '@/apis/apiClient'
import { API } from '@/apis/routes'
import ApiResponseDTO from '@/dto/common/apiResponseDTO'
import recruitmentResponseDTO from '@/dto/employment/recruitment/recruitmentResponseDTO'
import recruitmentDetailResponseDTO from '@/dto/employment/recruitment/recruitmentDetailResponseDTO'

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

// 채용 공고 상세 조회
export const fetchRecruitmentDetail = async (id, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(API.RECRUITMENT.RECRUITMENT_DETAIL(id))
        console.log('📦 raw response:', response);
        const apiResponse = ApiResponseDTO.fromJSON(response.data)

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400)
        }

        return recruitmentDetailResponseDTO.fromJSON(apiResponse.data)
    }, options) ?? {};
}

// 채용 공고 등록
export const createRecruitment = async (dto) => {
    return withErrorHandling(async () => {
        const { data } = await api.post(API.RECRUITMENT.RECRUITMENT_CREATE, dto);
        const apiResponse = ApiResponseDTO.fromJSON(data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message);
        }

        return apiResponse.data;
    });
};

// 채용 공고 수정
export const updateRecruitment = async (id, dto) => {
    return withErrorHandling(async () => {
        const response = await api.put(`${API.RECRUITMENT.RECRUITMENT_UPDATE}/${id}`, dto)
        const apiResponse = ApiResponseDTO.fromJSON(response.data)

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400)
        }

        return apiResponse.data
    })
}