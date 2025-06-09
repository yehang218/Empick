package com.piveguyz.empickbackend.orgstructure.department.query.controller;

import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.orgstructure.department.query.dto.DeptChangeHistoryQueryDTO;
import com.piveguyz.empickbackend.orgstructure.department.query.service.DeptChangeHistoryQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dept-change-history")
@Tag(name = "부서 이동 내역 API", description = "부서 이동 내역 생성, 조회 API")
@RequiredArgsConstructor
public class DeptChangeHistoryQueryController {

    private final DeptChangeHistoryQueryService deptChangeHistoryQueryService;

    @GetMapping("/member/{memberId}")
    @Operation(summary = "특정 사원의 부서 이동 기록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DeptChangeHistoryQueryDTO.class))),
            @ApiResponse(responseCode = "404", description = "사원 정보를 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomApiResponse.class)))
    })
    public ResponseEntity<CustomApiResponse<List<DeptChangeHistoryQueryDTO>>> getDeptChangeHistoriesByMemberId(
            @Parameter(description = "조회 대상 사원 ID", example = "1")
            @PathVariable Integer memberId) {
        List<DeptChangeHistoryQueryDTO> histories = deptChangeHistoryQueryService.getDeptChangeHistoriesByMemberId(memberId);
        return ResponseEntity.ok(CustomApiResponse.of(ResponseCode.SUCCESS, histories));
    }
}
