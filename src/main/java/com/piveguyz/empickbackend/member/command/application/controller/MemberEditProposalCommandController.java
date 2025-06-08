package com.piveguyz.empickbackend.member.command.application.controller;

import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.member.command.application.dto.MemberEditProposalCommandDTO;
import com.piveguyz.empickbackend.member.command.application.service.MemberEditProposalCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사원 API", description = "사원 정보 변경 요청 생성 API")
@RestController
@RequestMapping("/api/v1/member/edit")
@RequiredArgsConstructor
public class MemberEditProposalCommandController {

    private final MemberEditProposalCommandService memberEditCommandService;

    @Operation(
            summary = "사원 정보 수정 요청 생성",
            description = """
                - 사원의 특정 필드에 대한 변경 요청을 생성합니다.
                - 변경 사유 및 요청 값은 DTO로 전달됩니다.
                - 필드 타입 (fieldType) 값은 다음 중 하나입니다:
                  - STRING (0)
                  - INT (1)
                  - DATETIME (2)
                - 타겟 필드 (targetField) 값은 다음 중 하나입니다:
                  - NAME
                  - PHONE
                  - EMAIL
                  - ADDRESS
                  - PICTURE_URL
                  - RANK
                  - POSITION
                  - JOB
                  - DEPARTMENT
                - 요청 상태 (status) 값은 다음 중 하나입니다:
                  - PENDING (0)
                  - APPROVED (1)
                  - REJECTED (2)
                - 예시로 복사해 테스트해보세요:
                ```
                {
                  "memberId": 1,
                  "targetField": "PHONE",
                  "originalValue": "010-1234-5678",
                  "requestedValue": "010-8765-4321",
                  "fieldType": "STRING",
                  "reason": "전화번호 변경"
                }
                ```
                """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 요청 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "사원을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/proposal")
    public ResponseEntity<CustomApiResponse<Void>> createEditRequest(
            @RequestBody MemberEditProposalCommandDTO dto) {

        memberEditCommandService.createMemberEditRequest(dto);
        return ResponseEntity.ok(CustomApiResponse.of(ResponseCode.SUCCESS));
    }
}
