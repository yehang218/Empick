export default class QuestionResponseDTO {
    constructor(id, content, type, difficulty, createdMemberId, updatedMemberId, createdMemberName, updatedMemberName
        , createMemberPictureUrl, updatedMemberPictureUrl
    ) {
        this.id = id;
        this.content = content;
        this.type = type;
        this.difficulty = difficulty;
        this.createdMemberId = createdMemberId;
        this.updatedMemberId = updatedMemberId;
        this.createdMemberName = createdMemberName;
        this.updatedMemberName = updatedMemberName;
        this.createMemberPictureUrl = createMemberPictureUrl;
        this.updatedMemberPictureUrl = updatedMemberPictureUrl;
    }

    static fromJSON(json) {
        return new QuestionResponseDTO(
            json.id,
            json.content,
            json.type,
            json.difficulty,
            json.createdMemberId,
            json.updatedMemberId,
            json.createdMemberName,
            json.updatedMemberName,
            json.createMemberPictureUrl,
            json.updatedMemberPictureUrl
        );
    }

    toJSON() {
        return {
            id: this.id,
            content: this.content,
            type: this.type,
            difficulty: this.difficulty,
            createdMemberId: this.createdMemberId,
            updatedMemberId: this.updatedMemberId,
            createdMemberName: this.createdMemberName,
            updatedMemberName: this.updatedMemberName,
            createMemberPictureUrl: this.createMemberPictureUrl,
            updatedMemberPictureUrl: this.updatedMemberPictureUrl
        };
    }
}