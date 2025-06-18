package com.piveguyz.empickbackend.orgstructure.attendance.query.controller;

import com.piveguyz.empickbackend.common.constants.ApiExamples;
import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.orgstructure.attendance.query.dto.AttendanceCategoryQueryDTO;
import com.piveguyz.empickbackend.orgstructure.attendance.query.service.AttendanceCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/attendance")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "근태 API", description = "근태 카테고리 조회 관련 API")
public class AttendanceCategoryQueryController {

    private final AttendanceCategoryService attendanceCategoryService;

    @Operation(
            summary = "모든 근태 카테고리 조회",
            description = """
            - 시스템에 등록된 모든 근태 카테고리 목록을 조회합니다.
            - 0=출근, 1=퇴근, 2=지각, 3=조퇴
            """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AttendanceCategoryQueryDTO.class)))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_500_EXAMPLE))
            )
    })
    @GetMapping("/categories")
    public ResponseEntity<CustomApiResponse<List<AttendanceCategoryQueryDTO>>> getAllAttendanceCategories() {
        log.info("모든 근태 카테고리 조회 요청");

        List<AttendanceCategoryQueryDTO> categories = attendanceCategoryService.getAllAttendanceCategories();

        log.info("근태 카테고리 조회 완료 - 총 {}개", categories.size());
        return ResponseEntity.ok(CustomApiResponse.of(ResponseCode.SUCCESS, categories));
    }

    @Operation(
            summary = "근태 카테고리 상세 조회",
            description = """
            - 특정 ID의 근태 카테고리 상세 정보를 조회합니다.
            """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = AttendanceCategoryQueryDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_400_EXAMPLE))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "근태 카테고리를 찾을 수 없음",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_404_EXAMPLE))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_500_EXAMPLE))
            )
    })
    @GetMapping("/categories/{id}")
    public ResponseEntity<CustomApiResponse<AttendanceCategoryQueryDTO>> getAttendanceCategoryById(
            @Parameter(description = "근태 카테고리 ID", required = true, example = "1")
            @PathVariable int id
    ) {
        log.info("근태 카테고리 상세 조회 요청 - ID: {}", id);

        AttendanceCategoryQueryDTO category = attendanceCategoryService.getAttendanceCategoryById(id);

        log.info("근태 카테고리 상세 조회 완료 - ID: {}, Status: {}", id, category.getStatus());
        return ResponseEntity.ok(CustomApiResponse.of(ResponseCode.SUCCESS, category));
    }

    @Operation(
            summary = "근태 카테고리 status별 조회",
            description = "특정 status의 근태 카테고리 목록을 조회합니다. (0=출근, 1=퇴근, 2=지각, 3=조퇴)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 status 값",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_400_EXAMPLE))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_500_EXAMPLE)))
    })
    @GetMapping("/categories/status/{status}")
    public ResponseEntity<CustomApiResponse<List<AttendanceCategoryQueryDTO>>> getAttendanceCategoriesByStatus(
            @Parameter(description = "근태 카테고리 상태 (0=출근, 1=퇴근, 2=지각, 3=조퇴)", required = true, example = "0")
            @PathVariable int status
    ) {
        log.info("근태 카테고리 status별 조회 요청 - Status: {}", status);

        List<AttendanceCategoryQueryDTO> categories = attendanceCategoryService.getAttendanceCategoriesByStatus(status);

        log.info("근태 카테고리 status별 조회 완료 - Status: {}, 총 {}개", status, categories.size());
        return ResponseEntity.ok(CustomApiResponse.of(ResponseCode.SUCCESS, categories));
    }

    @Operation(
            summary = "복수 ID로 근태 카테고리 조회",
            description = "여러 ID에 해당하는 근태 카테고리 목록을 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_400_EXAMPLE))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_500_EXAMPLE)))
    })
    @GetMapping("/categories/bulk")
    public ResponseEntity<CustomApiResponse<List<AttendanceCategoryQueryDTO>>> getAttendanceCategoriesByIds(
            @Parameter(description = "근태 카테고리 ID 목록 (쉼표로 구분)", required = true, example = "1,2,3")
            @RequestParam List<Integer> ids
    ) {
        log.info("복수 ID로 근태 카테고리 조회 요청 - IDs: {}", ids);

        List<AttendanceCategoryQueryDTO> categories = attendanceCategoryService.getAttendanceCategoriesByIds(ids);

        log.info("복수 ID로 근태 카테고리 조회 완료 - 요청 {}개, 조회 {}개", ids.size(), categories.size());
        return ResponseEntity.ok(CustomApiResponse.of(ResponseCode.SUCCESS, categories));
    }

    @Operation(
            summary = "근태 카테고리 총 개수 조회",
            description = "시스템에 등록된 근태 카테고리의 총 개수를 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.ERROR_500_EXAMPLE)))
    })
    @GetMapping("/categories/count")
    public ResponseEntity<CustomApiResponse<Integer>> getTotalCount() {
        log.info("근태 카테고리 총 개수 조회 요청");

        int count = attendanceCategoryService.getTotalCount();

        log.info("근태 카테고리 총 개수 조회 완료 - 총 {}개", count);
        return ResponseEntity.ok(CustomApiResponse.of(ResponseCode.SUCCESS, count));
    }
}