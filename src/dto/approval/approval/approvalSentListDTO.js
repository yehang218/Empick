export default class ApprovalSentListDTO {
    constructor(data) {
        this.id = data.id;
        this.categoryId = data.categoryId;
        this.writerId = data.writerId;
        this.createdAt = data.createdAt;
        this.status = data.status;
    }
} 