package com.piveguyz.empickbackend.approvals.approval.query.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.piveguyz.empickbackend.approvals.approval.query.dto.ApprovalReceivedDetailQueryDTO;
import com.piveguyz.empickbackend.approvals.approval.query.service.ApprovalDetailQueryService;
import com.piveguyz.empickbackend.approvals.approval.query.service.ApprovalQueryService;
import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "결재 문서 API", description = "결재 문서 관련 전체 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/approval/documents")
public class ApprovalDetailQueryController {

	private final ApprovalDetailQueryService approvalDetailQueryService;

	// 요청받은 결재문서 상세조회
	@GetMapping("/received/{id}")
	public ResponseEntity<CustomApiResponse<ApprovalReceivedDetailQueryDTO>> getReceivedApprovalDetail(
		@PathVariable("id") Integer approvalId,
		@RequestParam("memberId") Integer memberId
	) {
		ApprovalReceivedDetailQueryDTO dto = approvalDetailQueryService.getReceivedApprovalDetail(approvalId, memberId);
		return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
			.body(CustomApiResponse.of(ResponseCode.SUCCESS, dto));
	}

	// 요청한 결재문서 상세조회
}