export default class CreateJobtestQuestionRequestDTO {
    constructor(score, questionId) {
        this.score = score;
        this.questionId = questionId;
    }

    toJSON() {
        return {
            score: this.score,
            questionId: this.questionId
        };
    }

    static fromForm(json) {
        return new CreateJobtestQuestionRequestDTO(json.score, json.questionId);
    }
}