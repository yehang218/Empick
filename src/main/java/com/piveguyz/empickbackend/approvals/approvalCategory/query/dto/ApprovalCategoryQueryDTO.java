package com.piveguyz.empickbackend.approvals.approvalCategory.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApprovalCategoryQueryDTO {
	private Integer id;
	private Integer approvalCategoryId;
	private String name;
}
