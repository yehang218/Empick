
package com.piveguyz.empickbackend.orgstructure.position.command.application.controller;

import com.piveguyz.empickbackend.common.enums.IsActive;
import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.orgstructure.position.command.application.dto.PositionCommandDTO;
import com.piveguyz.empickbackend.orgstructure.position.command.application.service.PositionCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 직책 명령 컨트롤러
 * 직책 생성, 수정, 삭제 등의 명령 처리를 담당하는 REST API 컨트롤러
 */
@Tag(name = "직책 API", description = "직책 생성/수정/삭제/권한관리 API")
@Slf4j
@RestController
@RequestMapping("/api/v1/positions")
@RequiredArgsConstructor
public class PositionCommandController {

    private final PositionCommandService positionCommandService;

    /**
     * 직책 생성
     *
     * @param positionCommandDTO 직책 생성 요청 정보
     * @return 생성된 직책 정보
     */
    @Operation(
            summary = "직책 생성",
            description = "새로운 직책을 생성합니다.",
            requestBody = @RequestBody(
                    description = "직책 생성 요청 데이터",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PositionCommandDTO.class),
                            examples = @ExampleObject(
                                    name = "직책 생성 예시",
                                    summary = "팀장 직책 생성",
                                    value = """
                                    {
                                        "name": "팀장",
                                        "code": "TEAM_LEADER",
                                        "description": "팀을 이끄는 리더 역할을 담당합니다.",
                                        "isActive": 1,
                                        "roleId": 2
                                    }
                                    """
                            )
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "직책 생성 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomApiResponse.class),
                            examples = @ExampleObject(
                                    name = "생성 성공 응답",
                                    value = """
                                    {
                                        "code": "SUCCESS",
                                        "message": "요청이 성공적으로 처리되었습니다.",
                                        "data": {
                                            "name": "팀장",
                                            "code": "TEAM_LEADER",
                                            "description": "팀을 이끄는 리더 역할을 담당합니다.",
                                            "isActive": 1,
                                            "roleId": 2
                                        }
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 데이터",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "유효성 검증 실패",
                                    value = """
                                    {
                                        "code": "INVALID_INPUT",
                                        "message": "직책명은 필수입니다."
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "중복된 직책명 또는 코드",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "중복 오류",
                                    value = """
                                    {
                                        "code": "DUPLICATE_RESOURCE",
                                        "message": "이미 존재하는 직책명입니다: 팀장"
                                    }
                                    """
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<CustomApiResponse<PositionCommandDTO>> createPosition(
            @org.springframework.web.bind.annotation.RequestBody @Valid PositionCommandDTO positionCommandDTO) {

        log.info("직책 생성 요청: {}", positionCommandDTO);

        try {
            PositionCommandDTO createdPosition = positionCommandService.createPosition(positionCommandDTO);

            log.info("직책 생성 성공: {}", createdPosition);
            return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
                    .body(CustomApiResponse.of(ResponseCode.SUCCESS, createdPosition));

        } catch (IllegalArgumentException e) {
            log.warn("직책 생성 실패 - 유효성 검증 오류: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("직책 생성 중 예기치 않은 오류 발생", e);
            throw new RuntimeException("직책 생성에 실패했습니다.", e);
        }
    }

    /**
     * 직책 수정
     *
     * @param id 수정할 직책 ID
     * @param positionCommandDTO 직책 수정 요청 정보
     * @return 수정된 직책 정보
     */
    @Operation(
            summary = "직책 수정",
            description = "기존 직책 정보를 수정합니다. 부분 업데이트를 지원합니다.",
            requestBody = @RequestBody(
                    description = "직책 수정 요청 데이터 (부분 업데이트 가능)",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PositionCommandDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "전체 수정 예시",
                                            summary = "모든 필드 수정",
                                            value = """
                                            {
                                                "name": "선임 팀장",
                                                "code": "SENIOR_TEAM_LEADER",
                                                "description": "경험이 풍부한 선임 팀장입니다.",
                                                "isActive": 1,
                                                "roleId": 3
                                            }
                                            """
                                    ),
                                    @ExampleObject(
                                            name = "부분 수정 예시",
                                            summary = "이름과 설명만 수정",
                                            value = """
                                            {
                                                "name": "부팀장",
                                                "description": "팀장을 보좌하는 부팀장 역할입니다."
                                            }
                                            """
                                    )
                            }
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "직책 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "404", description = "직책을 찾을 수 없음"),
            @ApiResponse(responseCode = "409", description = "중복된 직책명 또는 코드")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CustomApiResponse<PositionCommandDTO>> updatePosition(
            @Parameter(description = "수정할 직책 ID", required = true, example = "1")
            @PathVariable int id,
            @org.springframework.web.bind.annotation.RequestBody @Valid PositionCommandDTO positionCommandDTO) {

        log.info("직책 수정 요청: ID={}, Data={}", id, positionCommandDTO);

        try {
            PositionCommandDTO updatedPosition = positionCommandService.updatePosition(id, positionCommandDTO);

            log.info("직책 수정 성공: ID={}", id);
            return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
                    .body(CustomApiResponse.of(ResponseCode.SUCCESS, updatedPosition));

        } catch (IllegalArgumentException e) {
            log.warn("직책 수정 실패 - 유효성 검증 오류: ID={}, Error={}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("직책 수정 중 예기치 않은 오류 발생: ID={}", id, e);
            throw new RuntimeException("직책 수정에 실패했습니다.", e);
        }
    }

    /**
     * 직책 활성 상태 변경
     *
     * @param id 직책 ID
     * @param isActive 변경할 활성 상태
     * @return 변경된 직책 ID
     */
    @Operation(
            summary = "직책 활성/비활성 상태 변경",
            description = "지정한 직책의 활성 상태를 변경합니다.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "직책 ID",
                            required = true,
                            example = "1"
                    ),
                    @Parameter(
                            name = "isActive",
                            description = "활성 상태 (ACTIVE: 활성, INACTIVE: 비활성)",
                            required = true,
                            example = "ACTIVE",
                            schema = @Schema(implementation = IsActive.class)
                    )
            }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "상태 변경 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "상태 변경 성공",
                                    value = """
                                    {
                                        "code": "SUCCESS",
                                        "message": "요청이 성공적으로 처리되었습니다.",
                                        "data": 1
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "404", description = "직책을 찾을 수 없음")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<CustomApiResponse<Integer>> updatePositionActiveStatus(
            @PathVariable int id,
            @RequestParam("isActive") IsActive isActive) {

        log.info("직책 활성 상태 변경 요청: ID={}, IsActive={}", id, isActive);

        try {
            Integer updatedId = positionCommandService.updatePositionActiveStatus(id, isActive);

            log.info("직책 활성 상태 변경 성공: ID={}, IsActive={}", id, isActive);
            return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
                    .body(CustomApiResponse.of(ResponseCode.SUCCESS, updatedId));

        } catch (IllegalArgumentException e) {
            log.warn("직책 활성 상태 변경 실패: ID={}, IsActive={}, Error={}", id, isActive, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("직책 활성 상태 변경 중 예기치 않은 오류 발생: ID={}, IsActive={}", id, isActive, e);
            throw new RuntimeException("직책 활성 상태 변경에 실패했습니다.", e);
        }
    }

    /**
     * 직책 삭제
     *
     * @param id 삭제할 직책 ID
     * @return 삭제 완료 응답
     */
    @Operation(
            summary = "직책 삭제",
            description = "직책을 삭제합니다. (물리 삭제) - 비활성 상태인 직책만 삭제 가능합니다.",
            parameters = @Parameter(
                    name = "id",
                    description = "삭제할 직책 ID",
                    required = true,
                    example = "1"
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "직책 삭제 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "삭제 성공",
                                    value = """
                                    {
                                        "code": "SUCCESS",
                                        "message": "요청이 성공적으로 처리되었습니다.",
                                        "data": null
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "삭제할 수 없는 상태",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "삭제 불가",
                                    value = """
                                    {
                                        "code": "INVALID_OPERATION",
                                        "message": "활성 상태인 직책은 삭제할 수 없습니다. 먼저 비활성화해주세요."
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(responseCode = "404", description = "직책을 찾을 수 없음")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomApiResponse<Void>> deletePosition(
            @PathVariable int id) {

        log.info("직책 삭제 요청: ID={}", id);

        try {
            positionCommandService.deletePosition(id);

            log.info("직책 삭제 성공: ID={}", id);
            return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
                    .body(CustomApiResponse.of(ResponseCode.SUCCESS, null));

        } catch (IllegalArgumentException e) {
            log.warn("직책 삭제 실패: ID={}, Error={}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("직책 삭제 중 예기치 않은 오류 발생: ID={}", id, e);
            throw new RuntimeException("직책 삭제에 실패했습니다.", e);
        }
    }

    /**
     * 직책에 권한 연결
     *
     * @param id 직책 ID
     * @param roleId 연결할 권한 ID
     * @return 수정된 직책 정보
     */
    @Operation(
            summary = "직책 권한 연결",
            description = "직책에 권한을 연결합니다.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "직책 ID",
                            required = true,
                            example = "1"
                    ),
                    @Parameter(
                            name = "roleId",
                            description = "연결할 권한 ID",
                            required = true,
                            example = "2"
                    )
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "권한 연결 성공"),
            @ApiResponse(responseCode = "404", description = "직책을 찾을 수 없음")
    })
    @PatchMapping("/{id}/role")
    public ResponseEntity<CustomApiResponse<PositionCommandDTO>> connectRole(
            @PathVariable int id,
            @RequestParam("roleId") Integer roleId) {

        log.info("직책 권한 연결 요청: PositionID={}, RoleID={}", id, roleId);

        try {
            PositionCommandDTO updatedPosition = positionCommandService.connectRole(id, roleId);

            log.info("직책 권한 연결 성공: PositionID={}, RoleID={}", id, roleId);
            return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
                    .body(CustomApiResponse.of(ResponseCode.SUCCESS, updatedPosition));

        } catch (IllegalArgumentException e) {
            log.warn("직책 권한 연결 실패: PositionID={}, RoleID={}, Error={}", id, roleId, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("직책 권한 연결 중 예기치 않은 오류 발생: PositionID={}, RoleID={}", id, roleId, e);
            throw new RuntimeException("직책 권한 연결에 실패했습니다.", e);
        }
    }

    /**
     * 직책에서 권한 연결 해제
     *
     * @param id 직책 ID
     * @return 수정된 직책 정보
     */
    @Operation(
            summary = "직책 권한 해제",
            description = "직책에서 권한 연결을 해제합니다.",
            parameters = @Parameter(
                    name = "id",
                    description = "직책 ID",
                    required = true,
                    example = "1"
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "권한 해제 성공"),
            @ApiResponse(responseCode = "404", description = "직책을 찾을 수 없음")
    })
    @DeleteMapping("/{id}/role")
    public ResponseEntity<CustomApiResponse<PositionCommandDTO>> disconnectRole(
            @PathVariable int id) {

        log.info("직책 권한 해제 요청: PositionID={}", id);

        try {
            PositionCommandDTO updatedPosition = positionCommandService.disconnectRole(id);

            log.info("직책 권한 해제 성공: PositionID={}", id);
            return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
                    .body(CustomApiResponse.of(ResponseCode.SUCCESS, updatedPosition));

        } catch (IllegalArgumentException e) {
            log.warn("직책 권한 해제 실패: PositionID={}, Error={}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("직책 권한 해제 중 예기치 않은 오류 발생: PositionID={}", id, e);
            throw new RuntimeException("직책 권한 해제에 실패했습니다.", e);
        }
    }

    /**
     * 특정 권한을 사용하는 모든 직책 비활성화
     *
     * @param roleId 권한 ID
     * @return 비활성화된 직책 수
     */
    @Operation(
            summary = "권한별 직책 일괄 비활성화",
            description = "특정 권한을 사용하는 모든 직책을 비활성화합니다.",
            parameters = @Parameter(
                    name = "roleId",
                    description = "권한 ID",
                    required = true,
                    example = "2"
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "일괄 비활성화 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "일괄 비활성화 성공",
                                    value = """
                                    {
                                        "code": "SUCCESS",
                                        "message": "요청이 성공적으로 처리되었습니다.",
                                        "data": 3
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 권한 ID")
    })
    @PatchMapping("/bulk/deactivate-by-role")
    public ResponseEntity<CustomApiResponse<Integer>> deactivatePositionsByRole(
            @RequestParam("roleId") Integer roleId) {

        log.info("권한별 직책 일괄 비활성화 요청: RoleID={}", roleId);

        try {
            int deactivatedCount = positionCommandService.deactivatePositionsByRole(roleId);

            log.info("권한별 직책 일괄 비활성화 성공: RoleID={}, Count={}", roleId, deactivatedCount);
            return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
                    .body(CustomApiResponse.of(ResponseCode.SUCCESS, deactivatedCount));

        } catch (Exception e) {
            log.error("권한별 직책 일괄 비활성화 중 예기치 않은 오류 발생: RoleID={}", roleId, e);
            throw new RuntimeException("권한별 직책 일괄 비활성화에 실패했습니다.", e);
        }
    }

    /**
     * 특정 권한을 사용하는 모든 직책에서 권한 연결 해제
     *
     * @param roleId 권한 ID
     * @return 권한이 해제된 직책 수
     */
    @Operation(
            summary = "권한별 직책 일괄 해제",
            description = "특정 권한을 사용하는 모든 직책에서 권한 연결을 해제합니다.",
            parameters = @Parameter(
                    name = "roleId",
                    description = "권한 ID",
                    required = true,
                    example = "2"
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "일괄 해제 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "일괄 해제 성공",
                                    value = """
                                    {
                                        "code": "SUCCESS",
                                        "message": "요청이 성공적으로 처리되었습니다.",
                                        "data": 5
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 권한 ID")
    })
    @PatchMapping("/bulk/disconnect-role")
    public ResponseEntity<CustomApiResponse<Integer>> disconnectRoleFromAllPositions(
            @RequestParam("roleId") Integer roleId) {

        log.info("전체 직책에서 권한 연결 해제 요청: RoleID={}", roleId);

        try {
            int disconnectedCount = positionCommandService.disconnectRoleFromAllPositions(roleId);

            log.info("전체 직책에서 권한 연결 해제 성공: RoleID={}, Count={}", roleId, disconnectedCount);
            return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
                    .body(CustomApiResponse.of(ResponseCode.SUCCESS, disconnectedCount));

        } catch (Exception e) {
            log.error("전체 직책에서 권한 연결 해제 중 예기치 않은 오류 발생: RoleID={}", roleId, e);
            throw new RuntimeException("전체 직책에서 권한 연결 해제에 실패했습니다.", e);
        }
    }
}