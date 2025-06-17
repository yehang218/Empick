package com.piveguyz.empickbackend.approvals.approval.command.application.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApproveCancelRequestDTO {
    // 취소 결재 승인 요청 DTO
    private Integer approvalId;       // 취소 요청한 결재문서 id
    private Integer targetApprovalId; // 취소 대상 결재문서 id
    private Integer approverId;       // 결재자 id
}
