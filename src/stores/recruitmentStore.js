// 📁 src/stores/recruitmentStore.js
import { defineStore } from 'pinia';
import { ref } from 'vue';
import { fetchRecruitmentRequestList } from '@/services/recruitmentService';

export const useRecruitmentStore = defineStore('recruitment', () => {
    // 채용 요청서 목록 상태
    const recruitmentRequestList = ref([]);
    const loadingRecruitmentRequest = ref(false);
    const recruitmentRequestError = ref(null);

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

    return {
        // 채용 요청서 관련
        recruitmentRequestList,
        loadingRecruitmentRequest,
        recruitmentRequestError,
        loadRecruitmentRequestList,
    };
});
