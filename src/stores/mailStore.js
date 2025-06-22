import { defineStore } from 'pinia';
import { ref } from 'vue';
import {
  createMailService,
  sendMailService,
  sendJobtestMailService,
  sendInterviewMailService,
  findAllMailsService,
  findMailByIdService,
  findMailsByEmailService,
} from '@/services/mailService';

export const useMailStore = defineStore('mail', () => {
  const mails = ref([]);
  const selectedMail = ref(null);
  const loading = ref(false);
  const error = ref(null);

  // 모든 메일 조회
  const fetchAllMails = async () => {
    loading.value = true;
    error.value = null;
    try {
      const result = await findAllMailsService();
      mails.value = result;
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  // ID로 메일 조회
  const fetchMailById = async (id) => {
    loading.value = true;
    error.value = null;
    try {
      const result = await findMailByIdService(id);
      selectedMail.value = result;
      return result;
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  // 이메일로 메일 조회
  const fetchMailsByEmail = async (email) => {
    loading.value = true;
    error.value = null;
    try {
      const result = await findMailsByEmailService(email);
      mails.value = result;
      return result;
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  // 메일 생성
  const createMail = async (dto) => {
    loading.value = true;
    error.value = null;
    try {
      const result = await createMailService(dto);
      return result;
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  // 메일 전송
  const sendMail = async (dto) => {
    loading.value = true;
    error.value = null;
    try {
      const result = await sendMailService(dto);
      return result;
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  // 실무 테스트 메일 전송
  const sendJobtestMail = async (id, senderId) => {
    loading.value = true;
    error.value = null;
    try {
      const result = await sendJobtestMailService(id, senderId);
      return result;
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  // 면접 메일 전송
  const sendInterviewMail = async (id, senderId) => {
    loading.value = true;
    error.value = null;
    try {
      const result = await sendInterviewMailService(id, senderId);
      return result;
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  // 선택된 메일 초기화
  const clearSelectedMail = () => {
    selectedMail.value = null;
  };

  // 에러 초기화
  const clearError = () => {
    error.value = null;
  };

  return {
    // 상태
    mails,
    selectedMail,
    loading,
    error,
    
    // 액션
    fetchAllMails,
    fetchMailById,
    fetchMailsByEmail,
    createMail,
    sendMail,
    sendJobtestMail,
    sendInterviewMail,
    clearSelectedMail,
    clearError,
  };
});
