class ApprovalCategoryItemDTO {
    constructor(id, categoryId, name) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
    }

    static fromJSON(json) {
        return new ApprovalCategoryItemDTO(json.id, json.categoryId, json.name);
    }
}

export {
    ApprovalCategoryItemDTO,
};

export default ApprovalCategoryItemDTO;