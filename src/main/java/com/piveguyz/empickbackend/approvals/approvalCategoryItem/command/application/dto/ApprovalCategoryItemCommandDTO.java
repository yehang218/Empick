package com.piveguyz.empickbackend.approvals.approvalCategoryItem.command.application.dto;

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
public class ApprovalCategoryItemCommandDTO {
    private Integer id;
    private Integer categoryId;
    private String name;
}
