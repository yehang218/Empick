package com.piveguyz.empickbackend.interviewScore.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewScoreQueryDTO {
    private Integer id;
    private Integer interviewId;
    private Integer interviewerId;
    private Integer itemId;
    private Double score;
    private String review;
}
