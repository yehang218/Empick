import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { getQuestionsService } from '@/services/jobtestQuestionService';
import { getQuestionTypeLabel } from '@/constants/employment/questionTypes';
import { getDifficultyLabel } from '@/constants/employment/difficulty';

export const useJobtestStore = defineStore('jobtest', () => {
    // 상태 정의
    const questions = ref([]);
    const loading = ref(false);
    const error = ref(null);

    // 게터
    const hasSelectedQuestions = computed(() => {
        return questions.value.some(q => q.selected);
    });

    // 문제 목록 id 큰 순 조회
    const fetchQuestions = async () => {
        loading.value = true;
        error.value = null;

        try {
            const response = await getQuestionsService();
            const mapped = response
                .sort((a, b) => b.id - a.id)
                .map(question => ({
                    ...question,
                    id: Number(question.id), // 숫자로!
                    selected: false,
                    type: getQuestionTypeLabel(question.type),
                    difficulty: getDifficultyLabel(question.difficulty)
                }));
            questions.value.splice(0, questions.value.length, ...mapped);
        } catch (err) {
            error.value = err.message;
            throw err;
        } finally {
            loading.value = false;
        }
    };


    // 문제 선택 토글
    const toggleQuestionSelection = (questionId) => {
        const question = questions.value.find(q => q.id === questionId);
        if (question) {
            question.selected = !question.selected;
        }
    };

    // 선택된 문제들 가져오기
    const getSelectedQuestions = () => {
        return questions.value.filter(q => q.selected);
    };

    // 선택된 문제들의 ID 가져오기
    const getSelectedQuestionIds = () => {
        return questions.value
            .filter(q => q.selected)
            .map(q => q.id);
    };

    // 모든 선택 해제
    const clearSelection = () => {
        questions.value.forEach(q => q.selected = false);
    };

    return {
        // 상태
        questions,
        loading,
        error,

        // 게터
        hasSelectedQuestions,

        // 액션
        fetchQuestions,
        toggleQuestionSelection,
        getSelectedQuestions,
        getSelectedQuestionIds,
        clearSelection
    };
}); 