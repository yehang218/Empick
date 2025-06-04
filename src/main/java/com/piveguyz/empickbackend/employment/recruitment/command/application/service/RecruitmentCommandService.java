package com.piveguyz.empickbackend.employment.recruitment.command.application.service;

import com.piveguyz.empickbackend.employment.recruitment.command.application.dto.RecruitmentCommandDTO;

public interface RecruitmentCommandService {
	void createRecruitment(RecruitmentCommandDTO dto);

	void updateRecruitment(int id, RecruitmentCommandDTO dto);
}
