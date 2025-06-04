package com.piveguyz.empickbackend.employment.recruitmentRequest.query.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.piveguyz.empickbackend.employment.recruitmentRequest.query.dto.RecruitmentRequestQueryDTO;
import com.piveguyz.empickbackend.employment.recruitmentRequest.query.service.RecruitmentRequestQueryService;

import lombok.RequiredArgsConstructor;

@RestController("QueryRecruitmentController")
@RequestMapping("api/v1/employment/recruitment/request")
@RequiredArgsConstructor
public class RecruitmentRequestQueryController {
	private final RecruitmentRequestQueryService recruitmentRequestQueryService;

	// 채용 요청서 조회
	@GetMapping
	public List<RecruitmentRequestQueryDTO> getAllRequests() {
		return recruitmentRequestQueryService.getAll();
	}

	// 채용 요청서 상세 조회
	@GetMapping("/{id}")
	public RecruitmentRequestQueryDTO getRequestById(@PathVariable int id) {
		return recruitmentRequestQueryService.getById(id);
	}
}
