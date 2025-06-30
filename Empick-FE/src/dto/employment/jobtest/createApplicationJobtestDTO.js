export default class CreateApplicationJobtestCommandDTO {
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
        return new CreateApplicationJobtestCommandDTO(
            json.applicationId,
            json.jobtestId,
        );
    }
}
