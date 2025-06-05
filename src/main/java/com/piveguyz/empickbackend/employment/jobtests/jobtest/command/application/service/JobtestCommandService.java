package com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.service;

import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto.CreateJobtestCommandDTO;
import jakarta.validation.Valid;

public interface JobtestCommandService {
    CreateJobtestCommandDTO createJobtest(@Valid CreateJobtestCommandDTO createJobtestCommandDTO);
}
