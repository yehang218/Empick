// stores/interviewSheetItemStore.js

import { defineStore } from 'pinia';
import { ref } from 'vue';

import {
  createSheetItemService,
  deleteSheetItemService,
  findAllSheetItemsService,
  findSheetItemByIdService,
  findSheetItemsBySheetIdService,
  findSheetItemsByCriteriaIdService
} from '@/services/interviewSheetItemService';

export const useInterviewSheetItemStore = defineStore('interviewSheetItem', () => {
  // 상태
  const sheetItemList = ref([]);
  const selectedSheetItem = ref(null);
  const loading = ref(false);
  const error = ref(null);

  // 전체 조회
  const fetchAllSheetItems = async () => {
    loading.value = true;
    error.value = null;
    try {
      const data = await findAllSheetItemsService();
      sheetItemList.value = data;
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  // 단일 조회 (id)
  const fetchSheetItemById = async (id) => {
    loading.value = true;
    error.value = null;
    try {
      const data = await findSheetItemByIdService(id);
      selectedSheetItem.value = data;
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  // 평가표 ID로 조회
  const fetchSheetItemsBySheetId = async (sheetId) => {
    loading.value = true;
    error.value = null;
    try {
      const data = await findSheetItemsBySheetIdService(sheetId);
      sheetItemList.value = data;
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  // 평가 기준 ID로 조회
  const fetchSheetItemsByCriteriaId = async (criteriaId) => {
    loading.value = true;
    error.value = null;
    try {
      const data = await findSheetItemsByCriteriaIdService(criteriaId);
      sheetItemList.value = data;
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  // 생성
  const createSheetItem = async (dto) => {
    const result = await createSheetItemService(dto);
    await fetchAllSheetItems();
    return result;
  };

  // 삭제
  const deleteSheetItem = async (id) => {
    const result = await deleteSheetItemService(id);
    await fetchAllSheetItems();
    return result;
  };

  return {
    // 상태
    sheetItemList,
    selectedSheetItem,
    loading,
    error,

    // 액션
    fetchAllSheetItems,
    fetchSheetItemById,
    fetchSheetItemsBySheetId,
    fetchSheetItemsByCriteriaId,

    createSheetItem,
    deleteSheetItem
  };
});
