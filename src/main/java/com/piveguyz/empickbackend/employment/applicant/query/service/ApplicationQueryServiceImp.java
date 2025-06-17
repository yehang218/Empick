package com.piveguyz.empickbackend.employment.applicant.query.service;

import com.piveguyz.empickbackend.employment.applicant.query.dto.ApplicationQueryDTO;
import com.piveguyz.empickbackend.employment.applicant.query.mapper.ApplicationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationQueryServiceImp implements ApplicationQueryService {

    private final ApplicationMapper applicationMapper;

    @Override
    public List<ApplicationQueryDTO> findAllApplication() {

        return applicationMapper.findAllApplication();
    }

    @Override
    public ApplicationQueryDTO findApplicationById(int id) {
        return applicationMapper.findApplicationById(id);
    }

}
