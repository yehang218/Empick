export default class RecruitmentRequestDTO {
    constructor(departmentId, headcount, startedAt, endedAt, qualification, preference, responsibility, employmentType, workLocation, memberId) {
        this.departmentId = departmentId;
        this.headcount = headcount;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.qualification = qualification;
        this.preference = preference;
        this.responsibility = responsibility;
        this.employmentType = employmentType;
        this.workLocation = workLocation;
        this.memberId = memberId;
    }
}
