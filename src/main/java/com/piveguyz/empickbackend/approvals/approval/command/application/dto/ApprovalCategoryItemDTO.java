package com.piveguyz.empickbackend.approvals.approval.command.application.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalCategoryItemDTO {
    private Integer id;
    private String name;
}
