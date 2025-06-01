import { defineStore } from 'pinia';
import { ref } from 'vue';
import {
    getCounter,
    incrementCounter,
    decrementCounter,
} from '@/services/counterService';

export const useCounterStore = defineStore('counter', () => {
    // ✅ 상태 정의
    const count = ref(0);
    const error = ref(null);
    const loading = ref(false);

    // ✅ 공통 핸들러
    const handle = async (fn) => {
        loading.value = true;
        error.value = null;

        try {
            await fn();
        } catch (err) {
            error.value = err?.response?.data?.message || '오류가 발생했습니다.';
        } finally {
            loading.value = false;
        }
    };

    // ✅ 액션 정의
    const fetchCount = async () => {
        await handle(async () => {
            count.value = await getCounter();
        });
    };

    const increment = async () => {
        await handle(async () => {
            count.value = await incrementCounter();
        });
    };

    const decrement = async () => {
        await handle(async () => {
            count.value = await decrementCounter();
        });
    };

    return {
        count,
        error,
        loading,
        fetchCount,
        increment,
        decrement,
    };
});
