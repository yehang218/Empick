package com.piveguyz.empickbackend.employment.applicationItem.query.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.applicationItem.query.dto.ApplicationItemCategoryQueryDTO;
import com.piveguyz.empickbackend.employment.applicationItem.query.service.ApplicationItemCategoryQueryService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "지원서 항목 조회 API", description = "지원서 항목 조회 및 카테고리 조회")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employment/application/item")
public class ApplicationItemCategoryQueryController {
	private final ApplicationItemCategoryQueryService applicationItemCategoryQueryService;

	@GetMapping("/category")
	public ResponseEntity<CustomApiResponse<List<ApplicationItemCategoryQueryDTO>>> getAllCategories() {
		List<ApplicationItemCategoryQueryDTO> result = applicationItemCategoryQueryService.getAllCategories();
		return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
			.body(CustomApiResponse.of(ResponseCode.SUCCESS, result));
	}
}