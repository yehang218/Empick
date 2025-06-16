package com.piveguyz.empickbackend.approvals.approvalCategory.query.mapper;

import com.piveguyz.empickbackend.approvals.approvalCategory.query.dto.ApprovalCategoryQueryDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ApprovalCategoryQueryMapper {
	List<ApprovalCategoryQueryDTO> findAll();

	ApprovalCategoryQueryDTO findById(Integer id);

	List<ApprovalCategoryQueryDTO> findByCategoryId(Integer categoryId);

	List<ApprovalCategoryQueryDTO> searchByName(String name);
}
