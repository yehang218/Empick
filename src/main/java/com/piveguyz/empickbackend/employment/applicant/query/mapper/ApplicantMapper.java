package com.piveguyz.empickbackend.employment.applicant.query.mapper;

import com.piveguyz.empickbackend.employment.applicant.query.dto.ApplicantFullInfoDTO;
import com.piveguyz.empickbackend.employment.applicant.query.dto.ApplicantQueryDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ApplicantMapper {
    List<ApplicantQueryDTO> findAllApplicant();

    ApplicantQueryDTO findApplicantById(Integer id);

    List<ApplicantQueryDTO> searchApplicantsByName(String name);

    List<ApplicantFullInfoDTO> findAllApplicantFullInfo();
}
