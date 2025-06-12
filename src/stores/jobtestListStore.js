import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { getQuestionsService } from '@/services/jobtestService';
import { getDifficultyLabel } from '@/constants/employment/difficulty';

export const useJobtestListStore = defineStore('jobtestList', () => {
    const jobtests = ref([]);
    const selectedIds = ref([]);
    const loading = ref(false);
    const error = ref(null);

    const hasSelectedJobtests = computed(() => selectedIds.value.length > 0);

    const fetchJobtests = async () => {
        loading.value = true;
        error.value = null;
        try {
            const response = await getQuestionsService(); // ✅ 서비스 사용
            jobtests.value = response.map(item => ({
                ...item,
                difficulty: getDifficultyLabel(item.difficulty)
            }));
        } catch (e) {
            error.value = e.message || '실무 테스트 목록 로딩 실패';
            throw e;
        } finally {
            loading.value = false;
        }
    };

    const toggleSelection = (id) => {
        const index = selectedIds.value.indexOf(id);
        if (index >= 0) selectedIds.value.splice(index, 1);
        else selectedIds.value.push(id);
    };

    const clearSelection = () => selectedIds.value.splice(0);

    return {
        jobtests,
        selectedIds,
        loading,
        error,
        hasSelectedJobtests,
        fetchJobtests,
        toggleSelection,
        clearSelection
    };
});