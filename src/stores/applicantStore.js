import { defineStore } from 'pinia';
import { ref } from 'vue';

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
import ApplicantFullInfoListDTO from '@/dto/employment/applicant/applicantFullInfoListDTO';

export const useApplicantStore = defineStore('applicant', () => {
    // 상태
    const applicantList = ref([]);
    const selectedApplicant = ref(null);
    const loading = ref(false);
    const error = ref(null);
    const bookmarkedApplicants = ref(new Set());

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
    }

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

        // 액션
        fetchAllApplicants,
        fetchApplicantById,
        fetchApplicantFullInfoList,
        searchApplicantsByName,
        createApplicant,
        fetchBookmarksByMemberId,
        addBookmark,
        removeBookmark,
        isBookmarked
    };
});
