package com.piveguyz.empickbackend.orgstructure.job.query.controller;

import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.orgstructure.job.query.dto.JobResponseDTO;
import com.piveguyz.empickbackend.orgstructure.job.query.service.JobQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/jobs")
@Tag(name = "직무 API", description = "직무 조회 및 관리 API")
public class JobQueryController {
    private final JobQueryService jobQueryService;

    @GetMapping
    @Operation(summary = "직무 전체 조회", description = "등록된 모든 직무 목록을 조회합니다.")
    public ResponseEntity<CustomApiResponse<List<JobResponseDTO>>> getAllJobs() {
        List<JobResponseDTO> jobs = jobQueryService.getAllJobs();
        return ResponseEntity.ok(CustomApiResponse.of(ResponseCode.SUCCESS, jobs));
    }

}