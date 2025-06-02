package com.piveguyz.empickbackend.auth.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(force = true)
public class LoginResponseDTO {
    private final String accessToken;
    private final String refreshToken;
}
