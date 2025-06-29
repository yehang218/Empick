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
            startedAt: this.formatToLocalDateTime(this.startedAt),
            endedAt: this.formatToLocalDateTime(this.endedAt),
            createdMemberId: this.createdMemberId,
            jobtestQuestions: this.jobtestQuestions.map(o =>
                typeof o.toJSON === 'function' ? o.toJSON() : CreateJobtestQuestionRequestDTO.fromForm(o).toJSON()
            ),
        };
    }

    formatToLocalDateTime(date) {
        if (!date) return null;
        
        // Date 객체를 LocalDateTime 형식 (YYYY-MM-DDTHH:mm:ss)으로 변환
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        const seconds = String(date.getSeconds()).padStart(2, '0');
        
        return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
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
            startedAt: this.formatToLocalDateTime(this.startedAt),
            endedAt: this.formatToLocalDateTime(this.endedAt),
            updatedMemberId: this.updatedMemberId,
            jobtestQuestions: this.jobtestQuestions.map(o =>
                typeof o.toJSON === 'function' ? o.toJSON() : CreateJobtestQuestionRequestDTO.fromForm(o).toJSON()
            ),
        };
    }

    formatToLocalDateTime(date) {
        if (!date) return null;
        
        // Date 객체를 LocalDateTime 형식 (YYYY-MM-DDTHH:mm:ss)으로 변환
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        const seconds = String(date.getSeconds()).padStart(2, '0');
        
        return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
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
