
package com.piveguyz.empickbackend.orgstructure.rank.command.application.controller;

import com.piveguyz.empickbackend.common.enums.IsActive;
import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.orgstructure.rank.command.application.dto.RankCommandDTO;
import com.piveguyz.empickbackend.orgstructure.rank.command.application.service.RankCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 직급 Command Controller
 * 직급 생성, 수정, 삭제 등의 명령 처리를 담당하는 REST API 컨트롤러
 */
@Tag(name = "직급 API", description = "직급 생성, 수정, 삭제 등의 명령 처리 API")
@Slf4j
@RestController
@RequestMapping("/api/v1/ranks")
@RequiredArgsConstructor
public class RankCommandController {

    private final RankCommandService rankCommandService;

    /**
     * 직급 생성
     */
    @Operation(
            summary = "직급 생성",
            description = "새로운 직급을 생성합니다. 직급명, 코드, 활성 여부는 필수 입력 항목입니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "직급 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "409", description = "중복된 직급 코드"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping
    public ResponseEntity<CustomApiResponse<RankCommandDTO>> createRank(
            @Parameter(description = "생성할 직급 정보", required = true)
            @Valid @RequestBody RankCommandDTO rankCommandDTO) {

        log.info("직급 생성 API 호출 - name: {}, code: {}", rankCommandDTO.getName(), rankCommandDTO.getCode());

        CustomApiResponse<RankCommandDTO> response = rankCommandService.createRank(rankCommandDTO);

        log.info("직급 생성 API 응답 완료 - success: {}", response.isSuccess());

        return ResponseEntity.ok(response);
    }

    /**
     * 직급 수정
     */
    @Operation(
            summary = "직급 수정",
            description = "기존 직급 정보를 수정합니다. ID로 대상 직급을 식별하고, 요청 본문의 정보로 업데이트합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "직급 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "404", description = "직급을 찾을 수 없음"),
            @ApiResponse(responseCode = "409", description = "중복된 직급 코드"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CustomApiResponse<RankCommandDTO>> updateRank(
            @Parameter(description = "수정할 직급 ID", required = true, example = "1")
            @PathVariable Integer id,
            @Parameter(description = "수정할 직급 정보", required = true)
            @Valid @RequestBody RankCommandDTO rankCommandDTO) {

        log.info("직급 수정 API 호출 - id: {}, name: {}", id, rankCommandDTO.getName());

        CustomApiResponse<RankCommandDTO> response = rankCommandService.updateRank(id, rankCommandDTO);

        log.info("직급 수정 API 응답 완료 - id: {}, success: {}", id, response.isSuccess());

        return ResponseEntity.ok(response);
    }

    /**
     * 직급 삭제
     */
    @Operation(
            summary = "직급 삭제",
            description = "기존 직급을 삭제합니다. 해당 직급을 사용하는 사원이 있는 경우 삭제가 제한될 수 있습니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "직급 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
            @ApiResponse(responseCode = "404", description = "직급을 찾을 수 없음"),
            @ApiResponse(responseCode = "409", description = "삭제 제약 조건 위반"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomApiResponse<Void>> deleteRank(
            @Parameter(description = "삭제할 직급 ID", required = true, example = "1")
            @PathVariable Integer id) {

        log.info("직급 삭제 API 호출 - id: {}", id);

        CustomApiResponse<Void> response = rankCommandService.deleteRank(id);

        log.info("직급 삭제 API 응답 완료 - id: {}, success: {}", id, response.isSuccess());

        return ResponseEntity.ok(response);
    }

    /**
     * 직급 활성 상태 변경
     */
    @Operation(
            summary = "직급 활성 상태 변경",
            description = "기존 직급의 활성 상태를 변경합니다. ACTIVE(활성) 또는 INACTIVE(비활성)로 설정할 수 있습니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "직급 상태 변경 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
            @ApiResponse(responseCode = "404", description = "직급을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<CustomApiResponse<RankCommandDTO>> updateRankStatus(
            @Parameter(description = "대상 직급 ID", required = true, example = "1")
            @PathVariable Integer id,
            @Parameter(description = "변경할 활성 상태", required = true, example = "ACTIVE")
            @RequestParam IsActive isActive) {

        log.info("직급 상태 변경 API 호출 - id: {}, isActive: {}", id, isActive);

        CustomApiResponse<RankCommandDTO> response = rankCommandService.updateRankStatus(id, isActive);

        log.info("직급 상태 변경 API 응답 완료 - id: {}, isActive: {}, success: {}", id, isActive, response.isSuccess());

        return ResponseEntity.ok(response);
    }

    /**
     * 직급 활성화 (편의 메서드)
     */
    @Operation(
            summary = "직급 활성화",
            description = "기존 직급을 활성 상태로 변경합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "직급 활성화 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
            @ApiResponse(responseCode = "404", description = "직급을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PatchMapping("/{id}/activate")
    public ResponseEntity<CustomApiResponse<RankCommandDTO>> activateRank(
            @Parameter(description = "활성화할 직급 ID", required = true, example = "1")
            @PathVariable Integer id) {

        log.info("직급 활성화 API 호출 - id: {}", id);

        CustomApiResponse<RankCommandDTO> response = rankCommandService.updateRankStatus(id, IsActive.ACTIVE);

        log.info("직급 활성화 API 응답 완료 - id: {}, success: {}", id, response.isSuccess());

        return ResponseEntity.ok(response);
    }

    /**
     * 직급 비활성화 (편의 메서드)
     */
    @Operation(
            summary = "직급 비활성화",
            description = "기존 직급을 비활성 상태로 변경합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "직급 비활성화 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
            @ApiResponse(responseCode = "404", description = "직급을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<CustomApiResponse<RankCommandDTO>> deactivateRank(
            @Parameter(description = "비활성화할 직급 ID", required = true, example = "1")
            @PathVariable Integer id) {

        log.info("직급 비활성화 API 호출 - id: {}", id);

        CustomApiResponse<RankCommandDTO> response = rankCommandService.updateRankStatus(id, IsActive.INACTIVE);

        log.info("직급 비활성화 API 응답 완료 - id: {}, success: {}", id, response.isSuccess());

        return ResponseEntity.ok(response);
    }
}