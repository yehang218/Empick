package com.piveguyz.empickbackend.employment.jobtests.jobtest.query.service;

import com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto.ApplicationJobtestQueryDTO;

import java.util.List;

public interface ApplicationJobtestQueryService {
    List<ApplicationJobtestQueryDTO> getAllApplicationJobTests();
}
