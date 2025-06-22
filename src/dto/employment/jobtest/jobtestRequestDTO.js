import CreateJobtestQuestionRequestDTO from '@/dto/employment/jobtest/createJobtestQuestionRequestDTO'

export class CreateJobtestRequestDTO {
    constructor(title, difficulty, testTime, startedAt, endedAt, createdMemberId, jobtestQuestions) {
        this.title = title;
        this.difficulty = difficulty;
        this.testTime = testTime;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.createdMemberId = createdMemberId;
        this.jobtestQuestions = jobtestQuestions;
    }

    toJSON() {
        return {
            title: this.title,
            difficulty: this.difficulty,
            testTime: this.testTime,
            startedAt: this.startedAt,
            endedAt: this.endedAt,
            createdMemberId: this.createdMemberId,
            jobtestQuestions: this.jobtestQuestions.map(o =>
                typeof o.toJSON === 'function' ? o.toJSON() : CreateJobtestQuestionRequestDTO.fromForm(o).toJSON()
            ),
        };
    }

    static fromForm(json) {
        return new CreateJobtestRequestDTO(
            json.title,
            json.difficulty,
            json.testTime,
            json.startedAt,
            json.endedAt,
            json.createdMemberId,
            json.jobtestQuestions
        );
    }
}

export class UpdateJobtestRequestDTO {
    constructor(title, difficulty, testTime, startedAt, endedAt, updatedMemberId, jobtestQuestions) {
        this.title = title;
        this.difficulty = difficulty;
        this.testTime = testTime;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.updatedMemberId = updatedMemberId;
        this.jobtestQuestions = jobtestQuestions;
    }

    toJSON() {
        return {
            title: this.title,
            difficulty: this.difficulty,
            testTime: this.testTime,
            startedAt: this.startedAt,
            endedAt: this.endedAt,
            updatedMemberId: this.updatedMemberId,
            jobtestQuestions: this.jobtestQuestions.map(o =>
                typeof o.toJSON === 'function' ? o.toJSON() : CreateJobtestQuestionRequestDTO.fromForm(o).toJSON()
            ),
        };
    }

    static fromForm(json) {
        return new UpdateJobtestRequestDTO(
            json.title,
            json.difficulty,
            json.testTime,
            json.startedAt,
            json.endedAt,
            json.updatedMemberId,
            json.jobtestQuestions
        );
    }
}
