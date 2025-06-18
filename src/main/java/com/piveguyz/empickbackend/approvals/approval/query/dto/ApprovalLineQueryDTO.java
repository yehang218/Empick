package com.piveguyz.empickbackend.approvals.approval.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApprovalLineQueryDTO {
    private Integer order;
    private Integer positionId;
    private String positionName;
    private Integer memberId;
    private String memberName;
    private String departmentName;
}
