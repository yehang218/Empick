export default class recruitmentCreateDTO {
    constructor(
        title,
        content,
        recruitType,
        imageUrl,
        startedAt,
        endedAt,
        recruitmentTemplateId,
        introduceTemplateId,
        recruitmentRequestId,
        applicationItems,
        recruitmentProcesses
    ) {
        this.title = title
        this.content = content
        this.recruitType = recruitType
        this.imageUrl = imageUrl
        this.startedAt = startedAt
        this.endedAt = endedAt
        this.recruitmentTemplateId = recruitmentTemplateId
        this.introduceTemplateId = introduceTemplateId
        this.recruitmentRequestId = recruitmentRequestId
        this.applicationItems = applicationItems || []
        this.recruitmentProcesses = recruitmentProcesses || []
    }

    static fromForm(form) {
        return new recruitmentCreateDTO(
            form.title,
            form.content,
            form.recruitType,
            form.imageUrl,
            form.startedAt,
            form.endedAt,
            form.recruitmentTemplateId ?? null,
            form.introduceTemplateId ?? null,
            form.recruitmentRequestId ?? null,
            form.applicationItems ?? [],
            form.recruitmentProcesses ?? []
        )
    }
}
