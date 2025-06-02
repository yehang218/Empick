package com.piveguyz.empickbackend.employment.jobtest.command.application.controller;

import com.piveguyz.empickbackend.common.response.ApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.jobtest.command.application.dto.CreateQuestionCommandDTO;
import com.piveguyz.empickbackend.employment.jobtest.command.application.dto.DeleteQuestionCommandDTO;
import com.piveguyz.empickbackend.employment.jobtest.command.application.dto.UpdateQuestionCommandDTO;
import com.piveguyz.empickbackend.employment.jobtest.command.application.service.QuestionCommandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// 문제 등록 관련 API
@RestController
@RequestMapping("/api/v1/employment/jobtest/question")
public class QuestionCommandController {
    private final QuestionCommandService questionCommandService;

    public QuestionCommandController(QuestionCommandService questionCommandService) {
        this.questionCommandService = questionCommandService;
    }

    // 실무 테스트 문제 등록
    @PostMapping
    public ResponseEntity<ApiResponse<CreateQuestionCommandDTO>> createQuestion(@RequestBody CreateQuestionCommandDTO createQuestionCommandDTO) {
        CreateQuestionCommandDTO newQuestionDTO = questionCommandService.createQuestion(createQuestionCommandDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(ResponseCode.SUCCESS, newQuestionDTO));
    }

    // 실무 테스트 문제 수정
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UpdateQuestionCommandDTO>> updateQuestion(
            @PathVariable int id,
            @RequestBody UpdateQuestionCommandDTO updateQuestionCommandDTO) {
        updateQuestionCommandDTO = UpdateQuestionCommandDTO.builder()
                .id(id)
                .content(updateQuestionCommandDTO.getContent())
                .detailContent(updateQuestionCommandDTO.getDetailContent())
                .type(updateQuestionCommandDTO.getType())
                .difficulty(updateQuestionCommandDTO.getDifficulty())
                .answer(updateQuestionCommandDTO.getAnswer())
                .updatedMemberId(updateQuestionCommandDTO.getUpdatedMemberId())
                .build();
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.SUCCESS, questionCommandService.updateQuestion(updateQuestionCommandDTO)));
    }

    // 실무 테스트 문제 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<DeleteQuestionCommandDTO>> deleteQuestion(@PathVariable int id) {
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.SUCCESS, questionCommandService.deleteQuestion(id)));
    }
}
