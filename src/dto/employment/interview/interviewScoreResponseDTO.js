export default class InterviewScoreResponseDTO {

    constructor(id, interviewerId, itemId, score, review) {
        this.id = id;
        this.interviewerId = interviewerId;
        this.itemId = itemId;
        this.score = score;
        this.review = review;
    }

    static fromJSON(json) {
        return new InterviewResponseDTO (
            json.id,
            json.interviewerId,
            json.itemId,
            json.score,
            json.review
        );
    }

    toJSON() {
        return {
            id: this.id,
            interviewerId: this.interviewerId,
            itemId: this.itemId,
            score: this.score,
            review: this.review
        };
    }
}