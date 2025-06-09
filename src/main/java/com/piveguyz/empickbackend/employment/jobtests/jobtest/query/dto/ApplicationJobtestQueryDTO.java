package com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto;

import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.aggregate.enums.JobtestStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationJobtestQueryDTO {
    private String applicantName;
    private String recruitmentTitle;
    private String jobTestTitle;
    private JobtestStatus gradingStatus;
    private Integer gradingTotalScore;
    private JobtestStatus evaluationStatus;
    private Integer evaluationScore;
    private Integer applicationJobTestId;
}
