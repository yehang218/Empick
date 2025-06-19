
package com.piveguyz.empickbackend.orgstructure.attendance.command.application.controller;

import com.piveguyz.empickbackend.auth.facade.AuthFacade;
import com.piveguyz.empickbackend.common.constants.ApiExamples;
import com.piveguyz.empickbackend.common.constants.RoleCode;
import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.orgstructure.attendance.command.application.dto.AttendanceRecordCreateRequestDTO;
import com.piveguyz.empickbackend.orgstructure.attendance.command.application.dto.AttendanceRecordCreateResponseDTO;
import com.piveguyz.empickbackend.orgstructure.attendance.command.application.dto.AttendanceRecordUpdateRequestDTO;
import com.piveguyz.empickbackend.orgstructure.attendance.command.application.service.AttendanceRecordCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/attendance-records")
@RequiredArgsConstructor
@Tag(name = "근태 API", description = "근태 기록 생성, 수정, 삭제 API")
public class AttendanceRecordCommandController {

    private final AttendanceRecordCommandService attendanceRecordCommandService;
    private final AuthFacade authFacade;

    @Operation(
            summary = "근태 기록 생성",
            description = """
                    - 새로운 근태 기록을 생성합니다.
                    - 근태 카테고리: 0=출근, 1=퇴근, 2=지각, 3=조퇴
                    - 기록 시각을 지정하지 않으면 현재 시각으로 설정됩니다.
                    - 출근/퇴근의 경우 하루에 한 번만 기록 가능합니다.
                    """,
                    security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "근태 기록 생성 성공",
                    content = @Content(schema = @Schema(implementation = AttendanceRecordCreateResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (중복 기록 등)",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_400_EXAMPLE))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_401_EXAMPLE))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_500_EXAMPLE))
            )
    })
    @PostMapping
    public ResponseEntity<CustomApiResponse<AttendanceRecordCreateResponseDTO>> createAttendanceRecord(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "근태 기록 생성 요청 정보",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AttendanceRecordCreateRequestDTO.class))
            )
            @RequestBody AttendanceRecordCreateRequestDTO requestDTO) {

        log.info("근태 기록 생성 요청 - 카테고리: {}", requestDTO.getAttendanceCategoryId());

        // JWT에서 회원 ID 추출
        Integer memberId = authFacade.getCurrentMemberId();

        // 근태 기록 생성
        AttendanceRecordCreateResponseDTO response = attendanceRecordCommandService.createAttendanceRecord(memberId, requestDTO);

        log.info("근태 기록 생성 완료 - ID: {}, 회원ID: {}", response.getId(), memberId);
        return ResponseEntity.ok(CustomApiResponse.of(ResponseCode.SUCCESS, response));
    }

    @Operation(
            summary = "근태 기록 수정",
            description = """
                    - 기존 근태 기록을 수정합니다.
                    - 본인이 작성한 기록만 수정 가능합니다.
                    - 삭제된 기록은 수정할 수 없습니다.
                    - 수정 시 상태가 '수정됨(1)'으로 변경됩니다.
                    """,
                    security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "근태 기록 수정 성공",
                    content = @Content(schema = @Schema(implementation = AttendanceRecordCreateResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (삭제된 기록 수정 시도 등)",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_400_EXAMPLE))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_401_EXAMPLE))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "권한 없음 (타인의 기록 수정 시도)",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_403_EXAMPLE))
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
    @PutMapping("/{recordId}")
    public ResponseEntity<CustomApiResponse<AttendanceRecordCreateResponseDTO>> updateAttendanceRecord(
            @Parameter(description = "수정할 근태 기록 ID", required = true, example = "1")
            @PathVariable Integer recordId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "근태 기록 수정 요청 정보",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AttendanceRecordUpdateRequestDTO.class))
            )
            @RequestBody AttendanceRecordUpdateRequestDTO requestDTO) {

        log.info("근태 기록 수정 요청 - 기록ID: {}", recordId);

        // JWT에서 회원 ID 추출
        Integer memberId = authFacade.getCurrentMemberId();
        authFacade.checkHasRole(RoleCode.ROLE_HR_ACCESS);

        // 근태 기록 수정
        AttendanceRecordCreateResponseDTO response = attendanceRecordCommandService.updateAttendanceRecord(recordId, memberId, requestDTO);

        log.info("근태 기록 수정 완료 - 기록ID: {}, 회원ID: {}", recordId, memberId);
        return ResponseEntity.ok(CustomApiResponse.of(ResponseCode.SUCCESS, response));
    }

    @Operation(
            summary = "근태 기록 삭제",
            description = """
                    - 근태 기록을 삭제합니다. (소프트 삭제)
                    - 본인이 작성한 기록만 삭제 가능합니다.
                    - 이미 삭제된 기록은 다시 삭제할 수 없습니다.
                    - 실제로 데이터가 삭제되지 않고 deleted_at 필드에 삭제 시각이 기록됩니다.
                    """
                    ,security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "근태 기록 삭제 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (이미 삭제된 기록 삭제 시도 등)",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_400_EXAMPLE))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_401_EXAMPLE))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "권한 없음 (타인의 기록 삭제 시도)",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_403_EXAMPLE))
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
    @DeleteMapping("/{recordId}")
    public ResponseEntity<CustomApiResponse<Void>> deleteAttendanceRecord(
            @Parameter(description = "삭제할 근태 기록 ID", required = true, example = "1")
            @PathVariable Integer recordId) {

        log.info("근태 기록 삭제 요청 - 기록ID: {}", recordId);

        // JWT에서 회원 ID 추출
        Integer memberId = authFacade.getCurrentMemberId();
        authFacade.checkHasRole(RoleCode.ROLE_HR_ACCESS);
        // 근태 기록 삭제
        attendanceRecordCommandService.deleteAttendanceRecord(recordId, memberId);

        log.info("근태 기록 삭제 완료 - 기록ID: {}, 회원ID: {}", recordId, memberId);
        return ResponseEntity.ok(CustomApiResponse.of(ResponseCode.SUCCESS));
    }

    @Operation(
            summary = "오늘 출근 기록 확인",
            description = """
                    - 현재 로그인한 사용자의 오늘 출근 기록 존재 여부를 확인합니다.
                    - 중복 출근 방지를 위해 사용할 수 있습니다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "확인 성공",
                    content = @Content(schema = @Schema(implementation = Boolean.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_401_EXAMPLE))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_500_EXAMPLE))
            )
    })
    @GetMapping("/check-in/today")
    public ResponseEntity<CustomApiResponse<Boolean>> hasCheckInToday() {

        log.info("오늘 출근 기록 확인 요청");

        // JWT에서 회원 ID 추출
        Integer memberId = authFacade.getCurrentMemberId();

        // 오늘 출근 기록 확인
        boolean hasCheckIn = attendanceRecordCommandService.hasCheckInToday(memberId);

        log.info("오늘 출근 기록 확인 완료 - 회원ID: {}, 출근기록 존재: {}", memberId, hasCheckIn);
        return ResponseEntity.ok(CustomApiResponse.of(ResponseCode.SUCCESS, hasCheckIn));
    }

    @Operation(
            summary = "오늘 퇴근 기록 확인",
            description = """
                    - 현재 로그인한 사용자의 오늘 퇴근 기록 존재 여부를 확인합니다.
                    - 중복 퇴근 방지를 위해 사용할 수 있습니다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "확인 성공",
                    content = @Content(schema = @Schema(implementation = Boolean.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_401_EXAMPLE))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_500_EXAMPLE))
            )
    })
    @GetMapping("/check-out/today")
    public ResponseEntity<CustomApiResponse<Boolean>> hasCheckOutToday() {

        log.info("오늘 퇴근 기록 확인 요청");

        // JWT에서 회원 ID 추출
        Integer memberId = authFacade.getCurrentMemberId();

        // 오늘 퇴근 기록 확인
        boolean hasCheckOut = attendanceRecordCommandService.hasCheckOutToday(memberId);

        log.info("오늘 퇴근 기록 확인 완료 - 회원ID: {}, 퇴근기록 존재: {}", memberId, hasCheckOut);
        return ResponseEntity.ok(CustomApiResponse.of(ResponseCode.SUCCESS, hasCheckOut));
    }
}