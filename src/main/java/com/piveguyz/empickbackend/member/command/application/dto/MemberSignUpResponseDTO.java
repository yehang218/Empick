package com.piveguyz.empickbackend.member.command.application.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberSignUpResponseDTO {
    private int id;
    private String email;
    private String name;
    private Integer employeeNumber;
    private LocalDate createdAt;
}
