export default class CreateGradingCriteriaRequestDTO {
    constructor(content, detailContent, scoreWeight) {
        this.content = content;
        this.detailContent = detailContent;
        this.scoreWeight = scoreWeight;
    }

    toJSON() {
        return {
            content: this.content,
            detailContent: this.detailContent,
            scoreWeight: this.scoreWeight
        };
    }

    static fromForm(criteria) {
        return new CreateGradingCriteriaRequestDTO(
            criteria.content,
            criteria.detailContent,
            criteria.scoreWeight
        );
    }
}