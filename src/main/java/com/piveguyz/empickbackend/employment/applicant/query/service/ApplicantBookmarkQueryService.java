package com.piveguyz.empickbackend.employment.applicant.query.service;

import com.piveguyz.empickbackend.employment.applicant.query.dto.ApplicantBookmarkQueryDTO;

import java.util.List;

public interface ApplicantBookmarkQueryService {
    List<ApplicantBookmarkQueryDTO> findAllApplicantBookmark();

    List<ApplicantBookmarkQueryDTO> findApplicantBookmarkByMemberId(int memberId);
}
