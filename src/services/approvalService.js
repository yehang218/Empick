import api from '@/apis/apiClient';
import { ApprovalAPI } from '@/apis/routes/approval';
import ApiResponseDTO from '@/dto/common/apiResponseDTO';
import { withErrorHandling, throwCustomApiError } from '@/utils/errorHandler';

// 결재 문서 목록 조회 (작성자 기준)
export const getApprovalsByWriterId = async (writerId) => {  };

// 결재 문서 목록 조회 (결재자 기준)
export const getApprovalsByApproverId = async (approverId) => {  };

// 단건 결재 조회
export const getApprovalById = async (approvalId) => { };

// 결재 생성
export const createApproval = async (dto) => {  };

// 승인 처리
export const approveApproval = async (approvalId) => {  };

// 반려 처리
export const rejectApproval = async (approvalId, rejectReason) => {  };