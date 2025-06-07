package com.piveguyz.empickbackend.employment.applicationItem.query.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.applicationItem.query.dto.ApplicationItemCategoryQueryDTO;
import com.piveguyz.empickbackend.employment.applicationItem.query.mapper.ApplicationItemCategoryQueryMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationItemCategoryQueryServiceImpl implements ApplicationItemCategoryQueryService {
	private final ApplicationItemCategoryQueryMapper applicationItemCategoryQueryMapper;

	@Override
	public List<ApplicationItemCategoryQueryDTO> getAllCategories() {
		List<ApplicationItemCategoryQueryDTO> list = applicationItemCategoryQueryMapper.findAllCategories();
		if (list == null || list.isEmpty()) {
			throw new BusinessException(ResponseCode.EMPLOYMENT_APPLICATION_ITEM_CATEGORY_EMPTY);
		}
		return list;
	}
}
