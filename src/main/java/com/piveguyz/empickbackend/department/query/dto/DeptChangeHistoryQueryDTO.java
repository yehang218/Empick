package com.piveguyz.empickbackend.department.query.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DeptChangeHistoryQueryDTO {

    private Integer id;
    private Integer memberId;
    private String deptName;
    private String positionName;
    private String jobName;
    private String rankName;
    private LocalDateTime workStartAt;
    private LocalDateTime workEndAt;
}
