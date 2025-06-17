package com.piveguyz.empickbackend.orgstructure.position.query.controller;

import com.piveguyz.empickbackend.common.enums.IsActive;
import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.orgstructure.position.query.dto.PositionQueryDTO;
import com.piveguyz.empickbackend.orgstructure.position.query.service.PositionQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 직책 조회 컨트롤러
 * RESTful API를 통한 직책 조회 기능 제공
 */
@Tag(name = "직책 API", description = "직책 관리")
@Slf4j
@RestController
@RequestMapping("/api/v1/positions/query")
@RequiredArgsConstructor
public class PositionQueryController {

    private final PositionQueryService positionQueryService;

    @Operation(
            summary = "모든 직책 조회",
            description = "시스템에 등록된 모든 직책을 조회합니다 (활성/비활성 구분 없이)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PositionQueryDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping
    public ResponseEntity<CustomApiResponse<List<PositionQueryDTO>>> getAllPositions() {
        log.debug("모든 직책 조회 API 호출");
        CustomApiResponse<List<PositionQueryDTO>> response = positionQueryService.findAll();
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "활성 직책 조회",
            description = "현재 활성 상태인 직책만 조회합니다"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PositionQueryDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/active")
    public ResponseEntity<CustomApiResponse<List<PositionQueryDTO>>> getActivePositions() {
        log.debug("활성 직책 조회 API 호출");
        CustomApiResponse<List<PositionQueryDTO>> response = positionQueryService.findActivePositions();
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "비활성 직책 조회",
            description = "현재 비활성 상태인 직책만 조회합니다"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PositionQueryDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/inactive")
    public ResponseEntity<CustomApiResponse<List<PositionQueryDTO>>> getInactivePositions() {
        log.debug("비활성 직책 조회 API 호출");
        CustomApiResponse<List<PositionQueryDTO>> response = positionQueryService.findInactivePositions();
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "활성 여부별 직책 조회",
            description = "지정된 활성 여부에 따라 직책을 조회합니다"
    )
    @Parameter(
            name = "isActive",
            description = "활성 여부 (ACTIVE: 활성, INACTIVE: 비활성)",
            required = true,
            schema = @Schema(implementation = IsActive.class)
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PositionQueryDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/status/{isActive}")
    public ResponseEntity<CustomApiResponse<List<PositionQueryDTO>>> getPositionsByStatus(
            @PathVariable IsActive isActive) {
        log.debug("활성 여부별 직책 조회 API 호출. isActive: {}", isActive);
        CustomApiResponse<List<PositionQueryDTO>> response = positionQueryService.findByIsActive(isActive);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "ID로 직책 조회",
            description = "직책 ID를 사용하여 특정 직책을 조회합니다"
    )
    @Parameter(
            name = "id",
            description = "직책 ID (양의 정수)",
            required = true,
            example = "1"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PositionQueryDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 ID 형식"),
            @ApiResponse(responseCode = "404", description = "직책을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CustomApiResponse<PositionQueryDTO>> getPositionById(@PathVariable Integer id) {
        log.debug("ID로 직책 조회 API 호출. id: {}", id);
        CustomApiResponse<PositionQueryDTO> response = positionQueryService.findById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "이름으로 직책 조회",
            description = "직책명을 사용하여 특정 직책을 조회합니다"
    )
    @Parameter(
            name = "name",
            description = "직책명 (정확한 이름)",
            required = true,
            example = "팀장"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PositionQueryDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 직책명"),
            @ApiResponse(responseCode = "404", description = "직책을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<CustomApiResponse<PositionQueryDTO>> getPositionByName(@PathVariable String name) {
        log.debug("이름으로 직책 조회 API 호출. name: {}", name);
        CustomApiResponse<PositionQueryDTO> response = positionQueryService.findByName(name);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "권한 정보 포함 직책 조회",
            description = "권한 정보가 포함된 직책 목록을 조회합니다"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PositionQueryDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/with-role")
    public ResponseEntity<CustomApiResponse<List<PositionQueryDTO>>> getPositionsWithRoleInfo() {
        log.debug("권한 정보 포함 직책 조회 API 호출");
        CustomApiResponse<List<PositionQueryDTO>> response = positionQueryService.findWithRoleInfo();
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "사원 수 정보 포함 직책 조회",
            description = "각 직책에 배정된 사원 수 정보가 포함된 직책 목록을 조회합니다"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PositionQueryDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/with-member-count")
    public ResponseEntity<CustomApiResponse<List<PositionQueryDTO>>> getPositionsWithMemberCount() {
        log.debug("사원 수 정보 포함 직책 조회 API 호출");
        CustomApiResponse<List<PositionQueryDTO>> response = positionQueryService.findWithMemberCount();
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "상세 정보 포함 직책 조회",
            description = "권한 정보와 사원 수 정보를 모두 포함한 상세 직책 목록을 조회합니다"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PositionQueryDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/with-full-details")
    public ResponseEntity<CustomApiResponse<List<PositionQueryDTO>>> getPositionsWithFullDetails() {
        log.debug("상세 정보 포함 직책 조회 API 호출");
        CustomApiResponse<List<PositionQueryDTO>> response = positionQueryService.findWithFullDetails();
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "특정 권한을 가진 직책 조회",
            description = "지정된 권한 ID를 가진 직책들을 조회합니다"
    )
    @Parameter(
            name = "roleId",
            description = "권한 ID (양의 정수)",
            required = true,
            example = "1"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PositionQueryDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 권한 ID"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/role/{roleId}")
    public ResponseEntity<CustomApiResponse<List<PositionQueryDTO>>> getPositionsByRoleId(@PathVariable Integer roleId) {
        log.debug("권한 ID별 직책 조회 API 호출. roleId: {}", roleId);
        CustomApiResponse<List<PositionQueryDTO>> response = positionQueryService.findByRoleId(roleId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "직책명 검색",
            description = "키워드를 포함하는 직책명을 가진 직책들을 검색합니다 (부분 일치)"
    )
    @Parameter(
            name = "keyword",
            description = "검색할 키워드 (직책명의 일부)",
            required = true,
            example = "매니저"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "검색 성공",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PositionQueryDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 검색 키워드"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/search")
    public ResponseEntity<CustomApiResponse<List<PositionQueryDTO>>> searchPositionsByName(
            @RequestParam String keyword) {
        log.debug("키워드로 직책 검색 API 호출. keyword: {}", keyword);
        CustomApiResponse<List<PositionQueryDTO>> response = positionQueryService.findByNameContaining(keyword);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "설명이 있는 직책 조회",
            description = "설명이 등록된 직책들만 조회합니다"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PositionQueryDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/with-description")
    public ResponseEntity<CustomApiResponse<List<PositionQueryDTO>>> getPositionsWithDescription() {
        log.debug("설명이 있는 직책 조회 API 호출");
        CustomApiResponse<List<PositionQueryDTO>> response = positionQueryService.findPositionsWithDescription();
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "설명이 없는 직책 조회",
            description = "설명이 등록되지 않은 직책들만 조회합니다"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PositionQueryDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/without-description")
    public ResponseEntity<CustomApiResponse<List<PositionQueryDTO>>> getPositionsWithoutDescription() {
        log.debug("설명이 없는 직책 조회 API 호출");
        CustomApiResponse<List<PositionQueryDTO>> response = positionQueryService.findPositionsWithoutDescription();
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "사원이 있는 직책 조회",
            description = "현재 사원이 배정된 직책들만 조회합니다"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PositionQueryDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/with-members")
    public ResponseEntity<CustomApiResponse<List<PositionQueryDTO>>> getPositionsWithMembers() {
        log.debug("사원이 있는 직책 조회 API 호출");
        CustomApiResponse<List<PositionQueryDTO>> response = positionQueryService.findPositionsWithMembers();
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "사원이 없는 직책 조회",
            description = "현재 사원이 배정되지 않은 직책들만 조회합니다"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PositionQueryDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/without-members")
    public ResponseEntity<CustomApiResponse<List<PositionQueryDTO>>> getPositionsWithoutMembers() {
        log.debug("사원이 없는 직책 조회 API 호출");
        CustomApiResponse<List<PositionQueryDTO>> response = positionQueryService.findPositionsWithoutMembers();
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "권한이 연결된 직책 조회",
            description = "권한이 설정된 직책들만 조회합니다"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PositionQueryDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/linked-with-role")
    public ResponseEntity<CustomApiResponse<List<PositionQueryDTO>>> getPositionsLinkedWithRole() {
        log.debug("권한이 연결된 직책 조회 API 호출");
        CustomApiResponse<List<PositionQueryDTO>> response = positionQueryService.findPositionsWithRole();
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "권한이 연결되지 않은 직책 조회",
            description = "권한이 설정되지 않은 직책들만 조회합니다"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PositionQueryDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/not-linked-with-role")
    public ResponseEntity<CustomApiResponse<List<PositionQueryDTO>>> getPositionsNotLinkedWithRole() {
        log.debug("권한이 연결되지 않은 직책 조회 API 호출");
        CustomApiResponse<List<PositionQueryDTO>> response = positionQueryService.findPositionsWithoutRole();
        return ResponseEntity.ok(response);
    }
}