package com.piveguyz.empickbackend.orgstructure.department.query.dto;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class DeptMemberQueryResponseDTO {
    private Integer id;
    private String name;
    private Integer employeeNumber;
    private String email;
    private String phone;
    private String positionName;
    private String jobName;
    private String rankName;
    private String pictureUrl;
    private LocalDateTime hireAt;
}
