// src/composables/useApiHandler.js
import { ref } from 'vue';

export function useApiHandler() {
    const loading = ref(false);
    const error = ref(null);

    async function execute(apiFn) {
        loading.value = true;
        error.value = null;

        try {
            const result = await apiFn();
            return result;
        } catch (err) {
            error.value = err?.response?.data?.message || '서버 통신 중 오류가 발생했습니다.';
            return null;
        } finally {
            loading.value = false;
        }
    }

    return {
        loading,
        error,
        execute,
    };
}
