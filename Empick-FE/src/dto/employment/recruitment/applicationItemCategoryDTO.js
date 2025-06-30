// 지원서 항목 카테고리 DTO
export default class ApplicationItemCategoryDTO {
    constructor(id, name, inputType, displayOrder, applicationItemCategoryId) {
        this.id = id
        this.name = name
        this.inputType = inputType
        this.displayOrder = displayOrder
        this.applicationItemCategoryId = applicationItemCategoryId
    }

    static fromJSON(json) {
        return new ApplicationItemCategoryDTO(
            json.id,
            json.name,
            json.inputType,
            json.displayOrder,
            json.applicationItemCategoryId
        )
    }
}