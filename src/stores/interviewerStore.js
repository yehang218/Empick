// stores/interviewerStore.js

import { defineStore } from 'pinia';
import { ref } from 'vue';

import {
    createInterviewerService,
    updateInterviewerScoreService,
    updateInterviewerReviewService,
    deleteInterviewerService,
    getAllInterviewers,
    getInterviewerByIdService,
    getInterviewersByInterviewIdService
} from '@/services/interviewerService';

export const useInterviewerStore = defineStore('interviewer', () => {
    // 상태
    const interviewerList = ref([]);
    const selectedInterviewer = ref(null);
    const loading = ref(false);
    const error = ref(null);

    // 🔍 조회 함수

    const fetchAllInterviewers = async () => {
        loading.value = true;
        error.value = null;
        try {
            const result = await getAllInterviewers();
            interviewerList.value = result;
        } catch (err) {
            error.value = err.message;
            throw err;
        } finally {
            loading.value = false;
        }
    };

    const fetchInterviewerById = async (id) => {
        loading.value = true;
        error.value = null;
        try {
            const result = await getInterviewerByIdService(id);
            selectedInterviewer.value = result
        } catch (err) {
            error.value = err.message;
            throw err;
        } finally {
            loading.value = false;
        }
    };

    const fetchInterviewersByInterviewId = async (interviewId) => {
        loading.value = true;
        error.value = null;
        try {
            const result = await getInterviewersByInterviewIdService(interviewId);
            interviewerList.value = result;
        } catch (err) {
            error.value = err.message;
            throw err;
        } finally {
            loading.value = false;
        }
    };

    // ✍️ 생성/수정/삭제 함수

    const createInterviewer = async (dto) => {
        const result = await createInterviewerService(dto);
        await fetchAllInterviewers();
        return result;
    };

    const updateInterviewerScore = async (id) => {
        const result = await updateInterviewerScoreService(id);
        await fetchAllInterviewers();
        return result;
    };

    const updateInterviewerReview = async (id, review) => {
        const result = await updateInterviewerReviewService(id, review);
        await fetchAllInterviewers();
        return result;
    };

    const deleteInterviewer = async (id) => {
        const result = await deleteInterviewerService(id);
        await fetchAllInterviewers();
        return result;
    };

    return {
        // 상태
        interviewerList,
        selectedInterviewer,
        loading,
        error,

        // 액션
        fetchAllInterviewers,
        fetchInterviewerById,
        fetchInterviewersByInterviewId,
        createInterviewer,
        updateInterviewerScore,
        updateInterviewerReview,
        deleteInterviewer
    };
});
