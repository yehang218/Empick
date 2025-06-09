package com.piveguyz.empickbackend.employment.jobtests.evaluation.query.controller;

import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.jobtests.evaluation.query.dto.JobTestEvaluationDetailDTO;
import com.piveguyz.empickbackend.employment.jobtests.evaluation.query.service.EvaluationResultQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "실무 테스트 평가 결과 Query API", description = "실무테스트 평가 결과 조회 API")
@RestController
@RequestMapping("/api/v1/employment/jobtest/evaluation-result")
public class EvaluationResultQueryController {

    private final EvaluationResultQueryService evaluationResultQueryService;

    public EvaluationResultQueryController(EvaluationResultQueryService evaluationResultQueryService) {
        this.evaluationResultQueryService = evaluationResultQueryService;
    }

    @Operation(
            summary = "특정 지원서 평가 결과 조회",
            description = """
                     특정 지원서에 대한 평가 결과를 조회합니다.
                    """
    )
    @ApiResponses(value = {
    })
    @GetMapping("/{applicationId}")
    public ResponseEntity<CustomApiResponse<JobTestEvaluationDetailDTO>> getEvaluationResultByApplicationId(
            @PathVariable int applicationId) {
        JobTestEvaluationDetailDTO evaluationResultDTO = evaluationResultQueryService.getEvaluationResultByApplicationId(applicationId);
        return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
                .body(CustomApiResponse.of(ResponseCode.SUCCESS, evaluationResultDTO));
    }
}
