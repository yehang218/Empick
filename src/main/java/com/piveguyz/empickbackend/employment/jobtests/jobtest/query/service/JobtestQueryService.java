package com.piveguyz.empickbackend.employment.jobtests.jobtest.query.service;

import com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto.JobtestApplicationsDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto.JobtestQuestionListQueryDTO;

import java.util.List;

public interface JobtestQueryService {
    List<JobtestQuestionListQueryDTO> getAllJobTests();
    JobtestApplicationsDTO findJobTestWithApplications(int id);
}
