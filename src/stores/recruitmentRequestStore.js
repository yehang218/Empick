// 📁 src/stores/recruitmentRequestStore.js
import { defineStore } from 'pinia';
import { ref } from 'vue';
import {
    fetchRecruitmentRequestList,
    fetchRecruitmentRequestDetail,
    createRecruitmentRequest
} from '@/services/recruitmentRequestService';

export const useRecruitmentRequestStore = defineStore('recruitmentRequest', () => {
    // 채용 요청서 목록
    const recruitmentRequestList = ref([]);
    const loadingRecruitmentRequest = ref(false);
    const recruitmentRequestError = ref(null);

    // 채용 요청서 상세
    const recruitmentRequestDetail = ref(null);
    const loadingDetail = ref(false);
    const detailError = ref(null);

    // 요청서 등록 로딩 상태 및 에러 메시지
    const submitting = ref(false);
    const submitError = ref(null);

    // 목록 불러오기
    const loadRecruitmentRequestList = async () => {
        loadingRecruitmentRequest.value = true;
        recruitmentRequestError.value = null;

        try {
            const result = await fetchRecruitmentRequestList();
            recruitmentRequestList.value = result;
        } catch (err) {
            recruitmentRequestError.value = err.message;
        } finally {
            loadingRecruitmentRequest.value = false;
        }
    };

    // 상세 불러오기
    const loadRecruitmentRequestDetail = async (id) => {
        loadingDetail.value = true;
        detailError.value = null;
        try {
            const result = await fetchRecruitmentRequestDetail(id);
            recruitmentRequestDetail.value = result;
        } catch (err) {
            detailError.value = err.message;
        } finally {
            loadingDetail.value = false;
        }
    };

    // 등록 함수
    const submitRecruitmentRequest = async (formDTO) => {
        submitting.value = true;
        submitError.value = null;

        try {
            await createRecruitmentRequest(formDTO);
        } catch (err) {
            submitError.value = err.message;
            throw err;
        } finally {
            submitting.value = false;
        }
    };

    return {
        // 목록 관련
        recruitmentRequestList,
        loadingRecruitmentRequest,
        recruitmentRequestError,
        loadRecruitmentRequestList,

        // 상세 관련
        recruitmentRequestDetail,
        loadingDetail,
        detailError,
        loadRecruitmentRequestDetail,

        // 등록 관련
        submitting,
        submitError,
        submitRecruitmentRequest
    };
});
