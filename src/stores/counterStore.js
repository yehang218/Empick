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
            await this._handle(async () => {
                this.count = await getCounter();
            });
        },

        async increment() {
            await this._handle(async () => {
                this.count = await incrementCounter();
            });
        },

        async decrement() {
            await this._handle(async () => {
                this.count = await decrementCounter();
            });
        },

        // ✅ 내부 전용 공통 처리 메서드
        async _handle(fn) {
            this.loading = true;
            this.error = null;

            try {
                await fn();
            } catch (err) {
                this.error = err?.response?.data?.message || '오류가 발생했습니다.';
            } finally {
                this.loading = false;
            }
        },
    },
});
