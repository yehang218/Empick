export default class InterviewCriteriaResponseDTO {

    constructor(id, sheetId, title, content, weight, isDeleted, memberId, updatedAt) {
        this.id = id;
        this.sheetId = sheetId;
        this.title = title;
        this.content = content;
        this.weight = weight;
        this.isDeleted = isDeleted;
        this.memberId = memberId;
        this.updatedAt = updatedAt;
    }

    static fromJSON(json) {
        return new InterviewCriteriaResponseDTO (
            json.id,
            json.sheetId,
            json.title,
            json.content,
            json.weight,
            json.isDeleted,
            json.memberId,
            json.updatedAt
        );
    }

    toJSON() {
        return {
            id: this.id,
            sheetId: this.content,
            title: this.title,
            content: this.content,
            weight: this.weight,
            isDeleted: this.isDeleted,
            memberId: this.memberId,
            updatedAt: this.updatedAt
        };
    }
}