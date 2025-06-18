// stores/attendanceStore.js
import { defineStore } from 'pinia';
import { ref } from 'vue';
import { attendanceService } from '@/services/attendanceService';
import {
    formatMinutesToDuration,
    calculateTimeDifferenceInMinutes,
    categorizeWorkHours,
    calculateNightWorkHours,
    groupAttendanceByDate,
    groupAttendanceByWeek,
    calculateMonthlyStats,
    transformAttendanceDataForUI
} from '@/utils/attendance/attendanceCalculator';

export const useAttendanceStore = defineStore('attendance', () => {
    // 근태 기록 관련 상태
    const attendanceRecords = ref([]);
    const currentRecord = ref(null);
    const myRecords = ref([]);
    const myRecentRecords = ref([]);
    const memberRecords = ref([]);

    // 근태 카테고리 관련 상태
    const attendanceCategories = ref([]);
    const currentCategory = ref(null);
    const categoriesCount = ref(0);

    // 오늘 출퇴근 상태
    const todayCheckin = ref(null);
    const todayCheckout = ref(null);

    // 캐싱 관련 상태
    const recordCache = ref(new Map()); // 개별 레코드 캐시
    const categoryCache = ref(new Map()); // 개별 카테고리 캐시
    const todayDataCache = ref({
        checkin: { data: null, timestamp: null },
        checkout: { data: null, timestamp: null }
    });
    const categoriesCountCache = ref({ data: null, timestamp: null });
    const myRecentRecordsCache = ref({ data: null, timestamp: null });

    // 로딩 상태
    const loading = ref(false);
    const error = ref(null);

    // 캐시 유효성 검사 헬퍼 함수
    const isCacheValid = (cacheItem, maxAge = 5 * 60 * 1000) => { // 기본 5분
        if (!cacheItem || !cacheItem.timestamp) return false;
        return Date.now() - cacheItem.timestamp < maxAge;
    };

    const isTodayCacheValid = (cacheItem) => {
        if (!cacheItem || !cacheItem.timestamp) return false;
        const today = new Date().toDateString();
        const cacheDate = new Date(cacheItem.timestamp).toDateString();
        return today === cacheDate;
    };

    // 로딩 상태 관리
    const setLoading = (loadingState) => {
        loading.value = loadingState;
    };

    const setError = (errorState) => {
        error.value = errorState;
    };

    // 공통 API 호출 래퍼 함수
    const apiWrapper = async (apiCall) => {
        setLoading(true);
        setError(null);

        try {
            const data = await apiCall();
            return data;
        } catch (err) {
            setError(err);
            throw err;
        } finally {
            setLoading(false);
        }
    };

    // 모든 근태 기록 조회
    const fetchAttendanceRecords = async (options = {}) => {
        return await apiWrapper(async () => {
            const data = await attendanceService.fetchAttendanceRecords(options);
            attendanceRecords.value = data;
            return data;
        });
    };

    // 근태 기록 상세 조회 (캐싱 적용)
    const fetchAttendanceRecordById = async (id, options = {}, forceRefresh = false) => {
        // 캐시 확인
        if (!forceRefresh && recordCache.value.has(id)) {
            const cachedRecord = recordCache.value.get(id);
            if (isCacheValid(cachedRecord)) {
                currentRecord.value = cachedRecord.data;
                return cachedRecord.data;
            }
        }

        return await apiWrapper(async () => {
            const data = await attendanceService.fetchAttendanceRecordById(id, options);
            currentRecord.value = data;
            // 캐시에 저장
            recordCache.value.set(id, {
                data: data,
                timestamp: Date.now()
            });
            return data;
        });
    };

    // 모든 근태 카테고리 조회 (기존 캐싱 유지)
    const fetchAttendanceCategories = async (options = {}, forceRefresh = false) => {
        if (!forceRefresh && attendanceCategories.value.length > 0) {
            return attendanceCategories.value;
        }

        return await apiWrapper(async () => {
            const data = await attendanceService.fetchAttendanceCategories(options);
            attendanceCategories.value = data;
            return data;
        });
    };

    // 근태 카테고리 상세 조회 (캐싱 적용)
    const fetchAttendanceCategoryById = async (id, options = {}, forceRefresh = false) => {
        // 캐시 확인
        if (!forceRefresh && categoryCache.value.has(id)) {
            const cachedCategory = categoryCache.value.get(id);
            if (isCacheValid(cachedCategory)) {
                currentCategory.value = cachedCategory.data;
                return cachedCategory.data;
            }
        }

        return await apiWrapper(async () => {
            const data = await attendanceService.fetchAttendanceCategoryById(id, options);
            currentCategory.value = data;
            // 캐시에 저장
            categoryCache.value.set(id, {
                data: data,
                timestamp: Date.now()
            });
            return data;
        });
    };

    // 근태 카테고리 status별 조회
    const fetchAttendanceCategoriesByStatus = async (status, options = {}) => {
        return await apiWrapper(async () => {
            const data = await attendanceService.fetchAttendanceCategoriesByStatus(status, options);
            attendanceCategories.value = data;
            return data;
        });
    };

    // 근태 카테고리 총 개수 조회 (캐싱 적용)
    const fetchAttendanceCategoriesCount = async (options = {}, forceRefresh = false) => {
        // 캐시 확인 (10분 유효)
        if (!forceRefresh && isCacheValid(categoriesCountCache.value, 10 * 60 * 1000)) {
            categoriesCount.value = categoriesCountCache.value.data;
            return categoriesCountCache.value.data;
        }

        return await apiWrapper(async () => {
            const data = await attendanceService.fetchAttendanceCategoriesCount(options);
            categoriesCount.value = data;
            // 캐시에 저장
            categoriesCountCache.value = {
                data: data,
                timestamp: Date.now()
            };
            return data;
        });
    };

    // 복수 ID로 근태 카테고리 조회
    const fetchAttendanceCategoriesBulk = async (ids, options = {}) => {
        return await apiWrapper(async () => {
            const data = await attendanceService.fetchAttendanceCategoriesBulk(ids, options);
            attendanceCategories.value = data;
            return data;
        });
    };

    // 오늘 퇴근 기록 확인 (오늘 캐싱 적용)
    const fetchTodayCheckout = async (options = {}, forceRefresh = false) => {
        // 오늘 캐시 확인
        if (!forceRefresh && isTodayCacheValid(todayDataCache.value.checkout)) {
            todayCheckout.value = todayDataCache.value.checkout.data;
            return todayDataCache.value.checkout.data;
        }

        return await apiWrapper(async () => {
            const data = await attendanceService.fetchTodayCheckout(options);
            todayCheckout.value = data;
            // 오늘 캐시에 저장
            todayDataCache.value.checkout = {
                data: data,
                timestamp: Date.now()
            };
            return data;
        });
    };

    // 오늘 출근 기록 확인 (오늘 캐싱 적용)
    const fetchTodayCheckin = async (options = {}, forceRefresh = false) => {
        // 오늘 캐시 확인
        if (!forceRefresh && isTodayCacheValid(todayDataCache.value.checkin)) {
            todayCheckin.value = todayDataCache.value.checkin.data;
            return todayDataCache.value.checkin.data;
        }

        return await apiWrapper(async () => {
            const data = await attendanceService.fetchTodayCheckin(options);
            todayCheckin.value = data;
            // 오늘 캐시에 저장
            todayDataCache.value.checkin = {
                data: data,
                timestamp: Date.now()
            };
            return data;
        });
    };

    // 근태 기록 생성
    const createAttendanceRecord = async (dto, options = {}) => {
        return await apiWrapper(async () => {
            const data = await attendanceService.createAttendanceRecord(dto, options);
            // 새로운 레코드를 기존 배열에 추가
            attendanceRecords.value.unshift(data);

            // 오늘 데이터 캐시 무효화 (새로운 출퇴근 기록이 생성되었을 수 있음)
            todayDataCache.value.checkin = { data: null, timestamp: null };
            todayDataCache.value.checkout = { data: null, timestamp: null };

            return data;
        });
    };

    // 근태 기록 수정
    const updateAttendanceRecord = async (recordId, dto, options = {}) => {
        return await apiWrapper(async () => {
            const data = await attendanceService.updateAttendanceRecord(recordId, dto, options);
            // 기존 레코드를 찾아서 업데이트
            const index = attendanceRecords.value.findIndex(record => record.id === recordId);
            if (index !== -1) {
                attendanceRecords.value[index] = data;
            }

            // 해당 레코드 캐시 업데이트
            if (recordCache.value.has(recordId)) {
                recordCache.value.set(recordId, {
                    data: data,
                    timestamp: Date.now()
                });
            }

            // 오늘 데이터 캐시 무효화
            todayDataCache.value.checkin = { data: null, timestamp: null };
            todayDataCache.value.checkout = { data: null, timestamp: null };

            return data;
        });
    };

    // 날짜 범위별 근태 기록 조회
    const fetchAttendanceRecordsByDateRange = async (startDate, endDate, options = {}) => {
        return await apiWrapper(async () => {
            const data = await attendanceService.fetchAttendanceRecordsByDateRange(startDate, endDate, options);
            attendanceRecords.value = data;
            return data;
        });
    };

    // 내 모든 근태 기록 조회
    const fetchMyAttendanceRecords = async (options = {}) => {
        return await apiWrapper(async () => {
            const data = await attendanceService.fetchMyAttendanceRecords(options);
            myRecords.value = data;
            return data;
        });
    };

    // 내 최근 근태 기록 조회 (캐싱 적용)
    const fetchMyRecentAttendanceRecords = async (options = {}, forceRefresh = false) => {
        // 캐시 확인 (3분 유효)
        if (!forceRefresh && isCacheValid(myRecentRecordsCache.value, 3 * 60 * 1000)) {
            myRecentRecords.value = myRecentRecordsCache.value.data;
            return myRecentRecordsCache.value.data;
        }

        return await apiWrapper(async () => {
            const data = await attendanceService.fetchMyRecentAttendanceRecords(options);
            myRecentRecords.value = data;
            // 캐시에 저장
            myRecentRecordsCache.value = {
                data: data,
                timestamp: Date.now()
            };
            return data;
        });
    };

    // 내 특정 기간 근태 기록 조회
    const fetchMyAttendanceRecordsByDateRange = async (startDate, endDate, options = {}) => {
        return await apiWrapper(async () => {
            const data = await attendanceService.fetchMyAttendanceRecordsByDateRange(startDate, endDate, options);
            myRecords.value = data;
            return data;
        });
    };

    // 회원별 근태 기록 조회
    const fetchMemberAttendanceRecords = async (memberId, options = {}) => {
        return await apiWrapper(async () => {
            const data = await attendanceService.fetchMemberAttendanceRecords(memberId, options);
            memberRecords.value = data;
            return data;
        });
    };

    // 캐시 초기화 함수들
    const clearAllCache = () => {
        recordCache.value.clear();
        categoryCache.value.clear();
        todayDataCache.value = {
            checkin: { data: null, timestamp: null },
            checkout: { data: null, timestamp: null }
        };
        categoriesCountCache.value = { data: null, timestamp: null };
        myRecentRecordsCache.value = { data: null, timestamp: null };
    };

    const clearTodayCache = () => {
        todayDataCache.value = {
            checkin: { data: null, timestamp: null },
            checkout: { data: null, timestamp: null }
        };
    };



    return {
        // 상태
        attendanceRecords, currentRecord, myRecords, myRecentRecords, memberRecords,
        attendanceCategories, currentCategory, categoriesCount,
        todayCheckin, todayCheckout, loading, error,
        // 메서드
        setLoading, setError,
        fetchAttendanceRecords, fetchAttendanceRecordById,
        fetchAttendanceCategories, fetchAttendanceCategoryById,
        fetchAttendanceCategoriesByStatus, fetchAttendanceCategoriesCount,
        fetchAttendanceCategoriesBulk, fetchTodayCheckout, fetchTodayCheckin,
        createAttendanceRecord, updateAttendanceRecord,
        fetchAttendanceRecordsByDateRange, fetchMyAttendanceRecords,
        fetchMyRecentAttendanceRecords, fetchMyAttendanceRecordsByDateRange,
        fetchMemberAttendanceRecords,
        // 캐시 관리
        clearAllCache, clearTodayCache,
        // 데이터 계산 및 변환
        formatMinutesToDuration, calculateTimeDifferenceInMinutes,
        categorizeWorkHours, calculateNightWorkHours,
        groupAttendanceByDate, groupAttendanceByWeek,
        calculateMonthlyStats, transformAttendanceDataForUI
    };
});