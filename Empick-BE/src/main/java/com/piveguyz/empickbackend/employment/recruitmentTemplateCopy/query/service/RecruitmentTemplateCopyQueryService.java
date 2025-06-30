package com.piveguyz.empickbackend.employment.recruitmentTemplateCopy.query.service;

import java.util.List;

import com.piveguyz.empickbackend.employment.recruitmentTemplateCopy.query.dto.RecruitmentTemplateCopyQueryDTO;

public interface RecruitmentTemplateCopyQueryService {
	List<RecruitmentTemplateCopyQueryDTO> getByRecruitmentId(int recruitmentId);
}
