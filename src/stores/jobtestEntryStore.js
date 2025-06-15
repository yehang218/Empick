import { defineStore } from 'pinia';
import { ref } from 'vue';
import { verifyJobtestEntryService } from '@/services/jobtestService';
import { JobtestEntryRequestDTO } from '@/dto/employment/jobtest/jobtestEntryRequestDTO';

export const useJobtestEntryStore = defineStore('jobtestEntry', () => {
    const errorMessage = ref('');

    /**
     * 입장 코드 검증
     */
    const verifyEntryCode = async (jobtestId, entryCode) => {
        errorMessage.value = '';

        try {
            const dto = new JobtestEntryRequestDTO(entryCode);
            const message = await verifyJobtestEntryService(jobtestId, dto, { redirect: false });
            return message;
        } catch (err) {
            errorMessage.value = err?.message || '입장 코드 검증 중 오류가 발생했습니다.';
            throw err;
        }
    };

    return {
        errorMessage,
        verifyEntryCode,
    };
});