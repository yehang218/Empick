export default class RecruitmentRequestResponseDTO {
    constructor(
        id,
        jobName,
        headcount,
        createdAt,
        departmentName,
        memberName,
        employmentType,
        workLocation,
        qualification,
        preference,
        startedAt,
        endedAt
    ) {
        this.id = id;
        this.jobName = jobName;
        this.headcount = headcount;
        this.createdAt = createdAt;
        this.departmentName = departmentName;
        this.memberName = memberName;
        this.employmentType = employmentType;
        this.workLocation = workLocation;
        this.qualification = qualification;
        this.preference = preference;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }

    static fromJSON(json) {
        return new RecruitmentRequestResponseDTO(
            json.id,
            json.jobName,
            json.headcount,
            json.createdAt,
            json.departmentName,
            json.memberName,
            json.employmentType,
            json.workLocation,
            json.qualification,
            json.preference,
            json.startedAt,
            json.endedAt
        );
    }
}
