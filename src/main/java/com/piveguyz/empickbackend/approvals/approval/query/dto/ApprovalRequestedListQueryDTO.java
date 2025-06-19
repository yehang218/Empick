package com.piveguyz.empickbackend.approvals.approval.query.dto;

import java.time.LocalDateTime;

import com.piveguyz.empickbackend.approvals.approval.command.domain.enums.ApprovalStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApprovalRequestedListQueryDTO {
	private Integer approvalId;
	private String categoryName;
	private LocalDateTime createdAt;
	private ApprovalStatus status;
}
