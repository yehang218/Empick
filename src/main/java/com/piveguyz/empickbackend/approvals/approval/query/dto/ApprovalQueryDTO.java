package com.piveguyz.empickbackend.approvals.approval.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ApprovalQueryDTO {
	private Integer id;
	private Integer categoryId;
	private Integer writerId;
	private LocalDateTime createdAt;
	private Integer status;
	private Integer approver1Id;
	private Integer approver2Id;
	private Integer approver3Id;
	private Integer approver4Id;
	private LocalDateTime approvedAt1;
	private LocalDateTime approvedAt2;
	private LocalDateTime approvedAt3;
	private LocalDateTime approvedAt4;
}
