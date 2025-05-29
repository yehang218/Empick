// src/stores/counterStore.js
import { defineStore } from 'pinia';
import {
    getCounter,
    incrementCounter,
    decrementCounter,
} from '@/services/counterService';

export const useCounterStore = defineStore('counter', {
    state: () => ({
        count: 0,
        error: null,
        loading: false,
    }),
    actions: {
        async fetchCount() {
            this.loading = true;
            this.error = null;

            try {
                const result = await getCounter();
                console.log('[STORE] fetchCount() - result:', result);
                this.count = result;
            } catch (err) {
                this.error = err?.response?.data?.message || '서버 오류';
                console.error('[STORE] fetchCount() - 실패:', this.error);
            } finally {
                this.loading = false;
            }
        },

        async increment() {
            this.loading = true;
            this.error = null;

            try {
                const result = await incrementCounter();
                console.log('[STORE] increment() - result:', result);
                this.count = result;
            } catch (err) {
                this.error = err?.response?.data?.message || '서버 오류';
                console.error('[STORE] increment() - 실패:', this.error);
            } finally {
                this.loading = false;
            }
        },

        async decrement() {
            this.loading = true;
            this.error = null;

            try {
                const result = await decrementCounter();
                console.log('[STORE] decrement() - result:', result);
                this.count = result;
            } catch (err) {
                this.error = err?.response?.data?.message || '서버 오류';
                console.error('[STORE] decrement() - 실패:', this.error);
            } finally {
                this.loading = false;
            }
        },
    },
});
