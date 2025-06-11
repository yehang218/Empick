import api from '@/apis/apiClient';
import { JobtestAPI } from '@/apis/routes/jobtest';
import JobtestResponseDTO from '@/dto/employment/jobtest/JobtestResponseDTO';
import ApiResponseDTO from '@/dto/common/ApiResponseDTO';
import { withErrorHandling } from '@/utils/errorHandler';

/**
 * 실무테스트 문제 목록을 조회하는 서비스
 * @param {Object} options - 에러 처리 옵션
 * @param {boolean} options.showToast - 토스트 메시지 표시 여부 (기본값: true)
 * @param {boolean} options.redirect - 리다이렉트 처리 여부 (기본값: true)
 * @returns {Promise<JobtestResponseDTO[]>} 실무테스트 문제 목록
 */
export const getQuestionsService = async (options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(JobtestAPI.QUESTIONS);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throw new Error(apiResponse.message);
        } else if (!apiResponse.data) {
            throw new Error('데이터가 존재하지 않습니다.');
        }

        // 응답 데이터를 DTO 객체로 변환
        return apiResponse.data.map(question => JobtestResponseDTO.fromJSON(question));
    }, options);
};