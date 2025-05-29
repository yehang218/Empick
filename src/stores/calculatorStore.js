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
                this.result = type === 'add'
                    ? await add(a, b)
                    : await subtract(a, b);
            } catch (err) {
                this.error = err?.response?.data?.message || '오류가 발생했습니다.';
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
