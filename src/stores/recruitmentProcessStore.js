import { defineStore } from 'pinia';
import { ref } from 'vue';
import { fetchRecruitmentProcesses } from '@/services/recruitmentProcessService';

export const useRecruitmentProcessStore = defineStore('recruitmentProcess', () => {
    const processList = ref([]);
    const loading = ref(false);
    const error = ref(null);

    const loadProcesses = async (recruitmentId) => {
        loading.value = true;
        error.value = null;

        try {
            const result = await fetchRecruitmentProcesses(recruitmentId);
            processList.value = result;
        } catch (err) {
            error.value = err.message;
        } finally {
            loading.value = false;
        }
    };

    return {
        processList,
        loading,
        error,
        loadProcesses
    };
});
