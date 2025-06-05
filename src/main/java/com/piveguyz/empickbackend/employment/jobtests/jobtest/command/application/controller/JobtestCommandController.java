package com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.controller;

import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto.CreateJobtestCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto.UpdateJobtestCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.service.JobtestCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "실무 테스트 Command API", description = "실무테스트 등록, 수정, 삭제 API")
@RestController
@RequestMapping("/api/v1/employment/jobtest")
public class JobtestCommandController {
    private final JobtestCommandService jobtestCommandService;

    public JobtestCommandController(JobtestCommandService jobtestCommandService) {
        this.jobtestCommandService = jobtestCommandService;
    }

    // 실무테스트 등록
    @Operation(
            summary = "실무테스트 등록",
            description = """
                     실무 테스트를 등록합니다.
                    - difficulty : EASY / MEDIUM / HARD
                    """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "2402", description = "작성자 정보가 유효하지 않습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "2421", description = "동일한 이름의 실무테스트가 이미 등록되어 있습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "2400", description = "유효하지 않은 난이도입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "2401", description = "유효하지 않은 실무 테스트 유형입니다."),
    })
    @PostMapping
    public ResponseEntity<CustomApiResponse<CreateJobtestCommandDTO>> createJobtest(@RequestBody @Valid CreateJobtestCommandDTO createJobtestCommandDTO) {
        CreateJobtestCommandDTO newJobtestDTO = jobtestCommandService.createJobtest(createJobtestCommandDTO);
        return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
                .body(CustomApiResponse.of(ResponseCode.SUCCESS, newJobtestDTO));
    }

    //     실무테스트 수정
    @Operation(
            summary = "실무테스트 수정",
            description = """
                    실무테스트를 수정합니다.
                    - id에 해당하는 실무테스트의 시험 시간, 유형, 난이도 수정 가능
                    """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "2402", description = "작성자 정보가 유효하지 않습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "2420", description = "이 실무테스트는 다른 곳에서 사용중이므로 수정하거나 삭제할 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "2401", description = "유효하지 않은 실무 테스트 유형입니다."),
    })
    @PatchMapping("/{id}")
    public ResponseEntity<CustomApiResponse<UpdateJobtestCommandDTO>> updateJobtest(
            @PathVariable int id,
            @RequestBody @Valid UpdateJobtestCommandDTO updateJobtestCommandDTO) {
        UpdateJobtestCommandDTO newJobtestDTO = jobtestCommandService.updateJobtest(id, updateJobtestCommandDTO);
        return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
                .body(CustomApiResponse.of(ResponseCode.SUCCESS, newJobtestDTO));
    }

    @Operation(
            summary = "실무테스트 수정",
            description = """
                    실무테스트를 수정합니다.
                    - id에 해당하는 실무테스트의 시험 시간, 유형, 난이도 수정 가능
                    """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "2422", description = "요청한 실무테스트를 찾을 수 없습니다."),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomApiResponse<Integer>> deleteJobtest(@PathVariable int id) {
        int deletId = jobtestCommandService.deleteJobtest(id);
        return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
                .body(CustomApiResponse.of(ResponseCode.SUCCESS, deletId));
    }
}
