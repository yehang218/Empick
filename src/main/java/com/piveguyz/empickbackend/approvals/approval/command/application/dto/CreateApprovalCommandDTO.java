package com.piveguyz.empickbackend.approvals.approval.command.application.dto;

import lombok.*;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateApprovalCommandDTO {
    private Integer categoryId;
    private Integer writerId;
    private List<ApproverDTO> approvers; // max 4명까지
    private List<ApprovalContentDTO> contents;

    @Getter @Setter
    public static class ApproverDTO {
        private Integer order; // 1~4
        private Integer memberId;
    }

    @Getter @Setter
    public static class ApprovalContentDTO {
        private Integer itemId;
        private String content;
    }
}
