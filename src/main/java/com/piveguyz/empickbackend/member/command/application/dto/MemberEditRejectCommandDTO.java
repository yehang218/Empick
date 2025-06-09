package com.piveguyz.empickbackend.member.command.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberEditRejectCommandDTO {
    private Integer editProposalId;
    private String rejectReason;
    private Integer reviewerId;  // 추가 필요
}
