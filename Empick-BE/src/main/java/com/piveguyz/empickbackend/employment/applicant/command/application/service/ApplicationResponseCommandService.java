package com.piveguyz.empickbackend.employment.applicant.command.application.service;

import com.piveguyz.empickbackend.employment.applicant.command.application.dto.ApplicationResponseCommandDTO;

public interface ApplicationResponseCommandService {
    ApplicationResponseCommandDTO saveResponse(ApplicationResponseCommandDTO dto);
}
