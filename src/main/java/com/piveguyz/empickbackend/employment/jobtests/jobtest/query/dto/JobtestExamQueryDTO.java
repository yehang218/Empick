package com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class JobtestExamQueryDTO {
    private Integer jobtestId;
    private int applicationJobTestId;
    private String title;
    private Integer testTime;
    private List<JobtestExamQuestionSummaryDTO> questions;
}