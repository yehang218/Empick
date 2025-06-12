// stores/interviewSheetStore.js

import { defineStore } from 'pinia';
import { ref } from 'vue';

import {
  createSheetService,
  updateSheetService,
  deleteSheetService,
  getAllSheetsService,
  getSheetByIdService,
  searchSheetByNameService,
} from '@/services/interview/interviewSheetService';

export const useInterviewSheetStore = defineStore('interviewSheet', () => {
  const sheetList = ref([]);
  const selectedSheet = ref(null);
  const loading = ref(false);
  const error = ref(null);

  // 전체 조회
  const fetchAllSheets = async () => {
    loading.value = true;
    error.value = null;
    try {
      const data = await getAllSheetsService();
      sheetList.value = data;
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  // ID로 조회
  const fetchSheetById = async (id) => {
    loading.value = true;
    error.value = null;
    try {
      const data = await getSheetByIdService(id);
      selectedSheet.value = data;
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  // 이름으로 검색
  const searchSheetByName = async (name) => {
    loading.value = true;
    error.value = null;
    try {
      const data = await searchSheetByNameService(name);
      sheetList.value = data;
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  // 생성
  const createSheet = async (dto) => {
    const result = await createSheetService(dto);
    await fetchAllSheets();
    return result;
  };

  // 수정
  const updateSheet = async (id, dto) => {
    const result = await updateSheetService(id, dto);
    await fetchAllSheets();
    return result;
  };

  // 삭제
  const deleteSheet = async (id) => {
    const result = await deleteSheetService(id);
    await fetchAllSheets();
    return result;
  };

  return {
    sheetList,
    selectedSheet,
    loading,
    error,

    fetchAllSheets,
    fetchSheetById,
    searchSheetByName,

    createSheet,
    updateSheet,
    deleteSheet,
  };
});
