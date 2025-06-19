package com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.controller;

import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto.JobtestEntryRequestDTO;
import com.piveguyz.empickbackend.facade.JobtestFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Tag(name = "실무테스트 API", description = "실무테스트 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employment/exam")
public class JobtestExamCommandController {

    private final JobtestFacade jobtestFacade;

    @Operation(
            summary = "실무테스트 입장",
            description = """
                    실무테스트 입장 검증
                    """
    )
    @ApiResponses(value = {
    })
    @PostMapping("/enter/{jobtestId}")
    public ResponseEntity<CustomApiResponse<String>> verifyEntry(
            @PathVariable int jobtestId,
            @RequestBody JobtestEntryRequestDTO request
    ) {
        jobtestFacade.verifyJobtestEnter(jobtestId, request);
        return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
                .body((CustomApiResponse.of(ResponseCode.SUCCESS, ResponseCode.SUCCESS.getMessage())));
    }
}
