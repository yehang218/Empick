import api from '@/apis/apiClient';
import { ApprovalAPI } from '@/apis/routes/approval';
import ApiResponseDTO from '@/dto/common/apiResponseDTO';
import { withErrorHandling, throwCustomApiError } from '@/utils/errorHandler';

// 결재 문서의 내용 목록 조회
export const getContentsByApprovalId = async (approvalId) => {  };

// 결재 문서의 내용 저장
export const saveApprovalContents = async (approvalId, contentDTOs) => {  };

// 항목별 내용 상세 조회
export const getContentById = async (approvalId, contentId) => {  };