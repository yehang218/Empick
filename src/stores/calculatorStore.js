import { defineStore } from 'pinia';
import { add, subtract } from '@/services/calculatorService';
import { useApiHandler } from '@/composables/useApiHandler';

export const useCalculatorStore = defineStore('calculator', {
    state: () => ({
        result: null,
        error: null,
        loading: false,
    }),
    actions: {
        async calculate(type, a, b) {
            const { execute, loading, error } = useApiHandler();
            const result = await execute(() =>
                type === 'add' ? add(a, b) : subtract(a, b)
            );

            if (result !== null) {
                this.result = result;
            }

            this.loading = loading.value;
            this.error = error.value;
        },
        resetResult() {
            this.result = null;
            this.error = null;
        },
    },
});
