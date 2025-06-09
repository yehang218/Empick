package com.piveguyz.empickbackend.employment.jobtests.evaluation.query.service;

import com.piveguyz.empickbackend.employment.jobtests.evaluation.query.dto.JobTestEvaluationDetailDTO;

public interface EvaluationResultQueryService {
    JobTestEvaluationDetailDTO getEvaluationResultByApplicationId(int applicationId);
}
