package com.piveguyz.empickbackend.employment.recruitmentProcess.query.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.recruitmentProcess.query.dto.RecruitmentProcessQueryDTO;
import com.piveguyz.empickbackend.employment.recruitmentProcess.query.service.RecruitmentProcessQueryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "채용 프로세스 조회 API", description = "채용공고별 전형 단계 조회")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employment/recruitment/process")
public class RecruitmentProcessQueryController {

	private final RecruitmentProcessQueryService recruitmentProcessQueryService;

	@Operation(summary = "채용공고 전형 단계 조회", description = "채용공고 ID 기준 전형 단계를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다."),
		@ApiResponse(responseCode = "2060", description = "채용 프로세스를 찾을 수 없습니다."),
		@ApiResponse(responseCode = "2064", description = "채용 공고를 찾을 수 없습니다."),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.")
	})
	@GetMapping("/{recruitmentId}")
	public ResponseEntity<CustomApiResponse<List<RecruitmentProcessQueryDTO>>> getProcesses(
		@PathVariable int recruitmentId
	) {
		List<RecruitmentProcessQueryDTO> result = recruitmentProcessQueryService.getProcessesByRecruitmentId(recruitmentId);
		return ResponseEntity.ok(CustomApiResponse.of(ResponseCode.SUCCESS, result));
	}
}