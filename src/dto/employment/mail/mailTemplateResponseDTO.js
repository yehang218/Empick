export default class mailTemplateResponseDTO {
    constructor(id, title, content, isDeleted, memberId, updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.isDeleted = isDeleted;
        this.memberId = memberId;
        this.updatedAt = updatedAt;
    }

    static fromJSON(json) {
        return new mailTemplateResponseDTO(
            json.id,
            json.title,
            json.content,
            json.isDeleted,
            json.memberId,
            json.updatedAt
        );
    }

    toJSON() {
        return {
            id: this.id,
            title: this.title,
            content: this.content,
            isDeleted: this.isDeleted,
            memberId: this.memberId,
            updatedAt: this.updatedAt
        };
    }
}