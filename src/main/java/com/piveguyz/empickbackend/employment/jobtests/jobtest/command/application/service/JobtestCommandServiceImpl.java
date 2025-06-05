package com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.service;

import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto.CreateJobtestCommandDTO;
import org.springframework.stereotype.Service;

@Service
public class JobtestCommandServiceImpl implements JobtestCommandService {
    
    // 실무테스트 등록
    @Override
    public CreateJobtestCommandDTO createJobtest(CreateJobtestCommandDTO createJobtestCommandDTO) {
        // 이미 존재하는 경우


        // 작성자가 없는 사원인 경우

        return null;
    }
}
