import { defineStore } from 'pinia';
import { ref } from 'vue';
import {
    fetchRecruitmentList,
    fetchRecruitmentDetail,
    createRecruitment,
    updateRecruitment
} from '@/services/recruitmentService';

export const useRecruitmentStore = defineStore('recruitment', () => {
    // 목록
    const list = ref([]);
    const loadingList = ref(false);
    const listError = ref(null);

    // 상세
    const detail = ref(null);
    const loadingDetail = ref(false);
    const detailError = ref(null);

    // 등록/수정 로딩
    const submitting = ref(false);
    const submitError = ref(null);

    // 목록 로딩
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

    // 상세 로딩
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

    // 등록
    const submitRecruitment = async (dto) => {
        submitting.value = true;
        submitError.value = null;

        try {
            await createRecruitment(dto);
        } catch (err) {
            submitError.value = err.message;
            throw err;
        } finally {
            submitting.value = false;
        }
    };

    // 수정
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

    return {
        list,
        loadingList,
        listError,
        loadRecruitmentList,

        detail,
        loadingDetail,
        detailError,
        loadRecruitmentDetail,

        submitting,
        submitError,
        submitRecruitment,
        updateExistingRecruitment
    };
});
