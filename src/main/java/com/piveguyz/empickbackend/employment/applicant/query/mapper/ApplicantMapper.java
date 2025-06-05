package com.piveguyz.empickbackend.employment.applicant.query.mapper;

import com.piveguyz.empickbackend.employment.applicant.query.dto.ApplicationQueryDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ApplicantMapper {
    List<ApplicationQueryDTO> findAllApplicant();

}
