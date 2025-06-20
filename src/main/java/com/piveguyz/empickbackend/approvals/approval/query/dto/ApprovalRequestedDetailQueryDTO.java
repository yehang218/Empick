package com.piveguyz.empickbackend.approvals.approval.query.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.piveguyz.empickbackend.approvals.approval.command.domain.enums.ApprovalStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalRequestedDetailQueryDTO {
	private Integer approvalId;
	private Integer categoryId;
	private String categoryName;
	private String writerName;
	private String writerDepartment;
	private String writerPosition;
	private LocalDateTime createdAt;
	private ApprovalStatus status;

	private List<ApprovalContentItemDTO> items;
	private List<ApprovalLineDetailDTO> approvers;

	private Integer targetApprovalId;            // 취소 대상 문서 ID
	private String targetCategoryName;           // 취소 대상 문서 유형
	private String targetWriterName;             // 취소 대상 작성자 이름
	private ApprovalStatus targetApprovalStatus; // 취소 대상 상태
}
