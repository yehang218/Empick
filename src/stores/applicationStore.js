import { defineStore } from 'pinia';
import { ref } from 'vue';

import {
  getAllApplicationsService,
  getApplicationByIdService,
  createApplicationService,
  updateApplicationStatusService,
  deleteApplicationService,
  createApplicationResponseService
} from '@/services/applicationService';

export const useApplicationStore = defineStore('application', () => {
  // 상태
  const applicationList = ref([]);
  const selectedApplication = ref(null);
  const loading = ref(false);
  const error = ref(null);

  // 🔍 전체 지원서 목록 조회
  const fetchAllApplications = async () => {
    loading.value = true;
    error.value = null;
    try {
      const result = await getAllApplicationsService();
      applicationList.value = result;
      return result;
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  // 🔍 ID로 단일 지원서 조회
  const fetchApplicationById = async (id) => {
    loading.value = true;
    error.value = null;
    try {
      const result = await getApplicationByIdService(id);
      selectedApplication.value = result;
      return result;
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  // ✍️ 지원서 생성
  const createApplication = async (dto) => {
    const result = await createApplicationService(dto);
    await fetchAllApplications();
    return result;
  };

  // 🔁 지원서 상태 업데이트
  const updateApplicationStatus = async (id, dto) => {
    const result = await updateApplicationStatusService(id, dto);
    await fetchAllApplications();
    return result;
  };

  // ❌ 지원서 삭제
  const deleteApplication = async (id) => {
    const result = await deleteApplicationService(id);
    await fetchAllApplications();
    return result;
  };

  // 📄 지원서 응답 생성
  const createApplicationResponse = async (dto) => {
    return await createApplicationResponseService(dto);
  };

  return {
    // 상태
    applicationList,
    selectedApplication,
    loading,
    error,

    // 액션
    fetchAllApplications,
    fetchApplicationById,
    createApplication,
    updateApplicationStatus,
    deleteApplication,
    createApplicationResponse,
  };
});
