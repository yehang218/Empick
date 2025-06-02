package com.piveguyz.empickbackend.employment.jobtest.command.application.controller;

import com.piveguyz.empickbackend.common.response.ApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.jobtest.command.application.dto.CreateQuestionCommandDTO;
import com.piveguyz.empickbackend.employment.jobtest.command.application.service.QuestionCommandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 문제 등록 관련 API
@RestController
@RequestMapping("/api/v1/employment/jobtest/question")
public class QuestionCommandController {
    private final QuestionCommandService questionCommandService;

    public QuestionCommandController(QuestionCommandService questionCommandService) {
        this.questionCommandService = questionCommandService;
    }

//    private final JobtestQuestionCommandService jobtestQuestionCommandService;
//
//    public JobtestQuestionCommandController(JobtestQuestionCommandService jobtestQuestionCommandService) {
//        this.jobtestQuestionCommandService = jobtestQuestionCommandService;
//
//    }

    // 실무 테스트 문제 등록
    @PostMapping
    public ResponseEntity<ApiResponse<CreateQuestionCommandDTO>> createQuestion(@RequestBody CreateQuestionCommandDTO createQuestionCommandDTO) {
        CreateQuestionCommandDTO newQuestionDTO = questionCommandService.createQuestion(createQuestionCommandDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(ResponseCode.SUCCESS, newQuestionDTO));
    }


    // 실무 테스트 문제 수정


    // 실무 테스트 문제 삭제
}
