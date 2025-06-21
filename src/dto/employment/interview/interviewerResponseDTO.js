export default class InterviewerResponseDTO {

    constructor(id, interviewId, memberId, score, review) {
        this.id = id;
        this.interviewId = interviewId;
        this.memberId = memberId;
        this.score = score;
        this.review = review;
    }

    static fromJSON(json) {
        if (!json) {
            console.warn('⚠️ InterviewerResponseDTO.fromJSON에 null/undefined가 들어왔습니다');
            return null;
        }
        return new InterviewerResponseDTO(
            json.id,
            json.interviewId,
            json.memberId,
            json.score,
            json.review
        );
    }

    toJSON() {
        return {
            id: this.id,
            interviewId: this.interviewId,
            memberId: this.memberId,
            score: this.score,
            review: this.review
        };
    }
}