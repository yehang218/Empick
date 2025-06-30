package com.piveguyz.empickbackend.employment.interviews.interviewScore.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewScoreCommandDTO {
    private Integer id;
    private Integer interviewerId;
    private Integer criteriaId;
    private Integer score;
    private String review;
}
