class ApprovalCategoryItemDTO {
    constructor(id, categoryId, name, inputType) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.inputType = inputType;
    }

    static fromJSON(json) {
        return new ApprovalCategoryItemDTO(json.id, json.categoryId, json.name, json.inputType);
    }
}

export {
    ApprovalCategoryItemDTO,
};

export default ApprovalCategoryItemDTO;