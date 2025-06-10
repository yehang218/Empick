package com.piveguyz.empickbackend.orgstructure.department.query.controller;

import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.orgstructure.department.query.dto.DeptMemberQueryResponseDTO;
import com.piveguyz.empickbackend.orgstructure.department.query.service.DeptMemberQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DeptMemberQueryController {

    private final DeptMemberQueryService deptMemberQueryService;

    @Operation(summary = "부서별 사원 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "부서원 조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 부서에 사원이 없습니다.")
    })
    @GetMapping("/{departmentId}/members")
    public ResponseEntity<CustomApiResponse<List<DeptMemberQueryResponseDTO>>> getMembersByDepartment(
            @PathVariable int departmentId) {
        List<DeptMemberQueryResponseDTO> members = deptMemberQueryService.getMembersByDepartmentId(departmentId);
        return ResponseEntity.ok(CustomApiResponse.of(ResponseCode.SUCCESS, members));
    }
}
