package com.piveguyz.empickbackend.approvals.approvalCategoryItem.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApprovalCategoryItemQueryDTO {
	private Integer id;
	private Integer categoryId;
	private String name;
}
