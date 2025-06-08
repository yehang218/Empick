package com.piveguyz.empickbackend.employment.jobtests.evaluation.query.service;

import com.piveguyz.empickbackend.employment.jobtests.evaluation.query.dto.EvaluationCriteriaQueryDTO;

import java.util.List;

public interface EvaluationCriteriaQueryService {
    List<EvaluationCriteriaQueryDTO> getEvaluationCriteriaByJobtestId(int jobtestId);

    EvaluationCriteriaQueryDTO getEvaluationCriteriaById(int id);
}
