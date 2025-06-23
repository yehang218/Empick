export default class ApplicationJobtestResponseDTO {
    constructor(id, applicationId, jobtestId, jobtestTitle) {
        this.id = id;
        this.applicationId = applicationId;
        this.jobtestId = jobtestId;
        this.jobtestTitle = jobtestTitle;
    }

    toJSON() {
        return {
            id: this.id,
            applicationId: this.applicationId,
            jobtestId: this.jobtestId,
            jobtestTitle: this.jobtestTitle,
        };
    }

    static fromJSON(json) {
        if (!json) return null;
        return new ApplicationJobtestResponseDTO(
            json.id,
            json.applicationId,
            json.jobtestId,
            json.jobtestTitle,
        );
    }
}
