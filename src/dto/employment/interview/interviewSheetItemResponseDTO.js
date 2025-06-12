export default class InterviewSheetItemResponseDTO {

    constructor(id, sheetId, criteriaId, weight, memberId, updatedAt) {
        this.id = id;
        this.sheetId = sheetId;
        this.criteriaId = criteriaId;
        this.weight = weight;
        this.memberId = memberId;
        this.updatedAt = updatedAt;
    }

    static fromJSON(json) {
        return new InterviewResponseDTO (
            json.id,
            json.sheetId,
            json.criteriaId,
            json.weight,
            json.memberId,
            json.updatedAt
        );
    }

    toJSON() {
        return {
            id: this.id,
            sheetId: this.sheetId,
            criteriaId: this.criteriaId,
            weight: this.weight,
            memberId: this.memberId,
            updatedAt: this.updatedAt
        };
    }
}