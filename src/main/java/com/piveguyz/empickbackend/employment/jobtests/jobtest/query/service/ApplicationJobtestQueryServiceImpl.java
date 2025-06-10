package com.piveguyz.empickbackend.employment.jobtests.jobtest.query.service;

import com.piveguyz.empickbackend.employment.jobtests.jobtest.query.mapper.ApplicationJobtestMapper;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto.ApplicationJobtestQueryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationJobtestQueryServiceImpl implements ApplicationJobtestQueryService {
    private final ApplicationJobtestMapper applicationJobtestMapper;

    @Override
    public List<ApplicationJobtestQueryDTO> getAllApplicationJobTests() {
        return applicationJobtestMapper.selectAllApplicationJobtest();
    }
}
