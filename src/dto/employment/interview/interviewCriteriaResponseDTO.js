export default class InterviewCriteriaResponseDTO {

    constructor(id, content, detailContent, isDeleted, memberId, updatedAt) {
        this.id = id;
        this.content = content;
        this.detailContent = detailContent;
        this.isDeleted = isDeleted;
        this.memberId = memberId;
        this.updatedAt = updatedAt;
    }

    static fromJSON(json) {
        return new InterviewCriteriaResponseDTO (
            json.id,
            json.content,
            json.detailContent,
            json.isDeleted,
            json.memberId,
            json.updatedAt
        );
    }

    toJSON() {
        return {
            id: this.id,
            content: this.content,
            detailContent: this.detailContent,
            isDeleted: this.isDeleted,
            memberId: this.addmemberIdress,
            updatedAt: this.updatedAt
        };
    }
}