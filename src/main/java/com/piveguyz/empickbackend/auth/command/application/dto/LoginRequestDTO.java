package com.piveguyz.empickbackend.auth.command.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequestDTO {

    @NotBlank(message = "사번은 필수 입력값입니다.")
    private String employeeNumber;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;

}
