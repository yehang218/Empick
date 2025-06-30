import api from '@/apis/apiClient';
import { ApprovalAPI } from '@/apis/routes/approval';
import ApiResponseDTO from '@/dto/common/apiResponseDTO';
import { withErrorHandling, throwCustomApiError } from '@/utils/errorHandler';

// 특정 결재 유형의 항목 목록 조회
export const getCategoryItemsByCategoryId = async (categoryId) => { };

// 결재 유형 항목 추가
export const createCategoryItem = async (categoryId, dto) => {  };

// 항목 상세 조회
export const getCategoryItemById = async (categoryId, itemId) => {  };