package com.piveguyz.empickbackend.employment.recruitmentRequest.command.application.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.recruitmentRequest.command.application.dto.RecruitmentRequestCommandDTO;
import com.piveguyz.empickbackend.employment.recruitmentRequest.command.application.service.RecruitmentRequestCommandService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "채용 요청서 등록 API", description = "채용 요청서 등록")
@RestController("RecruitmentRequestCommandController")
@RequiredArgsConstructor
@RequestMapping("/api/v1/employment/recruitment/request")
public class RecruitmentRequestCommandController {

	private final RecruitmentRequestCommandService recruitmentRequestCommandService;

	// 채용 요청서 등록
	@PostMapping
	public ResponseEntity<CustomApiResponse<Void>> create(@RequestBody RecruitmentRequestCommandDTO dto) {
		recruitmentRequestCommandService.create(dto);
		return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
			.body(CustomApiResponse.of(ResponseCode.SUCCESS));
	}
}
