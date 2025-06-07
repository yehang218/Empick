package com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.service;

import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto.CreateJobtestCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto.UpdateJobtestCommandDTO;
import jakarta.validation.Valid;

public interface JobtestCommandService {
    CreateJobtestCommandDTO createJobtest(@Valid CreateJobtestCommandDTO createJobtestCommandDTO);

    UpdateJobtestCommandDTO updateJobtest(int id, @Valid UpdateJobtestCommandDTO updateJobtestCommandDTO);

    Integer deleteJobtest(int id);
}
