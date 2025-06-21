import CreateQuestionOptionRequestDTO from './createQuestionOptionRequestDTO';
import CreateGradingCriteriaRequestDTO from './createGradingCriteriaRequestDTO';

export class CreateQuestionRequestDTO {
    constructor(content, detailContent, type, difficulty, answer, createdMemberId, questionOptions, gradingCriteria) {
        this.content = content;
        this.detailContent = detailContent;
        this.type = type;
        this.difficulty = difficulty;
        this.answer = answer;
        this.createdMemberId = createdMemberId;
        this.questionOptions = questionOptions;
        this.gradingCriteria = gradingCriteria;
    }

    toJSON() {
        return {
            content: this.content,
            detailContent: this.detailContent,
            type: this.type,
            difficulty: this.difficulty,
            answer: this.answer,
            createdMemberId: this.createdMemberId,
            questionOptions: this.questionOptions.map(o =>
                typeof o.toJSON === 'function' ? o.toJSON() : CreateQuestionOptionRequestDTO.fromForm(o).toJSON()
            ),
            gradingCriteria: this.gradingCriteria.map(c =>
                typeof c.toJSON === 'function' ? c.toJSON() : CreateGradingCriteriaRequestDTO.fromForm(c).toJSON()
            )
        };
    }

    static fromForm(form) {
        return new CreateQuestionRequestDTO(
            form.content,
            form.detailContent,
            form.type,
            form.difficulty,
            form.answer,
            form.createdMemberId,
            form.questionOptions,
            form.gradingCriteria
        );
    }
}

export class UpdateQuestionRequestDTO {
    constructor(content, detailContent, type, difficulty, answer, updatedMemberId, questionOptions, gradingCriteria) {
        this.content = content;
        this.detailContent = detailContent;
        this.type = type;
        this.difficulty = difficulty;
        this.answer = answer;
        this.updatedMemberId = updatedMemberId;
        this.questionOptions = questionOptions;
        this.gradingCriteria = gradingCriteria;
    }

    toJSON() {
        return {
            content: this.content,
            detailContent: this.detailContent,
            type: this.type,
            difficulty: this.difficulty,
            answer: this.answer,
            updatedMemberId: this.updatedMemberId,
            questionOptions: this.questionOptions?.map(o =>
                typeof o.toJSON === 'function' ? o.toJSON() : CreateQuestionOptionRequestDTO.fromForm(o).toJSON()
            ),
            gradingCriteria: this.gradingCriteria?.map(c =>
                typeof c.toJSON === 'function' ? c.toJSON() : CreateGradingCriteriaRequestDTO.fromForm(c).toJSON()
            )
        };
    }

    static fromForm(form) {
        return new UpdateQuestionRequestDTO(
            form.content,
            form.detailContent,
            form.type,
            form.difficulty,
            form.answer,
            form.updatedMemberId,
            form.questionOptions,
            form.gradingCriteria
        );
    }
}
