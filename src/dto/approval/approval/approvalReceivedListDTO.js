export default class approvalReceivedListDTO {
    constructor(data) {
        this.approvalId = data.approvalId;
        this.categoryName = data.categoryName;
        this.writerName = data.writerName;
        this.createdAt = data.createdAt;
        this.status = data.status;
        this.myApprovalStep = data.myApprovalStep;
        this.isMyTurn = data.isMyTurn;
    }
}