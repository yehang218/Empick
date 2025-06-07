package com.piveguyz.empickbackend.employment.applicant.query.service;

import com.piveguyz.empickbackend.employment.applicant.query.dto.ApplicantQueryDTO;

import java.util.List;

public interface ApplicantQueryService {
    List<ApplicantQueryDTO> findAllApplicant();
}
