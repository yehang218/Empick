import RecruitmentResponseDTO from '@/dto/employment/recruitment/recruitmentResponseDTO'
import ApplicationItemDTO from '@/dto/employment/recruitment/applicationItemDTO'
import RecruitmentProcessDTO from '@/dto/employment/recruitment/recruitmentProcessDTO'

export default class RecruitmentDetailResponseDTO {
    constructor(recruitment, request, template, applicationItems, processes) {
        this.recruitment = recruitment
        this.request = request
        this.template = template
        this.applicationItems = applicationItems
        this.processes = processes
    }

    static fromJSON(json) {

        if (!json || !json.id) {
            return new RecruitmentDetailResponseDTO(null, null, null, [], [])
        }

        const recruitment = {
            id: json.id,
            title: json.title,
            content: json.content,
            recruitType: json.recruitType,
            status: json.status,
            imageUrl: json.imageUrl,
            startedAt: json.startedAt,
            endedAt: json.endedAt,
            createdAt: json.createdAt,
            updatedAt: json.updatedAt,
            deletedAt: json.deletedAt,
            memberId: json.memberId,
            memberName: json.memberName,
            departmentName: json.departmentName,
            recruitmentTemplateId: json.recruitmentTemplateId,
            introduceTemplateId: json.introduceTemplateId,
            recruitmentRequestId: json.recruitmentRequestId
        }

        return new RecruitmentDetailResponseDTO(
            recruitment,
            null, // request
            null, // template
            [],   // applicationItems
            []    // processes
        )
    }
}
