package com.piveguyz.empickbackend.employment.interviews.interview.command.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewCommandDTO {
    private Integer id;
    private Integer sheetId;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime datetime;
    private String address;
    private Double score;
    private String interviewReview;
}
