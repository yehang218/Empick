package com.piveguyz.empickbackend.employment.applicant.query.controller;

import com.piveguyz.empickbackend.employment.applicant.query.dto.ApplicantQueryDTO;
import com.piveguyz.empickbackend.employment.applicant.query.service.ApplicantQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Applicant", description = "지원자 조회 API")
@RestController
@RequestMapping("api/v1/applicant")
@RequiredArgsConstructor
public class ApplicantQueryController {

    private final ApplicantQueryService applicantQueryService;

    @Operation(summary = "지원자 전체 조회", description = "지원자 목록 전체를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<ApplicantQueryDTO>> getAllApplicant() {
        return ResponseEntity.ok(applicantQueryService.findAllApplicant());
    }
}
