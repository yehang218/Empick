import api from '@/apis/apiClient';
import { JobtestAPI } from '@/apis/routes/jobtest';
import JobtestResponseDTO from '@/dto/employment/jobtest/jobtestResponseDTO';
import ApiResponseDTO from '@/dto/common/apiResponseDTO';
import { withErrorHandling, throwCustomApiError } from '@/utils/errorHandler';

/**
 * 실무테스트 문제 목록을 조회하는 서비스
 */
export const getQuestionsService = async (options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(JobtestAPI.QUESTIONS);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        // 성공 상태로 오지 않았다면 에러 처리
        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.data.map(item => JobtestResponseDTO.fromJSON(item));
    }, options);
};
