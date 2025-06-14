import { defineStore } from 'pinia';
import { ref } from 'vue';
import { getAnswersByApplicationJobtestId } from '@/services/answerService';

export const useAnswerStore = defineStore('answer', () => {
    const answers = ref([]);
    const loading = ref(false);
    const error = ref(null);

    const fetchAnswers = async (applicationJobtestId) => {
        loading.value = true;
        error.value = null;
        try {
            const data = await getAnswersByApplicationJobtestId(applicationJobtestId);
            answers.value = data;
        } catch (err) {
            error.value = err.message || '답변 불러오기 실패';
        } finally {
            loading.value = false;
        }
    };

    return {
        answers, loading, error, fetchAnswers
    };
});
