package com.piveguyz.empickbackend.approvals.approvalCategory.query.controller;

import com.piveguyz.empickbackend.approvals.approvalCategory.query.dto.ApprovalCategoryQueryDTO;
import com.piveguyz.empickbackend.approvals.approvalCategory.query.service.ApprovalCategoryQueryService;
import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "결재 문서 유형 API", description = "결재 문서 유형 관련 전체 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/approval/categories")
public class ApprovalCategoryQueryController {
	private final ApprovalCategoryQueryService service;

	@Operation(
			summary = "결재 유형 전체 조회",
			description = """
                    - 결재 유형 전체를 조회합니다.
                    """
	)
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다."),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
	})
	@GetMapping
	public ResponseEntity<CustomApiResponse<List<ApprovalCategoryQueryDTO>>> findAll() {
		List<ApprovalCategoryQueryDTO> dtoList = service.findAll();
		ResponseCode result = ResponseCode.SUCCESS;
		return ResponseEntity.status(result.getHttpStatus())
			.body(CustomApiResponse.of(result, dtoList));
	}

	@GetMapping("/{id}")
	public ResponseEntity<CustomApiResponse<ApprovalCategoryQueryDTO>> findById(@PathVariable("id") Integer id) {
		ApprovalCategoryQueryDTO dto = service.findById(id);
		ResponseCode result = ResponseCode.SUCCESS;
		return ResponseEntity.status(result.getHttpStatus())
				.body(CustomApiResponse.of(result, dto));
	}

	// 상위 카테고리로 하위 카테고리 찾기
	@GetMapping("/{parentId}/children")
	public ResponseEntity<CustomApiResponse<List<ApprovalCategoryQueryDTO>>> findChildren(@PathVariable("parentId") Integer parentId) {
		List<ApprovalCategoryQueryDTO> dtoList = service.findByCategoryId(parentId);
		ResponseCode result = ResponseCode.SUCCESS;
		return ResponseEntity.status(result.getHttpStatus())
				.body(CustomApiResponse.of(result, dtoList));
	}

	@GetMapping("/name")
	public ResponseEntity<CustomApiResponse<List<ApprovalCategoryQueryDTO>>> searchByName(@RequestParam("name") String name){
		List<ApprovalCategoryQueryDTO> dtoList = service.searchByName(name);
		ResponseCode result = ResponseCode.SUCCESS;
		return ResponseEntity.status(result.getHttpStatus())
				.body(CustomApiResponse.of(result, dtoList));
	}
}