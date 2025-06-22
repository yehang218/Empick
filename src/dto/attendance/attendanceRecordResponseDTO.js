import { ATTENDANCE_CONSTANTS } from '@/config/attendance/constants';

export default class AttendanceRecordResponseDTO {
    constructor(
        id = null,
        memberId = null,
        memberName = null,
        attendanceCategoryId = null,
        attendanceCategoryLabel = null,
        recordTime = null,
        status = null,
        createdAt = null,
        updatedAt = null
    ) {
        this.id = id;                                    // 근태 기록 ID
        this.memberId = memberId;                        // 회원 ID
        this.memberName = memberName;                    // 조인된 정보
        this.attendanceCategoryId = attendanceCategoryId; // 근태 카테고리 ID

        // 라벨이 없으면 categoryId로 자동 생성
        this.attendanceCategoryLabel = attendanceCategoryLabel ||
            ATTENDANCE_CONSTANTS.CATEGORY_LABELS[attendanceCategoryId] || '알 수 없음';

        this.recordTime = recordTime;                    // 기록 시각
        this.status = status;                            // 0=평시, 1=수정됨, 2=수정요청중
        this.createdAt = createdAt;                      // 생성 시각
        this.updatedAt = updatedAt;                      // 수정 시각
    }

    // JSON으로 변환
    toJSON() {
        return {
            id: this.id,
            memberId: this.memberId,
            memberName: this.memberName,
            attendanceCategoryId: this.attendanceCategoryId,
            attendanceCategoryLabel: this.attendanceCategoryLabel,
            recordTime: this.recordTime,
            status: this.status,
            createdAt: this.createdAt,
            updatedAt: this.updatedAt
        };
    }

    // JSON에서 객체 생성
    static fromJSON(json) {
        return new AttendanceRecordResponseDTO(
            json.id,
            json.memberId,
            json.memberName,
            json.attendanceCategoryId,
            json.attendanceCategoryLabel,
            json.recordTime,
            json.status,
            json.createdAt,
            json.updatedAt
        );
    }
}
