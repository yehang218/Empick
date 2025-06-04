package com.piveguyz.empickbackend.employment.applicant.query.controller;

import com.piveguyz.empickbackend.employment.applicant.query.dto.ApplicantBookmarkQueryDTO;
import com.piveguyz.empickbackend.employment.applicant.query.service.ApplicantBookmarkQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "ApplicantBookmark", description = "북마크 지원자 조회 API")
@RestController
@RequestMapping("api/v1/applicant-bookmark")
@RequiredArgsConstructor
public class ApplicantBookmarkQueryController {

    private final ApplicantBookmarkQueryService applicantBookmarkQueryService;

    @Operation(summary = "북마크 지원자 전체 조회", description = "북마크 지원자 목록 전체를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<ApplicantBookmarkQueryDTO>> getAllApplicantBookmark() {
        return ResponseEntity.ok(applicantBookmarkQueryService.findAllApplicantBookmark());
    }
}
