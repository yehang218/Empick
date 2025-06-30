export default class ApplicationCommandDTO {
  constructor(id, status, introduceRatingResultId) {
    this.id = id;
    this.status = status;
    this.introduceRatingResultId = introduceRatingResultId;
  }

  static fromJSON(json) {
    return new ApplicationCommandDTO(
      json.id,
      json.status,
      json.introduceRatingResultId || json.introduce_rating_result_id
    );
  }

  static toJSON(dto) {
    return {
      id: dto.id,
      status: dto.status,
      introduceRatingResultId: dto.introduceRatingResultId,
      introduce_rating_result_id: dto.introduceRatingResultId // 백엔드 호환성
    };
  }
}
