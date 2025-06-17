package com.piveguyz.empickbackend.approvals.approval.command.application.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApproveCancelRequestDTO {
    // 취소 결재 승인 요청 DTO
    private Integer approverId;       // 결재자 id
}
