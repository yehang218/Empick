export default class AttendanceRecordCreateRequestDTO {
    constructor(attendanceCategoryId = null, recordTime = null) {
        this.attendanceCategoryId = attendanceCategoryId;    // 근태 카테고리 ID (0=출근, 1=퇴근, 2=지각, 3=조퇴)
        this.recordTime = recordTime;                        // 기록 시각 (null이면 현재 시각 사용)
    }

    // memberId는 JWT에서 추출하므로 DTO에 포함하지 않음
}