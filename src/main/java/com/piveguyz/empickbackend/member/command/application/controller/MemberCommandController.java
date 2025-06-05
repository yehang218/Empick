package com.piveguyz.empickbackend.member.command.application.controller;

import com.piveguyz.empickbackend.common.constants.ApiExamples;
import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.member.command.application.dto.MemberSignUpRequestDTO;
import com.piveguyz.empickbackend.member.command.application.dto.MemberSignUpResponseDTO;
import com.piveguyz.empickbackend.member.command.application.service.MemberCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
@Tag(name = "사원 API", description = "사원 등록 및 관리 API")
@RequiredArgsConstructor
public class MemberCommandController {

    private final MemberCommandService memberCommandService;

    @PostMapping
    @Operation(summary = "사원 등록", description = "새로운 사원을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공",
                    content = @Content(schema = @Schema(implementation = MemberSignUpResponseDTO.class))),
//            @ApiResponse(responseCode = "1001", description = "삭제된 사원",
//                    content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_1001_EXAMPLE))),
            @ApiResponse(responseCode = "409", description = "이메일 중복 오류", content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_409_EXAMPLE))),
            @ApiResponse(responseCode = "400", description = "유효성 검사 실패", content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_400_EXAMPLE))),
    })
    public ResponseEntity<CustomApiResponse<MemberSignUpResponseDTO>> signup(@RequestBody @Valid MemberSignUpRequestDTO request) {
        MemberSignUpResponseDTO result = memberCommandService.signUp(request);
        return ResponseEntity.status(ResponseCode.CREATED.getHttpStatus())
                .body(CustomApiResponse.of(ResponseCode.CREATED, result));
    }
}
