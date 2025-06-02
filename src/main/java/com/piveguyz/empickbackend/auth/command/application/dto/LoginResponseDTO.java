package com.piveguyz.empickbackend.auth.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class LoginResponseDTO {
    private final String accessToken;
    private final String refreshToken;
}
