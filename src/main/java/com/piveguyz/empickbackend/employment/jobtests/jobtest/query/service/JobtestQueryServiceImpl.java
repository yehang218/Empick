package com.piveguyz.empickbackend.employment.jobtests.jobtest.query.service;

import com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto.JobtestApplicationsDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto.JobtestQuestionListQueryDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.query.mapper.JobtestMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobtestQueryServiceImpl implements JobtestQueryService {
    private final JobtestMapper jobtestMapper;

    JobtestQueryServiceImpl(JobtestMapper jobtestMapper) {
        this.jobtestMapper = jobtestMapper;
    }

    @Override
    public List<JobtestQuestionListQueryDTO> getAllJobTests() {
        return jobtestMapper.findAllJobTests();
    }

    @Override
    public JobtestApplicationsDTO findJobTestWithApplications(int id) {
        return jobtestMapper.findJobTestWithApplications(id);
    }
}
