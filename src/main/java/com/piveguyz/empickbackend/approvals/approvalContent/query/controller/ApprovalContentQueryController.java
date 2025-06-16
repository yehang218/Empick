package com.piveguyz.empickbackend.approvals.approvalContent.query.controller;

import com.piveguyz.empickbackend.approvals.approvalContent.query.dto.ApprovalContentQueryDTO;
import com.piveguyz.empickbackend.approvals.approvalContent.query.service.ApprovalContentQueryService;
import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "결재 문서 내용 API", description = "결재 문서 내용 관련 전체 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/approval-content")
public class ApprovalContentQueryController {
	private final ApprovalContentQueryService service;

	@GetMapping
	public ResponseEntity<CustomApiResponse<List<ApprovalContentQueryDTO>>> findAll() {
		List<ApprovalContentQueryDTO> dtoList = service.findAll();
		ResponseCode result = ResponseCode.SUCCESS;
		return ResponseEntity.status(result.getHttpStatus())
			.body(CustomApiResponse.of(result, dtoList));
	}

	@GetMapping("{id}")
	public ResponseEntity<CustomApiResponse<ApprovalContentQueryDTO>> findById(@PathVariable("id") Integer id) {
		ApprovalContentQueryDTO dto = service.findById(id);
		ResponseCode result = ResponseCode.SUCCESS;
		return ResponseEntity.status(result.getHttpStatus())
				.body(CustomApiResponse.of(result, dto));
	}

	@GetMapping("/approval/{approvalId}")
	public  ResponseEntity<CustomApiResponse<List<ApprovalContentQueryDTO>>> findByApprovalId(@PathVariable("approvalId") Integer approvalId) {
		List<ApprovalContentQueryDTO> dtoList = service.findByApprovalId(approvalId);
		ResponseCode result = ResponseCode.SUCCESS;
		return ResponseEntity.status(result.getHttpStatus())
				.body(CustomApiResponse.of(result, dtoList));
	}
}