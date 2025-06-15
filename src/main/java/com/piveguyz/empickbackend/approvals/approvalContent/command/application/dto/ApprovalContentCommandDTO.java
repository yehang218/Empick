package com.piveguyz.empickbackend.approvals.approvalContent.command.application.dto;

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
public class ApprovalContentCommandDTO {
    private Integer id;
    private Integer approvalId;
    private Integer itemId;
    private String content;
}
