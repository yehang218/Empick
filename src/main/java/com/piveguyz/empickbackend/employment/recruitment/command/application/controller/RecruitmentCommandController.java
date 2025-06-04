package com.piveguyz.empickbackend.employment.recruitment.command.application.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.recruitment.command.application.dto.RecruitmentCommandDTO;
import com.piveguyz.empickbackend.employment.recruitment.command.application.service.RecruitmentCommandService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "채용 공고 커맨드 API", description = "채용 공고 등록/수정/삭제")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employment/recruitment")
public class RecruitmentCommandController {
	private final RecruitmentCommandService recruitmentCommandService;

	@Operation(summary = "채용 공고 등록", description = "채용 공고를 등록합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다."),
		@ApiResponse(responseCode = "2031", description = "채용 기간이 유효하지 않습니다."),
		@ApiResponse(responseCode = "2035", description = "채용 공고 제목을 입력하지 않았습니다."),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.")
	})
	@PostMapping
	public ResponseEntity<CustomApiResponse<Void>> create(@RequestBody RecruitmentCommandDTO dto) {
		recruitmentCommandService.createRecruitment(dto);
		return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
			.body(CustomApiResponse.of(ResponseCode.SUCCESS));
	}

	@Operation(summary = "채용 공고 수정", description = "게시되지 않은 채용 공고의 내용을 수정합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다."),
		@ApiResponse(responseCode = "2030", description = "요청한 채용 공고를 찾을 수 없습니다."),
		@ApiResponse(responseCode = "2034", description = "게시된 채용 공고는 수정할 수 없습니다."),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.")
	})
	@PutMapping("/{id}")
	public ResponseEntity<CustomApiResponse<Void>> update(@PathVariable int id, @RequestBody RecruitmentCommandDTO dto) {
		recruitmentCommandService.updateRecruitment(id, dto);
		return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
			.body(CustomApiResponse.of(ResponseCode.SUCCESS));
	}
}
