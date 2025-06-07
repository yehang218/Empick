package com.piveguyz.empickbackend.employment.applicant.query.service;

import com.piveguyz.empickbackend.employment.applicant.query.dto.ApplicantQueryDTO;
import com.piveguyz.empickbackend.employment.applicant.query.mapper.ApplicantMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicantQueryServiceImp implements ApplicantQueryService {

    private final ApplicantMapper applicantMapper;

    @Override
    public List<ApplicantQueryDTO> findAllApplicant() {
        return applicantMapper.findAllApplicant();
    }
}
