import { defineStore } from 'pinia';
import { ref } from 'vue';
import { createApplicationJobtestService } from '@/services/jobtestService';

export const useApplicationJobtestStore = defineStore('applicationJobtest', () => {
    const errorMessage = ref('');

    const assignJobtest = async (dtoList) => {
        errorMessage.value = '';
        try {
            for (const dto of dtoList) {
                console.log(dto.applicationId);
                await createApplicationJobtestService(dto.applicationId, dto.jobtestId, { showToast: false, redirect: false });
            }
        } catch (e) {
            errorMessage.value = e?.message || '문제 할당 중 오류가 발생했습니다.';
            throw e;
        }
    };

    return {
        assignJobtest,
        errorMessage
    };
});