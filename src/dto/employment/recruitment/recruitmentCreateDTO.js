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
        recruitmentProcesses,
        memberId
    ) {
        this.title = title;
        this.content = content;
        
        this.recruitType = recruitType;

        // 빈 문자열이면 null로 처리
        this.imageUrl = imageUrl || null;

        this.startedAt = startedAt;
        this.endedAt = endedAt;

        this.recruitmentTemplateId = recruitmentTemplateId ?? null;
        this.introduceTemplateId = introduceTemplateId ?? null;
        this.recruitmentRequestId = recruitmentRequestId ?? null;

        this.applicationItems = applicationItems || [];
        this.recruitmentProcesses = recruitmentProcesses || [];

        this.memberId = memberId ?? null;
    }

    static fromForm(form) {
        return new recruitmentCreateDTO(
            form.title,
            form.content,
            form.recruitType,
            form.imageUrl || null,             // "" → null 처리
            form.startedAt,
            form.endedAt,
            form.recruitmentTemplateId ?? null,
            form.introduceTemplateId ?? null,
            form.recruitmentRequestId ?? null,
            form.applicationItems ?? [],
            form.recruitmentProcesses ?? [],
            form.memberId ?? null
        );
    }
}
