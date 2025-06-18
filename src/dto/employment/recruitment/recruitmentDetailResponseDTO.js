import RecruitmentResponseDTO from '@/dto/employment/recruitment/recruitmentResponseDTO'
import ApplicationItemDTO from '@/dto/employment/recruitment/applicationItemDTO'
import RecruitmentProcessDTO from '@/dto/employment/recruitment/recruitmentProcessDTO'

export default class recruitmentDetailResponseDTO {
    constructor(recruitment, request, template, applicationItems, processes) {
        this.recruitment = recruitment
        this.request = request
        this.template = template
        this.applicationItems = applicationItems
        this.processes = processes
    }

    static fromJSON(json) {
        console.log('✅ DTO 호출됨:', json) // 확인용

        if (!json || !json.id) {
            console.warn('❌ 잘못된 JSON 구조:', json)
            return new recruitmentDetailResponseDTO(null, null, null, [], [])
        }

        // 🔥 여기가 핵심! json.recruitment ❌ → json ✅
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

        return new recruitmentDetailResponseDTO(
            recruitment,
            null, // request
            null, // template
            [],   // applicationItems
            []    // processes
        )
    }
}
