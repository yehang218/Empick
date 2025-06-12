package com.piveguyz.empickbackend.employment.interviews.interviewCriteria.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewCriteriaQueryDTO {
    private Integer id;
    private String content;
    private String detailContent;
    private String isDeleted;
    private Integer memberId;
    private LocalDateTime updatedAt;
}
