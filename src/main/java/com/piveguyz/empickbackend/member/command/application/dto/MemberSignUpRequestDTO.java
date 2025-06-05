package com.piveguyz.empickbackend.member.command.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemberSignUpRequestDTO {

    @NotBlank
    private String password;

    @NotNull
    private Integer employeeNumber;

    @NotBlank
    private String name;

    private String birth;

    @NotBlank
    private String phone;

    @NotBlank
    private String pictureUrl;

    @NotBlank
    private String email;

    @NotBlank
    private String address;

    private Integer vacationCount = 0;

    @NotNull
    private LocalDateTime hireAt;

    private LocalDateTime resignAt;

    private Integer createdMemberId;
    private Integer deletedMemberId;
    private Integer updatedMemberId;
    private LocalDateTime lastLoginAt;

    @NotNull
    private Integer status;

    private Integer departmentId;
    private Integer positionId;
    private Integer jobId;
    private Integer rankId;
}
