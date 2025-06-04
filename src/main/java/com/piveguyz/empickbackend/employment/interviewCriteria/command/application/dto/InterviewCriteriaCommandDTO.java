package com.piveguyz.empickbackend.employment.interviewCriteria.command.application.dto;

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
    private String content;
    private String detailContent;
    private String isDeleted;
    private Integer memberId;
    private LocalDateTime updatedAt;
}
