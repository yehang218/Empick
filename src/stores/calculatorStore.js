// src/stores/calculatorStore.js
import { defineStore } from 'pinia';
import { add, subtract } from '@/services/calculatorService';

export const useCalculatorStore = defineStore('calculator', {
    state: () => ({
        result: null,
        error: null,
        loading: false,
    }),
    actions: {
        async calculate(type, a, b) {
            this.loading = true;
            this.error = null;

            try {
                const result = await (type === 'add' ? add(a, b) : subtract(a, b));
                console.log('[STORE] calculate() - result:', result);
                this.result = result;
            } catch (err) {
                this.error = err?.response?.data?.message || '계산 중 오류 발생';
                console.error('[STORE] calculate() - 실패:', this.error);
            } finally {
                this.loading = false;
            }
        },

        resetResult() {
            this.result = null;
            this.error = null;
        },
    },
});
