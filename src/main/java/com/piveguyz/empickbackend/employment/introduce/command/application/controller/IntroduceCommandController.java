package com.piveguyz.empickbackend.employment.introduce.command.application.controller;


import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.introduce.command.application.dto.IntroduceCommandDTO;
import com.piveguyz.empickbackend.employment.introduce.command.application.service.IntroduceCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "자기소개서 API", description = "자기소개서 관련 API")
@RestController
@RequestMapping("/api/v1/employment/introduce")
@RequiredArgsConstructor
public class IntroduceCommandController {

    private final IntroduceCommandService introduceCommandService;

    @PostMapping
    @Operation(summary = "자기소개서 등록", description = "지원자가 자기소개서를 등록합니다.")
    public ResponseEntity<CustomApiResponse<IntroduceCommandDTO>>
    createIntroduce(@RequestBody @Valid IntroduceCommandDTO dto) {

        IntroduceCommandDTO created = introduceCommandService.create(dto);
        return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
                .body(CustomApiResponse.of(ResponseCode.SUCCESS, created));
    }
}
