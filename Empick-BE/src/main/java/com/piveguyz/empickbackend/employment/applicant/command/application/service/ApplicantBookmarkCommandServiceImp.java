package com.piveguyz.empickbackend.employment.applicant.command.application.service;

import com.piveguyz.empickbackend.employment.applicant.command.application.dto.ApplicantBookmarkCommandDTO;
import com.piveguyz.empickbackend.employment.applicant.command.domain.aggregate.applicationBookmark.ApplicantBookmarkEntity;
import com.piveguyz.empickbackend.employment.applicant.command.domain.aggregate.applicationBookmark.ApplicantBookmarkId;
import com.piveguyz.empickbackend.employment.applicant.command.domain.repository.ApplicantBookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicantBookmarkCommandServiceImp implements ApplicantBookmarkCommandService {

    private final ApplicantBookmarkRepository applicantBookmarkRepository;

    @Override
    @Transactional
    public ApplicantBookmarkCommandDTO addBookmark(ApplicantBookmarkCommandDTO dto) {
        ApplicantBookmarkEntity entity = new ApplicantBookmarkEntity(new ApplicantBookmarkId(dto.getMemberId(), dto.getApplicantId()));
        applicantBookmarkRepository.save(entity);

        return dto;
    }

    @Override
    @Transactional
    public ApplicantBookmarkCommandDTO removeBookmark(ApplicantBookmarkCommandDTO dto) {
            applicantBookmarkRepository
                    .findById(new ApplicantBookmarkId(dto.getMemberId(), dto.getApplicantId()))
                    .ifPresent(applicantBookmarkRepository::delete);
            return dto;
        }
    }
