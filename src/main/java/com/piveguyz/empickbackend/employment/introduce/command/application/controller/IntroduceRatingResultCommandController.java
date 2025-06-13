package com.piveguyz.empickbackend.employment.introduce.command.application.controller;

import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.introduce.command.application.dto.IntroduceRatingResultCommandDTO;
import com.piveguyz.empickbackend.employment.introduce.command.application.service.IntroduceRatingResultCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "자기소개서 API", description = "자기소개서 관련 API")
@RestController
@RequestMapping("/api/v1/employment/introduce-rating-result")
@RequiredArgsConstructor
public class IntroduceRatingResultCommandController {

    private final IntroduceRatingResultCommandService introduceRatingResultCommandService;

    @Operation(summary = "자기소개서 평가 결과 등록", description = "자기소개서 평가 결과를 등록한다.")
    @PostMapping
    public ResponseEntity<CustomApiResponse<IntroduceRatingResultCommandDTO>> create(
            @RequestBody IntroduceRatingResultCommandDTO dto) {
        IntroduceRatingResultCommandDTO createdDTO = introduceRatingResultCommandService.create(dto);
        return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
                .body(CustomApiResponse.of(ResponseCode.SUCCESS, createdDTO));
    }

    @Operation(summary = "자기소개서 평가 결과 수정", description = "자기소개서 평가 결과를 수정한다.")
    @PatchMapping
    public ResponseEntity<CustomApiResponse<IntroduceRatingResultCommandDTO>> update(
            @RequestBody IntroduceRatingResultCommandDTO dto) {
        IntroduceRatingResultCommandDTO updatedDTO = introduceRatingResultCommandService.update(dto);
        return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
                .body(CustomApiResponse.of(ResponseCode.SUCCESS, updatedDTO));
    }

    @Operation(summary = "자기소개서 평가 결과 삭제", description = "자기소개서 평가 결과를 삭제한다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomApiResponse<Integer>> delete(@PathVariable int id) {
        int deletedId = introduceRatingResultCommandService.delete(id);
        return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
                .body(CustomApiResponse.of(ResponseCode.SUCCESS, deletedId));
    }
}