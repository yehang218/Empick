package com.piveguyz.empickbackend.employment.jobtests.evaluation.command.application.service;

import com.piveguyz.empickbackend.employment.jobtests.evaluation.command.application.dto.CreateEvaluationResultCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.evaluation.command.application.dto.UpdateEvaluationResultCommandDTO;
import jakarta.validation.Valid;

public interface EvaluationResultCommandService {
    CreateEvaluationResultCommandDTO createEvaluationResult(@Valid CreateEvaluationResultCommandDTO createEvaluationResultCommandDTO);

    UpdateEvaluationResultCommandDTO updateEvaluationResult(int id, @Valid UpdateEvaluationResultCommandDTO updateEvaluationResultCommandDTO);

    int deleteEvaluationResult(int id);
}
