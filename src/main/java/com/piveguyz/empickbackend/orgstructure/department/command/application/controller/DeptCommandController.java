package com.piveguyz.empickbackend.orgstructure.department.command.application.controller;

import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.orgstructure.department.command.application.dto.DeptCommandDTO;
import com.piveguyz.empickbackend.orgstructure.department.command.application.service.DeptCommandService;
import com.piveguyz.empickbackend.common.enums.IsActive;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "부서 API", description = "부서 생성/수정/상태관리 API")
@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DeptCommandController {

    private final DeptCommandService deptCommandService;

    @Operation(
            summary = "부서 등록",
            description = "새로운 부서를 등록합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "부서 등록 성공"),
            @ApiResponse(responseCode = "400", description = "요청 데이터 오류")
    })
    @PostMapping
    public ResponseEntity<CustomApiResponse<DeptCommandDTO>> createDept(@Valid @RequestBody DeptCommandDTO deptCommandDTO) {
        DeptCommandDTO created = deptCommandService.createDept(deptCommandDTO);

        return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
                .body(CustomApiResponse.of(ResponseCode.SUCCESS, created));
    }

    @Operation(
            summary = "부서 수정",
            description = "기존 부서 정보를 수정합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "부서 수정 성공"),
            @ApiResponse(responseCode = "404", description = "부서를 찾을 수 없음")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CustomApiResponse<DeptCommandDTO>> updateDept(
            @PathVariable Integer id,
            @RequestBody @Valid DeptCommandDTO deptCommandDTO
    ) {
        DeptCommandDTO updated = deptCommandService.updateDept(id, deptCommandDTO);
        return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
                .body(CustomApiResponse.of(ResponseCode.SUCCESS, updated));
    }

    @Operation(
            summary = "부서 활성/비활성 상태 변경",
            description = "지정한 부서의 활성 상태를 변경합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상태 변경 성공"),
            @ApiResponse(responseCode = "404", description = "부서를 찾을 수 없음")
    })
    @PatchMapping("/{id}/active")
    public ResponseEntity<CustomApiResponse<Integer>> updateDeptActiveStatus(
            @PathVariable Integer id,
            @RequestParam("isActive") IsActive isActive
    ) {
        Integer updatedId = deptCommandService.updateDeptActiveStatus(id, isActive);

        return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
                .body(CustomApiResponse.of(ResponseCode.SUCCESS, updatedId));
    }
}