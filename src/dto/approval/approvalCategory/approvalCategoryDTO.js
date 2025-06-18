class ApprovalCategoryDTO {
    constructor(id, name) {
        this.id = id;
        this.name = name;
    }

    static fromJSON(json) {
        return new ApprovalCategoryDTO(json.id, json.name);
    }
}

export {
    ApprovalCategoryDTO
};

export default ApprovalCategoryDTO;
