export default class InterviewScoreResponseDTO {

    constructor(id, interviewerId, criteriaId, score, review) {
        this.id = id;
        this.interviewerId = interviewerId;
        this.criteriaId = criteriaId;
        this.score = score;
        this.review = review;
    }

    static fromJSON(json) {
        if (!json) {
            console.warn('⚠️ InterviewScoreResponseDTO.fromJSON에 null/undefined가 들어왔습니다');
            return null;
        }
        return new InterviewScoreResponseDTO(
            json.id,
            json.interviewerId,
            json.criteriaId,
            json.score,
            json.review
        );
    }

    toJSON() {
        return {
            id: this.id,
            interviewerId: this.interviewerId,
            criteriaId: this.criteriaId,
            score: this.score,
            review: this.review
        };
    }
}