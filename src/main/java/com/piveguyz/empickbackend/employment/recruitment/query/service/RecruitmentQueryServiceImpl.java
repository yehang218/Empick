package com.piveguyz.empickbackend.employment.recruitment.query.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.recruitment.query.dto.RecruitmentQueryConditionDTO;
import com.piveguyz.empickbackend.employment.recruitment.query.dto.RecruitmentQueryDTO;
import com.piveguyz.empickbackend.employment.recruitment.query.mapper.RecruitmentQueryMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecruitmentQueryServiceImpl implements RecruitmentQueryService {
	private final RecruitmentQueryMapper recruitmentQueryMapper;

	@Override
	public List<RecruitmentQueryDTO> findRecruitments(RecruitmentQueryConditionDTO condition) {
		return recruitmentQueryMapper.findRecruitments(condition);
	}

	@Override
	public RecruitmentQueryDTO findById(int recruitmentId) {
		RecruitmentQueryDTO result = recruitmentQueryMapper.findById(recruitmentId);
		if (result == null) {
			throw new BusinessException(ResponseCode.EMPLOYMENT_RECRUITMENT_NOT_FOUND);
		}
		return result;
	}
}
