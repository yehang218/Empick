package com.piveguyz.empickbackend.employment.jobtests.answer.command.application.controller;

import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.application.dto.CreateAnswerCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.application.dto.UpdateAnswerCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.application.service.AnswerCommandService;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.application.dto.CreateAnswerResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "실무테스트 API", description = "실무테스트 관련 API")
@RestController
@RequestMapping("/api/v1/employment/answers")
public class AnswerCommandController {
    private final AnswerCommandService answerCommandService;

    public AnswerCommandController(AnswerCommandService answerCommandService) {
        this.answerCommandService = answerCommandService;
    }

    @Operation(
            summary = "실무테스트 지원서별 답변 등록",
            description = """
                     실무테스트 지원서별 답변을 등록합니다.
                     isCorrect : WRONG, PARTIAL, CORRECT (null 가능)
                    """
    )
    @ApiResponses(value = {
    })
    @PostMapping
    public ResponseEntity<CustomApiResponse<CreateAnswerResponseDTO>> createAnswer(
            @RequestBody @Valid CreateAnswerCommandDTO createAnswerCommandDTO) {
        CreateAnswerResponseDTO newAnswerDTO = answerCommandService.createAnswer(createAnswerCommandDTO);
        return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
                .body(CustomApiResponse.of(ResponseCode.SUCCESS, newAnswerDTO));
    }

    @Operation(
            summary = "실무테스트 지원서별 답변 수정",
            description = """
                     실무테스트 지원서별 답변을 수정합니다.
                     정답 여부만 수정 가능합니다.
                      isCorrect : WRONG, PARTIAL, CORRECT (null 가능)
                    """
    )
    @ApiResponses(value = {
    })
    @PatchMapping("/{id}")
    public ResponseEntity<CustomApiResponse<UpdateAnswerCommandDTO>> updateAnswer(
            @PathVariable int id,
            @RequestBody @Valid UpdateAnswerCommandDTO updateAnswerCommandDTO
    ) {
        UpdateAnswerCommandDTO updateAnswerDTO = answerCommandService.updateAnswerCommandDTO(id, updateAnswerCommandDTO);
        return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
                .body(CustomApiResponse.of(ResponseCode.SUCCESS, updateAnswerDTO));
    }


    @Operation(
            summary = "실무테스트 지원서별 답변 삭제",
            description = """
                     실무테스트 지원서별 답변을 삭제합니다.
                    """
    )
    @ApiResponses(value = {
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomApiResponse<Integer>> deleteAnswer(
            @PathVariable int id) {
        int deleteId = answerCommandService.deleteAnswer(id);
        return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
                .body(CustomApiResponse.of(ResponseCode.SUCCESS, deleteId));
    }
}
