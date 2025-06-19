export default class ApplicationListResponseDTO {
  constructor(id, recruitmentId, createdAt, status, applicantId, introduceRatingResultId, updatedAt, updatedBy) {
    this.id = id;
    this.recruitmentId = recruitmentId;
    this.createdAt = createdAt;
    this.status = status;
    this.applicantId = applicantId;
    this.introduceRatingResultId = introduceRatingResultId;
    this.updatedAt = updatedAt;
    this.updatedBy = updatedBy;
  }

  static fromJSON(json) {
    return new ApplicationListResponseDTO(
      json.id,
      json.recruitment_id,            // snake_case → camelCase
      json.created_at,
      json.status,
      json.applicant_id,
      json.introduce_rating_result_id,
      json.updated_at,
      json.updated_by
    );
  }
}
