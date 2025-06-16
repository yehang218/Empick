package com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.service;

import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto.CreateApplicationJobtestCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto.JobtestEntryRequestDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto.UpdateApplicationJobtestCommandDTO;
import jakarta.validation.Valid;

public interface ApplicationJobtestCommandService {
    CreateApplicationJobtestCommandDTO createApplicaionJobtest(@Valid CreateApplicationJobtestCommandDTO createApplicationJobtestCommandDTO);

    UpdateApplicationJobtestCommandDTO updateApplicationJobtest(int id, @Valid UpdateApplicationJobtestCommandDTO updateApplicationJobtestCommandDTO);

    int deleteApplicationJobtest(int id);

    void updateGradingStatusAndScore(int applicationJobTestId, double totalScore);

    void verifyEntryCode(int jobtestId, JobtestEntryRequestDTO requestDTO);
}
