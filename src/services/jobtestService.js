import api from '@/apis/apiClient';
import { JobtestAPI } from '@/apis/routes/jobtest';
import { withErrorHandling, throwCustomApiError } from '@/utils/errorHandler';
import ApiResponseDTO from '@/dto/common/apiResponseDTO';

import JobtestListResponseDTO from '@/dto/employment/jobtest/jobtestListResponseDTO';
import JobtestDetailDTO from '@/dto/employment/jobtest/jobtestDetailDTO';

// 실무테스트 목록 조회 서비스
export const getJobtestListService = async (options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(JobtestAPI.JOBTESTS);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        // 성공 상태로 오지 않았다면 에러 처리
        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.data.map(item => JobtestListResponseDTO.fromJSON(item));
    }, options);
};

// 실무테스트 상세 조회 서비스
export const getJobtestService = async (jobtestId, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(JobtestAPI.JOBTEST_DETAIL(jobtestId));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        console.log('[DEBUG] Jobtest API Response:', apiResponse);
        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return JobtestDetailDTO.fromJSON(apiResponse.data);
    }, options);
};

// 실무테스트 등록 서비스
export const createJobtestService = async (
    dto,
    options = {}
) => {
    return withErrorHandling(async () => {
        const response = await api.post(JobtestAPI.JOBTESTS, dto.toJSON());
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.message;
    }, options);
}

// 지원서에 실무테스트 할당
export const createApplicationJobtestService = async (
    dto,
    options = {}
) => {
    return withErrorHandling(async () => {
        const response = await api.post(JobtestAPI.APPLICATION_JOBTESTS, dto);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);
        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }
        return apiResponse.data;
    }, options);
}

// 실무테스트 입장
export const verifyJobtestEntryService = async (jobtestId, dto, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.post(JobtestAPI.JOBTEST_ENTER(jobtestId), dto.toJSON());
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.data;
    }, options);
};
