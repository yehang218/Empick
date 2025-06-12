package com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class JobtestQueryDTO {
    private int id;
    private String title;
    private int difficulty;
    private int testTime;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private String creatorName;
    private String updatedName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<JobtestQuestionQueryDTO> questions;
    private List<JobtestEvaluationCriteriaQueryDTO> evaluationCriteria;
}
