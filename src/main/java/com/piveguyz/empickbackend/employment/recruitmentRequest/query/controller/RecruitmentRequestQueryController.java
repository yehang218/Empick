package com.piveguyz.empickbackend.employment.recruitmentRequest.query.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.recruitmentRequest.query.dto.RecruitmentRequestQueryDTO;
import com.piveguyz.empickbackend.employment.recruitmentRequest.query.service.RecruitmentRequestQueryService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "채용 요청서 조회 API", description = "채용 요청서 목록 및 상세 조회")
@RestController("QueryRecruitmentController")
@RequestMapping("api/v1/employment/recruitment/request")
@RequiredArgsConstructor
public class RecruitmentRequestQueryController {
	private final RecruitmentRequestQueryService recruitmentRequestQueryService;

	// 채용 요청서 전체 목록 조회
	@GetMapping
	public ResponseEntity<CustomApiResponse<List<RecruitmentRequestQueryDTO>>> getAllRequests() {
		List<RecruitmentRequestQueryDTO> result = recruitmentRequestQueryService.getAll();
		return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
			.body(CustomApiResponse.of(ResponseCode.SUCCESS, result));
	}

	// 채용 요청서 상세 조회
	@GetMapping("/{id}")
	public ResponseEntity<CustomApiResponse<RecruitmentRequestQueryDTO>> getRequestById(@PathVariable int id) {
		RecruitmentRequestQueryDTO result = recruitmentRequestQueryService.getById(id);
		return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
			.body(CustomApiResponse.of(ResponseCode.SUCCESS, result));
	}
}
