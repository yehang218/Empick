package com.piveguyz.empickbackend.member.query.controller;

import com.piveguyz.empickbackend.common.constants.ApiExamples;
import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.member.query.dto.MemberResponseDTO;
import com.piveguyz.empickbackend.member.query.service.MemberQueryService;
import com.piveguyz.empickbackend.security.CustomMemberDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원 API", description = "회원 정보 조회 API")
@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberQueryController {

    private final MemberQueryService memberQueryService;

    @Operation(summary = "내 정보 조회", description = """
            - 로그인한 사용자의 정보를 조회합니다. (JWT 토큰 필요)
            - 스웨거의 Authorize 버튼이 작동하지 않는 관계로, 아래의 curl 명령어로 테스트 바랍니다. \\
            curl -X GET http://localhost:5001/api/v1/member/me -H "Authorization: Bearer {ACCESS_TOKEN_VALUE}"
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 정보 조회 성공", content = @Content(schema = @Schema(implementation = MemberResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패 (JWT 토큰 필요)", content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_401_EXAMPLE))),
            @ApiResponse(responseCode = "404", description = "회원 정보를 찾을 수 없음", content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_404_EXAMPLE)))
    })
    @GetMapping("/me")
    public ResponseEntity<CustomApiResponse<MemberResponseDTO>> getCurrentMember(@AuthenticationPrincipal CustomMemberDetails memberDetails) {
        MemberResponseDTO responseDTO = memberQueryService.getMemberInfo(memberDetails.getMember().getId());
        return ResponseEntity.status(HttpStatus.OK).body(CustomApiResponse.of(ResponseCode.SUCCESS, responseDTO));
    }
}
