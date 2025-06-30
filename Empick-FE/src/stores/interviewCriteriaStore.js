import { defineStore } from 'pinia';
import { ref, computed } from 'vue';

import {
    createCriteriaService,
    updateCriteriaService,
    deleteCriteriaService,
    getAllCriteriaService,
    getCriteriaByIdService,
    getCriteriaBySheetIdService,
    searchCriteriaByTitleService,
} from '@/services/interviewCriteriaService';

export const useInterviewCriteriaStore = defineStore('interviewCriteria', () => {
    // 🔹 상태
    const criteriaList = ref([]);
    const selectedCriteria = ref(null);
    const searchResults = ref([]);

    const loading = ref(false);
    const error = ref(null);

    // 🔹 게터
    const hasCriteria = computed(() => criteriaList.value.length > 0);
    const hasSelected = computed(() => !!selectedCriteria.value);
    const hasSearchResults = computed(() => searchResults.value.length > 0);

    // 🔹 액션
    const fetchAllCriteria = async () => {
        loading.value = true;
        error.value = null;
        try {
            criteriaList.value = await getAllCriteriaService();
        } catch (err) {
            error.value = err.message;
        } finally {
            loading.value = false;
        }
    };

    const fetchCriteriaById = async (id) => {
        loading.value = true;
        error.value = null;
        try {
            const result = await getCriteriaByIdService(id);
            selectedCriteria.value = result[0] || null;
        } catch (err) {
            error.value = err.message;
        } finally {
            loading.value = false;
        }
    };

    const fetchCriteriaBySheetId = async (sheetId) => {
        loading.value = true
        error.value = null
        try {
            const result = await getCriteriaBySheetIdService(sheetId)
            criteriaList.value = result
            selectedCriteria.value = null
            return result
        } catch (err) {
            error.value = err.message
            criteriaList.value = []
            throw err
        } finally {
            loading.value = false
        }
    }

    const searchCriteriaByTitle = async (title) => {
        loading.value = true;
        error.value = null;
        try {
            searchResults.value = await searchCriteriaByTitleService(title);
        } catch (err) {
            error.value = err.message;
        } finally {
            loading.value = false;
        }
    };

    const createCriteria = async (dto) => {
        loading.value = true;
        error.value = null;
        try {
            const created = await createCriteriaService(dto);
            criteriaList.value.push(created);
            return created;
        } catch (err) {
            error.value = err.message;
            throw err;
        } finally {
            loading.value = false;
        }
    };

    const updateCriteria = async (id, dto) => {
        loading.value = true;
        error.value = null;
        try {
            const updated = await updateCriteriaService(id, dto);
            const index = criteriaList.value.findIndex(c => c.id === id);
            if (index !== -1) {
                criteriaList.value[index] = updated;
            }
            if (selectedCriteria.value?.id === id) {
                selectedCriteria.value = updated;
            }
            return updated;
        } catch (err) {
            error.value = err.message;
            throw err;
        } finally {
            loading.value = false;
        }
    };

    const deleteCriteria = async (id) => {
        loading.value = true;
        error.value = null;
        try {
            const deleted = await deleteCriteriaService(id);
            criteriaList.value = criteriaList.value.filter(c => c.id !== id);
            if (selectedCriteria.value?.id === id) {
                selectedCriteria.value = null;
            }
            return deleted;
        } catch (err) {
            error.value = err.message;
            throw err;
        } finally {
            loading.value = false;
        }
    };

    const clearSelection = () => {
        selectedCriteria.value = null;
    };

    const clearSearchResults = () => {
        searchResults.value = [];
    };

    return {
        // 상태
        criteriaList,
        selectedCriteria,
        searchResults,
        loading,
        error,

        // 게터
        hasCriteria,
        hasSelected,
        hasSearchResults,

        // 액션
        fetchAllCriteria,
        fetchCriteriaById,
        fetchCriteriaBySheetId,
        searchCriteriaByTitle,
        createCriteria,
        updateCriteria,
        deleteCriteria,
        clearSelection,
        clearSearchResults,
    };
});
