export default class AnswerRequestDTO {
    constructor({ content, applicationJobTestId, questionId }) {
        this.content = content;
        this.applicationJobTestId = applicationJobTestId;
        this.questionId = questionId;
    }

    toJSON() {
        return {
            content: this.content,
            applicationJobTestId: this.applicationJobTestId,
            questionId: this.questionId
        };
    }

    static fromForm(json) {
        return new AnswerRequestDTO(
            json.content,
            json.applicationJobTestId,
            json.questionId
        );
    }
}