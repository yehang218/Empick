package com.piveguyz.empickbackend.employment.jobtests.question.query.controller;


import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.jobtests.question.query.dto.QuestionListQueryDTO;
import com.piveguyz.empickbackend.employment.jobtests.question.query.dto.QuestionQueryDTO;
import com.piveguyz.empickbackend.employment.jobtests.question.query.service.QuestionQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/question")
public class QuestionQueryController {

    private final QuestionQueryService questionQueryService;

    QuestionQueryController(QuestionQueryService questionQueryService) {
        this.questionQueryService = questionQueryService;
    }

    @GetMapping
    public ResponseEntity<List<QuestionListQueryDTO>> getAllQuestions() {
        List<QuestionListQueryDTO> questionList = questionQueryService.getAllQuestions();
        return ResponseEntity.ok(questionList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomApiResponse<QuestionQueryDTO>> getQuestionById(@PathVariable int id) {
        QuestionQueryDTO questionDTO = questionQueryService.getQuestionById(id);
        return ResponseEntity.ok(CustomApiResponse.of(ResponseCode.SUCCESS, questionDTO));
    }
}
