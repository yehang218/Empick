package com.piveguyz.empickbackend.orgstructure.member.command.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProposalStatusUpdateDTO {
    private String status; // APPROVED, REJECTED 등
}
