export default class AttendanceRecordUpdateRequestDTO {
    constructor(attendanceCategoryId = null, recordTime = null) {
        this.attendanceCategoryId = attendanceCategoryId;    // 수정할 근태 카테고리 ID
        this.recordTime = recordTime;                        // 수정할 기록 시각
    }

    // 수정할 근태 기록 ID는 PathVariable로 받음

    // JSON으로 변환
    toJSON() {
        return {
            attendanceCategoryId: this.attendanceCategoryId,
            recordTime: this.recordTime
        };
    }

    // JSON에서 객체 생성
    static fromJSON(json) {
        return new AttendanceRecordUpdateRequestDTO(
            json.attendanceCategoryId,
            json.recordTime
        );
    }
}