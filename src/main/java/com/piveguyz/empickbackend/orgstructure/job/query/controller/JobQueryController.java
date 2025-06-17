
package com.piveguyz.empickbackend.orgstructure.job.query.controller;

import com.piveguyz.empickbackend.common.enums.IsActive;
import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.orgstructure.job.query.dto.JobResponseDTO;
import com.piveguyz.empickbackend.orgstructure.job.query.service.JobQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/jobs")
@Tag(name = "직무 API", description = "직무 조회 및 관리 API")
public class JobQueryController {

    private final JobQueryService jobQueryService;

    // ==================== 기본 조회 API ====================

    @GetMapping
    @Operation(summary = "직무 전체 조회", description = "등록된 모든 직무 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<CustomApiResponse<List<JobResponseDTO>>> getAllJobs() {
        CustomApiResponse<List<JobResponseDTO>> response = jobQueryService.getAllJobs();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    @Operation(summary = "활성 직무 조회", description = "활성 상태인 직무 목록을 조회합니다.")
    public ResponseEntity<CustomApiResponse<List<JobResponseDTO>>> getActiveJobs() {
        CustomApiResponse<List<JobResponseDTO>> response = jobQueryService.getActiveJobs();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/inactive")
    @Operation(summary = "비활성 직무 조회", description = "비활성 상태인 직무 목록을 조회합니다.")
    public ResponseEntity<CustomApiResponse<List<JobResponseDTO>>> getInactiveJobs() {
        CustomApiResponse<List<JobResponseDTO>> response = jobQueryService.getInactiveJobs();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "활성 여부별 직무 조회", description = "지정된 활성 상태에 따른 직무 목록을 조회합니다.")
    public ResponseEntity<CustomApiResponse<List<JobResponseDTO>>> getJobsByActiveStatus(
            @Parameter(description = "활성 상태 (ACTIVE/INACTIVE)", required = true)
            @PathVariable IsActive status) {
        CustomApiResponse<List<JobResponseDTO>> response = jobQueryService.getJobsByActiveStatus(status);
        return ResponseEntity.ok(response);
    }

    // ==================== 단건 조회 API ====================

    @GetMapping("/{id}")
    @Operation(summary = "직무 단건 조회 (ID)", description = "ID로 특정 직무 정보를 조회합니다.")
    public ResponseEntity<CustomApiResponse<JobResponseDTO>> getJobById(
            @Parameter(description = "직무 ID", required = true)
            @PathVariable Integer id) {
        CustomApiResponse<JobResponseDTO> response = jobQueryService.getJobById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "직무 단건 조회 (코드)", description = "코드로 특정 직무 정보를 조회합니다.")
    public ResponseEntity<CustomApiResponse<JobResponseDTO>> getJobByCode(
            @Parameter(description = "직무 코드", required = true)
            @PathVariable String code) {
        CustomApiResponse<JobResponseDTO> response = jobQueryService.getJobByCode(code);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "직무 단건 조회 (이름)", description = "이름으로 특정 직무 정보를 조회합니다.")
    public ResponseEntity<CustomApiResponse<JobResponseDTO>> getJobByName(
            @Parameter(description = "직무명", required = true)
            @PathVariable String name) {
        CustomApiResponse<JobResponseDTO> response = jobQueryService.getJobByName(name);
        return ResponseEntity.ok(response);
    }

    // ==================== 상세 정보 포함 조회 API ====================

    @GetMapping("/with-role-info")
    @Operation(summary = "권한 정보 포함 직무 조회", description = "권한 정보가 포함된 직무 목록을 조회합니다.")
    public ResponseEntity<CustomApiResponse<List<JobResponseDTO>>> getJobsWithRoleInfo() {
        CustomApiResponse<List<JobResponseDTO>> response = jobQueryService.getJobsWithRoleInfo();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/with-member-count")
    @Operation(summary = "사원 수 정보 포함 직무 조회", description = "사원 수 정보가 포함된 직무 목록을 조회합니다.")
    public ResponseEntity<CustomApiResponse<List<JobResponseDTO>>> getJobsWithMemberCount() {
        CustomApiResponse<List<JobResponseDTO>> response = jobQueryService.getJobsWithMemberCount();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/with-full-details")
    @Operation(summary = "상세 정보 포함 직무 조회", description = "권한 정보와 사원 수 정보를 모두 포함한 직무 목록을 조회합니다.")
    public ResponseEntity<CustomApiResponse<List<JobResponseDTO>>> getJobsWithFullDetails() {
        CustomApiResponse<List<JobResponseDTO>> response = jobQueryService.getJobsWithFullDetails();
        return ResponseEntity.ok(response);
    }

    // ==================== 필터링 조회 API ====================

    @GetMapping("/role/{roleId}")
    @Operation(summary = "권한별 직무 조회", description = "특정 권한을 가진 직무 목록을 조회합니다.")
    public ResponseEntity<CustomApiResponse<List<JobResponseDTO>>> getJobsByRoleId(
            @Parameter(description = "권한 ID", required = true)
            @PathVariable Integer roleId) {
        CustomApiResponse<List<JobResponseDTO>> response = jobQueryService.getJobsByRoleId(roleId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/with-description")
    @Operation(summary = "설명 있는 직무 조회", description = "설명이 등록된 직무 목록을 조회합니다.")
    public ResponseEntity<CustomApiResponse<List<JobResponseDTO>>> getJobsWithDescription() {
        CustomApiResponse<List<JobResponseDTO>> response = jobQueryService.getJobsWithDescription();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/without-description")
    @Operation(summary = "설명 없는 직무 조회", description = "설명이 등록되지 않은 직무 목록을 조회합니다.")
    public ResponseEntity<CustomApiResponse<List<JobResponseDTO>>> getJobsWithoutDescription() {
        CustomApiResponse<List<JobResponseDTO>> response = jobQueryService.getJobsWithoutDescription();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/with-members")
    @Operation(summary = "사원 있는 직무 조회", description = "사원이 배정된 직무 목록을 조회합니다.")
    public ResponseEntity<CustomApiResponse<List<JobResponseDTO>>> getJobsWithMembers() {
        CustomApiResponse<List<JobResponseDTO>> response = jobQueryService.getJobsWithMembers();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/without-members")
    @Operation(summary = "사원 없는 직무 조회", description = "사원이 배정되지 않은 직무 목록을 조회합니다.")
    public ResponseEntity<CustomApiResponse<List<JobResponseDTO>>> getJobsWithoutMembers() {
        CustomApiResponse<List<JobResponseDTO>> response = jobQueryService.getJobsWithoutMembers();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/with-role")
    @Operation(summary = "권한 설정된 직무 조회", description = "권한이 설정된 직무 목록을 조회합니다.")
    public ResponseEntity<CustomApiResponse<List<JobResponseDTO>>> getJobsWithRole() {
        CustomApiResponse<List<JobResponseDTO>> response = jobQueryService.getJobsWithRole();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/without-role")
    @Operation(summary = "권한 미설정 직무 조회", description = "권한이 설정되지 않은 직무 목록을 조회합니다.")
    public ResponseEntity<CustomApiResponse<List<JobResponseDTO>>> getJobsWithoutRole() {
        CustomApiResponse<List<JobResponseDTO>> response = jobQueryService.getJobsWithoutRole();
        return ResponseEntity.ok(response);
    }

    // ==================== 검색 API ====================

    @GetMapping("/search")
    @Operation(summary = "통합 검색", description = "이름 또는 코드에서 키워드를 검색합니다.")
    public ResponseEntity<CustomApiResponse<List<JobResponseDTO>>> searchJobs(
            @Parameter(description = "검색 키워드", required = true)
            @RequestParam String keyword) {
        CustomApiResponse<List<JobResponseDTO>> response = jobQueryService.searchJobs(keyword);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/name")
    @Operation(summary = "이름 검색", description = "직무명에서 키워드를 검색합니다.")
    public ResponseEntity<CustomApiResponse<List<JobResponseDTO>>> searchJobsByName(
            @Parameter(description = "검색 키워드", required = true)
            @RequestParam String keyword) {
        CustomApiResponse<List<JobResponseDTO>> response = jobQueryService.searchJobsByName(keyword);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/code")
    @Operation(summary = "코드 검색", description = "직무 코드에서 키워드를 검색합니다.")
    public ResponseEntity<CustomApiResponse<List<JobResponseDTO>>> searchJobsByCode(
            @Parameter(description = "검색 키워드", required = true)
            @RequestParam String keyword) {
        CustomApiResponse<List<JobResponseDTO>> response = jobQueryService.searchJobsByCode(keyword);
        return ResponseEntity.ok(response);
    }

    // ==================== 페이징 조회 API ====================

    @GetMapping("/paged")
    @Operation(summary = "페이징 조회", description = "페이징을 지원하는 직무 목록 조회입니다.")
    public ResponseEntity<CustomApiResponse<Page<JobResponseDTO>>> getJobsPaged(
            @Parameter(description = "페이지 번호 (0부터 시작)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "활성 상태 필터 (선택사항)")
            @RequestParam(required = false) IsActive isActive) {
        CustomApiResponse<Page<JobResponseDTO>> response = jobQueryService.getJobsPaged(page, size, isActive);
        return ResponseEntity.ok(response);
    }

    // ==================== 통계 및 분석 API ====================

    @GetMapping("/statistics")
    @Operation(summary = "직무 통계", description = "직무 관련 통계 정보를 조회합니다.")
    public ResponseEntity<CustomApiResponse<Map<String, Object>>> getJobStatistics() {
        CustomApiResponse<Map<String, Object>> response = jobQueryService.getJobStatistics();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/member-count-summary")
    @Operation(summary = "직무별 사원 수 집계", description = "직무별 사원 수 집계 정보를 조회합니다.")
    public ResponseEntity<CustomApiResponse<Map<String, Integer>>> getJobMemberCountSummary() {
        CustomApiResponse<Map<String, Integer>> response = jobQueryService.getJobMemberCountSummary();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/top-by-member-count")
    @Operation(summary = "사원 수 기준 상위 직무", description = "사원 수가 많은 상위 직무 목록을 조회합니다.")
    public ResponseEntity<CustomApiResponse<List<JobResponseDTO>>> getTopJobsByMemberCount(
            @Parameter(description = "조회할 상위 개수")
            @RequestParam(defaultValue = "5") int limit) {
        CustomApiResponse<List<JobResponseDTO>> response = jobQueryService.getTopJobsByMemberCount(limit);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/recently-created")
    @Operation(summary = "최근 생성 직무", description = "최근에 생성된 직무 목록을 조회합니다.")
    public ResponseEntity<CustomApiResponse<List<JobResponseDTO>>> getRecentlyCreatedJobs(
            @Parameter(description = "조회할 개수")
            @RequestParam(defaultValue = "5") int limit) {
        CustomApiResponse<List<JobResponseDTO>> response = jobQueryService.getRecentlyCreatedJobs(limit);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/recently-updated")
    @Operation(summary = "최근 수정 직무", description = "최근에 수정된 직무 목록을 조회합니다.")
    public ResponseEntity<CustomApiResponse<List<JobResponseDTO>>> getRecentlyUpdatedJobs(
            @Parameter(description = "조회할 개수")
            @RequestParam(defaultValue = "5") int limit) {
        CustomApiResponse<List<JobResponseDTO>> response = jobQueryService.getRecentlyUpdatedJobs(limit);
        return ResponseEntity.ok(response);
    }

    // ==================== 유틸리티 API ====================

    @GetMapping("/validate/{id}")
    @Operation(summary = "직무 유효성 검증", description = "직무의 유효성을 검증합니다.")
    public ResponseEntity<CustomApiResponse<Map<String, Object>>> validateJob(
            @Parameter(description = "직무 ID", required = true)
            @PathVariable Integer id) {
        CustomApiResponse<Map<String, Object>> response = jobQueryService.validateJob(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/exists/id/{id}")
    @Operation(summary = "직무 존재 확인 (ID)", description = "ID로 직무 존재 여부를 확인합니다.")
    public ResponseEntity<CustomApiResponse<Boolean>> existsById(
            @Parameter(description = "직무 ID", required = true)
            @PathVariable Integer id) {
        CustomApiResponse<Boolean> response = jobQueryService.existsById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/exists/code/{code}")
    @Operation(summary = "직무 존재 확인 (코드)", description = "코드로 직무 존재 여부를 확인합니다.")
    public ResponseEntity<CustomApiResponse<Boolean>> existsByCode(
            @Parameter(description = "직무 코드", required = true)
            @PathVariable String code) {
        CustomApiResponse<Boolean> response = jobQueryService.existsByCode(code);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/exists/name/{name}")
    @Operation(summary = "직무 존재 확인 (이름)", description = "이름으로 직무 존재 여부를 확인합니다.")
    public ResponseEntity<CustomApiResponse<Boolean>> existsByName(
            @Parameter(description = "직무명", required = true)
            @PathVariable String name) {
        CustomApiResponse<Boolean> response = jobQueryService.existsByName(name);
        return ResponseEntity.ok(response);
    }

    // ==================== 중복 검사 API ====================

    @GetMapping("/duplicate/code")
    @Operation(summary = "코드 중복 검사", description = "직무 코드의 중복 여부를 확인합니다.")
    public ResponseEntity<CustomApiResponse<Boolean>> isCodeDuplicated(
            @Parameter(description = "검사할 직무 코드", required = true)
            @RequestParam String code) {
        CustomApiResponse<Boolean> response = jobQueryService.isCodeDuplicated(code);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/duplicate/name")
    @Operation(summary = "이름 중복 검사", description = "직무명의 중복 여부를 확인합니다.")
    public ResponseEntity<CustomApiResponse<Boolean>> isNameDuplicated(
            @Parameter(description = "검사할 직무명", required = true)
            @RequestParam String name) {
        CustomApiResponse<Boolean> response = jobQueryService.isNameDuplicated(name);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/duplicate/code/exclude/{excludeId}")
    @Operation(summary = "코드 중복 검사 (수정용)", description = "특정 ID를 제외한 코드 중복 여부를 확인합니다.")
    public ResponseEntity<CustomApiResponse<Boolean>> isCodeDuplicatedExcludingId(
            @Parameter(description = "검사할 직무 코드", required = true)
            @RequestParam String code,
            @Parameter(description = "제외할 직무 ID", required = true)
            @PathVariable Integer excludeId) {
        CustomApiResponse<Boolean> response = jobQueryService.isCodeDuplicatedExcludingId(code, excludeId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/duplicate/name/exclude/{excludeId}")
    @Operation(summary = "이름 중복 검사 (수정용)", description = "특정 ID를 제외한 이름 중복 여부를 확인합니다.")
    public ResponseEntity<CustomApiResponse<Boolean>> isNameDuplicatedExcludingId(
            @Parameter(description = "검사할 직무명", required = true)
            @RequestParam String name,
            @Parameter(description = "제외할 직무 ID", required = true)
            @PathVariable Integer excludeId) {
        CustomApiResponse<Boolean> response = jobQueryService.isNameDuplicatedExcludingId(name, excludeId);
        return ResponseEntity.ok(response);
    }
}