package com.piveguyz.empickbackend.department.query.controller;

import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.department.query.dto.DeptMemberQueryResponseDTO;
import com.piveguyz.empickbackend.department.query.service.DeptMemberQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "부서 API", description = "부서와 관련된 조회 API")
@RestController
@RequestMapping("/api/v1/department")
@RequiredArgsConstructor
public class DeptMemberQueryController {

    private final DeptMemberQueryService deptMemberQueryService;

    @Operation(summary = "부서별 사원 조회", description = "부서 ID로 소속 사원을 조회합니다.")
    @GetMapping("/{departmentId}/members")
    public ResponseEntity<CustomApiResponse<List<DeptMemberQueryResponseDTO>>> getMembersByDepartmentId(
            @PathVariable int departmentId) {
        List<DeptMemberQueryResponseDTO> members = deptMemberQueryService.getMembersByDepartmentId(departmentId);
        return ResponseEntity.ok(CustomApiResponse.of(ResponseCode.SUCCESS, members));
    }
}
