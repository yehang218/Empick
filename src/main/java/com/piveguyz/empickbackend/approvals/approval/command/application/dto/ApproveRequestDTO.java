package com.piveguyz.empickbackend.approvals.approval.command.application.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApproveRequestDTO {
    // 승인 요청 DTO (결재 승인 누르면)
    private Integer approverId;   // 결재자(본인) ID
}
