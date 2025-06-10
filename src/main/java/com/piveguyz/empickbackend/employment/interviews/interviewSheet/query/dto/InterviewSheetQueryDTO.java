package com.piveguyz.empickbackend.employment.interviews.interviewSheet.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewSheetQueryDTO {
    private Integer id;
    private String name;
    private String isDeleted;
    private Integer memberId;
    private LocalDateTime updatedAt;
}
