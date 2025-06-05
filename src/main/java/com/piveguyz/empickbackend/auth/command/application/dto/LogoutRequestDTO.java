package com.piveguyz.empickbackend.auth.command.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LogoutRequestDTO {
    private String refreshToken;
    private Integer employeeNumber;
}
