package com.piveguyz.empickbackend.employment.applicant.query.mapper;

import com.piveguyz.empickbackend.employment.applicant.query.dto.ApplicantBookmarkQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface ApplicantBookmarkMapper {
    List<ApplicantBookmarkQueryDTO> findAllApplicantBookmark();
    List<ApplicantBookmarkQueryDTO> findApplicantBookmarkByMemberId(@Param("memberId") int memberId);
}
