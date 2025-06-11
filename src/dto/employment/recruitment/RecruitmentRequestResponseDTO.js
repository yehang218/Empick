export default class RecruitmentRequestResponseDTO {
    constructor(id, positionName, headcount, requestedAt, departmentName, memberName, status) {
        this.id = id;
        this.positionName = positionName;
        this.headcount = headcount;
        this.requestedAt = requestedAt;
        this.departmentName = departmentName;
        this.memberName = memberName;
        this.status = status;
    }

    static fromJSON(json) {
        return new RecruitmentRequestResponseDTO(
            json.id,
            json.positionName,
            json.headcount,
            json.requestedAt,
            json.departmentName,
            json.memberName,
            json.status
        );
    }
}
