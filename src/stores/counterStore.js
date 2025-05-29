import { defineStore } from 'pinia';
import {
    getCounter,
    incrementCounter,
    decrementCounter,
} from '@/services/counterService';
import { useApiHandler } from '@/composables/useApiHandler';

export const useCounterStore = defineStore('counter', {
    state: () => ({
        count: 0,
        error: null,
        loading: false,
    }),
    actions: {
        async fetchCount() {
            const { execute, loading, error } = useApiHandler();
            const result = await execute(() => getCounter());
            if (result !== null) this.count = result;
            this.loading = loading.value;
            this.error = error.value;
        },
        async increment() {
            const { execute, loading, error } = useApiHandler();
            const result = await execute(() => incrementCounter());
            if (result !== null) this.count = result;
            this.loading = loading.value;
            this.error = error.value;
        },
        async decrement() {
            const { execute, loading, error } = useApiHandler();
            const result = await execute(() => decrementCounter());
            if (result !== null) this.count = result;
            this.loading = loading.value;
            this.error = error.value;
        },
    },
});
