// 문제 요약 DTO
export class JobtestQuestionSummaryDTO {
    constructor(score, optionNumber, questionId, difficulty, content) {
        this.score = score;
        this.optionNumber = optionNumber;
        this.questionId = questionId;
        this.difficulty = difficulty;
        this.content = content;
    }

    static fromJSON(json) {
        return new JobtestQuestionSummaryDTO(
            json.score,
            json.optionNumber,
            json.questionId,
            json.difficulty,
            json.content
        );
    }

    toJSON() {
        return {
            score: this.score,
            optionNumber: this.optionNumber,
            questionId: this.questionId,
            difficulty: this.difficulty
        };
    }
}

// 평가 기준 DTO
export class EvaluationCriteriaDTO {
    constructor(content, detailContent, scoreWeight) {
        this.content = content;
        this.detailContent = detailContent;
        this.scoreWeight = scoreWeight;
    }

    static fromJSON(json) {
        return new EvaluationCriteriaDTO(
            json.content,
            json.detailContent,
            json.scoreWeight
        );
    }

    toJSON() {
        return {
            content: this.content,
            detailContent: this.detailContent,
            scoreWeight: this.scoreWeight
        };
    }
}

// 지원자 DTO
export class JobtestApplicationDTO {
    constructor({
        applicationId,
        applicantId,
        applicantName,
        recruitmentTitle,
        gradingStatus,
        gradingScore,
        gradingMemberName,
        evaluationStatus,
        evaluationScore,
        evaluationMemberName,
        applicationJobtestId
    }) {
        this.applicationId = applicationId;
        this.applicantId = applicantId;
        this.applicantName = applicantName;
        this.recruitmentTitle = recruitmentTitle;
        this.gradingStatus = gradingStatus;
        this.gradingScore = gradingScore;
        this.gradingMemberName = gradingMemberName;
        this.evaluationStatus = evaluationStatus;
        this.evaluationScore = evaluationScore;
        this.evaluationMemberName = evaluationMemberName;
        this.applicationJobtestId = applicationJobtestId;
    }

    static fromJSON(json) {
        return new JobtestApplicationDTO(json);
    }

    toJSON() {
        return { ...this };
    }
}

// 실무 테스트 상세 DTO
export default class JobtestDetailDTO {
    constructor({
        id,
        title,
        difficulty,
        testTime,
        startedAt,
        endedAt,
        createdName,
        updatedName,
        createdAt,
        updatedAt,
        questions = [],
        evaluationCriteria = [],
        applications = []
    }) {
        this.id = id;
        this.title = title;
        this.difficulty = difficulty;
        this.testTime = testTime;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.createdName = createdName;
        this.updatedName = updatedName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.questions = questions;
        this.evaluationCriteria = evaluationCriteria;
        this.applications = applications;
    }

    static fromJSON(json) {
        return new JobtestDetailDTO({
            ...json,
            questions: (json.questions || []).filter(q => q.questionId !== 0).map(JobtestQuestionSummaryDTO.fromJSON),
            evaluationCriteria: json.evaluationCriteria?.map(EvaluationCriteriaDTO.fromJSON) || [],
            applications: (json.applications || []).filter(a => a.applicationId !== 0 && a.applicantName !== null).map(JobtestApplicationDTO.fromJSON),
        });
    }

    toJSON() {
        return {
            id: this.id,
            title: this.title,
            difficulty: this.difficulty,
            testTime: this.testTime,
            startedAt: this.startedAt,
            endedAt: this.endedAt,
            createdName: this.createdName,
            updatedName: this.updatedName,
            createdAt: this.createdAt,
            updatedAt: this.updatedAt,
            questions: this.questions.map(q => q.toJSON()),
            evaluationCriteria: this.evaluationCriteria.map(c => c.toJSON()),
            applications: this.applications.map(a => a.toJSON())
        };
    }
}
