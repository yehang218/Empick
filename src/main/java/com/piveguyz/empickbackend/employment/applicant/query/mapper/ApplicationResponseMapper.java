package com.piveguyz.empickbackend.employment.applicant.query.mapper;

import com.piveguyz.empickbackend.employment.applicant.query.dto.ApplicationResponseQueryDTO;

import java.util.List;

public interface ApplicationResponseMapper {
    List<ApplicationResponseQueryDTO> findAllApplicationResponse();

    List<ApplicationResponseQueryDTO> findApplicationResponsesByApplicationId(Integer applicationId);
}
