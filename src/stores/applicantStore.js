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
    getApplicantFullInfoListService,
    getApplicantsByRecruitmentIdService
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
    const selectedApplicants = ref([]);


    // 각 지원서에 고유 키 추가 함수
    const addUniqueKeys = (applicants) => {
        return applicants.map((applicant, index) => ({
            ...applicant,
            // applicantId 또는 applicationId를 기반으로 한 고유 키 생성
            uniqueKey: applicant.applicationId
                ? `app_${applicant.applicationId}`
                : applicant.applicantId
                    ? `applicant_${applicant.applicantId}_${index}`
                    : `temp_${Date.now()}_${index}_${Math.random().toString(36).substr(2, 9)}`,
        }));
    };

    // 🔍 전체 지원자 조회
    const fetchAllApplicants = async () => {
        loading.value = true;
        error.value = null;
        try {
            const result = await getAllApplicantsService();
            applicantList.value = addUniqueKeys(result);
            bookmarkedApplicants.value = new Set(
                result.filter(applicant => applicant.bookmarked).map(a => a.id)
            );
            return applicantList.value;
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
            applicantList.value = addUniqueKeys(result);
            return applicantList.value;
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
                let aValue = a[sortKey.value];
                let bValue = b[sortKey.value];

                // null/undefined 처리
                if (aValue == null && bValue == null) return 0;
                if (aValue == null) return 1;
                if (bValue == null) return -1;

                // 날짜 처리 (birth, createdAt, updatedAt)
                if (sortKey.value === 'birth' || sortKey.value === 'createdAt' || sortKey.value === 'updatedAt') {
                    aValue = new Date(aValue);
                    bValue = new Date(bValue);
                }

                // 문자열 처리
                if (typeof aValue === 'string' && typeof bValue === 'string') {
                    const comparison = aValue.localeCompare(bValue, 'ko', { numeric: true });
                    return sortOrder.value === 'asc' ? comparison : -comparison;
                }

                // 숫자 처리
                if (typeof aValue === 'number' && typeof bValue === 'number') {
                    return sortOrder.value === 'asc' ? aValue - bValue : bValue - aValue;
                }

                // 날짜 처리
                if (aValue instanceof Date && bValue instanceof Date) {
                    return sortOrder.value === 'asc' ? aValue - bValue : bValue - aValue;
                }

                // 기본 처리
                return sortOrder.value === 'asc' ?
                    String(aValue).localeCompare(String(bValue)) :
                    String(bValue).localeCompare(String(aValue));
            });
        }

        return result;
    });

    const setSearchQuery = (query) => {
        searchQuery.value = query;
    };

    const setSort = (options) => {
        console.log('setSort 호출됨:', options);
        if (options.sortBy && options.sortBy.length > 0) {
            sortKey.value = options.sortBy[0];
            sortOrder.value = options.sortDesc && options.sortDesc[0] ? 'desc' : 'asc';
        } else {
            sortKey.value = '';
            sortOrder.value = 'asc';
        }
        console.log('정렬 설정:', { sortKey: sortKey.value, sortOrder: sortOrder.value });
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
            applicantList.value = addUniqueKeys(result);
            return applicantList.value;
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

    // 상태 초기화
    const resetState = () => {
        applicantList.value = [];
        selectedApplicant.value = null;
        error.value = null;
        searchQuery.value = '';
        sortKey.value = '';
        sortOrder.value = 'asc';
    };

    // 선택된 지원자 데이터 가공
    const getSelectedApplicantsData = (selectedItems) => {
        if (!selectedItems || selectedItems.length === 0) {
            return [];
        }

        return selectedItems.map(selectedItem => {
            const applicant = applicantList.value.find(
                item => item.uniqueKey === selectedItem.uniqueKey
            );
            if (!applicant) return null;

            return {
                applicantId: applicant.applicantId,
                applicationId: applicant.applicationId,
                name: applicant.name,
                birth: applicant.birth,
                phone: applicant.phone,
                email: applicant.email,
                address: applicant.address,
                profileUrl: applicant.profileUrl,
                jobName: applicant.jobName,
                createdAt: applicant.createdAt,
                status: applicant.status,
                recruitmentId: applicant.recruitmentId,
                introduceRatingResultId: applicant.introduceRatingResultId,
                education: applicant.education,
                experience: applicant.experience,
                skills: applicant.skills,
                motivation: applicant.motivation,
                coverLetter: applicant.coverLetter,
                portfolioUrl: applicant.portfolioUrl,
                introduceScore: applicant.introduceScore,
                introduceStatus: applicant.introduceStatus,
                jobtestTotalScore: applicant.jobtestTotalScore,
                jobtestEvaluationScore: applicant.jobtestEvaluationScore,
                jobtestStatus: applicant.jobtestStatus,
                interviewScore: applicant.interviewScore,
                interviewAddress: applicant.interviewAddress,
                interviewDatetime: applicant.interviewDatetime
            };
        }).filter(Boolean); // null 값 제거
    };

    // 선택된 지원자 관리
    const setSelectedApplicants = (selected) => {
        console.log('Store에서 선택된 지원자 설정:', selected);
        selectedApplicants.value = selected;
    };

    const addSelectedApplicant = (applicant) => {
        console.log('Store에 지원자 추가:', applicant);
        if (!selectedApplicants.value.find(item => item.uniqueKey === applicant.uniqueKey)) {
            selectedApplicants.value.push(applicant);
        }
    };

    const removeSelectedApplicant = (uniqueKey) => {
        console.log('Store에서 지원자 제거:', uniqueKey);
        selectedApplicants.value = selectedApplicants.value.filter(item => item.uniqueKey !== uniqueKey);
    };

    const clearSelectedApplicants = () => {
        console.log('Store에서 선택된 지원자 모두 제거');
        selectedApplicants.value = [];
    };

    // 채용공고별 지원자 조회
    const loadApplicantsByRecruitmentId = async (recruitmentId) => {
        loading.value = true;
        error.value = null;
        try {
            const result = await getApplicantsByRecruitmentIdService(recruitmentId);
            applicantList.value = result;
            return result;
        } catch (err) {
            error.value = err.message || '지원자 조회 중 오류 발생';
            console.error('❌ 지원자 조회 오류:', err);
        } finally {
            loading.value = false;
        }
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
        selectedApplicants,

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
        setSort,
        resetState,
        getSelectedApplicantsData,
        setSelectedApplicants,
        addSelectedApplicant,
        removeSelectedApplicant,
        clearSelectedApplicants,
        loadApplicantsByRecruitmentId
    };
});