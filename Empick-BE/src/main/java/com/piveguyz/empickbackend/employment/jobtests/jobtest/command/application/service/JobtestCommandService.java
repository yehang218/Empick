package com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.service;

import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto.CreateJobtestCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto.UpdateJobtestCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.aggregate.JobtestEntity;
import jakarta.validation.Valid;

import java.util.Optional;

public interface JobtestCommandService {
    int createJobtest(@Valid CreateJobtestCommandDTO createJobtestCommandDTO);

    UpdateJobtestCommandDTO updateJobtest(int id, @Valid UpdateJobtestCommandDTO updateJobtestCommandDTO);

    Integer deleteJobtest(int id);

    Optional<JobtestEntity> findById(int jobtestId);
}
