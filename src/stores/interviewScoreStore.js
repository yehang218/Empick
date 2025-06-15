// stores/interviewScoreStore.js

import { defineStore } from 'pinia';
import { ref } from 'vue';

import {
    createInterviewScoreService,
    updateInterviewScoreService,
    deleteInterviewScoreService,
    findAllInterviewScoresService,
    findInterviewScoreByIdService,
    findInterviewScoresByInterviewerIdService
} from '@/services/interviewScoreService';

export const useInterviewScoreStore = defineStore('interviewScore', () => {
    // 상태
    const scoreList = ref([]);
    const selectedScore = ref(null);
    const loading = ref(false);
    const error = ref(null);

    // 🔍 조회

    const fetchAllScores = async () => {
        loading.value = true;
        error.value = null;
        try {
            const result = await findAllInterviewScoresService();
            scoreList.value = result;
        } catch (err) {
            error.value = err.message;
            throw err;
        } finally {
            loading.value = false;
        }
    };

    const fetchScoreById = async (id) => {
        loading.value = true;
        error.value = null;
        try {
            const result = await findInterviewScoreByIdService(id);
            selectedScore.value = result;
        } catch (err) {
            error.value = err.message;
            throw err;
        } finally {
            loading.value = false;
        }
    };

    const fetchScoresByInterviewerId = async (interviewerId) => {
        loading.value = true;
        error.value = null;
        try {
            const result = await findInterviewScoresByInterviewerIdService(interviewerId);
            scoreList.value = result;
        } catch (err) {
            error.value = err.message;
            throw err;
        } finally {
            loading.value = false;
        }
    };

    // ✍️ 생성/수정/삭제

    const createScore = async (dto) => {
        const result = await createInterviewScoreService(dto);
        await fetchAllScores();
        return result;
    };

    const updateScore = async (id, dto) => {
        const result = await updateInterviewScoreService(id, dto);
        await fetchAllScores();
        return result;
    };

    const deleteScore = async (id) => {
        const result = await deleteInterviewScoreService(id);
        await fetchAllScores();
        return result;
    };

    return {
        // 상태
        scoreList,
        selectedScore,
        loading,
        error,

        // 액션
        fetchAllScores,
        fetchScoreById,
        fetchScoresByInterviewerId,
        createScore,
        updateScore,
        deleteScore
    };
});
