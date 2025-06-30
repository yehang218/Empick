export default class AttendanceRecordCreateRequestDTO {
    constructor(attendanceCategoryId = null, recordTime = null) {
        this.attendanceCategoryId = attendanceCategoryId;    // 근태 카테고리 ID (1=출근, 2=퇴근, 3=지각, 4=조퇴)
        this.recordTime = recordTime;                        // 기록 시간
    }

    // memberId는 JWT에서 추출하므로 DTO에 포함하지 않음
}