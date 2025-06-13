export default class recruitmentResponseDTO {
    constructor(id, title, recruitType, startedAt, endedAt, departmentName, status, writer) {
        this.id = id
        this.title = title
        this.recruitType = recruitType
        this.startedAt = startedAt
        this.endedAt = endedAt
        this.departmentName = departmentName
        this.status = status
        this.writer = writer
    }

    static fromJSON(json) {
        return new recruitmentResponseDTO(
            json.id,
            json.title,
            json.recruitType,
            json.startedAt,
            json.endedAt,
            json.departmentName,
            json.status,
            json.writer
        )
    }
}
