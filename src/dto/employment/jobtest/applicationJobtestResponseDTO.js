export default class ApplicationJobtestResponseDTO {
    constructor(id, applicationId, jobtestId, jobtestTitle, score, status) {
        this.id = id;
        this.applicationId = applicationId;
        this.jobtestId = jobtestId;
        this.jobtestTitle = jobtestTitle;
        this.score = score;
        this.status = status;
    }

    toJSON() {
        return {
            id: this.id,
            applicationId: this.applicationId,
            jobtestId: this.jobtestId,
            jobtestTitle: this.jobtestTitle,
            score: this.score,
            status: this.status,
        };
    }

    static fromJSON(json) {
        if (!json) return null;
        return new ApplicationJobtestResponseDTO(
            json.id,
            json.applicationId,
            json.jobtestId,
            json.jobtestTitle,
            json.score,
            json.status,
        );
    }
}
