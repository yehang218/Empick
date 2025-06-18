export default class ApplicationCommandDTO {
  constructor(id, status) {
    this.id = id;
    this.status = status;
  }

  static fromJSON(json) {
    return new ApplicationCommandDTO(
      json.id,
      json.status
    );
  }

  static toJSON(dto) {
    return {
      id: dto.id,
      status: dto.status
    };
  }
}
