package com.piveguyz.empickbackend.employment.jobtests.grading.command.application.service;

import com.piveguyz.empickbackend.employment.jobtests.grading.command.application.dto.CreateGradingCriteriaCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.grading.command.application.dto.UpdateGradingCriteriaCommandDTO;
import jakarta.validation.Valid;

public interface GradingCriteriaCommandService {
    CreateGradingCriteriaCommandDTO createGradingCriteria(@Valid CreateGradingCriteriaCommandDTO createGradingCriteriaCommandDTO);

    UpdateGradingCriteriaCommandDTO updateGradingCriteria(int id, UpdateGradingCriteriaCommandDTO updateGradingCriteriaCommandDTO);

    int deleteGradingCriteria(int id);
}
