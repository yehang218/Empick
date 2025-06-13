package com.piveguyz.empickbackend.orgstructure.job.query.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobResponseDTO {
    private Integer id;
    private String name;
    private String code;
    private Integer isActive;
    private String description;
    private Integer roleId;
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;
}
