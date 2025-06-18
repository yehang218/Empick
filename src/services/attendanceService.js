import api from '@/apis/apiClient';
import { AttendanceAPI } from '@/apis/routes/attendance';
import ApiResponseDTO from '@/dto/common/apiResponseDTO';
import { withErrorHandling, throwCustomApiError } from '@/utils/errorHandler';

// 모든 근태 기록 조회
export const fetchAttendanceRecords = async (options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(AttendanceAPI.ALL_RECORDS);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.data;
    }, options);
};

// 근태 기록 상세 조회
export const fetchAttendanceRecordById = async (id, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(AttendanceAPI.RECORD_DETAIL(id));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);
        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.data;
    }, options);
};


// 모든 근태 카테고리 조회
export const fetchAttendanceCategories = async (options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(AttendanceAPI.ALL_CATEGORIES);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.data;
    }, options);
};

// 근태 카테고리 상세 조회
export const fetchAttendanceCategoryById = async (id, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(AttendanceAPI.CATEGORY_DETAIL(id));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.data;
    }, options);
};

// 근태 카테고리 status별 조회
export const fetchAttendanceCategoriesByStatus = async (status, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(AttendanceAPI.CATEGORIES_BY_STATUS(status));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.data;
    }, options);
};

// 근태 카테고리 총 개수 조회
export const fetchAttendanceCategoriesCount = async (options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(AttendanceAPI.CATEGORIES_COUNT);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.data;
    }, options);
};

// 복수 ID로 근태 카테고리 조회
export const fetchAttendanceCategoriesBulk = async (ids, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(AttendanceAPI.CATEGORIES_BULK, { params: { ids } });
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.data;
    }, options);
};

// 오늘 퇴근 기록 확인
export const fetchTodayCheckout = async (options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(AttendanceAPI.TODAY_CHECKOUT);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.data;
    }, options);
};

// 오늘 출근 기록 확인
export const fetchTodayCheckin = async (options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(AttendanceAPI.TODAY_CHECKIN);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.data;
    }, options);
};

// 근태 기록 생성
export const createAttendanceRecord = async (dto, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.post(AttendanceAPI.CREATE_RECORD, dto);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.data;
    }, options);
};

// 근태 기록 수정
export const updateAttendanceRecord = async (recordId, dto, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.put(AttendanceAPI.UPDATE_RECORD(recordId), dto);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.data;
    }, options);
};

// 날짜 범위별 근태 기록 조회
export const fetchAttendanceRecordsByDateRange = async (startDate, endDate, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(AttendanceAPI.RECORDS_BY_DATE_RANGE, {
            params: { startDate, endDate }
        });
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.data;
    }, options);
};

// 내 모든 근태 기록 조회
export const fetchMyAttendanceRecords = async (options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(AttendanceAPI.MY_RECORDS);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.data;
    }, options);
};

// 내 최근 근태 기록 조회
export const fetchMyRecentAttendanceRecords = async (options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(AttendanceAPI.MY_RECENT_RECORDS);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.data;
    }, options);
};

// 내 특정 기간 근태 기록 조회
export const fetchMyAttendanceRecordsByDateRange = async (startDate, endDate, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(AttendanceAPI.MY_RECORDS_BY_DATE_RANGE, {
            params: { startDate, endDate }
        });
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.data;
    }, options);
};

// 회원별 근태 기록 조회
export const fetchMemberAttendanceRecords = async (memberId, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(AttendanceAPI.MEMBER_RECORDS(memberId));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.data;
    }, options);
};

// 근태 기록 삭제
export const deleteAttendanceRecord = async (recordId, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.delete(AttendanceAPI.DELETE_RECORD(recordId));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.data;
    }, options);
};