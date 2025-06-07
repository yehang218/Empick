package com.piveguyz.empickbackend.auth.command.application.dto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshTokenRequestDTO {
    private String refreshToken;
    private List<String> roles;
    private Integer employeeNumber;
}
