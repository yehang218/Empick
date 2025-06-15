export default class CreateApplicationJobtestCommandDTO {
    constructor(applicationId, jobtestId, entryCode) {
        this.applicationId = applicationId;
        this.jobtestId = jobtestId;
        this.entryCode = entryCode;
    }

    toJSON() {
        return {
            evaluatorComment: this.evaluatorComment,
            submittedAt: this.submittedAt,
            gradingTotalScore: this.gradingTotalScore,
            evaluationScore: this.evaluationScore,
            gradingStatus: this.gradingStatus,
            evaluationStatus: this.evaluationStatus,
            entryCode: this.entryCode,
            applicationId: this.applicationId,
            jobtestId: this.jobtestId,
            memberId: this.memberId
        };
    }

    static fromForm(json) {
        return new CreateApplicationJobtestCommandDTO(
            json.applicationId,
            json.jobtestId,
            json.entryCode
        );
    }
}
