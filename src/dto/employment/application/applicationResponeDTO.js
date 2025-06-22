export default class ApplicationResponseDTO {

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
        // null 체크 추가
        if (!json || typeof json !== 'object') {
            console.warn('⚠️ ApplicationResponseDTO.fromJSON: json이 null이거나 유효하지 않습니다:', json);
            return null;
        }
        
        try {
            return new ApplicationResponseDTO (
                json.id || null,
                json.recruitmentId || null,
                json.createdAt || null,
                json.status || null,
                json.applicantId || null,
                json.introduceRatingResultId || null,
                json.updatedAt || null,
                json.updatedBy || null
            );
        } catch (error) {
            console.error('❌ ApplicationResponseDTO.fromJSON 변환 실패:', error, json);
            return null;
        }
    }

    toJSON() {
        return {
            id: this.id,
            recruitmentId: this.recruitmentId,
            createdAt: this.createdAt,
            status: this.status,
            profileUrl: this.applicantId,
            introduceRatingResultId: this.introduceRatingResultId,
            updatedAt: this.updatedAt,
            updatedBy: this.updatedBy
        };
    }
}