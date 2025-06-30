// 문제 선지 DTO
export class QuestionOptionDTO {
    constructor(optionNumber, content) {
        this.optionNumber = optionNumber;
        this.content = content;
    }
    static fromJSON(json) {
        return new QuestionOptionDTO(json.optionNumber, json.content);
    }
    toJSON() {
        return { optionNumber: this.optionNumber, content: this.content };
    }
}

// 사용중인 실무테스트 DTO
export class UsedJobtestDTO {
    constructor(id, title) {
        this.id = id;
        this.title = title;
    }
    static fromJSON(json) {
        return new UsedJobtestDTO(json.id, json.title);
    }
    toJSON() {
        return { id: this.id, title: this.title };
    }
}

// 채점기준 DTO
// export class GradingCriteriaQueryDTO {
//     constructor(content, scoreWeight) {
//         this.content = content;
//         this.scoreWeight = scoreWeight;
//     }
//     static fromJSON(json) {
//         return new GradingCriteriaQueryDTO(json.content, json.scoreWeight);
//     }
//     toJSON() {
//         return { content: this.content, scoreWeight: this.scoreWeight };
//     }
// }


export default class QuestionQueryDTO {
    constructor({
        id,
        content,
        detailContent,
        type,
        difficulty,
        answer,
        createdMemberId,
        createdMemberName,
        updatedMemberId,
        updatedMemberName,
        createdAt,
        updatedAt,
        options,
        usedJobTests,
        // gradingCriteria
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
        this.options = options || [];
        this.usedJobTests = usedJobTests || [];
        // this.gradingCriteria = gradingCriteria || [];
    }

    static fromJSON(json) {
        return new QuestionQueryDTO({
            id: json.id,
            content: json.content,
            detailContent: json.detailContent,
            type: json.type,
            difficulty: json.difficulty,
            answer: json.answer,
            createdMemberId: json.createdMemberId,
            createdMemberName: json.createdMemberName,
            updatedMemberId: json.updatedMemberId,
            updatedMemberName: json.updatedMemberName,
            createdAt: json.createdAt,
            updatedAt: json.updatedAt,
            options: json.options ? json.options.map(QuestionOptionDTO.fromJSON) : [],
            usedJobTests: json.usedJobTests ? json.usedJobTests.map(UsedJobtestDTO.fromJSON) : [],
            // gradingCriteria: json.gradingCriteria ? json.gradingCriteria.map(GradingCriteriaQueryDTO.fromJSON) : []
        });
    }

    toJSON() {
        return {
            id: this.id,
            content: this.content,
            detailContent: this.detailContent,
            type: this.type,
            difficulty: this.difficulty,
            answer: this.answer,
            createdMemberId: this.createdMemberId,
            createdMemberName: this.createdMemberName,
            updatedMemberId: this.updatedMemberId,
            updatedMemberName: this.updatedMemberName,
            createdAt: this.createdAt,
            updatedAt: this.updatedAt,
            options: this.options.map(o => o.toJSON()),
            usedJobTests: this.usedJobTests.map(u => u.toJSON())
            // gradingCriteria: this.gradingCriteria.map(g => g.toJSON())
        };
    }
}
