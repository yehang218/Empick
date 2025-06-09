package com.piveguyz.empickbackend.member.command.application.controller;

import com.piveguyz.empickbackend.common.constants.ApiExamples;
import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.infra.s3.dto.S3UploadResponseDTO;
import com.piveguyz.empickbackend.member.command.application.dto.MemberProfileUploadRequestDTO;
import com.piveguyz.empickbackend.member.command.application.dto.MemberSignUpRequestDTO;
import com.piveguyz.empickbackend.member.command.application.dto.MemberSignUpResponseDTO;
import com.piveguyz.empickbackend.member.command.application.facade.MemberProfileCommandFacade;
import com.piveguyz.empickbackend.member.command.application.service.MemberCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/members")
@Tag(name = "사원 API", description = "사원 등록 및 관리 API")
@RequiredArgsConstructor
public class MemberCommandController {

    private final MemberCommandService memberCommandService;
    private final MemberProfileCommandFacade memberProfileFacade;

    @PostMapping
    @Operation(summary = "사원 등록", description = "새로운 사원을 등록합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공",
                    content = @Content(schema = @Schema(implementation = MemberSignUpResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "이메일 중복 오류", content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_409_EXAMPLE))),
            @ApiResponse(responseCode = "400", description = "유효성 검사 실패", content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_400_EXAMPLE))),
    })
    public ResponseEntity<CustomApiResponse<MemberSignUpResponseDTO>> signup(@RequestBody @Valid MemberSignUpRequestDTO request) {
        MemberSignUpResponseDTO result = memberCommandService.signUp(request);
        return ResponseEntity.status(ResponseCode.CREATED.getHttpStatus())
                .body(CustomApiResponse.of(ResponseCode.CREATED, result));
    }

    @PostMapping(value = "/{memberId}/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "프로필 이미지 업로드", description = "프로필 이미지를 S3에 업로드하고 Member DB에 반영합니다.")
    public ResponseEntity<CustomApiResponse<S3UploadResponseDTO>> uploadProfileImage(
            @PathVariable int memberId,
            @Parameter(description = "저장될 파일명 예: profile.png", example = "profile.png")
            @RequestParam("fileName") String fileName,
            @Parameter(description = "업로드할 파일", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            @RequestPart("file") MultipartFile file) {

        // prefix를 강제로 설정
        String prefix = "profiles/" + memberId;

        MemberProfileUploadRequestDTO request = MemberProfileUploadRequestDTO.builder()
                .prefix(prefix)
                .fileName(fileName)
                .file(file)
                .build();

        S3UploadResponseDTO result = memberProfileFacade.uploadProfileImage(memberId, request);

        return ResponseEntity.status(ResponseCode.CREATED.getHttpStatus())
                .body(CustomApiResponse.of(ResponseCode.CREATED, result));
    }
}

