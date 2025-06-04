package com.piveguyz.empickbackend.interview.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


public class InterviewCommandDTO {
    private Integer id;
    private Integer applicantId;
    private Integer sheetId;
    private LocalDateTime date;
    private String address;
    private Double score;
    private String interviewReview;
}
