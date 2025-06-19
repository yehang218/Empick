export default class ApprovalRequestedListDTO {
    constructor(data) {
        this.approvalId = data.approvalId;
        this.categoryName = data.categoryName;
        this.createdAt = data.createdAt;
        this.status = data.status;
    }
}