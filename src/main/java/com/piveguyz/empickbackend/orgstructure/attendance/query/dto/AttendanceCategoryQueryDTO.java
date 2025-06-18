package com.piveguyz.empickbackend.orgstructure.attendance.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AttendanceCategoryQueryDTO {
    private Integer id;            // 카테고리 ID
    private Integer status;        // 0=출근, 1=퇴근, 2=지각, 3=조퇴
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
