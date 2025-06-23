import { defineStore } from 'pinia';
import { ref } from 'vue';
import { createApplicationJobtestService, getApplicationJobtestByApplicationIdService } from '@/services/jobtestService';

export const useApplicationJobtestStore = defineStore('applicationJobtest', () => {
    const errorMessage = ref('');
    const applicationJobtest = ref(null);
    const loading = ref(false);
    const error = ref(null);

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

    const fetchApplicationJobtest = async (applicationId) => {
        loading.value = true;
        error.value = null;
        try {
            const data = await getApplicationJobtestByApplicationIdService(applicationId);
            console.log('API raw data:', data);
            applicationJobtest.value = data;
        } catch (err) {
            error.value = err.message || '지원서에 할당된 실무테스트 불러오기 실패';
        } finally {
            loading.value = false;
        }
    };

    return {
        assignJobtest,
        errorMessage,
        applicationJobtest,
        fetchApplicationJobtest,
        loading,
        error
    };
});