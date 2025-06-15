package com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto;

import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.aggregate.enums.JobtestStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateApplicationJobtestCommandDTO {
    private String evaluatorComment;
    private LocalDateTime submittedAt;
    private Double gradingTotalScore;
    private Double evaluationScore;
    private JobtestStatus gradingStatus;
    private JobtestStatus evaluationStatus;
    private String entryCode;

    private int applicationId;
    private int jobtestId;
    private Integer gradingMemberId;
    private Integer evaluationMemberId;
}
