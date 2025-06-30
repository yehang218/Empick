package com.piveguyz.empickbackend.employment.applicant.query.service;

import com.piveguyz.empickbackend.employment.applicant.query.dto.ApplicantFullInfoDTO;
import com.piveguyz.empickbackend.employment.applicant.query.dto.ApplicantQueryDTO;

import java.util.List;

public interface ApplicantQueryService {
    List<ApplicantQueryDTO> findAllApplicant();

    ApplicantQueryDTO findApplicantById(Integer id);

    List<ApplicantQueryDTO> searchApplicantsByName(String name);

    List<ApplicantFullInfoDTO> findAllApplicantFullInfo();
}
