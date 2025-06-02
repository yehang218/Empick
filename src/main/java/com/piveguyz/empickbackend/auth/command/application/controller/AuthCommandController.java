package com.piveguyz.empickbackend.auth.command.application.controller;

import com.piveguyz.empickbackend.auth.command.application.dto.LoginRequestDTO;
import com.piveguyz.empickbackend.auth.command.application.dto.LoginResponseDTO;
import com.piveguyz.empickbackend.auth.command.application.service.AuthCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "인증 API", description = "로그인/회원가입/토큰 발급 등 인증 관련 API")
public class AuthCommandController {

    private final AuthCommandService authCommandService;

    @Operation(
            summary = "로그인",
            description = "사번과 비밀번호를 통해 로그인을 수행하고 AccessToken 및 RefreshToken을 발급합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(schema = @Schema(implementation = LoginResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO requestDTO) {
        LoginResponseDTO responseDTO = authCommandService.login(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }
}
