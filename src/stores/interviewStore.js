// stores/interviewStore.js

import { defineStore } from 'pinia';
import { ref } from 'vue';

import {
  createInterviewService,
  updateInterviewService,
  updateInterviewDatetimeService,
  updateInterviewAddressService,
  deleteInterviewService,
  getAllInterviewsService,
  getInterviewByIdService,
  getInterviewByApplicationIdService,
  getInterviewsByDateService,
  checkAvailableDatetimeService
} from '@/services/interviewService';

export const useInterviewStore = defineStore('interview', () => {
  // 상태
  const interviewList = ref([]);
  const selectedInterview = ref(null);
  const loading = ref(false);
  const error = ref(null);
  const isDatetimeAvailable = ref(null);

  // 조회

  const fetchAllInterviews = async () => {
    loading.value = true;
    error.value = null;
    try {
      const data = await getAllInterviewsService();
      interviewList.value = data;
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  const fetchInterviewById = async (id) => {
    loading.value = true;
    error.value = null;
    try {
      const data = await getInterviewByIdService(id);
      if (data) {
        selectedInterview.value = data;
      } else {
        selectedInterview.value = null;
        console.log('!error : fetchInterviewById');
      }
      selectedInterview.value = data;
      return data;
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  const fetchInterviewByApplicationId = async (applicationId) => {
    loading.value = true;
    error.value = null;
    try {
      const data = await getInterviewByApplicationIdService(applicationId);
      selectedInterview.value = data;
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  const fetchInterviewsByDate = async (date) => {
    loading.value = true;
    error.value = null;
    try {
      const data = await getInterviewsByDateService(date);
      interviewList.value = data;
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  const checkDatetimeAvailability = async (datetime) => {
    loading.value = true;
    error.value = null;
    try {
      const result = await checkAvailableDatetimeService(datetime);
      isDatetimeAvailable.value = result;
    } catch (err) {
      error.value = err.message;
      isDatetimeAvailable.value = null;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  // 생성/수정/삭제

  const createInterview = async (dto) => {
    const result = await createInterviewService(dto);
    await fetchAllInterviews();
    return result;
  };

  const updateInterview = async (id, dto) => {
    const result = await updateInterviewService(id, dto);
    await fetchAllInterviews();
    return result;
  };

  const updateInterviewDatetime = async (id, datetime) => {
    const result = await updateInterviewDatetimeService(id, datetime);
    await fetchAllInterviews();
    return result;
  };

  const updateInterviewAddress = async (id, address) => {
    const result = await updateInterviewAddressService(id, address);
    await fetchAllInterviews();
    return result;
  };

  const deleteInterview = async (id) => {
    const result = await deleteInterviewService(id);
    await fetchAllInterviews();
    return result;
  };

  return {
    // 상태
    interviewList,
    selectedInterview,
    loading,
    error,
    isDatetimeAvailable,

    // 액션
    fetchAllInterviews,
    fetchInterviewById,
    fetchInterviewByApplicationId,
    fetchInterviewsByDate,
    checkDatetimeAvailability,

    createInterview,
    updateInterview,
    updateInterviewDatetime,
    updateInterviewAddress,
    deleteInterview
  };
});
