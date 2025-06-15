package com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto;

import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.aggregate.enums.JobtestStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationInfoDTO {
    private int applicationId;
    private int applicantId;
    private String applicantName;
    private String recruitmentTitle;

    private JobtestStatus gradingStatus;
    private double gradingScore;
    private String gradingMemberName;

    private JobtestStatus evaluationStatus;
    private double evaluationScore;
    private String evaluationMemberName;

    private int applicationJobtestId;
    private int entryCode;
}
