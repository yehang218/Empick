package com.piveguyz.empickbackend.employment.jobtests.jobtest.query.service;

import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto.JobtestEntryRequestDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto.ApplicationJobtestQueryDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto.JobtestExamQueryDTO;

import java.util.List;

public interface ApplicationJobtestQueryService {
    List<ApplicationJobtestQueryDTO> getAllApplicationJobTests();
    int verifyEntryCode(int jobtestId, JobtestEntryRequestDTO requestDTO);

    void checkSubmittedAt(int applicationJobtestId);
}
