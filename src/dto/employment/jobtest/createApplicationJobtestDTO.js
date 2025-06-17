export default class CreateApplicationJobtestCommandDTO {
    constructor(applicationId, jobtestId) {
        this.applicationId = applicationId;
        this.jobtestId = jobtestId;
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
        );
    }
}
