import { defineStore } from 'pinia';
import { ref, onMounted } from 'vue';
import { getAnswersByApplicationJobtestId } from '@/services/answerService';
import { fetchAnswers } from '@/services/jobtestService';

export const useAnswerStore = defineStore('answer', {
    state: () => ({
        answers: ref([]),
        loading: ref(false),
        error: ref(null),
    }),
    actions: {
        async fetchAnswers(applicationJobtestId) {
            this.loading = true;
            this.error = null;
            try {
                const data = await getAnswersByApplicationJobtestId(applicationJobtestId);
                this.answers = data;
            } catch (err) {
                this.error = err.message || '답변 불러오기 실패';
            } finally {
                this.loading = false;
            }
        },
    },
});
