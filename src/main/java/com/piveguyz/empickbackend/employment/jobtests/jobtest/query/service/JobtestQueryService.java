package com.piveguyz.empickbackend.employment.jobtests.jobtest.query.service;

import com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto.ApplicationWithJobtestQueryDTO;

import java.util.List;

public interface JobtestQueryService {
    List<ApplicationWithJobtestQueryDTO> getAllApplicationJobTestInfo();
}
