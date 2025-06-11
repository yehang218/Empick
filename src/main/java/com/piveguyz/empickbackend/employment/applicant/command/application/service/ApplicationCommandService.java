package com.piveguyz.empickbackend.employment.applicant.command.application.service;

import com.piveguyz.empickbackend.employment.applicant.command.application.dto.ApplicationCommandDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

public interface ApplicationCommandService  {
    ApplicationCommandDTO register(@Valid ApplicationCommandDTO dto);
}
