package com.piveguyz.empickbackend.approvals.approvalCategoryItem.query.dto;

import com.piveguyz.empickbackend.common.enums.InputType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalCategoryItemQueryDTO {
	private Integer id;
	private Integer categoryId;
	private String name;
	private InputType inputType;
}
