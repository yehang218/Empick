package com.piveguyz.empickbackend.approvals.approval.command.application.controller;

import com.piveguyz.empickbackend.approvals.approval.command.application.dto.CreateApprovalCommandDTO;
import com.piveguyz.empickbackend.approvals.approval.command.application.service.ApprovalCommandService;
import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "결재 API", description = "결재 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/approvals")
public class ApprovalCommandController {

    private final ApprovalCommandService approvalCommandService;

    @Operation(
            summary = "결재 문서 생성",
            description = """
                    결재 문서를 생성합니다.
                    """
    )
    @ApiResponses(value = {
    })
    @PostMapping
    public ResponseEntity<CustomApiResponse<Integer>> createApproval(@RequestBody CreateApprovalCommandDTO dto) {
        Integer approvalId = approvalCommandService.createApproval(dto);
        return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
                .body(CustomApiResponse.of(ResponseCode.SUCCESS, approvalId));
    }
}
