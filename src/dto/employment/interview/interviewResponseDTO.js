export default class InterviewResponseDTO {

    constructor(id, applicationId, sheetId, datetime, address, score, interviewReview) {
        this.id = id;
        this.applicationId = applicationId;
        this.sheetId = sheetId;
        this.datetime = datetime;
        this.address = address;
        this.score = score;
        this.interviewReview = interviewReview;
    }

    static fromJSON(json) {
        return new InterviewResponseDTO (
            json.id,
            json.applicationId,
            json.sheetId,
            json.datetime,
            json.address,
            json.score,
            json.interviewReview
        );
    }

    toJSON() {
        return {
            id: this.id,
            applicationId: this.applicationId,
            sheetId: this.sheetId,
            datetime: this.datetime,
            address: this.address,
            score: this.score,
            interviewReview: this.interviewReview
        };
    }
}