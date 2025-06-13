package com.piveguyz.empickbackend.orgstructure.job.query.application.service;

import com.piveguyz.empickbackend.orgstructure.job.query.dto.JobResponseDTO;
import com.piveguyz.empickbackend.orgstructure.job.query.mapper.JobMapper;
import com.piveguyz.empickbackend.orgstructure.job.query.service.JobQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobQueryServiceImpl implements JobQueryService {

    private final JobMapper jobMapper;

    @Override
    public List<JobResponseDTO> getAllJobs() {
        return jobMapper.selectAllJobs();
    }
}