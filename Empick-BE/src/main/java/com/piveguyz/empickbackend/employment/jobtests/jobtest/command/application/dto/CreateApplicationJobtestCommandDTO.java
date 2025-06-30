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
    private int applicationId;
    private int jobtestId;

    private Integer gradingMemberId;
    private Integer evaluationMemberId;
}
