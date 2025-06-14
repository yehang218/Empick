// AnswerQueryDTO.js

// 1. GradingResultDTO
// class GradingResultDTO {
//     constructor(id, gradingCriteriaId, isSatisfied, answerId) {
//         this.id = id;
//         this.gradingCriteriaId = gradingCriteriaId;
//         this.isSatisfied = isSatisfied; // Boolean
//         this.answerId = answerId;
//     }

//     static fromJSON(json) {
//         return new GradingResultDTO(
//             json.id,
//             json.gradingCriteriaId,
//             json.isSatisfied,
//             json.answerId
//         );
//     }
// }

// // 2. GradingCriteriaDTO
// class GradingCriteriaDTO {
//     constructor(id, content, detailContent, scoreWeight) {
//         this.id = id;
//         this.content = content;
//         this.detailContent = detailContent;
//         this.scoreWeight = scoreWeight;
//     }

//     static fromJSON(json) {
//         return new GradingCriteriaDTO(
//             json.id,
//             json.content,
//             json.detailContent,
//             json.scoreWeight
//         );
//     }
// }

// 3. QuestionOptionDTO
class QuestionOptionDTO {
    constructor(id, optionNumber, content) {
        this.id = id;
        this.optionNumber = optionNumber;
        this.content = content;
    }

    static fromJSON(json) {
        return new QuestionOptionDTO(
            json.id,
            json.optionNumber,
            json.content
        );
    }
}

// 4. QuestionQueryDTO
class QuestionQueryDTO {
    constructor({
        id, content, detailContent, type, difficulty, answer,
        createdMemberId, createdMemberName, updatedMemberId, updatedMemberName,
        createdAt, updatedAt, options, usedJobTests
        // , gradingCriteria
    }) {
        this.id = id;
        this.content = content;
        this.detailContent = detailContent;
        this.type = type;
        this.difficulty = difficulty;
        this.answer = answer;
        this.createdMemberId = createdMemberId;
        this.createdMemberName = createdMemberName;
        this.updatedMemberId = updatedMemberId;
        this.updatedMemberName = updatedMemberName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.options = (options || []).map(QuestionOptionDTO.fromJSON);
        this.usedJobTests = usedJobTests || [];
        // this.gradingCriteria = (gradingCriteria || []).map(GradingCriteriaDTO.fromJSON);
    }

    static fromJSON(json) {
        return new QuestionQueryDTO(json);
    }
}

// 5. AnswerQueryDTO (루트)
class AnswerQueryDTO {
    constructor({
        id, content, attempt, isCorrect, score, applicationJobTestId, questionId,
        question, maxScore
        // , gradingResults
    }) {
        this.id = id;
        this.content = content;
        this.attempt = attempt;
        this.isCorrect = isCorrect;
        this.score = score;
        this.maxScore = maxScore;
        this.applicationJobTestId = applicationJobTestId;
        this.questionId = questionId;
        this.question = question ? QuestionQueryDTO.fromJSON(question) : null;
        // this.gradingResults = (gradingResults || []).map(GradingResultDTO.fromJSON);
    }

    static fromJSON(json) {
        return new AnswerQueryDTO(json);
    }
}

// ----- export -----
export {
    AnswerQueryDTO,
    QuestionQueryDTO,
    QuestionOptionDTO,
    // GradingCriteriaDTO,
    // GradingResultDTO
};
// 또는 default export도 필요하다면:
export default AnswerQueryDTO;
