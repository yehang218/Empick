package com.piveguyz.empickbackend.approvals.approval.query.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApprovalLineDetailDTO {
	private Integer stepOrder;
	private Integer memberId;
	private String memberName;
	private String positionName;
	private String departmentName;

	private boolean approved;           // 승인 여부
	private LocalDateTime approvedAt;   // 승인 일시
}
