package com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class JobtestApplicationsDTO {
    private int id;
    private String title;
    private int difficulty;
    private int testTime;
    private String creatorName;
    private String updatedName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<JobtestQuestionQueryDTO> questions;
    private List<JobtestEvaluationCriteriaQueryDTO> evaluationCriteria;
    private List<ApplicationInfoDTO> applications;  // 지원자 요약 목록
}
