export default class MemberSignUpRequestDTO {
    constructor({
        password = '',
        name = '',
        birth = '',
        phone = '',
        pictureUrl = '',
        email = '',
        address = '',
        vacationCount = 0,
        hireAt = '',
        resignAt = '',
        deletedMemberId = 0,
        updatedMemberId = 0,
        lastLoginAt = '',
        status = 1,
        departmentId = null,
        positionId = null,
        jobId = null,
        rankId = null,
    } = {}) {
        this.password = password;
        this.name = name;
        this.birth = birth;
        this.phone = phone;
        this.pictureUrl = pictureUrl;
        this.email = email;
        this.address = address;
        this.vacationCount = vacationCount;
        this.hireAt = hireAt;
        this.resignAt = resignAt;
        this.deletedMemberId = deletedMemberId;
        this.updatedMemberId = updatedMemberId;
        this.lastLoginAt = lastLoginAt;
        this.status = status;
        this.departmentId = departmentId;
        this.positionId = positionId;
        this.jobId = jobId;
        this.rankId = rankId;
    }
} 