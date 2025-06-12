export default class jobtestMailResponseDTO {
    constructor(
        id, 
        recruitmentId, 
        recruitmentTitle, 
        applicantId, 
        applicantName, 
        email, 
        applicationJobTestId, 
        entryCode, 
        jobTestId, 
        jobTestTitle, 
        testTime, 
        startedAt, 
        endedAt, 
        problemCount
    ) {
        this.id = id;
        this.recruitmentId = recruitmentId;
        this.recruitmentTitle = recruitmentTitle;
        this.applicantId = applicantId;
        this.applicantName = applicantName;
        this.email = email;
        this.applicationJobTestId = applicationJobTestId;
        this.entryCode = entryCode;
        this.jobTestId = jobTestId;
        this.jobTestTitle = jobTestTitle;
        this.testTime = testTime;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.problemCount = problemCount;
    }

    static fromJSON(json) {
        return new jobtestMailResponseDTO (
            json.id,
            json.recruitmentId,
            json.recruitmentTitle,
            json.applicantId,
            json.applicantName,
            json.email,
            json.applicationJobTestId,
            json.entryCode,
            json.jobTestId,
            json.jobTestTitle,
            json.testTime,
            json.startedAt,
            json.endedAt,
            json.problemCount
        );
    }

    toJSON() {
        return {
            id: this.id,
            recruitmentId: this.recruitmentId,
            recruitmentTitle: this.recruitmentTitle,
            applicantId: this.applicantId,
            applicantName: this.applicantName,
            email: this.email,
            applicationJobTestId: this.applicationJobTestId,
            entryCode: this.entryCode,
            jobTestId: this.jobTestId,
            jobTestTitle: this.jobTestTitle,
            testTime: this.testTime,
            startedAt: this.startedAt,
            endedAt: this.endedAt,
            problemCount: this.problemCount
        };
    }
}