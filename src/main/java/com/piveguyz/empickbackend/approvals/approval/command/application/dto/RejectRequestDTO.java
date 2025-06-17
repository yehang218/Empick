package com.piveguyz.empickbackend.approvals.approval.command.application.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RejectRequestDTO {
    private Integer approverId;   // 결재자(본인) ID
    private String rejectReason; // 추가할 수도 있어서 우선 넣음
}