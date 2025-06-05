package com.piveguyz.empickbackend.employment.jobtests.jobtest.query.service;

import com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto.ApplicationWithJobtestQueryDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto.JobtestQueryDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto.JobtestQuestionListQueryDTO;

import java.util.List;

public interface JobtestQueryService {
    List<JobtestQuestionListQueryDTO> getAllJobTestsSimple();
    JobtestQueryDTO getJobTestById(int id);
}
