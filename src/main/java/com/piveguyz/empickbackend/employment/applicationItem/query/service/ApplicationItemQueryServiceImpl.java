package com.piveguyz.empickbackend.employment.applicationItem.query.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.applicationItem.query.dto.ApplicationItemQueryDTO;
import com.piveguyz.empickbackend.employment.applicationItem.query.mapper.ApplicationItemQueryMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationItemQueryServiceImpl implements ApplicationItemQueryService {
	private final ApplicationItemQueryMapper applicationItemQueryMapper;

	@Override
	public List<ApplicationItemQueryDTO> getItemsByRecruitmentId(int recruitmentId) {
		List<ApplicationItemQueryDTO> items = applicationItemQueryMapper.findByRecruitmentId(recruitmentId);

		if (items == null || items.isEmpty()) {
			throw new BusinessException(ResponseCode.EMPLOYMENT_APPLICATION_ITEM_TEMPLATE_NOT_FOUND);
		}

		return items;
	}
}
