export default class AttendanceRecordCreateResponseDTO {
    constructor(
        id = null,
        memberId = null,
        attendanceCategoryId = null,
        recordTime = null,
        status = null,
        createdAt = null
    ) {
        this.id = id;                                    // 생성된 근태 기록 ID
        this.memberId = memberId;                        // 회원 ID
        this.attendanceCategoryId = attendanceCategoryId; // 근태 카테고리 ID
        this.recordTime = recordTime;                    // 기록 시각
        this.status = status;                            // 0=평시, 1=수정됨, 2=수정요청중
        this.createdAt = createdAt;                      // 생성 시각
    }

    // JSON으로 변환
    toJSON() {
        return {
            id: this.id,
            memberId: this.memberId,
            attendanceCategoryId: this.attendanceCategoryId,
            recordTime: this.recordTime,
            status: this.status,
            createdAt: this.createdAt
        };
    }

    // JSON에서 객체 생성
    static fromJSON(json) {
        return new AttendanceRecordCreateResponseDTO(
            json.id,
            json.memberId,
            json.attendanceCategoryId,
            json.recordTime,
            json.status,
            json.createdAt
        );
    }
}