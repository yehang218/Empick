package com.piveguyz.empickbackend.employment.interviews.interviewCriteria.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewCriteriaCommandDTO {
    private Integer id;
    private Integer sheetId;
    private String title;
    private String content;
    private Double weight;
    private String isDeleted;
    private Integer memberId;
    private LocalDateTime updatedAt;
}
