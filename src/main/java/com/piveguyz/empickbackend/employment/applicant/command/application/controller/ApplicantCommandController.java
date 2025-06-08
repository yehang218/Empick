package com.piveguyz.empickbackend.employment.applicant.command.application.controller;

import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.applicant.command.application.dto.ApplicantCommandDTO;
import com.piveguyz.empickbackend.employment.applicant.command.application.service.ApplicantCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "지원자 생성/수정/삭제 API", description = "지원자 등록, 수정, 삭제 API")
@RestController
@RequestMapping("/api/v1/employment/applicant")
@RequiredArgsConstructor
public class ApplicantCommandController {

    private final ApplicantCommandService applicantCommandService;

    // 지원자 등록
    @Operation(summary = "지원자 등록", description = "지원자를 새로 등록합니다.")
    @ApiResponses(value = {})
    @PostMapping
    public ResponseEntity<CustomApiResponse<ApplicantCommandDTO>> createApplicant(
            @RequestBody @Valid ApplicantCommandDTO applicantCommandDTO) {
        ApplicantCommandDTO created = applicantCommandService.createApplicant(applicantCommandDTO);
        return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
                .body(CustomApiResponse.of(ResponseCode.SUCCESS, created));
    }

//    // 지원자 수정
//    @Operation(summary = "지원자 수정", description = "지원자 정보를 수정합니다.")
//    @ApiResponses(value = {})
//    @PatchMapping("/{id}")
//    public ResponseEntity<CustomApiResponse<ApplicantCommandDTO>> updateApplicant(
//            @PathVariable int id,
//            @RequestBody @Valid ApplicantCommandDTO applicantCommandDTO) {
//        ApplicantCommandDTO updated = applicantCommandService.updateApplicant(id, applicantCommandDTO);
//        return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
//                .body(CustomApiResponse.of(ResponseCode.SUCCESS, updated));
//    }
//
//    // 지원자 삭제
//    @Operation(summary = "지원자 삭제", description = "지원자를 삭제합니다.")
//    @ApiResponses(value = {})
//    @DeleteMapping("/{id}")
//    public ResponseEntity<CustomApiResponse<Integer>> deleteApplicant(@PathVariable int id) {
//        int deletedId = applicantCommandService.deleteApplicant(id);
//        return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
//                .body(CustomApiResponse.of(ResponseCode.SUCCESS, deletedId));
//    }
}