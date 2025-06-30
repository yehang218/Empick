package com.piveguyz.empickbackend.employment.applicant.command.application.service;

import com.piveguyz.empickbackend.employment.applicant.command.application.dto.ApplicantBookmarkCommandDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public interface ApplicantBookmarkCommandService {
    ApplicantBookmarkCommandDTO addBookmark(ApplicantBookmarkCommandDTO dto);
    ApplicantBookmarkCommandDTO removeBookmark(ApplicantBookmarkCommandDTO dto);
}

