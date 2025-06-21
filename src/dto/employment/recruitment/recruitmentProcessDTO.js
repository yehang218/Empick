export default class RecruitmentProcessDTO {
    constructor(id, stepType, displayOrder) {
        this.id = id;
        this.stepType = stepType; // 'DOCUMENT', 'PRACTICAL', 'INTERVIEW'
        this.displayOrder = displayOrder;
    }

    static fromJSON(json) {
        return new RecruitmentProcessDTO(
            json.id,
            json.stepType,
            json.displayOrder
        );
    }
}
