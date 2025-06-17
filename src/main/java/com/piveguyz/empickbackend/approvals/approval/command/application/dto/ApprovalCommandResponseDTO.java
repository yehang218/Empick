package com.piveguyz.empickbackend.approvals.approval.command.application.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalCommandResponseDTO {
    private Integer approvalId;
    private String categoryName;
    private String writerName;
    private String status;
    private LocalDateTime createdAt;
    private List<ContentDTO> contents;

    @Getter @Setter
    public static class ContentDTO {
        private String itemName;
        private String content;
    }
}
