package com.piveguyz.empickbackend.employment.interviews.interviewer.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewerCommandDTO {
    private Integer id;
    private Integer interviewId;
    private Integer interviewerId;
    private Double score;
    private String review;
}
