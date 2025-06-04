package com.piveguyz.empickbackend.employment.applicant.query.controller;

import com.piveguyz.empickbackend.employment.applicant.query.dto.ApplicationQueryDTO;
import com.piveguyz.empickbackend.employment.applicant.query.service.ApplicationQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Application", description = "지원서 조회 API")
@RestController
@RequestMapping("/api/v1/application")
@RequiredArgsConstructor
public class ApplicationQueryController {

    private final ApplicationQueryService applicationQueryService;

    @Operation(summary = "전체 지원서 조회", description = "지원서 전체를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<ApplicationQueryDTO>> getAllApplication() {
        return ResponseEntity.ok(applicationQueryService.findAllApplication());
    }
}
