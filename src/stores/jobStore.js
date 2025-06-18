import { defineStore } from 'pinia';
import { ref } from 'vue';
import { fetchJobList } from '@/services/jobService';

export const useJobStore = defineStore('job', () => {
    const jobList = ref([]);

    const loadJobList = async () => {
        try {
            const result = await fetchJobList();
            jobList.value = result;
        } catch (err) {
            console.error('직무 목록 조회 실패:', err);
        }
    };

    return {
        jobList,
        loadJobList,
    };
}); 