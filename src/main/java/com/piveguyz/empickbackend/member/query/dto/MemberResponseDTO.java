package com.piveguyz.empickbackend.member.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberResponseDTO {
    private int id;
    private String name;
    private int employeeNumber;
    private String email;
}
