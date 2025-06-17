package com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.service;

import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto.CreateJobtestQuestionCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto.CreateJobtestQuestionResponseDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto.UpdateJobtestQuestionCommandDTO;
import jakarta.validation.Valid;

public interface JobtestQuestionCommandService {
    CreateJobtestQuestionResponseDTO createJobtestQuestion(@Valid CreateJobtestQuestionCommandDTO createJobtestQuestionCommandDTO, int jobtestId);

    UpdateJobtestQuestionCommandDTO updateJobtestQuestion(int id, UpdateJobtestQuestionCommandDTO updateJobtestQuestionCommandDTO);

    int deleteJobtestQuestion(int id);
}
