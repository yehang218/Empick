package com.piveguyz.empickbackend.employment.applicant.query.service;

import com.piveguyz.empickbackend.employment.applicant.query.dto.ApplicationResponseQueryDTO;
import com.piveguyz.empickbackend.employment.applicant.query.mapper.ApplicationResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationResponseQueryServiceImp implements ApplicationResponseQueryService {

    private final ApplicationResponseMapper applicationResponseMapper;

    @Override
    public List<ApplicationResponseQueryDTO> findAllApplicationResponse() {
        return applicationResponseMapper.findAllApplicationResponse();
    }

    @Override
    public List<ApplicationResponseQueryDTO> findApplicationResponsesByApplicationId(Integer applicationId) {
        return applicationResponseMapper.findApplicationResponsesByApplicationId(applicationId);
    }
}