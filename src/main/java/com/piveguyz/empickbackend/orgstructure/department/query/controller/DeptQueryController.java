package com.piveguyz.empickbackend.orgstructure.department.query.controller;

import com.piveguyz.empickbackend.orgstructure.department.query.dto.DeptResponseQueryDTO;
import com.piveguyz.empickbackend.orgstructure.department.query.service.DeptQueryService;
import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/departments")
@Tag(name = "부서 API", description = "부서 조회 관련 API")
public class DeptQueryController {

    private final DeptQueryService deptQueryService;

    @GetMapping
    @Operation(summary = "부서 목록 조회", description = "전체 부서 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "부서 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = DeptResponseQueryDTO.class)))
    })
    public ResponseEntity<CustomApiResponse<List<DeptResponseQueryDTO>>> getDepartments() {
        List<DeptResponseQueryDTO> list = deptQueryService.getDepartments();
        return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
                .body(CustomApiResponse.of(ResponseCode.SUCCESS, list));
    }

    @GetMapping("/{id}")
    @Operation(summary = "단일 부서 조회", description = "부서 ID로 단일 부서 상세정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "단일 부서 정보 조회 성공",
                    content = @Content(schema = @Schema(implementation = DeptResponseQueryDTO.class))),
            @ApiResponse(responseCode = "404", description = "해당 부서를 찾을 수 없음")
    })
    public ResponseEntity<CustomApiResponse<DeptResponseQueryDTO>> getDepartmentById(@PathVariable Integer id) {
        DeptResponseQueryDTO dto = deptQueryService.getDepartmentById(id);
        return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
                .body(CustomApiResponse.of(ResponseCode.SUCCESS, dto));
    }
}