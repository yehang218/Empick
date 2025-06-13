package com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto;

import com.piveguyz.empickbackend.employment.jobtests.common.enums.JobtestDifficulty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class JobtestQueryDTO {
    private int id;
    private String title;
    private JobtestDifficulty difficulty;
    private int testTime;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private String createdName;
    private String updatedName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<JobtestQuestionQueryDTO> questions;
    private List<JobtestEvaluationCriteriaQueryDTO> evaluationCriteria;
    private List<ApplicationInfoDTO> applications;  // 지원자 요약 목록
}
