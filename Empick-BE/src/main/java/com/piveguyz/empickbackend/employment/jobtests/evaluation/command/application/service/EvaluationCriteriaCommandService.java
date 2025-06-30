package com.piveguyz.empickbackend.employment.jobtests.evaluation.command.application.service;

import com.piveguyz.empickbackend.employment.jobtests.evaluation.command.application.dto.CreateEvaluationCriteriaCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.evaluation.command.application.dto.UpdateEvaluationCriteriaCommandDTO;
import jakarta.validation.Valid;

public interface EvaluationCriteriaCommandService {
    CreateEvaluationCriteriaCommandDTO createEvaluationCriteria(@Valid CreateEvaluationCriteriaCommandDTO createEvaluationCriteriaCommandDTO);
    UpdateEvaluationCriteriaCommandDTO updateEvaluationCriteriaCommandDTO(int id, UpdateEvaluationCriteriaCommandDTO updateEvaluationCriteriaCommandDTO);
    Integer deleteEvaluationCriteria(int id);
}
