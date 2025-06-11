import { defineStore } from 'pinia';
import { ref } from 'vue';
import { fetchRecruitmentRequests, fetchRecruitmentRequestDetail } from '@/services/recruitmentRequestService';

export const useRecruitmentRequestStore = defineStore('recruitmentRequest', () => {
    const list = ref([]);
    const detail = ref(null);

    const loadList = async () => {
        list.value = await fetchRecruitmentRequests();
    };

    const loadDetail = async (id) => {
        detail.value = await fetchRecruitmentRequestDetail(id);
    };

    return { list, detail, loadList, loadDetail };
});
