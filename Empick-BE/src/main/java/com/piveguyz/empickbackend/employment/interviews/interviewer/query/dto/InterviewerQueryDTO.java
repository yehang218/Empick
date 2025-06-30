package com.piveguyz.empickbackend.employment.interviews.interviewer.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewerQueryDTO {
    private Integer id;
    private Integer interviewId;
    private Integer memberId;
    private Double score;
    private String review;
}
