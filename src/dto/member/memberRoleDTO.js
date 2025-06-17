class MemberRoleDTO {
    constructor(data) {
        this.code = data.code;
        this.name = data.name;
        this.description = data.description;
        this.roleType = data.roleType;
        this.createdAt = data.createdAt;
        this.updatedAt = data.updatedAt;
        this.deletedAt = data.deletedAt;
    }

    static fromJSON(json) {
        return new MemberRoleDTO(json);
    }

    toJSON() {
        return {
            code: this.code,
            name: this.name,
            description: this.description,
            roleType: this.roleType,
            createdAt: this.createdAt,
            updatedAt: this.updatedAt,
            deletedAt: this.deletedAt
        };
    }
}

export default MemberRoleDTO;