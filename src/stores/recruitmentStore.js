// stores/recruitmentStore.js
import { defineStore } from 'pinia';
import { ref } from 'vue';
import {
    fetchRecruitmentList,
    fetchRecruitmentDetail,
    createRecruitment,
    updateRecruitment,
    deleteRecruitment
} from '@/services/recruitmentService';
import { fetchApplicationItemCategories } from '@/services/applicationItemService';

export const useRecruitmentStore = defineStore('recruitment', () => {
    const list = ref([]);
    const loadingList = ref(false);
    const listError = ref(null);

    const detail = ref(null);
    const loadingDetail = ref(false);
    const detailError = ref(null);

    const submitting = ref(false);
    const submitError = ref(null);

    const requestDetail = ref(null);

    const loadRecruitmentList = async () => {
        loadingList.value = true;
        listError.value = null;

        try {
            const result = await fetchRecruitmentList();
            list.value = result;
        } catch (err) {
            listError.value = err.message;
        } finally {
            loadingList.value = false;
        }
    };

    const loadRecruitmentDetail = async (id) => {
        loadingDetail.value = true;
        detailError.value = null;

        try {
            const result = await fetchRecruitmentDetail(id);
            detail.value = result;
        } catch (err) {
            detailError.value = err.message;
        } finally {
            loadingDetail.value = false;
        }
    };

    // 초안 저장
    const draftRecruitment = ref(null);
    const setDraftRecruitment = (formData) => {
        draftRecruitment.value = formData;
    };
    const clearDraftRecruitment = () => {
        draftRecruitment.value = null;
    };

    // 지원서 항목 초안
    const selectedApplicationItemIds = ref([]);
    const requiredApplicationItemIds = ref([]);
    const setDraftApplicationItems = (selected, required) => {
        selectedApplicationItemIds.value = selected;
        requiredApplicationItemIds.value = required;
    };
    const clearDraftApplicationItems = () => {
        selectedApplicationItemIds.value = [];
        requiredApplicationItemIds.value = [];
    };

    // 지원서 항목 카테고리 리스트
    const applicationItemCategoryList = ref([]);
    const setApplicationItemCategoryList = (categories) => {
        applicationItemCategoryList.value = categories;
    };
    const clearApplicationItemCategoryList = () => {
        applicationItemCategoryList.value = [];
    };

    // 등록/수정
    const submitRecruitment = async (dto) => {
        submitting.value = true;
        submitError.value = null;
        try {
            return await createRecruitment(dto);
        } catch (err) {
            submitError.value = err.message;
            throw err;
        } finally {
            submitting.value = false;
        }
    };

    const updateExistingRecruitment = async (id, dto) => {
        submitting.value = true;
        submitError.value = null;
        try {
            await updateRecruitment(id, dto);
        } catch (err) {
            submitError.value = err.message;
            throw err;
        } finally {
            submitting.value = false;
        }
    };

    // 삭제
    const deleteExistingRecruitment = async (id) => {
        submitting.value = true;
        submitError.value = null;
        try {
            await deleteRecruitment(id);
        } catch (err) {
            submitError.value = err.message;
            throw err;
        } finally {
            submitting.value = false;
        }
    };

    const loadApplicationItemCategories = async () => {
        try {
            const result = await fetchApplicationItemCategories();
            applicationItemCategoryList.value = result;
        } catch (err) {
            console.error('지원서 항목 카테고리 불러오기 실패:', err);
        }
    };

    return {
        list,
        loadingList,
        listError,
        loadRecruitmentList,

        detail,
        loadingDetail,
        detailError,
        loadRecruitmentDetail,

        draftRecruitment,
        setDraftRecruitment,
        clearDraftRecruitment,

        selectedApplicationItemIds,
        requiredApplicationItemIds,
        setDraftApplicationItems,
        clearDraftApplicationItems,

        applicationItemCategoryList,
        setApplicationItemCategoryList,
        clearApplicationItemCategoryList,
        loadApplicationItemCategories,

        submitting,
        submitError,
        submitRecruitment,
        updateExistingRecruitment,
        deleteExistingRecruitment,

        requestDetail
    };
});
