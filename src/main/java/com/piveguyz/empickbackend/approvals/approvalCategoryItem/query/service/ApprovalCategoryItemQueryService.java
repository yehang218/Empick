package com.piveguyz.empickbackend.approvals.approvalCategoryItem.query.service;

import com.piveguyz.empickbackend.approvals.approvalCategoryItem.query.dto.ApprovalCategoryItemQueryDTO;

import java.util.List;

public interface ApprovalCategoryItemQueryService {

	List<ApprovalCategoryItemQueryDTO> findAll();

	ApprovalCategoryItemQueryDTO findById(Integer id);

	List<ApprovalCategoryItemQueryDTO> findByCategoryId(Integer categoryId);
}
