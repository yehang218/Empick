package com.piveguyz.empickbackend.employment.applicationItem.query.service;

import java.util.List;

import com.piveguyz.empickbackend.employment.applicationItem.query.dto.ApplicationItemCategoryQueryDTO;

public interface ApplicationItemCategoryQueryService {
	List<ApplicationItemCategoryQueryDTO> getAllCategories();
}
