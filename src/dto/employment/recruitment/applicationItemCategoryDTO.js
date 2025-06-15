export default class applicationItemCategoryDTO {
    constructor(id, name, inputType, displayOrder, applicationItemCategoryId) {
        this.id = id
        this.name = name
        this.inputType = inputType
        this.displayOrder = displayOrder
        this.applicationItemCategoryId = applicationItemCategoryId
    }

    static fromJSON(json) {
        return new applicationItemCategoryDTO(
            json.id,
            json.name,
            json.inputType,
            json.displayOrder,
            json.applicationItemCategoryId
        )
    }
}