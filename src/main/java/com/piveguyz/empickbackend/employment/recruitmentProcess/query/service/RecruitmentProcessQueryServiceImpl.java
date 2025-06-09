package com.piveguyz.empickbackend.employment.recruitmentProcess.query.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.piveguyz.empickbackend.employment.recruitmentProcess.query.dto.RecruitmentProcessQueryDTO;
import com.piveguyz.empickbackend.employment.recruitmentProcess.query.mapper.RecruitmentProcessQueryMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecruitmentProcessQueryServiceImpl implements RecruitmentProcessQueryService {

	private final RecruitmentProcessQueryMapper recruitmentProcessQueryMapper;

	@Override
	public List<RecruitmentProcessQueryDTO> getProcessesByRecruitmentId(int recruitmentId) {
		return recruitmentProcessQueryMapper.findByRecruitmentId(recruitmentId);
	}
}