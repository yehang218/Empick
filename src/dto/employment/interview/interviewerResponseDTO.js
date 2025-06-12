export default class InterviewerResponseDTO {
 
    constructor(id, interviewId, interviewerId, score, review) {
        this.id = id;
        this.interviewId = interviewId;
        this.interviewerId = interviewerId;
        this.score = score;
        this.review = review;
    }

    static fromJSON(json) {
        return new InterviewResponseDTO (
            json.id,
            json.interviewId,
            json.interviewerId,
            json.score,
            json.review
        );
    }

    toJSON() {
        return {
            id: this.id,
            interviewId: this.interviewId,
            interviewerId: this.interviewerId,
            score: this.score,
            review: this.review
        };
    }
}