import api from '@/apis/apiClient';
import { ApprovalAPI } from '@/apis/routes/approval';
import ApiResponseDTO from '@/dto/common/apiResponseDTO';
import { withErrorHandling, throwCustomApiError } from '@/utils/errorHandler';
import ApprovalReceivedListDTO from '@/dto/approval/approval/approvalReceivedListDTO';
import ApprovalRequestedListDTO from '@/dto/approval/approval/approvalRequestedListDTO';
import ApprovalSentListDTO from '@/dto/approval/approval/approvalSentListDTO';

import ApprovalCategoryDTO from '@/dto/approval/approvalCategory/approvalCategoryDTO';
import ApprovalCategoryItemDTO from '@/dto/approval/approvalCategoryItem/approvalCategoryItemDTO';
import { ApprovalContentDTO, CreateApprovalDTO } from '@/dto/approval/approval/createApprovalDTO';
import { InputTypeEnum } from '@/constants/common/inputType';

// 결재 카테고리 목록 조회
export const getApprovalCategories = async (options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(ApprovalAPI.CATEGORIES);
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return (apiResponse.data || []).map(ApprovalCategoryDTO.fromJSON);
    }, options);
};

// 선택 카테고리의 결재 항목 조회
export const getApprovalCategoryItems = async (categoryId, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(ApprovalAPI.CATEGORY_ITEMS(categoryId));
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return (apiResponse.data || []).map(ApprovalCategoryItemDTO.fromJSON);
    }, options);
};

// 결재 문서 목록 조회 (작성자 기준)
export const getApprovalsByWriterId = async (writerId) => { };

// 결재 문서 목록 조회 (결재자 기준)
export const getApprovalsByApproverId = async (approverId) => { };

// 단건 결재 조회
export const getApprovalById = async (approvalId) => { };

// 결재 생성
export const createApprovalService = async (dto, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.post(ApprovalAPI.DOCUMENTS, dto.toJSON());
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.message;
    }, options);
};

// 승인 처리
export const approveApproval = async (approvalId) => { };

// 반려 처리
export const rejectApproval = async (approvalId, rejectReason) => { };

// 자신이 결재자인 결재문서 목록 조회
export const getReceivedApprovals = async (memberId) => {
    return withErrorHandling(async () => {
        const response = await api.get(ApprovalAPI.RECEIVED_DOCUMENTS_LIST(memberId));
        return (response.data.data || []).map(item => new ApprovalReceivedListDTO(item));
    });
};

// 자신이 요청한 결재문서 목록 조회
export const getRequestedApprovals = async (memberId) => {
    return withErrorHandling(async () => {
        const response = await api.get(ApprovalAPI.REQUESTED_DOCUMENTS_LIST(memberId));
        return (response.data.data || []).map(item => new ApprovalRequestedListDTO(item));
    });
};

// 결재 라인 조회
export const getApprovalLine = async (categoryId, writerId, options = {}) => {
    return withErrorHandling(async () => {
        const response = await api.get(
            ApprovalAPI.APPROVAL_LINE(categoryId) + `?writerId=${writerId}`
        );
        const apiResponse = ApiResponseDTO.fromJSON(response.data);

        if (!apiResponse.success) {
            throwCustomApiError(apiResponse.code, apiResponse.message, 400);
        }

        return apiResponse.data;
    }, options);
};

// 결재 항목 DTO 생성 함수
export const createApprovalContentDTOs = (categoryItems) => {
    return categoryItems.map(item => {
        let content = '';
        if (item.inputType === InputTypeEnum.FILE) content = null;
        return new ApprovalContentDTO({ itemId: item.id, content });
    });
};

// CreateApprovalDTO 생성 함수
export const createApprovalDTO = () => {
    return new CreateApprovalDTO();
};

// JSON으로부터 CreateApprovalDTO 생성 함수
export const createApprovalDTOFromJson = (json) => {
    return CreateApprovalDTO.fromJSON(json);
};

// 폼 데이터 정제 함수
export const sanitizeApprovalForm = (form, memberId) => {
    // 데이터 정제
    const sanitizedData = {
        categoryId: form.categoryId,
        writerId: Number(memberId),
        approvers: form.approvers.map((approver, index) => ({
            order: index + 1,
            memberId: Number(approver.memberId)
        })),
        contents: form.contents.map(content => ({
            itemId: Number(content.itemId),
            content: String(content.content ?? '').trim()
        }))
    };

    // DTO 인스턴스로 반환
    return CreateApprovalDTO.fromJSON(sanitizedData);
};