class ApprovalCategoryDTO {
    constructor(id, name, approvalCategoryId = null) {
        this.id = id;
        this.name = name;
        this.approvalCategoryId = approvalCategoryId;
    }

    static fromJSON(json) {
        return new ApprovalCategoryDTO(json.id, json.name, json.approval_category_id);
    }
}

export {
    ApprovalCategoryDTO
};

export default ApprovalCategoryDTO;
