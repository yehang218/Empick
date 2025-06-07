package com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobtestEvaluationCriteriaQueryDTO {
    private int id;
    private String content;
    private String detailContent;
    private double scoreWeight;
}
