package com.piveguyz.empickbackend.orgstructure.member.command.application.controller;

import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.orgstructure.member.command.application.dto.MemberEditProposalCommandDTO;
import com.piveguyz.empickbackend.orgstructure.member.command.application.dto.MemberEditRejectCommandDTO;
import com.piveguyz.empickbackend.orgstructure.member.command.application.service.MemberEditProposalCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사원 정보 수정 요청 API", description = "사원 정보 변경 요청 생성, 승인, 거절 API")
@RestController
@RequestMapping("/api/v1/member/edit")
@RequiredArgsConstructor
public class MemberEditProposalCommandController {

    private final MemberEditProposalCommandService memberEditCommandService;

    @Operation(
            summary = "사원 정보 수정 요청 생성",
            description = """
                - 사원의 특정 필드에 대한 변경 요청을 생성합니다.
                - DTO 형식:
                {
                  "memberId": 1,
                  "targetField": "PHONE",
                  "originalValue": "010-1234-5678",
                  "requestedValue": "010-8765-4321",
                  "fieldType": "STRING",
                  "reason": "전화번호 변경"
                }
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

    @Operation(summary = "사원 정보 수정 요청 승인", description = "요청 ID와 검토자 ID를 전달해 요청을 승인합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/proposal/{proposalId}/approve")
    public ResponseEntity<CustomApiResponse<Void>> approveProposal(
            @PathVariable("proposalId") Integer proposalId) {

        memberEditCommandService.approveEditProposal(proposalId);
        return ResponseEntity.ok(CustomApiResponse.of(ResponseCode.SUCCESS));
    }

    @Operation(summary = "사원 정보 수정 요청 거절", description = "요청 ID, 검토자 ID, 거절 사유를 전달해 요청을 거절합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/proposal/reject")
    public ResponseEntity<CustomApiResponse<Void>> rejectProposal(
            @RequestBody MemberEditRejectCommandDTO dto) {

        memberEditCommandService.reject(dto);
        return ResponseEntity.ok(CustomApiResponse.of(ResponseCode.SUCCESS));
    }
}
