package com.piveguyz.empickbackend.employment.jobtest.command.application.controller;

import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.jobtest.command.application.dto.CreateQuestionCommandDTO;
import com.piveguyz.empickbackend.employment.jobtest.command.application.dto.DeleteQuestionCommandDTO;
import com.piveguyz.empickbackend.employment.jobtest.command.application.dto.UpdateQuestionCommandDTO;
import com.piveguyz.empickbackend.employment.jobtest.command.application.service.QuestionCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "실무 테스트 문제 API", description = "실무테스트 문제 등록, 수정, 삭제 API")
@RestController
@RequestMapping("/api/v1/employment/jobtest/question")
public class QuestionCommandController {
    private final QuestionCommandService questionCommandService;

    public QuestionCommandController(QuestionCommandService questionCommandService) {
        this.questionCommandService = questionCommandService;
    }

    @Operation(
            summary = "실무테스트 문제 등록",
            description = """
    - 실무 테스트 문제를 등록합니다.
    - type : MULTIPLE / SUBJECTIVE / DESCRIPTIVE,
    - difficulty : EASY / MEDIUM / HARD
    """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "2400", description = "실무테스트 문제 등록에 실패했습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "2401", description = "동일한 문제가 이미 등록되어 있습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "2402", description = "회원 정보를 찾을 수 없음")
    })
    @PostMapping
    public ResponseEntity<CustomApiResponse<CreateQuestionCommandDTO>> createQuestion(@RequestBody @Valid CreateQuestionCommandDTO createQuestionCommandDTO) {
        CreateQuestionCommandDTO newQuestionDTO = questionCommandService.createQuestion(createQuestionCommandDTO);
        return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
                .body(CustomApiResponse.of(ResponseCode.SUCCESS, newQuestionDTO));
    }

    // 실무 테스트 문제 수정
    @PutMapping("/{id}")
    public ResponseEntity<CustomApiResponse<UpdateQuestionCommandDTO>> updateQuestion(
            @PathVariable int id,
            @RequestBody UpdateQuestionCommandDTO updateQuestionCommandDTO) {
        return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
                .body((CustomApiResponse.of(ResponseCode.SUCCESS, questionCommandService.updateQuestion(updateQuestionCommandDTO))));
    }

    // 실무 테스트 문제 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomApiResponse<DeleteQuestionCommandDTO>> deleteQuestion(@PathVariable int id) {
        return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
                .body(CustomApiResponse.of(ResponseCode.SUCCESS, questionCommandService.deleteQuestion(id)));
    }
}
