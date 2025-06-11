export default class RecruitmentRequestResponseDTO {
    constructor(id, jobName, headcount, createdAt, departmentName, memberName, status) {
        this.id = id;
        this.jobName = jobName;
        this.headcount = headcount;
        this.createdAt = createdAt;
        this.departmentName = departmentName;
        this.memberName = memberName;
        this.status = status;
    }

    static fromJSON(json) {
        return new RecruitmentRequestResponseDTO(
            json.id,
            json.jobName,
            json.headcount,
            json.createdAt,
            json.departmentName,
            json.memberName,
            json.status
        );
    }
}
