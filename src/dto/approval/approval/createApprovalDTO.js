class ApproverDTO {
    constructor({ order = 1, memberId = null } = {}) {
        this.order = order;
        this.memberId = memberId;
    }
    static fromJSON(json) {
        return new ApproverDTO(json);
    }
}

class ApprovalContentDTO {
    constructor({ itemId = null, content = '' } = {}) {
        this.itemId = itemId;
        this.content = content;
    }
    static fromJSON(json) {
        return new ApprovalContentDTO(json);
    }
}

class CreateApprovalDTO {
    constructor({
        categoryId = null,
        writerId = null,
        approvers = [],
        contents = []
    } = {}) {
        this.categoryId = categoryId;
        this.writerId = writerId;
        this.approvers = (approvers || []).map(ApproverDTO.fromJSON);
        this.contents = (contents || []).map(ApprovalContentDTO.fromJSON);
    }

    static fromJSON(json) {
        return new CreateApprovalDTO(json);
    }

    toJSON() {
        return {
            categoryId: this.categoryId,
            writerId: this.writerId,
            approvers: this.approvers.map(a => ({ order: a.order, memberId: a.memberId })),
            contents: this.contents.map(c => ({ itemId: c.itemId, content: c.content }))
        };
    }
}

export {
    CreateApprovalDTO,
    ApprovalContentDTO,
    ApproverDTO,
}

export default CreateApprovalDTO;