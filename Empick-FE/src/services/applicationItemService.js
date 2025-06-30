import api from '@/apis/apiClient';
import { API } from '@/apis/routes';
import ApplicationItemCategoryDTO from '@/dto/employment/recruitment/applicationItemCategoryDTO';
import ApplicationItemDTO from '@/dto/employment/recruitment/applicationItemDTO';
import ApiResponseDTO from '@/dto/common/apiResponseDTO';
import { withErrorHandling, throwCustomApiError } from '@/utils/errorHandler';

// 항목 카테고리 전체 조회
export const fetchApplicationItemCategories = async (options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(API.RECRUITMENT.APPLICATION_ITEM_CATEGORIES);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.data.map(item => ApplicationItemCategoryDTO.fromJSON(item));
    }, options);
};

// 채용공고별 지원서 항목 조회
export const fetchApplicationItemsByRecruitment = async (recruitmentId, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(API.RECRUITMENT.APPLICATION_ITEMS_BY_RECRUITMENT(recruitmentId));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.data.map(item => ApplicationItemDTO.fromJSON(item));
    }, options);
};
