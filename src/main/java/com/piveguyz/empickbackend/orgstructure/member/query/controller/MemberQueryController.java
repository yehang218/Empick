package com.piveguyz.empickbackend.orgstructure.member.query.controller;

import com.piveguyz.empickbackend.auth.facade.AuthFacade;
import com.piveguyz.empickbackend.common.constants.ApiExamples;
import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.orgstructure.facade.MemberProfileFacade;
import com.piveguyz.empickbackend.orgstructure.member.query.dto.MemberEditProposalQueryDTO;
import com.piveguyz.empickbackend.orgstructure.member.query.dto.MemberResponseDTO;
import com.piveguyz.empickbackend.orgstructure.member.query.service.MemberEditProposalQueryService;
import com.piveguyz.empickbackend.orgstructure.member.query.service.MemberQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "사원 API", description = "사원 등록 및 관리 API")
@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberQueryController {

    private final MemberProfileFacade memberProfileFacade;
    private final MemberQueryService memberQueryService;
    private final AuthFacade authFacade;  // 🔥 AuthFacade 추가
    private final MemberEditProposalQueryService queryService;

    @Operation(summary = "내 정보 조회", description = """
            - 로그인한 사용자의 정보를 조회합니다. (JWT 토큰 필요)
            - 스웨거의 Authorize 버튼이 작동하지 않는 관계로, 아래의 curl 명령어로 테스트 바랍니다. \\
            curl -X GET http://localhost:5001/api/v1/member/me -H "Authorization: Bearer {ACCESS_TOKEN_VALUE}"
            """, security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 정보 조회 성공", content = @Content(schema = @Schema(implementation = MemberResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패 (JWT 토큰 필요)", content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_401_EXAMPLE))),
            @ApiResponse(responseCode = "404", description = "회원 정보를 찾을 수 없음", content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_404_EXAMPLE)))
    })
    @GetMapping("/me")
    public ResponseEntity<CustomApiResponse<MemberResponseDTO>> getCurrentMember() {
        Integer memberId = authFacade.getCurrentMemberId();
        MemberResponseDTO responseDTO = memberProfileFacade.getMemberInfo(memberId);
        return ResponseEntity.ok(CustomApiResponse.of(ResponseCode.SUCCESS, responseDTO));
    }

    @Operation(summary = "사원 이름으로 조회", description = "- 사원 이름으로 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사원 조회 성공", content = @Content(schema = @Schema(implementation = MemberResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "사원 정보를 찾을 수 없음", content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_404_EXAMPLE)))
    })
    @GetMapping("/search/name")
    public ResponseEntity<CustomApiResponse<List<MemberResponseDTO>>> getMembersByName(
            @RequestParam("name") String name) {
        List<MemberResponseDTO> members = memberQueryService.getMembersByName(name);
        return ResponseEntity.ok(CustomApiResponse.of(ResponseCode.SUCCESS, members));
    }

    @Operation(summary = "사번으로 조회", description = "- 사번으로 사원 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사원 조회 성공", content = @Content(schema = @Schema(implementation = MemberResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "사원 정보를 찾을 수 없음", content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_404_EXAMPLE)))
    })
    @GetMapping("/search/employeeNumber")
    public ResponseEntity<CustomApiResponse<List<MemberResponseDTO>>> getMembersByEmployeeNumber(
            @RequestParam("employeeNumber") int employeeNumber) {
        List<MemberResponseDTO> members = memberQueryService.getMembersByEmployeeNumber(employeeNumber);
        return ResponseEntity.ok(CustomApiResponse.of(ResponseCode.SUCCESS, members));
    }

    @Operation(summary = "프로필 이미지 다운로드", description = """
            - memberId로 S3에서 프로필 이미지를 다운로드합니다.
            - **현재 DB에 55번 id의 사원의 프로필 사진 경로만 제대로 등록 되어 있음**
            """)
    @GetMapping("/{memberId}/profile-image")
    public ResponseEntity<byte[]> getProfileImage(
            @PathVariable int memberId) {

        byte[] imageData = memberProfileFacade.downloadProfileImage(memberId);
        String profileImageKey = memberProfileFacade.getProfileImageKey(memberId);
        String contentType = guessContentType(profileImageKey);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(imageData);
    }


    @Operation(
            summary = "사원 정보 수정 요청 조회",
            description = """
                    - 사원 ID로 수정 요청 내역을 조회합니다.
                    - memberId를 생략하면 전체 요청을 조회합니다.
                    - 상태값(status)은 다음과 같습니다:
                      - 0: PENDING
                      - 1: APPROVED
                      - 2: REJECTED
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 요청 조회 성공", content = @Content(schema = @Schema(implementation = MemberEditProposalQueryDTO.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_400_EXAMPLE))),
            @ApiResponse(responseCode = "404", description = "데이터를 찾을 수 없음", content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_404_EXAMPLE)))
    })
    @GetMapping("/proposal")
    public ResponseEntity<CustomApiResponse<List<MemberEditProposalQueryDTO>>> getProposals(
            @Parameter(description = "사원 ID (선택)") @RequestParam(required = false) Integer memberId) {
        List<MemberEditProposalQueryDTO> proposals = queryService.getProposals(memberId);
        return ResponseEntity.ok(CustomApiResponse.of(ResponseCode.SUCCESS, proposals));
    }

    private String guessContentType(String key) {
        String lowerKey = key.toLowerCase();

        if (lowerKey.endsWith(".png")) return "image/png";
        if (lowerKey.endsWith(".jpg") || lowerKey.endsWith(".jpeg")) return "image/jpeg";
        if (lowerKey.endsWith(".webp")) return "image/webp";
        if (lowerKey.endsWith(".gif")) return "image/gif";
        if (lowerKey.endsWith(".svg")) return "image/svg+xml";

        return "application/octet-stream";
    }
}
