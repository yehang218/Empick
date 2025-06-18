package com.piveguyz.empickbackend.orgstructure.attendance.query.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AttendanceRecordResponseQueryDTO {
    private Integer id;
    private Integer memberId;
    private String memberName;           // 조인된 정보
    private Integer attendanceCategoryId;
    private String attendanceCategoryLabel; // 예: 출근, 퇴근 등
    private LocalDateTime recordTime;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
