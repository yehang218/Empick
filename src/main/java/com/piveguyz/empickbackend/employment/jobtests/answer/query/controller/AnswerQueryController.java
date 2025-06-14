package com.piveguyz.empickbackend.employment.jobtests.answer.query.controller;

import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.jobtests.answer.query.dto.AnswerQueryDTO;
import com.piveguyz.empickbackend.employment.jobtests.answer.query.service.AnswerQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "실무테스트 API", description = "실무테스트 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employment/answers")
public class AnswerQueryController {

    private final AnswerQueryService answerQueryService;

    @GetMapping("/{applicationJobtestId}")
    public ResponseEntity<CustomApiResponse<List<AnswerQueryDTO>>> getAnswerList(@PathVariable int applicationJobtestId) {
        List<AnswerQueryDTO> result =answerQueryService.findAnswerWithApplicationJobtestId(applicationJobtestId);
        return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
                .body(CustomApiResponse.of(ResponseCode.SUCCESS, result));
    }
}
