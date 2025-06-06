package com.piveguyz.empickbackend.employment.applicationItem.query.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.piveguyz.empickbackend.employment.applicationItem.query.dto.ApplicationItemCategoryQueryDTO;
import com.piveguyz.empickbackend.employment.applicationItem.query.mapper.ApplicationItemCategoryQueryMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationItemCategoryQueryServiceImpl implements ApplicationItemCategoryQueryService {
	private final ApplicationItemCategoryQueryMapper applicationItemCategoryQueryMapper;

	@Override
	public List<ApplicationItemCategoryQueryDTO> getAllCategories() {
		return applicationItemCategoryQueryMapper.findAllCategories();
	}
}
