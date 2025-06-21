export default class InterviewSheetResponseDTO {

    constructor(id, name, isDeleted, memberId, updatedAt) {
        this.id = id;
        this.name = name;
        this.isDeleted = isDeleted;
        this.memberId = memberId;
        this.updatedAt = updatedAt;
    }

    static fromJSON(json) {
        if (!json) {
            console.warn('⚠️ InterviewSheetResponseDTO.fromJSON에 null/undefined가 들어왔습니다');
            return null;
        }
        return new InterviewSheetResponseDTO(
            json.id,
            json.name,
            json.isDeleted,
            json.memberId,
            json.updatedAt
        );
    }

    toJSON() {
        return {
            id: this.id,
            name: this.name,
            isDeleted: this.isDeleted,
            memberId: this.memberId,
            updatedAt: this.updatedAt
        };
    }
}