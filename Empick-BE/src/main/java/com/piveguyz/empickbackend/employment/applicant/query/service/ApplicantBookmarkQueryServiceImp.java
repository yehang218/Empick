package com.piveguyz.empickbackend.employment.applicant.query.service;

import com.piveguyz.empickbackend.employment.applicant.query.dto.ApplicantBookmarkQueryDTO;
import com.piveguyz.empickbackend.employment.applicant.query.mapper.ApplicantBookmarkMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicantBookmarkQueryServiceImp implements ApplicantBookmarkQueryService {

    private final ApplicantBookmarkMapper applicantBookmarkMapper;

    @Override
    public List<ApplicantBookmarkQueryDTO> findAllApplicantBookmark() {
        return applicantBookmarkMapper.findAllApplicantBookmark();
    }

    @Override
    public List<ApplicantBookmarkQueryDTO> findApplicantBookmarkByMemberId(int memberId) {
        return applicantBookmarkMapper.findApplicantBookmarkByMemberId(memberId);
    }
}
