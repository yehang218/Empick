package com.piveguyz.empickbackend.auth.command.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @Pattern(regexp = "^[0-9]+$", message = "사번은 숫자만 입력해야 합니다.")
    private String employeeNumber;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Size(min = 6, max = 30, message = "비밀번호는 6자 이상 30자 이하로 입력하세요.")
    private String password;

}
