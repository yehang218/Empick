package com.piveguyz.empickbackend.orgstructure.attendance.query.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AttendanceRecordQueryDTO {
    private Integer id;                   // 근태 기록 ID
    private Integer memberId;            // 회원 ID
    private Integer attendanceCategoryId; // 근태 카테고리 ID
    private LocalDateTime recordTime;    // 기록 시각
    private Integer status;              // 0=평시, 1=수정됨, 2=수정요청중
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;     // soft delete
}
