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
public class ApprovalReceivedDetailQueryDTO {
	private Integer approvalId;
	private Integer categoryId;
	private String categoryName;        // 연차, 반차 등
	private String writerName;
	private String writerDepartment;
	private String writerPosition;
	private LocalDateTime createdAt;
	private ApprovalStatus status;      // -2: 취소, -1: 반려, 0: 진행중, 1: 완료

	private List<ApprovalContentItemDTO> items;    // 결재 문서 내용 (항목명 + 내용)
	private List<ApprovalLineDetailDTO> approvers;  // 결재자들 (결재 순서, 이름, 부서, 직책, 상태)

	private boolean isMyTurn;           // 현재 사용자 차례 여부
}