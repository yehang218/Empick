package com.piveguyz.empickbackend.approvals.approval.query.dto;

import com.piveguyz.empickbackend.common.enums.InputType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApprovalContentItemDTO {
	private String itemName;
	private InputType inputType;
	private String content;
}