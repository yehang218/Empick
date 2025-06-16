import { defineStore } from 'pinia';
import { ref, computed } from 'vue';

import {
    getAllApplicantsService,
    getApplicantByIdService,
    searchApplicantsByNameService,
    createApplicantService,
    getBookmarksByMemberIdService,
    addApplicantBookmarkService,
    removeApplicantBookmarkService,
    getApplicantFullInfoListService
} from '@/services/applicantService';

export const useApplicantStore = defineStore('applicant', () => {
    // 상태
    const applicantList = ref([]);
    const selectedApplicant = ref(null);
    const loading = ref(false);
    const error = ref(null);
    const bookmarkedApplicants = ref(new Set());
    const searchQuery = ref('');
    const sortKey = ref('');
    const sortOrder = ref('asc');

    // 🔍 전체 지원자 조회
    const fetchAllApplicants = async () => {
        loading.value = true;
        error.value = null;
        try {
            const result = await getAllApplicantsService();
            applicantList.value = result;
            bookmarkedApplicants.value = new Set(
                result.filter(applicant => applicant.bookmarked).map(a => a.id)
            );
            return result;
        } catch (err) {
            error.value = err.message;
            throw err;
        } finally {
            loading.value = false;
        }
    };

    const fetchApplicantFullInfoList = async () => {
        loading.value = true;
        error.value = null;
        try {
            const result = await getApplicantFullInfoListService();
            applicantList.value = result;
            return result;
        } catch (err) {
            error.value = err.message;
            throw err;
        } finally {
            loading.value = false;
        }
    };

    // 필터링 및 정렬된 지원자 목록
    const filteredAndSortedApplicants = computed(() => {
        let result = [...applicantList.value];

        // 검색 필터링
        if (searchQuery.value) {
            const query = searchQuery.value.toLowerCase();
            result = result.filter(applicant =>
                applicant.name?.toLowerCase().includes(query) ||
                applicant.email?.toLowerCase().includes(query) ||
                applicant.phone?.toLowerCase().includes(query) ||
                applicant.jobName?.toLowerCase().includes(query)
            );
        }

        // 정렬
        if (sortKey.value) {
            result.sort((a, b) => {
                const aValue = a[sortKey.value];
                const bValue = b[sortKey.value];

                if (!aValue || !bValue) return 0;

                if (typeof aValue === 'string') {
                    return sortOrder.value === 'asc'
                        ? aValue.localeCompare(bValue)
                        : bValue.localeCompare(aValue);
                }

                return sortOrder.value === 'asc'
                    ? aValue - bValue
                    : bValue - aValue;
            });
        }

        return result;
    });

    const setSearchQuery = (query) => {
        searchQuery.value = query;
    };

    const setSort = (options) => {
        if (options.sortBy && options.sortBy.length > 0) {
            sortKey.value = options.sortBy[0];
            sortOrder.value = options.sortDesc[0] ? 'desc' : 'asc';
        } else {
            sortKey.value = '';
            sortOrder.value = 'asc';
        }
    };

    // 🔍 지원자 ID로 단일 조회
    const fetchApplicantById = async (id) => {
        loading.value = true;
        error.value = null;
        try {
            const result = await getApplicantByIdService(id);
            selectedApplicant.value = result;
            return result;
        } catch (err) {
            error.value = err.message;
            throw err;
        } finally {
            loading.value = false;
        }
    };

    // 🔍 이름으로 검색
    const searchApplicantsByName = async (name) => {
        loading.value = true;
        error.value = null;
        try {
            const result = await searchApplicantsByNameService(name);
            applicantList.value = result;
            return result;
        } catch (err) {
            error.value = err.message;
            throw err;
        } finally {
            loading.value = false;
        }
    };

    // ✍️ 지원자 등록
    const createApplicant = async (dto) => {
        const result = await createApplicantService(dto);
        await fetchAllApplicants();
        return result;
    };

    const fetchBookmarksByMemberId = async (id) => {
        const result = await getBookmarksByMemberIdService(id);
        return result;
    };

    // ⭐ 즐겨찾기 추가
    const addBookmark = async (memberId, applicantId) => {
        await addApplicantBookmarkService({ memberId, applicantId });
        bookmarkedApplicants.value.add(applicantId);
    };

    // ❌ 즐겨찾기 삭제
    const removeBookmark = async (memberId, applicantId) => {
        await removeApplicantBookmarkService(memberId, applicantId);
        bookmarkedApplicants.value.delete(applicantId);
    };

    const isBookmarked = (applicantId) => {
        return bookmarkedApplicants.value.has(applicantId);
    };

    return {
        // 상태
        applicantList,
        selectedApplicant,
        loading,
        error,
        bookmarkedApplicants,
        searchQuery,
        sortKey,
        sortOrder,
        filteredAndSortedApplicants,

        // 액션
        fetchAllApplicants,
        fetchApplicantById,
        fetchApplicantFullInfoList,
        searchApplicantsByName,
        createApplicant,
        fetchBookmarksByMemberId,
        addBookmark,
        removeBookmark,
        isBookmarked,
        setSearchQuery,
        setSort
    };
});
