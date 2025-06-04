package com.piveguyz.empickbackend.employment.interview.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewQueryDTO {
    private Integer id;
    private Integer applicantId;
    private Integer sheetId;
    private LocalDateTime date;
    private String address;
    private Double score;
    private String interviewReview;
}
