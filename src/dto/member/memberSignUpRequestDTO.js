export default class MemberSignUpRequestDTO {
    constructor({
        password = '',
        name = '',
        birth = '',
        phone = '',
        // pictureUrl 제거 - 백엔드에서 UUID로 자동 생성
        email = '',
        address = '',
        vacationCount = 0,
        hireAt = '',
        resignAt = '',
        deletedMemberId = 0,
        updatedMemberId = 0,
        lastLoginAt = '',
        status = 1, // 1: 활성, 0: 비활성 (기본값 수정)
        departmentId = null,
        positionId = null,
        jobId = null,
        rankId = null,
    } = {}) {
        this.password = password;
        this.name = name;
        this.birth = birth;
        this.phone = phone;
        // this.pictureUrl 제거 - 백엔드에서 UUID로 자동 생성됨
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

    /**
     * FormData로 변환 (프로필 이미지 제외)
     * @returns {Object} 폼 데이터 객체
     */
    toFormData() {
        const data = {};

        // null이 아닌 값들만 추가
        Object.keys(this).forEach(key => {
            if (this[key] !== null && this[key] !== undefined && this[key] !== '') {
                data[key] = this[key];
            }
        });

        return data;
    }
}