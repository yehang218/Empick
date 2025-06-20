export class ApprovalRequestedDetailDTO {
    constructor(data) {
        this.approvalId = data.approvalId;
        this.categoryId = data.categoryId;
        this.categoryName = data.categoryName;
        this.writerName = data.writerName;
        this.writerDepartment = data.writerDepartment;
        this.writerPosition = data.writerPosition;
        this.createdAt = data.createdAt;
        this.status = data.status;
        this.items = data.items?.map(item => new ApprovalContentItemDTO(item)) || [];
        this.approvers = data.approvers?.map(approver => new ApprovalLineDetailDTO(approver)) || [];
        this.targetApprovalId = data.targetApprovalId;
        this.targetCategoryName = data.targetCategoryName;
        this.targetWriterName = data.targetWriterName;
        this.targetApprovalStatus = data.targetApprovalStatus;
    }
}

class ApprovalContentItemDTO {
    constructor(data) {
        this.itemName = data.itemName;
        this.inputType = data.inputType;
        this.content = data.content;
    }
}

class ApprovalLineDetailDTO {
    constructor(data) {
        this.stepOrder = data.stepOrder;
        this.memberId = data.memberId;
        this.memberName = data.memberName;
        this.positionName = data.positionName;
        this.departmentName = data.departmentName;
        this.approved = data.approved;
        this.approvedAt = data.approvedAt;
    }
} 