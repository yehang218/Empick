export default class applicationResponseDTO {

    constructor(id, recruitmentId, createdAt, status, applicantId, introduceRatingResultId, updatedAt, updatedBy) {
        this.id = id;
        this.recruitmentId = recruitmentId;
        this.createdAt = createdAt;
        this.status = status;
        this.applicantId = applicantId;
        this.introduceRatingResultId = introduceRatingResultId;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

    static fromJSON(json) {
        return new InterviewerResponseDTO (
            json.id,
            json.recruitmentId,
            json.createdAt,
            json.status,
            json.applicantId,
            json.introduceRatingResultId,
            json.updatedAt,
            json.updatedBy
        );
    }

    toJSON() {
        return {
            id: this.id,
            recruitmentId: this.recruitmentId,
            createdAt: this.createdAt,
            status: this.status,
            profileUrl: this.applicantId,
            introduceRatingResultId: this.introduceRatingResultId,
            updatedAt: this.updatedAt,
            updatedBy: this.updatedBy
        };
    }
}