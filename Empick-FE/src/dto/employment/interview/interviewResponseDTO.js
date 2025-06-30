export default class InterviewResponseDTO {

    constructor(id, applicationId, sheetId, datetime, address, score) {
        this.id = id;
        this.applicationId = applicationId;
        this.sheetId = sheetId;
        this.datetime = datetime;
        this.address = address;
        this.score = score;
    }

    static fromJSON(json) {
        if (!json) {
            console.warn('⚠️InterviewResponseDTO.fromJSON');
            return null;
        }
        return new InterviewResponseDTO(
            json.id,
            json.applicationId,
            json.sheetId,
            json.datetime,
            json.address,
            json.score
        );
    }

    toJSON() {
        return {
            id: this.id,
            applicationId: this.applicationId,
            sheetId: this.sheetId,
            datetime: this.datetime,
            address: this.address,
            score: this.score
        };
    }
}