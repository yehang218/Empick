package com.piveguyz.empickbackend.approvals.approval.query.dto;

import com.piveguyz.empickbackend.approvals.approval.command.domain.enums.ApprovalStatus;
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
	private ApprovalStatus status;		// -1 : 반려, 0 : 결재 진행중, 1 : 결재 완료, 2: 결재 취소
	private Integer firstApproverId;
	private Integer secondApproverId;
	private Integer thirdApproverId;
	private Integer fourthApproverId;
	private LocalDateTime firstApprovedAt;
	private LocalDateTime secondApprovedAt;
	private LocalDateTime thirdApprovedAt;
	private LocalDateTime fourthApprovedAt;
}
