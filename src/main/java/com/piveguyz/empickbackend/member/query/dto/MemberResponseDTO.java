package com.piveguyz.empickbackend.member.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MemberResponseDTO {
    private Integer id;
    private Integer employeeNumber;
    private String name;
    private String email;
    private String phone;
    private String departmentName;
    private String positionName;
    private String jobName;
    private String rankName;
    private String pictureUrl;
    private Integer status;
    private LocalDateTime hireAt;
    private LocalDateTime resignAt;
}