package com.piveguyz.empickbackend.approvals.approval.query.controller;

import com.piveguyz.empickbackend.approvals.approval.query.dto.ApprovalQueryDTO;
import com.piveguyz.empickbackend.approvals.approval.query.service.ApprovalQueryService;
import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "결재 문서 API", description = "결재 문서 관련 전체 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/approval")
public class ApprovalQueryController {
	private final ApprovalQueryService service;

	@GetMapping
	public ResponseEntity<CustomApiResponse<List<ApprovalQueryDTO>>> findAll() {
		List<ApprovalQueryDTO> dtoList = service.findAll();
		ResponseCode result = ResponseCode.SUCCESS;
		return ResponseEntity.status(result.getHttpStatus())
			.body(CustomApiResponse.of(result, dtoList));
	}

	@GetMapping("/{id}")
	public ResponseEntity<CustomApiResponse<ApprovalQueryDTO>> findById(@PathVariable("id") Integer id) {
		ApprovalQueryDTO dto = service.findById(id);
		ResponseCode result = ResponseCode.SUCCESS;
		return ResponseEntity.status(result.getHttpStatus())
				.body(CustomApiResponse.of(result, dto));
	}

	@GetMapping("/category/{categoryId}")
	public ResponseEntity<CustomApiResponse<List<ApprovalQueryDTO>>> findByCategoryId(@PathVariable("categoryId") Integer categoryId) {
		List<ApprovalQueryDTO> dtoList = service.findByCategoryId(categoryId);
		ResponseCode result = ResponseCode.SUCCESS;
		return ResponseEntity.status(result.getHttpStatus())
				.body(CustomApiResponse.of(result, dtoList));
	}

	@GetMapping("/writer/{writerId}")
	public ResponseEntity<CustomApiResponse<List<ApprovalQueryDTO>>> findByWriterId(@PathVariable("writerId") Integer writerId) {
		List<ApprovalQueryDTO> dtoList = service.findByWriterId(writerId);
		ResponseCode result = ResponseCode.SUCCESS;
		return ResponseEntity.status(result.getHttpStatus())
				.body(CustomApiResponse.of(result, dtoList));
	}

	// 자신이 결재자인 결재문서 조회
	@GetMapping("/received")
	public ResponseEntity<CustomApiResponse<List<ApprovalQueryDTO>>> findReceivedApprovals(
		@RequestParam("memberId") Integer memberId) {

		List<ApprovalQueryDTO> dtoList = service.findReceivedApprovals(memberId);
		return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
			.body(CustomApiResponse.of(ResponseCode.SUCCESS, dtoList));
	}
}