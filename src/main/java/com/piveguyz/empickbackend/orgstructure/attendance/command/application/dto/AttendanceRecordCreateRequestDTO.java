package com.piveguyz.empickbackend.orgstructure.attendance.command.application.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRecordCreateRequestDTO {

    private Integer attendanceCategoryId;    // 근태 카테고리 ID (0=출근, 1=퇴근, 2=지각, 3=조퇴)
    private LocalDateTime recordTime;        // 기록 시각 (null이면 현재 시각 사용)

    // memberId는 JWT에서 추출하므로 DTO에 포함하지 않음
}