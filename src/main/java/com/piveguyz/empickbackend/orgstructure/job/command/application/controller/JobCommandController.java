package com.piveguyz.empickbackend.orgstructure.job.command.application.controller;

import com.piveguyz.empickbackend.common.enums.IsActive;
import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.orgstructure.job.command.application.dto.JobCommandDTO;
import com.piveguyz.empickbackend.orgstructure.job.command.application.service.JobCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "직무 API", description = "직무 생성/수정/상태관리 API")
@RestController
@RequestMapping("/api/v1/jobs")
@RequiredArgsConstructor
public class JobCommandController {
    private final JobCommandService jobCommandService;

    @Operation(
            summary = "직무 등록",
            description = "새로운 직무를 등록합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "직무 등록 성공"),
            @ApiResponse(responseCode = "400", description = "요청 데이터 오류")
    })
    @PostMapping
    public ResponseEntity<CustomApiResponse<JobCommandDTO>> createJob(@RequestBody @Valid JobCommandDTO jobCommandDTO) {
        JobCommandDTO created = jobCommandService.createJob(jobCommandDTO);
        return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
                .body(CustomApiResponse.of(ResponseCode.SUCCESS, created));
    }

    @Operation(
            summary = "직무 수정",
            description = "기존 직무 정보를 수정합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "직무 수정 성공"),
            @ApiResponse(responseCode = "404", description = "직무를 찾을 수 없음")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<CustomApiResponse<JobCommandDTO>> updateJob(
            @PathVariable int id,
            @RequestBody @Valid JobCommandDTO jobCommandDTO) {
        JobCommandDTO updated = jobCommandService.updateJob(id, jobCommandDTO);
        return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
                .body(CustomApiResponse.of(ResponseCode.SUCCESS, updated));
    }

    @Operation(
            summary = "직무 활성/비활성 상태 변경",
            description = "지정한 직무의 활성 상태를 변경합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상태 변경 성공"),
            @ApiResponse(responseCode = "404", description = "직무를 찾을 수 없음")
    })
    @PatchMapping("/{id}/active")
    public ResponseEntity<CustomApiResponse<Integer>> updateJobActiveStatus(
            @PathVariable int id,
            @RequestParam("isActive") int isActive) {
        IsActive isActiveEnum = IsActive.fromValue(isActive);
        Integer updatedId = jobCommandService.updateJobActiveStatus(id, isActiveEnum);
        return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
                .body(CustomApiResponse.of(ResponseCode.SUCCESS, updatedId));
    }
}