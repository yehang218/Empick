package com.piveguyz.empickbackend.employment.applicationItem.query.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.piveguyz.empickbackend.employment.applicationItem.query.dto.ApplicationItemCategoryQueryDTO;

@Mapper
public interface ApplicationItemCategoryQueryMapper {
	List<ApplicationItemCategoryQueryDTO> findAllCategories();
}
