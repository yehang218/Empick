package com.piveguyz.empickbackend.employment.applicant.query.service;

import com.piveguyz.empickbackend.employment.applicant.query.dto.ApplicationResponseQueryDTO;

import java.util.List;

public interface ApplicationResponseQueryService {
    List<ApplicationResponseQueryDTO> findAllApplicationResponse();
}
