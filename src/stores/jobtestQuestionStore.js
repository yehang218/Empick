import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import {
    getQuestionsService,
    getQuestionDetailService,
    createQuestionService,
    updateQuestionService,
    deleteQuestionService,
    deleteQuestionOptionsByQuestionId
} from '@/services/jobtestQuestionService';
import { getQuestionTypeLabel } from '@/constants/employment/questionTypes';
import { getDifficultyLabel } from '@/constants/employment/difficulty';
import { CreateQuestionRequestDTO, UpdateQuestionRequestDTO } from '@/dto/employment/jobtest/questionRequestDTO';

export const useJobtestQuestionStore = defineStore('question', () => {
    // 상태 정의
    const questions = ref([]);
    const loading = ref(false);
    const error = ref(null);
    const isEdit = ref(false);
    const form = ref({
        id: null,
        content: '',
        detailContent: '',
        type: 'MULTIPLE',
        difficulty: 'EASY',
        answer: '',
        createdMemberId: null,
        updatedMemberId: null,
        questionOptions: [],
        gradingCriteria: []
    });

    const hasSelectedQuestions = computed(() => {
        return questions.value.some(q => q.selected);
    });

    // ✅ 폼 초기화
    const resetForm = () => {
        form.value = {
            id: null,
            content: '',
            detailContent: '',
            type: 'MULTIPLE',
            difficulty: 'EASY',
            answer: '',
            createdMemberId: null,
            updatedMemberId: null,
            questionOptions: [],
            gradingCriteria: []
        };
        isEdit.value = false;
        error.value = null;
    };

    // ✅ 문제 상세 로드
    const loadQuestion = async (questionId, memberId) => {
        loading.value = true;
        error.value = null;

        try {
            const data = await getQuestionDetailService(questionId);
            form.value = {
                id: data.id,
                content: data.content,
                detailContent: data.detailContent,
                type: data.type,
                difficulty: data.difficulty,
                answer: data.answer,
                createdMemberId: data.createdMemberId,
                createdMemberName: data.createdMemberName,
                createdAt: data.createdAt,
                updatedMemberId: memberId,
                updatedMemberName: data.updatedMemberName,
                updatedAt: data.updatedAt,
                questionOptions: (data.options || []).map(opt => ({
                    ...opt,
                    isAnswer: opt.content === data.answer
                })),
                usedJobTests:data.usedJobTests || [],
                gradingCriteria: data.gradingCriteria || []
            };
            isEdit.value = true;
        } catch (err) {
            error.value = err.message || '문제 정보를 불러오는 중 오류 발생';
            throw err;
        } finally {
            loading.value = false;
        }
    };

    // ✅ 등록 / 수정 처리
    const submitQuestion = async (memberId) => {
        loading.value = true;
        error.value = null;

        try {
            const formCopy = { ...form.value };
            if (formCopy.type === 'MULTIPLE') {
                formCopy.answer = formCopy.questionOptions.find(opt => opt.isAnswer)?.content || '';
            }

            if (isEdit.value) {
                formCopy.updatedMemberId = memberId;
                const dto = new UpdateQuestionRequestDTO(
                    formCopy.content,
                    formCopy.detailContent,
                    formCopy.type,
                    formCopy.difficulty,
                    formCopy.answer,
                    formCopy.updatedMemberId,
                    formCopy.questionOptions,
                    formCopy.gradingCriteria
                );
                await updateQuestionService(formCopy.id, dto);
            } else {
                formCopy.createdMemberId = memberId;
                const dto = CreateQuestionRequestDTO.fromForm(formCopy);
                await createQuestionService(dto);
            }
        } catch (err) {
            error.value = err.message || '저장 중 오류 발생';
            throw err;
        } finally {
            loading.value = false;
        }
    };

    // ✅ 문제 목록 조회
    const fetchQuestions = async () => {
        loading.value = true;
        error.value = null;

        try {
            const response = await getQuestionsService();

            const mapped = response
                .filter(q => q.type !== 'DESCRIPTIVE') // 서술형 우선 제외
                .sort((a, b) => b.id - a.id)
                .map(question => ({
                    ...question,
                    id: Number(question.id),
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


    // ✅ 문제 일괄 삭제
    const deleteSelectedQuestions = async () => {
        const selectedIds = getSelectedQuestionIds();

        for (const id of selectedIds) {
            try {
                const question = questions.value.find(q => q.id === id);
                if (question?.type === '선택형' || question?.type === 'MULTIPLE') {
                    await deleteQuestionOptionsByQuestionId(id);
                }
                await deleteQuestionService(id);
            } catch (err) {
                console.error(`문제 ID ${id} 삭제 실패:`, err);
                throw err;
            }
        }

        await fetchQuestions();
        clearSelection();
    };

    // ✅ ID 배열로 문제 일괄 삭제
    const deleteQuestionsByIds = async (questionIds, questionTypes) => {
        const deletePromises = questionIds.map((id, index) => {
            return (async () => {
                try {
                    const type = questionTypes[index];
                    if (type === '선택형' || type === 'MULTIPLE') {
                        await deleteQuestionOptionsByQuestionId(id);
                    }
                    await deleteQuestionService(id);
                } catch (err) {
                    console.error(`문제 ID ${id} 삭제 실패:`, err);
                    throw err; // 오류를 다시 던져 Promise.all이 실패하도록 함
                }
            })();
        });
    
        try {
            await Promise.all(deletePromises);
            await fetchQuestions(); // 모든 삭제 성공 후 목록 새로고침
        } catch (err) {
            // 하나 이상의 삭제가 실패하면 여기로옴
            error.value = '일부 문제 삭제에 실패했습니다. 목록을 확인해주세요.';
            await fetchQuestions(); // 실패하더라도 목록은 새로고침
            throw err; // 호출한 쪽에서 오류를 처리할 수 있도록 다시 던짐
        }
    };

    // ✅ 단일 문제 삭제
const deleteQuestion = async (questionId, type) => {
    try {
        if (type === '선택형' || type === 'MULTIPLE') {
            await deleteQuestionOptionsByQuestionId(questionId)
        }
        await deleteQuestionService(questionId)
        await fetchQuestions()
    } catch (err) {
        error.value = err.message || '문제 삭제 중 오류 발생'
        throw err
    }
}

    // ✅ 선택 관련 유틸
    const toggleQuestionSelection = (questionId) => {
        const question = questions.value.find(q => q.id === questionId);
        if (question) {
            question.selected = !question.selected;
        }
    };

    const getSelectedQuestions = () => {
        return questions.value.filter(q => q.selected);
    };

    const getSelectedQuestionIds = () => {
        return questions.value.filter(q => q.selected).map(q => q.id);
    };

    const clearSelection = () => {
        questions.value.forEach(q => q.selected = false);
    };

    return {
        // 상태
        questions,
        form,
        loading,
        error,
        isEdit,

        // 게터
        hasSelectedQuestions,

        // 액션
        fetchQuestions,
        toggleQuestionSelection,
        getSelectedQuestions,
        getSelectedQuestionIds,
        clearSelection,
        deleteSelectedQuestions,
        deleteQuestion,
        deleteQuestionsByIds,

        // 등록/수정용
        resetForm,
        loadQuestion,
        submitQuestion
    };
});
