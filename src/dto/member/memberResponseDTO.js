/**
 * 회원 정보 응답 DTO
 */
export class MemberResponseDTO {
    constructor({
        id = null,
        employeeNumber = null,
        name = '',
        email = '',
        phone = '',
        departmentName = '',
        positionName = '',
        jobName = '',
        rankName = '',
        pictureUrl = '',
        status = 0,
        birth = null,
        address = '',
        hireAt = null,
        resignAt = null
    } = {}) {
        this.id = id;
        this.employeeNumber = employeeNumber;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.departmentName = departmentName;
        this.positionName = positionName;
        this.jobName = jobName;
        this.rankName = rankName;
        this.pictureUrl = pictureUrl;
        this.status = status;
        this.birth = birth;
        this.address = address;
        this.hireAt = hireAt;
        this.resignAt = resignAt;
    }

    static fromJSON(data) {
        return new MemberResponseDTO(data);
    }
}
