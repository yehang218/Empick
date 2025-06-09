package com.piveguyz.empickbackend.employment.recruitmentProcess.query.service;

import java.util.List;

import com.piveguyz.empickbackend.employment.recruitmentProcess.query.dto.RecruitmentProcessQueryDTO;

public interface RecruitmentProcessQueryService {
	List<RecruitmentProcessQueryDTO> getProcessesByRecruitmentId(int recruitmentId);
}