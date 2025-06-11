package com.piveguyz.empickbackend.employment.jobtests.evaluation.query.service;

import com.piveguyz.empickbackend.employment.jobtests.evaluation.query.dto.JobTestEvaluationDetailDTO;

import java.util.List;

public interface EvaluationResultQueryService {
    List<JobTestEvaluationDetailDTO> getEvaluationResultByApplicationId(int applicationId);
}
