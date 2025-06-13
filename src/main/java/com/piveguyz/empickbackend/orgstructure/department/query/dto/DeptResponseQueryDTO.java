package com.piveguyz.empickbackend.orgstructure.department.query.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class DeptResponseQueryDTO {
    private Integer id;
    private String name;
    private String code;
    private Integer isActive;
    private String description;
    private Integer roleId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
