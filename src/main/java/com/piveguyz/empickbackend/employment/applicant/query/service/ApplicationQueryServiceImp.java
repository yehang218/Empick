package com.piveguyz.empickbackend.employment.applicant.query.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
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
    public List<ApplicationQueryDTO> findApplicationById(int id) {
        return applicationMapper.findApplicationById(id);
    }

    @Override
    public ApplicationQueryDTO findApplicationByApplicantId(Integer applicantId) {
        ApplicationQueryDTO dto = applicationMapper.findApplicationByApplicantId(applicantId);
        if(dto == null){
            throw new BusinessException(ResponseCode.APPLICATION_RESPONSE_EMPTY_CONTENT);
        }
        return dto;
    }

}
