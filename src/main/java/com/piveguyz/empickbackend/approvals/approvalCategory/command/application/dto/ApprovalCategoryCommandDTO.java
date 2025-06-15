package com.piveguyz.empickbackend.approvals.approvalCategory.command.application.dto;

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
public class ApprovalCategoryCommandDTO {
    private Integer id;
    private Integer approvalCategoryId;
    private String name;
}
