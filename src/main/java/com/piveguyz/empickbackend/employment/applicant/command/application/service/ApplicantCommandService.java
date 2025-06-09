package com.piveguyz.empickbackend.employment.applicant.command.application.service;

import com.piveguyz.empickbackend.employment.applicant.command.application.dto.ApplicantCommandDTO;
import jakarta.validation.Valid;

public interface ApplicantCommandService {
    ApplicantCommandDTO createApplicant(ApplicantCommandDTO dto);
}