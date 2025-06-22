export default class ApplicationItemResponseDTO {
  constructor(id, applicationId, applicationItemId, content, createdAt, updatedAt, categoryName, inputType, isRequired) {
    this.id = id;
    this.applicationId = applicationId;
    this.applicationItemId = applicationItemId;
    this.content = content;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.categoryName = categoryName;
    this.inputType = inputType;
    this.isRequired = isRequired;
  }

  static fromJSON(json) {
    if (!json) {
      console.warn('⚠️ ApplicationItemResponseDTO.fromJSON - json is null/undefined');
      return null;
    }
    
    return new ApplicationItemResponseDTO(
      json.id,
      json.applicationId || json.application_id,
      json.applicationItemId || json.application_item_id,
      json.content,
      json.createdAt || json.created_at,
      json.updatedAt || json.updated_at,
      json.categoryName || json.category_name,
      json.inputType || json.input_type,
      json.isRequired || json.is_required
    );
  }

  toJSON() {
    return {
      id: this.id,
      applicationId: this.applicationId,
      applicationItemId: this.applicationItemId,
      content: this.content,
      createdAt: this.createdAt,
      updatedAt: this.updatedAt,
      categoryName: this.categoryName,
      inputType: this.inputType,
      isRequired: this.isRequired
    };
  }
} 