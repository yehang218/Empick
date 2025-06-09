package com.piveguyz.empickbackend.orgstructure.member.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberEditProposalQueryDTO {
    private Integer id;
    private Integer memberId;
    private String targetField;
    private String originalValue;
    private String requestedValue;
    private Integer status; // 0: PENDING, 1: APPROVED, 2: REJECTED
}
