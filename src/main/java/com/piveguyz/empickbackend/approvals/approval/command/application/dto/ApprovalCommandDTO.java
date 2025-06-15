package com.piveguyz.empickbackend.approvals.approval.command.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalCommandDTO {
    private Integer id;
    private Integer categoryId;
    private Integer writerId;
    private LocalDateTime createdAt;
    private Integer status;
    private Integer approver1Id;
    private Integer approver2Id;
    private Integer approver3Id;
    private Integer approver4Id;
    private LocalDateTime approvedAt1;
    private LocalDateTime approvedAt2;
    private LocalDateTime approvedAt3;
    private LocalDateTime approvedAt4;
}
