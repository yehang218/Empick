// 채용공고별 지원서 항목 DTO
export default class ApplicationItemDTO {
    constructor(id, applicationItemCategoryId, categoryName, inputType, required) {
        this.id = id
        this.applicationItemCategoryId = applicationItemCategoryId
        this.categoryName = categoryName
        this.inputType = inputType
        this.required = required
    }

    static fromJSON(json) {
        return new ApplicationItemDTO(
            json.id,
            json.applicationItemCategoryId,
            json.categoryName,
            json.inputType,
            json.required
        )
    }
}