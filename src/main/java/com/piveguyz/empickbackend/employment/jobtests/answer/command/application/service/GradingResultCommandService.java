package com.piveguyz.empickbackend.employment.jobtests.answer.command.application.service;

import com.piveguyz.empickbackend.employment.jobtests.answer.command.application.dto.CreateGradingResultCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.application.dto.UpdateGradingResultCommandDTO;

public interface GradingResultCommandService {
    CreateGradingResultCommandDTO createGradingResult(CreateGradingResultCommandDTO createGradingResultCommandDTO);

    UpdateGradingResultCommandDTO updateGradingResult(int id, UpdateGradingResultCommandDTO updateGradingResultCommandDTO);

    int deleteGradingResult(int id);
}
