package com.piveguyz.empickbackend.approvals.approvalCategory.query.service;

import com.piveguyz.empickbackend.approvals.approvalCategory.query.dto.ApprovalCategoryQueryDTO;

import java.util.List;

public interface ApprovalCategoryQueryService {
	List<ApprovalCategoryQueryDTO> findAll();

	ApprovalCategoryQueryDTO findById(Integer id);

	List<ApprovalCategoryQueryDTO> findByCategoryId(Integer categoryId);

	List<ApprovalCategoryQueryDTO> searchByName(String name);
}
