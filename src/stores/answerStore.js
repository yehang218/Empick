import { defineStore } from 'pinia';
import { ref, onMounted } from 'vue';
import { getAnswersByApplicationJobtestId, fetchAnswers } from '@/services/answerService';

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

    onMounted(async () => {
        const savedAnswers = await fetchAnswers({ applicationJobTestId: examData.value.applicationJobTestId })
        // savedAnswers를 answers.value에 맞게 매핑
        answers.value = questions.value.map(q => {
            const found = savedAnswers.find(a => a.questionId === q.questionId)
            if (!found) return null
            if (q.type === QUESTION_TYPES.MULTIPLE) {
                // 옵션 index로 변환
                return q.options.findIndex(opt => opt.content === found.content)
            } else {
                return found.content
            }
        })
    })

    return {
        answers, loading, error, fetchAnswers
    };
});
