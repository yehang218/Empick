package com.piveguyz.empickbackend.employment.mail.query.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class JobtestMailDTO {
    private Integer id;     // 지원서 id
    private Integer recruitmentId;
    private String recruitmentTitle;
    private Integer applicantId;
    private String applicantName;
    private String email;
    private Integer applicationJobTestId;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private String entryCode;
    private Integer jobTestId;
    private String jobTestTitle;
    private Integer problemCount;
}
