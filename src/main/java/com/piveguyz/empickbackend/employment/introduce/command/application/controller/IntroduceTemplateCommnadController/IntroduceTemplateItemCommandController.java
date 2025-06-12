package com.piveguyz.empickbackend.employment.introduce.command.application.controller.IntroduceTemplateCommnadController;

import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.introduce.command.application.dto.introduceTemplate.IntroduceTemplateItemCommandDTO;
import com.piveguyz.empickbackend.employment.introduce.command.application.service.introduceTemplateCommand.IntroduceTemplateItemCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "자기소개서 템플릿 항목 API", description = "자기소개서 템플릿 항목 관련 API")
@RestController
@RequestMapping("/api/v1/employment/introduce-template-item")
@RequiredArgsConstructor
public class IntroduceTemplateItemCommandController {

    private final IntroduceTemplateItemCommandService introduceTemplateItemCommandService;

    @PostMapping
    @Operation(summary = "템플릿 항목 등록", description = "자기소개서 템플릿 항목을 등록한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "등록 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<CustomApiResponse<
            IntroduceTemplateItemCommandDTO>> create(@RequestBody IntroduceTemplateItemCommandDTO dto) {
        IntroduceTemplateItemCommandDTO created = introduceTemplateItemCommandService.create(dto);
        return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
                .body(CustomApiResponse.of(ResponseCode.SUCCESS, created));
    }
}