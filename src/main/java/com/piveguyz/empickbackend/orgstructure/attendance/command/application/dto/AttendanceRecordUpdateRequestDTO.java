package com.piveguyz.empickbackend.orgstructure.attendance.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRecordUpdateRequestDTO {

    private Integer attendanceCategoryId;    // 수정할 근태 카테고리 ID
    private LocalDateTime recordTime;        // 수정할 기록 시각

    // 수정할 근태 기록 ID는 PathVariable로 받음
}