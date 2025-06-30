export default class CreateQuestionOptionRequestDTO {
    constructor(content) {
        this.content = content;
    }

    toJSON() {
        return {
            content: this.content
        };
    }

    static fromForm(option) {
        return new CreateQuestionOptionRequestDTO(option.content);
    }
}
