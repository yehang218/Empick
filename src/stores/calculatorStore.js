import { defineStore } from 'pinia';
import { ref } from 'vue';
import { add, subtract } from '@/services/calculatorService';

export const useCalculatorStore = defineStore('calculator', () => {
    // ✅ 상태 정의
    const result = ref(null);
    const error = ref(null);
    const loading = ref(false);

    // ✅ 계산 실행 함수
    const calculate = async (type, a, b) => {
        loading.value = true;
        error.value = null;

        try {
            result.value = type === 'add'
                ? await add(a, b)
                : await subtract(a, b);
        } catch (err) {
            error.value = err?.response?.data?.message || '오류가 발생했습니다.';
        } finally {
            loading.value = false;
        }
    };

    // ✅ 결과 초기화
    const resetResult = () => {
        result.value = null;
        error.value = null;
    };

    return {
        result,
        error,
        loading,
        calculate,
        resetResult,
    };
});
