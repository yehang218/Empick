package com.piveguyz.empickbackend.auth.command.application.service;

import com.piveguyz.empickbackend.auth.command.application.dto.LoginRequestDTO;
import com.piveguyz.empickbackend.auth.command.application.dto.LoginResponseDTO;

public interface AuthCommandService {
    LoginResponseDTO login(LoginRequestDTO requestDTO);
}
