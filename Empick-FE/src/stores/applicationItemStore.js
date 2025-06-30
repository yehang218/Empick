import { defineStore } from 'pinia';
import { ref } from 'vue';
import { fetchApplicationItemsByRecruitment } from '@/services/applicationItemService';

export const useApplicationItemStore = defineStore('applicationItem', () => {
    const items = ref([]);
    const loading = ref(false);
    const error = ref(null);

    // 공고별 항목 로딩 (ApplicationItemDTO)
    const loadApplicationItems = async (recruitmentId) => {
        loading.value = true;
        error.value = null;
        try {
            const result = await fetchApplicationItemsByRecruitment(recruitmentId);
            items.value = result;
        } catch (err) {
            error.value = err.message || '지원서 항목 조회 중 오류 발생';
            console.error('❌ 지원서 항목 로딩 오류:', err);
        } finally {
            loading.value = false;
        }
    };

    return {
        items,
        loading,
        error,
        loadApplicationItems
    };
});
