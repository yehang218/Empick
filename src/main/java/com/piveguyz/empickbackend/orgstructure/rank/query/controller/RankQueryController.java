package com.piveguyz.empickbackend.orgstructure.rank.query.controller;

import com.piveguyz.empickbackend.common.enums.IsActive;
import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.orgstructure.rank.query.dto.RankQueryDTO;
import com.piveguyz.empickbackend.orgstructure.rank.query.service.RankQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 직급 조회 컨트롤러
 * 직급 관련 조회 API를 제공하는 REST 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/ranks")
@RequiredArgsConstructor
@Tag(name = "직급 API", description = "직급 관리")
public class RankQueryController {

    private final RankQueryService rankQueryService;

    /**
     * 모든 직급 조회
     */
    @GetMapping
    @Operation(
            summary = "전체 직급 조회",
            description = """
        - 시스템에 등록된 모든 직급을 조회합니다.
        """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    public ResponseEntity<CustomApiResponse<List<RankQueryDTO>>> getAllRanks() {
        log.info("직급 전체 조회 요청");

        CustomApiResponse<List<RankQueryDTO>> response = rankQueryService.findAll();

        log.info("직급 전체 조회 완료 - 조회된 직급 수: {}",
                response.getData() != null ? response.getData().size() : 0);

        return ResponseEntity.ok(response);
    }

    /**
     * 활성 직급만 조회
     */
    @GetMapping("/active")
    @Operation(
            summary = "활성 직급 조회",
            description = """
        - 현재 활성 상태인 직급만 조회합니다.
        """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    public ResponseEntity<CustomApiResponse<List<RankQueryDTO>>> getActiveRanks() {
        log.info("활성 직급 조회 요청");

        CustomApiResponse<List<RankQueryDTO>> response = rankQueryService.findActiveRanks();

        log.info("활성 직급 조회 완료 - 조회된 직급 수: {}",
                response.getData() != null ? response.getData().size() : 0);

        return ResponseEntity.ok(response);
    }

    /**
     * 비활성 직급만 조회
     */
    @GetMapping("/inactive")
    @Operation(
            summary = "비활성 직급 조회",
            description = """
        - 현재 비활성 상태인 직급만 조회합니다.
        """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    public ResponseEntity<CustomApiResponse<List<RankQueryDTO>>> getInactiveRanks() {
        log.info("비활성 직급 조회 요청");

        CustomApiResponse<List<RankQueryDTO>> response = rankQueryService.findInactiveRanks();

        log.info("비활성 직급 조회 완료 - 조회된 직급 수: {}",
                response.getData() != null ? response.getData().size() : 0);

        return ResponseEntity.ok(response);
    }

    /**
     * 활성 여부별 직급 조회
     */
    @GetMapping("/status/{isActive}")
    @Operation(
            summary = "활성 여부별 직급 조회",
            description = """
        - 지정된 활성 여부에 따라 직급을 조회합니다.
        """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    public ResponseEntity<CustomApiResponse<List<RankQueryDTO>>> getRanksByStatus(
            @Parameter(description = "활성 여부 (ACTIVE/INACTIVE)", required = true)
            @PathVariable IsActive isActive) {

        log.info("활성 여부별 직급 조회 요청 - isActive: {}", isActive);

        CustomApiResponse<List<RankQueryDTO>> response = rankQueryService.findByIsActive(isActive);

        log.info("활성 여부별 직급 조회 완료 - isActive: {}, 조회된 직급 수: {}",
                isActive, response.getData() != null ? response.getData().size() : 0);

        return ResponseEntity.ok(response);
    }

    /**
     * ID로 직급 단건 조회
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "직급 단건 조회",
            description = """
        - id에 해당하는 직급의 정보를 조회합니다.
        """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "404", description = "요청한 직급을 찾을 수 없습니다.")
    })
    public ResponseEntity<CustomApiResponse<RankQueryDTO>> getRankById(
            @Parameter(description = "직급 ID", required = true)
            @PathVariable Integer id) {

        log.info("직급 ID로 조회 요청 - id: {}", id);

        CustomApiResponse<RankQueryDTO> response = rankQueryService.findById(id);

        log.info("직급 ID로 조회 완료 - id: {}, 조회 결과: {}",
                id, response.getData() != null ? "성공" : "없음");

        return ResponseEntity.ok(response);
    }

    /**
     * 코드로 직급 단건 조회
     */
    @GetMapping("/code/{code}")
    @Operation(
            summary = "직급 코드로 조회",
            description = """
        - code에 해당하는 직급의 정보를 조회합니다.
        """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "404", description = "요청한 직급을 찾을 수 없습니다.")
    })
    public ResponseEntity<CustomApiResponse<RankQueryDTO>> getRankByCode(
            @Parameter(description = "직급 코드", required = true)
            @PathVariable String code) {

        log.info("직급 코드로 조회 요청 - code: {}", code);

        CustomApiResponse<RankQueryDTO> response = rankQueryService.findByCode(code);

        log.info("직급 코드로 조회 완료 - code: {}, 조회 결과: {}",
                code, response.getData() != null ? "성공" : "없음");

        return ResponseEntity.ok(response);
    }

    /**
     * 권한 정보를 포함한 직급 조회
     */
    @GetMapping("/with-role")
    @Operation(
            summary = "권한 정보 포함 직급 조회",
            description = """
        - 직급과 연결된 권한 정보를 함께 조회합니다.
        """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    public ResponseEntity<CustomApiResponse<List<RankQueryDTO>>> getRanksWithRole() {
        log.info("권한 정보 포함 직급 조회 요청");

        CustomApiResponse<List<RankQueryDTO>> response = rankQueryService.findWithRoleInfo();

        log.info("권한 정보 포함 직급 조회 완료 - 조회된 직급 수: {}",
                response.getData() != null ? response.getData().size() : 0);

        return ResponseEntity.ok(response);
    }

    /**
     * 사원 수 정보를 포함한 직급 조회
     */
    @GetMapping("/with-member-count")
    @Operation(
            summary = "사원 수 포함 직급 조회",
            description = """
        - 각 직급에 속한 사원 수 정보를 함께 조회합니다.
        """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    public ResponseEntity<CustomApiResponse<List<RankQueryDTO>>> getRanksWithMemberCount() {
        log.info("사원 수 포함 직급 조회 요청");

        CustomApiResponse<List<RankQueryDTO>> response = rankQueryService.findWithMemberCount();

        log.info("사원 수 포함 직급 조회 완료 - 조회된 직급 수: {}",
                response.getData() != null ? response.getData().size() : 0);

        return ResponseEntity.ok(response);
    }

    /**
     * 상세 정보를 모두 포함한 직급 조회
     */
    @GetMapping("/details")
    @Operation(
            summary = "직급 상세 정보 조회",
            description = """
        - 직급, 권한, 사원 수 등 모든 상세 정보를 함께 조회합니다.
        """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    public ResponseEntity<CustomApiResponse<List<RankQueryDTO>>> getRanksWithFullDetails() {
        log.info("직급 상세 정보 조회 요청");

        CustomApiResponse<List<RankQueryDTO>> response = rankQueryService.findWithFullDetails();

        log.info("직급 상세 정보 조회 완료 - 조회된 직급 수: {}",
                response.getData() != null ? response.getData().size() : 0);

        return ResponseEntity.ok(response);
    }

    /**
     * 특정 권한을 가진 직급 조회
     */
    @GetMapping("/role/{roleId}")
    @Operation(
            summary = "권한별 직급 조회",
            description = """
        - 특정 권한을 가진 직급들을 조회합니다.
        """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "404", description = "해당 권한의 직급을 찾을 수 없습니다.")
    })
    public ResponseEntity<CustomApiResponse<List<RankQueryDTO>>> getRanksByRole(
            @Parameter(description = "권한 ID", required = true)
            @PathVariable Integer roleId) {

        log.info("권한별 직급 조회 요청 - roleId: {}", roleId);

        CustomApiResponse<List<RankQueryDTO>> response = rankQueryService.findByRoleId(roleId);

        log.info("권한별 직급 조회 완료 - roleId: {}, 조회된 직급 수: {}",
                roleId, response.getData() != null ? response.getData().size() : 0);

        return ResponseEntity.ok(response);
    }

    /**
     * 급여 밴드 범위별 직급 조회
     */
    @GetMapping("/salary-band")
    @Operation(
            summary = "급여 밴드 범위별 직급 조회",
            description = """
        - 지정된 급여 밴드 범위에 해당하는 직급들을 조회합니다.
        """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    public ResponseEntity<CustomApiResponse<List<RankQueryDTO>>> getRanksBySalaryBand(
            @Parameter(description = "최소 급여 밴드")
            @RequestParam(required = false) Integer minSalaryBand,
            @Parameter(description = "최대 급여 밴드")
            @RequestParam(required = false) Integer maxSalaryBand) {

        log.info("급여 밴드 범위별 직급 조회 요청 - min: {}, max: {}", minSalaryBand, maxSalaryBand);

        CustomApiResponse<List<RankQueryDTO>> response =
                rankQueryService.findBySalaryBandRange(minSalaryBand, maxSalaryBand);

        log.info("급여 밴드 범위별 직급 조회 완료 - 조회된 직급 수: {}",
                response.getData() != null ? response.getData().size() : 0);

        return ResponseEntity.ok(response);
    }

    /**
     * 사원이 있는 직급만 조회
     */
    @GetMapping("/with-members")
    @Operation(
            summary = "사원 보유 직급 조회",
            description = """
        - 현재 사원이 배정되어 있는 직급들만 조회합니다.
        """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    public ResponseEntity<CustomApiResponse<List<RankQueryDTO>>> getRanksWithMembers() {
        log.info("사원 보유 직급 조회 요청");

        CustomApiResponse<List<RankQueryDTO>> response = rankQueryService.findRanksWithMembers();

        log.info("사원 보유 직급 조회 완료 - 조회된 직급 수: {}",
                response.getData() != null ? response.getData().size() : 0);

        return ResponseEntity.ok(response);
    }

    /**
     * 사원이 없는 직급만 조회
     */
    @GetMapping("/without-members")
    @Operation(
            summary = "사원 미보유 직급 조회",
            description = """
        - 현재 사원이 배정되지 않은 직급들만 조회합니다.
        """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    public ResponseEntity<CustomApiResponse<List<RankQueryDTO>>> getRanksWithoutMembers() {
        log.info("사원 미보유 직급 조회 요청");

        CustomApiResponse<List<RankQueryDTO>> response = rankQueryService.findRanksWithoutMembers();

        log.info("사원 미보유 직급 조회 완료 - 조회된 직급 수: {}",
                response.getData() != null ? response.getData().size() : 0);

        return ResponseEntity.ok(response);
    }
}