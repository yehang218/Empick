package com.piveguyz.empickbackend.approvals.approvalContent.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApprovalContentQueryDTO {
	private Integer id;
	private Integer approvalId;
	private Integer itemId;
	private String content;
}
