package com.piveguyz.empickbackend.employment.mail.command.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InterviewMailCommandDTO {
    private Integer id;     // 지원서 id
    private Integer recruitmentId;
    private String recruitmentTitle;
    private Integer applicantId;
    private String applicantName;
    private String email;
    private Integer interviewId;
    private LocalDateTime interviewDatetime;
    private String interviewAddress;
}
