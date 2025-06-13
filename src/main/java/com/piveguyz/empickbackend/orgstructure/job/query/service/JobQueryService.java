package com.piveguyz.empickbackend.orgstructure.job.query.service;

import com.piveguyz.empickbackend.orgstructure.job.query.dto.JobResponseDTO;

import java.util.List;

public interface JobQueryService {
    List<JobResponseDTO> getAllJobs();
}
