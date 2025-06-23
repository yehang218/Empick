export default class ApplicationJobtestRequestDTO {
    constructor(applicationId, jobtestId) {
        this.applicationId = applicationId;
        this.jobtestId = jobtestId;
    }

    toJSON() {
        return {
            applicationId: this.applicationId,
            jobtestId: this.jobtestId,
        };
    }

    static fromForm(json) {
        return new ApplicationJobtestRequestDTO(
            json.applicationId,
            json.jobtestId,
        );
    }
} 