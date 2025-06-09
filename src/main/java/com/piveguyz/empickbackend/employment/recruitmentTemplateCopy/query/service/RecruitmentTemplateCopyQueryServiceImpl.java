package com.piveguyz.empickbackend.employment.recruitmentTemplateCopy.query.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.recruitmentTemplateCopy.query.dto.RecruitmentTemplateCopyQueryDTO;
import com.piveguyz.empickbackend.employment.recruitmentTemplateCopy.query.mapper.RecruitmentTemplateCopyQueryMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecruitmentTemplateCopyQueryServiceImpl implements RecruitmentTemplateCopyQueryService {
	private final RecruitmentTemplateCopyQueryMapper recruitmentTemplateCopyQueryMapper;

	@Override
	public List<RecruitmentTemplateCopyQueryDTO> getByRecruitmentId(int recruitmentId) {
		List<RecruitmentTemplateCopyQueryDTO> list = recruitmentTemplateCopyQueryMapper.findByRecruitmentId(recruitmentId);
		if (list == null || list.isEmpty()) {
			throw new BusinessException(ResponseCode.EMPLOYMENT_TEMPLATE_COPY_EMPTY);
		}
		return list;
	}
}
