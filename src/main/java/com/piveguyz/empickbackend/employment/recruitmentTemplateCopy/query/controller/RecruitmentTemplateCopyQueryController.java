package com.piveguyz.empickbackend.employment.recruitmentTemplateCopy.query.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.recruitmentTemplateCopy.query.dto.RecruitmentTemplateCopyQueryDTO;
import com.piveguyz.empickbackend.employment.recruitmentTemplateCopy.query.service.RecruitmentTemplateCopyQueryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "채용 템플릿 복사본 조회 API", description = "채용공고에 복사된 템플릿 항목 조회")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employment/recruitment/template/copy")
public class RecruitmentTemplateCopyQueryController {

	private final RecruitmentTemplateCopyQueryService recruitmentTemplateCopyQueryService;

	@Operation(summary = "채용 템플릿 복사본 조회", description = "채용공고 ID로 복사된 템플릿 항목들을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다."),
		@ApiResponse(responseCode = "2071", description = "채용 템플릿 복사본이 존재하지 않습니다."),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.")
	})
	@GetMapping("/{recruitmentId}")
	public ResponseEntity<CustomApiResponse<List<RecruitmentTemplateCopyQueryDTO>>> getCopies(@PathVariable int recruitmentId) {
		List<RecruitmentTemplateCopyQueryDTO> result = recruitmentTemplateCopyQueryService.getByRecruitmentId(recruitmentId);

		return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
			.body(CustomApiResponse.of(ResponseCode.SUCCESS, result));
	}
}