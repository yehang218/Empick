package com.piveguyz.empickbackend.orgstructure.attendance.query.controller;

import com.piveguyz.empickbackend.auth.facade.AuthFacade;
import com.piveguyz.empickbackend.common.constants.ApiExamples;
import com.piveguyz.empickbackend.common.constants.RoleCode;
import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.orgstructure.attendance.query.dto.AttendanceRecordResponseQueryDTO;
import com.piveguyz.empickbackend.orgstructure.attendance.query.service.AttendanceRecordResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/attendance")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "근태 API", description = "근태 기록 조회 관련 API")
public class AttendanceRecordQueryController {

    private final AttendanceRecordResponseService attendanceRecordResponseService;
    private final AuthFacade authFacade;

    // ========== 관리자용 API ==========

    @Operation(
            summary = "모든 근태 기록 조회",
            description = """
            - 시스템에 등록된 모든 근태 기록을 조회합니다. (관리자용)
            - 회원명, 근태 카테고리 라벨이 포함된 상세 정보로 응답합니다.
            - 인사팀 권한(ROLE_HR_ACCESS)이 필요합니다.
            """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AttendanceRecordResponseQueryDTO.class)))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 필요",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_401_EXAMPLE))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_500_EXAMPLE))
            )
    })
    @GetMapping("/records")
    public ResponseEntity<CustomApiResponse<List<AttendanceRecordResponseQueryDTO>>> getAllAttendanceRecords() {
        // 권한 체크: 인사팀 권한 필요
        authFacade.checkHasRole(RoleCode.ROLE_HR_ACCESS);

        log.info("모든 근태 기록 조회 요청");

        List<AttendanceRecordResponseQueryDTO> records = attendanceRecordResponseService.getAllAttendanceRecordsWithDetails();

        log.info("근태 기록 조회 완료 - 총 {}개", records.size());
        return ResponseEntity.ok(CustomApiResponse.of(ResponseCode.SUCCESS, records));
    }

    @Operation(
            summary = "근태 기록 상세 조회",
            description = """
            - 특정 ID의 근태 기록 상세 정보를 조회합니다.
            - 회원명, 근태 카테고리 라벨이 포함된 상세 정보로 응답합니다.
            """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = AttendanceRecordResponseQueryDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_400_EXAMPLE))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "근태 기록을 찾을 수 없음",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_404_EXAMPLE))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_500_EXAMPLE))
            )
    })
    @GetMapping("/records/{id}")
    public ResponseEntity<CustomApiResponse<AttendanceRecordResponseQueryDTO>> getAttendanceRecordById(
            @Parameter(description = "근태 기록 ID", required = true, example = "1")
            @PathVariable int id
    ) {
        log.info("근태 기록 상세 조회 요청 - ID: {}", id);

        AttendanceRecordResponseQueryDTO record = attendanceRecordResponseService.getAttendanceRecordByIdWithDetails(id);

        log.info("근태 기록 상세 조회 완료 - ID: {}, MemberName: {}", id, record.getMemberName());
        return ResponseEntity.ok(CustomApiResponse.of(ResponseCode.SUCCESS, record));
    }

    @Operation(
            summary = "회원별 근태 기록 조회",
            description = """
            - 특정 회원의 근태 기록을 조회합니다. (관리자용)
            - 회원명, 근태 카테고리 라벨이 포함된 상세 정보로 응답합니다.
            - 인사팀 권한(ROLE_HR_ACCESS)이 필요합니다.
            """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AttendanceRecordResponseQueryDTO.class)))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_400_EXAMPLE))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_500_EXAMPLE))
            )
    })
    @GetMapping("/records/member/{memberId}")
    public ResponseEntity<CustomApiResponse<List<AttendanceRecordResponseQueryDTO>>> getAttendanceRecordsByMemberId(
            @Parameter(description = "회원 ID", required = true, example = "1")
            @PathVariable int memberId
    ) {
        // 권한 체크: 인사팀 권한 필요
        authFacade.checkHasRole(RoleCode.ROLE_HR_ACCESS);

        log.info("회원별 근태 기록 조회 요청 - MemberID: {}", memberId);

        List<AttendanceRecordResponseQueryDTO> records = attendanceRecordResponseService.getAttendanceRecordsByMemberIdWithDetails(memberId);

        log.info("회원별 근태 기록 조회 완료 - MemberID: {}, 총 {}개", memberId, records.size());
        return ResponseEntity.ok(CustomApiResponse.of(ResponseCode.SUCCESS, records));
    }

    @Operation(
            summary = "날짜 범위별 근태 기록 조회",
            description = """
            - 특정 기간의 근태 기록을 조회합니다. (관리자용)
            - 날짜 형식: yyyy-MM-ddTHH:mm:ss (예: 2025-06-01T00:00:00)
            - 인사팀 권한(ROLE_HR_ACCESS)이 필요합니다.
            """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AttendanceRecordResponseQueryDTO.class)))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_400_EXAMPLE))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_500_EXAMPLE))
            )
    })
    @GetMapping("/records/date-range")
    public ResponseEntity<CustomApiResponse<List<AttendanceRecordResponseQueryDTO>>> getAttendanceRecordsByDateRange(
            @Parameter(description = "시작 날짜", required = true, example = "2025-06-01T00:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "종료 날짜", required = true, example = "2025-07-31T23:59:59")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        // 권한 체크: 인사팀 권한 필요
        authFacade.checkHasRole(RoleCode.ROLE_HR_ACCESS);

        log.info("날짜 범위별 근태 기록 조회 요청 - Start: {}, End: {}", startDate, endDate);

        List<AttendanceRecordResponseQueryDTO> records = attendanceRecordResponseService.getAttendanceRecordsByDateRangeWithDetails(startDate, endDate);

        log.info("날짜 범위별 근태 기록 조회 완료 - 기간: {} ~ {}, 총 {}개", startDate, endDate, records.size());
        return ResponseEntity.ok(CustomApiResponse.of(ResponseCode.SUCCESS, records));
    }

    // ========== 로그인된 사용자용 API ==========

    @Operation(
            summary = "내 모든 근태 기록 조회",
            description = """
            - 로그인된 사용자의 모든 근태 기록을 조회합니다.
            - JWT 토큰에서 사용자 정보를 자동으로 추출합니다.
            - 더미 데이터: 2025-06-01 ~ 2025-06-10 기간의 근태 기록
            """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AttendanceRecordResponseQueryDTO.class)))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 필요 (JWT 토큰 필요)",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_401_EXAMPLE))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_500_EXAMPLE))
            )
    })
    @GetMapping("/my")
    public ResponseEntity<CustomApiResponse<List<AttendanceRecordResponseQueryDTO>>> getMyAttendanceRecords() {
        Integer memberId = authFacade.getCurrentMemberId();
        log.info("내 모든 근태 기록 조회 요청 - MemberID: {}", memberId);

        List<AttendanceRecordResponseQueryDTO> records = attendanceRecordResponseService.getMyAttendanceRecords(memberId);

        log.info("내 모든 근태 기록 조회 완료 - MemberID: {}, 총 {}개", memberId, records.size());
        return ResponseEntity.ok(CustomApiResponse.of(ResponseCode.SUCCESS, records));
    }

    @Operation(
            summary = "내 최근 근태 기록 조회",
            description = """
            - 로그인된 사용자의 최근 N개 근태 기록을 조회합니다.
            - JWT 토큰에서 사용자 정보를 자동으로 추출합니다.
            - 더미 데이터: 2025-06-01 ~ 2025-06-10 기간의 근태 기록
            """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AttendanceRecordResponseQueryDTO.class)))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_400_EXAMPLE))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 필요 (JWT 토큰 필요)",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_401_EXAMPLE))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_500_EXAMPLE))
            )
    })
    @GetMapping("/my/recent")
    public ResponseEntity<CustomApiResponse<List<AttendanceRecordResponseQueryDTO>>> getMyRecentAttendanceRecords(
            @Parameter(description = "조회할 개수", required = false, example = "10")
            @RequestParam(defaultValue = "10") int limit
    ) {
        Integer memberId = authFacade.getCurrentMemberId();
        log.info("내 최근 근태 기록 조회 요청 - MemberID: {}, Limit: {}", memberId, limit);

        List<AttendanceRecordResponseQueryDTO> records = attendanceRecordResponseService.getMyRecentAttendanceRecords(memberId, limit);

        log.info("내 최근 근태 기록 조회 완료 - MemberID: {}, Limit: {}, 총 {}개", memberId, limit, records.size());
        return ResponseEntity.ok(CustomApiResponse.of(ResponseCode.SUCCESS, records));
    }

    @Operation(
            summary = "내 특정 기간 근태 기록 조회",
            description = """
            - 로그인된 사용자의 특정 기간 근태 기록을 조회합니다.
            - JWT 토큰에서 사용자 정보를 자동으로 추출합니다.
            - 날짜 형식: yyyy-MM-ddTHH:mm:ss (예: 2025-01-01T00:00:00)
            """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AttendanceRecordResponseQueryDTO.class)))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_400_EXAMPLE))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 필요 (JWT 토큰 필요)",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_401_EXAMPLE))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_500_EXAMPLE))
            )
    })
    @GetMapping("/my/date-range")
    public ResponseEntity<CustomApiResponse<List<AttendanceRecordResponseQueryDTO>>> getMyAttendanceRecordsByDateRange(
            @Parameter(description = "시작 날짜", required = true, example = "2025-01-01T00:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "종료 날짜", required = true, example = "2025-07-29T23:59:59")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        Integer memberId = authFacade.getCurrentMemberId();
        log.info("내 특정 기간 근태 기록 조회 요청 - MemberID: {}, Start: {}, End: {}", memberId, startDate, endDate);

        List<AttendanceRecordResponseQueryDTO> records = attendanceRecordResponseService.getMyAttendanceRecordsByDateRange(memberId, startDate, endDate);

        log.info("내 특정 기간 근태 기록 조회 완료 - MemberID: {}, 기간: {} ~ {}, 총 {}개", memberId, startDate, endDate, records.size());
        return ResponseEntity.ok(CustomApiResponse.of(ResponseCode.SUCCESS, records));
    }
}