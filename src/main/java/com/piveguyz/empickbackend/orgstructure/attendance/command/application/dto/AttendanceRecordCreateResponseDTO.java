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
public class AttendanceRecordCreateResponseDTO {

    private Integer id;                      // 생성된 근태 기록 ID
    private Integer memberId;                // 회원 ID
    private Integer attendanceCategoryId;    // 근태 카테고리 ID
    private LocalDateTime recordTime;        // 기록 시각
    private Integer status;                  // 0=평시, 1=수정됨, 2=수정요청중
    private LocalDateTime createdAt;         // 생성 시각
}