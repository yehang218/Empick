package com.piveguyz.empickbackend.employment.jobtests.evaluation.query.controller;

import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.jobtests.evaluation.query.dto.EvaluationCriteriaQueryDTO;
import com.piveguyz.empickbackend.employment.jobtests.evaluation.query.service.EvaluationCriteriaQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "실무 테스트 평가 기준 API", description = "실무테스트 문제 평가 기준 API")
@RestController
@RequestMapping("/api/v1/employment/jobtest/evaluation-criteria")
public class EvaluationCriteriaQueryController {
    private final EvaluationCriteriaQueryService evaluationCriteriaQueryService;

    EvaluationCriteriaQueryController(EvaluationCriteriaQueryService evaluationCriteriaQueryService) {
        this.evaluationCriteriaQueryService = evaluationCriteriaQueryService;
    }

    @Operation(
            summary = "실무테스트에 대한 평가 기준 조회",
            description = """
                    특정 실무테스트에 관한 평가 기준을 조회한다.
                    """
    )
    @ApiResponses(value = {
    })
    @GetMapping("/{jobtestId}")
    public ResponseEntity<CustomApiResponse<List<EvaluationCriteriaQueryDTO>>> getEvaluationCriteriaByJobtestId(@PathVariable int jobtestId) {
        List<EvaluationCriteriaQueryDTO> evaluationCriteriaList = evaluationCriteriaQueryService.getEvaluationCriteriaByJobtestId(jobtestId);
        return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
                .body(CustomApiResponse.of(ResponseCode.SUCCESS, evaluationCriteriaList));
    }

    @Operation(
            summary = "평가 기준 상세 조회",
            description = """
                    특정 평가 기준을 조회한다.
                    """
    )
    @ApiResponses(value = {
    })
    @GetMapping("/criteria/{id}")
    public ResponseEntity<CustomApiResponse<EvaluationCriteriaQueryDTO>> getEvaluationCriteriaById(@PathVariable int id) {
        EvaluationCriteriaQueryDTO evaluationCriteriaQueryDTO = evaluationCriteriaQueryService.getEvaluationCriteriaById(id);
        return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
                .body(CustomApiResponse.of(ResponseCode.SUCCESS, evaluationCriteriaQueryDTO));
    }
}
