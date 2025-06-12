export default class jobtestResponseDTO {
    constructor(id, content, type, difficulty, createdMemberId, updatedMemberId) {
        this.id = id;
        this.content = content;
        this.type = type;
        this.difficulty = difficulty;
        this.createdMemberId = createdMemberId;
        this.updatedMemberId = updatedMemberId;
    }

    static fromJSON(json) {
        return new jobtestResponseDTO(
            json.id,
            json.content,
            json.type,
            json.difficulty,
            json.createdMemberId,
            json.updatedMemberId
        );
    }

    toJSON() {
        return {
            id: this.id,
            content: this.content,
            type: this.type,
            difficulty: this.difficulty,
            createdMemberId: this.createdMemberId,
            updatedMemberId: this.updatedMemberId
        };
    }
}